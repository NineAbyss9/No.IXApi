
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class NihilisticOrderSpawner {
    private int tickDelay;
    private int spawnDelay;
    private int spawnChance;

    public NihilisticOrderSpawner() {
    }

    public int tick(ServerLevel level) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return 0;
        } else if (--this.tickDelay > 0) {
            return 0;
        } else if (!NoixmodAPIMainConfig.NihilisticOrderSpawn.get()) {
            return 0;
        } else {
            this.tickDelay = 1200;
            this.spawnDelay -= 1200;//1200
            if (this.spawnDelay > 0) {
                return 0;
            } else {
                this.spawnDelay = Maths.toTick(2400);
                int $$3 = this.spawnChance;
                this.spawnChance = Mth.clamp(this.spawnChance + 25, 25, 75);
                if (ThreadLocalRandom.current().nextInt(100) > $$3) {
                    return 0;
                } else if (this.spawn(level)) {
                    this.spawnChance = 25;
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    public boolean spawn(ServerLevel level) {
        Player player = level.getRandomPlayer();
        if (player == null) {
            return false;
        }
        BlockPos.MutableBlockPos pos = player.blockPosition().mutable().move(Maths.randomInteger(12), 0,
                Maths.randomInteger(12));
        if (player.blockPosition().getY() >= level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                pos).getY() - 32 && level.canSeeSky(player.blockPosition())) {
            if (this.hasEnoughSpace(level, pos)) {
                this.trySpawnOrder(level, pos, player);
                player.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 2.0f, 1.0f);
                return true;
            }
        }
        return false;
    }

    private void trySpawnOrder(ServerLevel level, BlockPos.MutableBlockPos pos, Player player) {
        pos.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY());
        Mob mob;
        for (int i = 0;i < 2;i++) {
            mob = NoixmodAPIEntities.CULTIST.get().spawn(level, pos, MobSpawnType.EVENT);
            if (mob != null) {
                mob.restrictTo(pos, 16);
                mob.setTarget(player);
            }
        }
        for (int i = 0;i < 3;++i) {
            mob = NoixmodAPIEntities.SWORD_CULTIST.get().spawn(level, pos, MobSpawnType.EVENT);
            if (mob != null) {
                mob.restrictTo(pos, 18);
                mob.setTarget(player);
            }
        }
        //for (int i = 0;i < 2;i++) {
            mob = NoixmodAPIEntities.SHADOW_WALKER.get().spawn(level, pos, MobSpawnType.EVENT);
            if (mob != null) {
                mob.restrictTo(pos, 18);
                mob.spawnAnim();
                mob.setTarget(player);
            }
        //}
    }

    private boolean hasEnoughSpace(Level pLevel, BlockPos pPos) {
        Iterator<BlockPos> var3 = BlockPos.betweenClosed(pPos, pPos.offset(1, 2,
                1)).iterator();
        BlockPos $$2;
        do {
            if (!var3.hasNext()) {
                return NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, pLevel, pPos,
                        NoixmodAPIEntities.SHADOW_WALKER.get());
            }
            $$2 = var3.next();
        } while(pLevel.getBlockState($$2).getCollisionShape(pLevel, $$2).isEmpty());
        return false;
    }
}
