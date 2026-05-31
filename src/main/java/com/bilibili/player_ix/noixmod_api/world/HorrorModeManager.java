
package com.bilibili.player_ix.noixmod_api.world;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.ApiMeleeAttackGoal;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.NineAbyss9.util.pair.Pair;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class HorrorModeManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private int tickCount;
    private int apostleLookingTime = 0;
    private float apostleSummonChance = 0F;
    private int spawnInterval;
    private static final Map<Integer, Integer> SPAWN_MAP =
            new LinkedHashMap<Integer, Integer>();
    private final Map<Integer, Pair<Goal, Goal>> attackableMobs =
            new HashMap<Integer, Pair<Goal, Goal>>();
    public static final Pair<Integer, Integer> TRACKER = Pair.of(0, 2);
    public static final int TRACKER_MAX_SPAWN_INTERVAL = Maths.toTick(300);
    public static final Pair<Integer, Integer> THE_GHOST = Pair.of(1, 1);
    public final Pair<Integer, Integer> spawnCache = Pair.mutable(0, 0);
    /**1 -> tracker
     *2 -> the ghost*/
    private BooleanList mobsWillSpawn = new BooleanArrayList(new boolean[] {
            true, false
    });
    private int removeAttackMobsTime;
    public HorrorModeManager() {
    }

    public void tick(Level pLevel)
    {
        ServerLevel serverLevel = (ServerLevel)pLevel;
        ++spawnInterval;
        ++tickCount;
            /*if (apostleLookingTime > 0) {
                --apostleLookingTime;
                if (apostleLookingTime == 90) {
                    for (Player player : serverLevel.players()) {
                        serverLevel.playSound(player, player.blockPosition(), NoixmodAPISounds.APOSTLE_CAST_SPELL.get(),
                                SoundSource.HOSTILE);
                    }
                    apostleSummonChance = 0.05F;
                }
            }
            var dragonFight = serverLevel.getDragonFight();
            if (dragonFight != null) {
                if (dragonFight.hasPreviouslyKilledDragon() && apostleSummonChance < 1F
                    && serverLevel.getGameTime() % 60L == 0L) {
                    apostleSummonChance += 0.0005F;
                }
            }
            if (this.apostleSummonChance >= 1F) {
                this.apostleLookingTime = 99;
            }*/
        if (tickCount % 1200 == 0) {
            if ((random().nextFloat() < 0.005F || this.spawnInterval > TRACKER_MAX_SPAWN_INTERVAL) &&
                    this.shouldSpawnTracker()) {
                var player = this.spawnTracker(pLevel);
                if (player == null) {
                    LOGGER.warn("WTF?Can't spawn Tracker?");
                } else {
                    player.connection.connection.send(
                            new ClientboundSetActionBarTextPacket(Component.translatable("info.noixmodapi.tracker_look")));
                    this.updateSpawnCache();
                }
                this.resetSpawnInterval();
            }
            if (random().nextFloat() < 0.0005F) {
                if (!serverLevel.players().isEmpty()) {
                    for (var player : serverLevel.players()) {
                        serverLevel.playSound(player, player.blockPosition(), NoixmodAPISounds.APOSTLE_IDLE.get(),
                                SoundSource.HOSTILE, 0.5F, 1.0F);
                    }
                }
                if (this.shouldSpawnTheGhost()) {
                    var player = this.spawnTheGhost(pLevel);
                    if (player == null) {
                        LOGGER.warn("WTF?Can't spawn \"Ghost\"?");
                    } else {
                        player.connection.send(new ClientboundSetActionBarTextPacket(
                                Component.translatable("info.noixmodapi.ghost_look")));
                        this.resetSpawnInterval();
                    }
                }
            }
            if (tickCount % 7200 == 0 && ThreadLocalRandom.current().nextFloat() < 0.1F) {
                var list = pLevel.players();
                if (list.isEmpty()) return;
                var player = list.get(ThreadLocalRandom.current().nextInt(list.size()));
                for (Animal animal : pLevel.getEntitiesOfClass(Animal.class, player.getBoundingBox().inflate(32)))
                {
                    if (ThreadLocalRandom.current().nextFloat() < 0.05F) {
                        var instance = animal.getAttribute(Attributes.ATTACK_DAMAGE);
                        if (instance == null) {
                            continue;
                        }
                        if (instance.getValue() <= 0.0D) {
                            instance.setBaseValue(1.0D);
                        }
                        var goal = new ApiMeleeAttackGoal(animal, 0.8D);
                        var goal1 = new NearestAttackableTargetGoal<>(animal, Player.class, true);
                        attackableMobs.putIfAbsent(animal.getId(), Pair.of(goal, goal1));
                        animal.goalSelector.addGoal(1, goal);
                        animal.targetSelector.addGoal(1, goal1);
                    }
                }
                this.removeAttackMobsTime = this.tickCount + Maths.toTick(15);
            }
            if (this.tickCount == removeAttackMobsTime) {
                for (var id : attackableMobs.entrySet()) {
                    var mob = (Mob)pLevel.getEntity(id.getKey());
                    //if (!(entity instanceof Mob mob)) continue; Improve memory
                    mob.goalSelector.removeGoal(id.getValue().left());
                    mob.targetSelector.removeGoal(id.getValue().right());
                }
                attackableMobs.clear();
            }
        }
    }

    public void setMobsWillSpawn(int index, boolean value) {
        this.mobsWillSpawn.set(index, value);
    }

    public void updateNextMobWillSpawn(int index) {
        this.mobsWillSpawn.set(index, false);
        if (this.mobsWillSpawn.size() < index) {
            this.mobsWillSpawn.set(index + 1, true);
        }
    }

    public void updateSpawnCache()
    {
        ///Stop updating if the index of {@linkplain spawnCache} is larger than {@linkplain mobsWillSpawn#size}
        if (this.mobsWillSpawn.size() <= spawnCache.left()) return;
        if (spawnCache.right() > SPAWN_MAP.get(spawnCache.left())) {
            this.updateNextMobWillSpawn(spawnCache.left());
            LOGGER.info("SpawnCache 's right value is max, turning to next part.");
            spawnCache.setLeft(spawnCache.left() + 1);
            spawnCache.setRight(0);
            return;
        }
        spawnCache.setRight(spawnCache.right() + 1);
    }

    public ServerPlayer spawnTheGhost(Level pLevel) {
        var ghost = NoixmodAPIEntities.THE_GHOST.get().create(pLevel);
        if (ghost == null) return null;
        var list = pLevel.players();
        if (list.isEmpty()) return null;
        ServerPlayer player = (ServerPlayer)list.get(random().nextInt(list.size()));
        for (int i = 0;i < 20;i++) {
            double x = player.getX() + random().nextDouble(-10, 10);
            double y = player.getY() + random().nextDouble(-10, 10);
            double z = player.getZ() + random().nextDouble(-10, 10);
            if (pLevel.noCollision(ghost.getBoundingBox().move(x, y, z))) {
                ghost.moveTo(x, y, z, 0F, 0F);
                if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player))
                    ghost.setTarget(player);
                player = pLevel.addFreshEntity(ghost) ? player : null;
                return player;
            }
        }
        if (!pLevel.isLoaded(pLevel.getSharedSpawnPos())) return null;
        ghost.moveTo(pLevel.getSharedSpawnPos(), 0F, 0F);
        return pLevel.addFreshEntity(ghost) ? player : null;
    }

    public ServerPlayer spawnTracker(Level pLevel) {
        return this.spawnTracker(pLevel, 0);
    }

    public ServerPlayer spawnTracker(Level pLevel, int type) {
        var ghost = NoixmodAPIEntities.TRACKER.get().create(pLevel);
        if (ghost == null) return null;
        var list = pLevel.players();
        if (list.isEmpty()) return null;
        ServerPlayer player = (ServerPlayer)list.get(random().nextInt(list.size()));
        for (int i = 0;i < 20;i++) {
            double x = player.getX() + random().nextDouble(-10, 10);
            double y = player.getY() + random().nextDouble(-10, 10);
            double z = player.getZ() + random().nextDouble(-10, 10);
            if (pLevel.noCollision(ghost.getBoundingBox().move(x, y, z))) {
                ghost.moveTo(x, y, z, 0F, 0F);
                ghost.setType(type);
                if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player))
                    ghost.setTarget(player);
                player = pLevel.addFreshEntity(ghost) ? player : null;
                return player;
            }
        }
        if (!pLevel.isLoaded(pLevel.getSharedSpawnPos())) return null;
        ghost.setType(type);
        ghost.moveTo(pLevel.getSharedSpawnPos(), 0F, 0F);
        return pLevel.addFreshEntity(ghost) ? player : null;
    }

    public boolean shouldSpawnTracker() {
        return this.mobsWillSpawn.getBoolean(TRACKER.left()) && spawnCache.right() < TRACKER.right();
    }

    public boolean shouldSpawnTheGhost() {
        return this.mobsWillSpawn.getBoolean(THE_GHOST.left());
    }

    public ThreadLocalRandom random()
    {
        return ThreadLocalRandom.current();
    }

    public void load(CompoundTag pCompound) {
        LOGGER.debug("Loading {} from nbt...", HorrorModeManager.class.getSimpleName());
        this.apostleLookingTime = pCompound.getInt("ApostleLookingTime");
        this.apostleSummonChance = pCompound.getFloat("ApostleSummonChance");
        this.tickCount = pCompound.getInt("TickCount");
        var list = pCompound.getCompound("MobsWillSpawn");
        for (int i = 0;i < list.size();i++) {
            this.mobsWillSpawn.set(i, pCompound.getBoolean("Mob" + i));
        }
        var spawnCache = pCompound.getCompound("SpawnCache");
        this.spawnCache.setKey(spawnCache.getInt("Index"));
        this.spawnCache.setValue(spawnCache.getInt("Count"));
        if (this.tickCount != Integer.MAX_VALUE) {
            return;
        }
        LOGGER.info("Oh, you played so much time!Resetting the tickCount of {} to 0.And thank for your playing." +
                        "Remember to take a break!",
                this.getClass().getSimpleName());
        this.tickCount = 0;
    }

    public void save(CompoundTag pCompound) {
        pCompound.putInt("ApostleLookingTime", this.apostleLookingTime);
        pCompound.putFloat("ApostleSummonChance", this.apostleSummonChance);
        pCompound.putInt("TickCount", tickCount);
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < mobsWillSpawn.size(); ++i) {
            tag.putBoolean("Mob" + i, mobsWillSpawn.getBoolean(i));
        }
        pCompound.put("MobsWillSpawn", tag);
        CompoundTag spawnCacheTag = new CompoundTag();
        spawnCacheTag.putInt("Index", this.spawnCache.left());
        spawnCacheTag.putInt("Count", this.spawnCache.right());
        pCompound.put("SpawnCache", spawnCacheTag);
    }

    public void resetSpawnInterval()
    {
        this.spawnInterval = 0;
    }

    public static boolean spawnTerribleMobs()
    {
        return NoixmodAPIMainConfig.SpawnHorror.get();
    }

    public static boolean horrorModeEnabled()
    {
        return NoixmodAPIMainConfig.HorrorMode.get();
    }

    public static void playStrangeSound(Entity pEntity)
    {
        pEntity.playSound(SoundEvents.AMBIENT_CAVE.value());
    }

    static {
        SPAWN_MAP.put(TRACKER.left(), TRACKER.right());
        SPAWN_MAP.put(THE_GHOST.left(), THE_GHOST.right());
    }
}
