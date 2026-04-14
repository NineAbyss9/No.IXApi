
package com.bilibili.player_ix.noixmod_api.entities.mod;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.network.NetworkHooks;

public abstract class APIMonster
extends Monster {
    public APIMonster(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        return super.hurt(damageSource, amount);
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static boolean checkAPIMonsterSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor accessor,
                                                    MobSpawnType spawnType, BlockPos pos, RandomSource source) {
        return APIMonster.checkMobSpawnRules(type, accessor, spawnType, pos, source) && accessor.getDifficulty()
                != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(accessor, pos, source);
    }

    public static boolean horrorMobsSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor accessor,
                                               MobSpawnType spawnType, BlockPos pos, RandomSource source) {
        return NoixmodAPIMainConfig.SpawnHorror.get() && checkAPIMonsterSpawnRules(type, accessor, spawnType, pos, source);
    }
}
