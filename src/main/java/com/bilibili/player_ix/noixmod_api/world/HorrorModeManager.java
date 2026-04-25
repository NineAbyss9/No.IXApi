
package com.bilibili.player_ix.noixmod_api.world;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.github.NineAbyss9.ix_api.api.annotation.MaybeDeprecated;
import com.github.NineAbyss9.ix_api.util.Maths;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.NineAbyss9.math.MathSupport;
import org.NineAbyss9.util.pair.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.NineAbyss9.math.MathSupport.random;

public class HorrorModeManager {
    @MaybeDeprecated
    public static final Map<Level, HorrorModeManager> horrorModeManagers = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(HorrorModeManager.class);
    private int apostleLookingTime = 0;
    private float apostleSummonChance = 0F;
    private int spawnInterval;
    public static final Pair<Integer, Integer> TRACKER = Pair.of(0, 2);
    public static final int TRACKER_MAX_SPAWN_INTERVAL = Maths.toTick(300);
    public static final Pair<Integer, Integer> THE_GHOST = Pair.of(1, 0);
    public final Pair<Integer, Integer> spawnCache = Pair.mutable(0, 0);
    /**1 -> tracker
     *2 -> the ghost*/
    private BooleanList mobsWillSpawn = new BooleanArrayList(new boolean[] {
            true, false
    });
    private int id;
    private Status status;
    public HorrorModeManager(int pId) {
        this.id = pId;
        this.status = Status.ONGOING;
    }

    public void tick(Level pLevel) {
        if (pLevel.isClientSide) {
            if (apostleLookingTime > 0) {
                --apostleLookingTime;
            }
        } else {
            ServerLevel serverLevel = (ServerLevel)pLevel;
            ++spawnInterval;
            if (apostleLookingTime > 0) {
                serverLevel.setDayTime(0L);
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
            }
            if (serverLevel.getGameTime() % 1200L == 0L) {
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

    public void revive() {
        LOGGER.info("Revive successfully.");
        this.status = Status.ONGOING;
    }

    public void stop() {
        this.status = Status.STOPPED;
    }

    public boolean isStopped() {
        return this.status == Status.STOPPED;
    }

    public void updateSpawnCache() {
        ///No update if the index of {@linkplain spawnCache} is larger than {@linkplain mobsWillSpawn#size}
        if (this.mobsWillSpawn.size() <= spawnCache.left()) return;
        if (this.mobsWillSpawn.getBoolean(spawnCache.left()))
            spawnCache.setValue(spawnCache.right() + 1);
        else {/// Turn to next part
            spawnCache.setKey(spawnCache.left() + 1);
            spawnCache.setValue(0);
        }
    }

    public Player spawnTheGhost(Level pLevel) {
        var ghost = NoixmodAPIEntities.THE_GHOST.get().create(pLevel);
        if (ghost == null) return null;
        var list = pLevel.players();
        if (list.isEmpty()) return null;
        Player player = list.get(MathSupport.random.nextInt(list.size()));
        for (int i = 0;i < 20;i++) {
            double x = player.getX() + MathSupport.random.nextDouble(-10, 10);
            double y = player.getY() + MathSupport.random.nextDouble(-10, 10);
            double z = player.getZ() + MathSupport.random.nextDouble(-10, 10);
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
        Player player = list.get(MathSupport.random.nextInt(list.size()));
        for (int i = 0;i < 20;i++) {
            double x = player.getX() + MathSupport.random.nextDouble(-10, 10);
            double y = player.getY() + MathSupport.random.nextDouble(-10, 10);
            double z = player.getZ() + MathSupport.random.nextDouble(-10, 10);
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
        this.id = pCompound.getInt("Id");
        this.apostleLookingTime = pCompound.getInt("ApostleLookingTime");
        this.apostleSummonChance = pCompound.getFloat("ApostleSummonChance");
        this.status = Status.getByName(pCompound.getString("Status"));
        var list = pCompound.getCompound("MobsWillSpawn");
        for (int i = 0;i < list.size();i++) {
            this.mobsWillSpawn.set(i, pCompound.getBoolean("Mob" + i));
        }
        var spawnCache = pCompound.getCompound("SpawnCache");
        this.spawnCache.setKey(spawnCache.getInt("Index"));
        this.spawnCache.setValue(spawnCache.getInt("Count"));
    }

    public void save(CompoundTag pCompound) {
        pCompound.putInt("Id", this.id);
        pCompound.putInt("ApostleLookingTime", this.apostleLookingTime);
        pCompound.putFloat("ApostleSummonChance", this.apostleSummonChance);
        pCompound.putString("Status", this.status.getName());
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < mobsWillSpawn.size(); ++i) {
            tag.putBoolean("Mob" + i, mobsWillSpawn.getBoolean(i));
        }
        pCompound.put("MobsWillSpawn", tag);
        CompoundTag spawnCache = new CompoundTag();
        spawnCache.putInt("Index", this.spawnCache.left());
        spawnCache.putInt("Count", this.spawnCache.right());
        pCompound.put("SpawnCache", spawnCache);
    }

    public int getId() {
        return id;
    }

    public void resetSpawnInterval() {
        this.spawnInterval = 0;
    }

    public static boolean spawnTerribleMobs() {
        return NoixmodAPIMainConfig.SpawnHorror.get();
    }

    public static boolean horrorModeEnabled() {
        return NoixmodAPIMainConfig.HorrorMode.get();
    }

    public static enum Status {
        ONGOING,
        STOPPED;

        public static final Status[] STATUSES = values();

        public static Status getByName(String pName) {
            for (Status horrorModeManager$Status : STATUSES) {
                if (pName.equalsIgnoreCase(horrorModeManager$Status.name())) {
                    return horrorModeManager$Status;
                }
            }
            return ONGOING;
        }

        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
