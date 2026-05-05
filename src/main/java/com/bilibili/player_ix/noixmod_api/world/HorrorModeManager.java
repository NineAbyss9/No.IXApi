
package com.bilibili.player_ix.noixmod_api.world;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.NineAbyss9.util.pair.Pair;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class HorrorModeManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private int tickCount;
    private int apostleLookingTime = 0;
    private float apostleSummonChance = 0F;
    private int spawnInterval;
    private static final Map<Integer, Integer> SPAWN_MAP = new LinkedHashMap<>();
    public static final Pair<Integer, Integer> TRACKER = Pair.of(0, 2);
    public static final int TRACKER_MAX_SPAWN_INTERVAL = Maths.toTick(300);
    public static final Pair<Integer, Integer> THE_GHOST = Pair.of(1, 1);
    public final Pair<Integer, Integer> spawnCache = Pair.mutable(0, 0);
    /**1 -> tracker
     *2 -> the ghost*/
    private BooleanList mobsWillSpawn = new BooleanArrayList(new boolean[] {
            true, false
    });
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
            if ((random.nextFloat() < 0.005F || this.spawnInterval > TRACKER_MAX_SPAWN_INTERVAL) &&
                    this.shouldSpawnTracker()) {
                var player = this.spawnTracker(pLevel);
                if (player == null) {
                    LOGGER.info("WTF?Can't spawn Tracker?");
                } else {
                    player.sendSystemMessage(Component.translatable("info.noixmodapi.tracker_look"));
                    this.updateSpawnCache();
                }
                this.resetSpawnInterval();
            }
            if (random.nextFloat() < 0.0005F) {
                if (!serverLevel.players().isEmpty()) {
                    for (var player : serverLevel.players()) {
                        serverLevel.playSound(player, player.blockPosition(), NoixmodAPISounds.APOSTLE_IDLE.get(),
                                SoundSource.HOSTILE, 0.5F, 1.0F);
                    }
                }
                if (this.shouldSpawnTheGhost()) {
                    var player = this.spawnTheGhost(pLevel);
                    if (player == null) {
                        LOGGER.info("WTF?Can't spawn \"Ghost\"?");
                    } else {
                        player.sendSystemMessage(Component.translatable("info.noixmodapi.ghost_look"));
                        this.resetSpawnInterval();
                    }
                }
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
        ///No update if the index of {@linkplain spawnCache} is larger than {@linkplain mobsWillSpawn#size}
        if (this.mobsWillSpawn.size() <= spawnCache.left()) return;
        if (spawnCache.right() > SPAWN_MAP.get(spawnCache.left())) {
            this.updateNextMobWillSpawn(spawnCache.left());
            LOGGER.info("SpawnCache 's right value is max, turning to next part.");
            spawnCache.setKey(spawnCache.left() + 1);
            spawnCache.setValue(0);
            return;
        }
        spawnCache.setValue(spawnCache.right() + 1);
    }

    public Player spawnTheGhost(Level pLevel) {
        var ghost = NoixmodAPIEntities.THE_GHOST.get().create(pLevel);
        if (ghost == null) return null;
        var list = pLevel.players();
        if (list.isEmpty()) return null;
        Player player = list.get(random.nextInt(list.size()));
        for (int i = 0;i < 20;i++) {
            double x = player.getX() + random.nextDouble(-10, 10);
            double y = player.getY() + random.nextDouble(-10, 10);
            double z = player.getZ() + random.nextDouble(-10, 10);
            if (pLevel.noCollision(ghost.getBoundingBox().move(x, y, z))) {
                ghost.moveTo(x, y, z, 0F, 0F);
                if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player))
                    ghost.setTarget(player);
                player = pLevel.addFreshEntity(ghost) ? player : null;
                return player;
            }
        }
        if (!pLevel.isLoaded(pLevel.getSharedSpawnPos())) return null;
        ghost.moveTo(pLevel.getSharedSpawnPos().getX(), pLevel.getSharedSpawnPos().getY(), pLevel.getSharedSpawnPos()
                        .getZ(), 0F, 0F);
        return pLevel.addFreshEntity(ghost) ? player : null;
    }

    public Player spawnTracker(Level pLevel) {
        var ghost = NoixmodAPIEntities.TRACKER.get().create(pLevel);
        if (ghost == null) return null;
        var list = pLevel.players();
        if (list.isEmpty()) return null;
        Player player = list.get(random.nextInt(list.size()));
        for (int i = 0;i < 20;i++) {
            double x = player.getX() + random.nextDouble(-10, 10);
            double y = player.getY() + random.nextDouble(-10, 10);
            double z = player.getZ() + random.nextDouble(-10, 10);
            if (pLevel.noCollision(ghost.getBoundingBox().move(x, y, z))) {
                ghost.moveTo(x, y, z, 0F, 0F);
                if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player))
                    ghost.setTarget(player);
                player = pLevel.addFreshEntity(ghost) ? player : null;
                return player;
            }
        }
        if (!pLevel.isLoaded(pLevel.getSharedSpawnPos())) return null;
        ghost.moveTo(pLevel.getSharedSpawnPos().getX(), pLevel.getSharedSpawnPos().getY(), pLevel.getSharedSpawnPos()
                .getZ(), 0F, 0F);
        return pLevel.addFreshEntity(ghost) ? player : null;
    }

    public boolean shouldSpawnTracker() {
        return this.mobsWillSpawn.getBoolean(TRACKER.left()) && spawnCache.right() < TRACKER.right();
    }

    public boolean shouldSpawnTheGhost() {
        return this.mobsWillSpawn.getBoolean(THE_GHOST.left());
    }

    public void load(CompoundTag pCompound) {
        LOGGER.info("Loading {} from nbt...", HorrorModeManager.class.getSimpleName());
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
