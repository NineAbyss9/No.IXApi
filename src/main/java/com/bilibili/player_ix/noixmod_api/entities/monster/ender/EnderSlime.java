
package com.bilibili.player_ix.noixmod_api.entities.monster.ender;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.concurrent.ThreadLocalRandom;

//Void block
@SuppressWarnings("deprecation")
public class EnderSlime
extends Slime
{
    private boolean doomedToDie;
    public EnderSlime(EntityType<? extends EnderSlime> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.doomedToDie) {
                this.remove(RemovalReason.KILLED);
                return;
            }
            if (this.tickCount % 40 != 0) {
                return;
            }
            // 检测虚空风险
            if (this.isAtVoidEdge()) {
                this.teleportToSafePosition();
            }
        }
    }

    private boolean isAtVoidEdge()
    {
        BlockPos pos = blockPosition();
        // 检查脚下是否为虚空
        if (this.level().getBlockState(pos.below()).isSolid()) {
            return false;
        }
        if (this.level().getBlockState(pos.below(2)).isSolid()) {
            return false;
        }
        return !this.level().getBlockState(pos.below(12)).isSolid();
        // 检查周围 2 格内是否有虚空
        /*for (int dx = -2;dx <= 2;dx++) {
            for (int dz = -2;dz <= 2;dz++) {
                BlockPos checkPos = pos.offset(dx, -1, dz);
                if (!this.level().getBlockState(checkPos).isSolid()) {
                    return true;
                }
            }
        }*/
    }

    private void teleportToSafePosition() {
        for (int i = 0;i < 24;i++) {
            if (this.randomTeleport(this.getX() + ThreadLocalRandom.current().nextInt(20) - 10,
                    this.getY() + ThreadLocalRandom.current().nextInt(20) - 10,
                    this.getZ() + ThreadLocalRandom.current().nextInt(20) - 10, true)) {
                return;
            }
        }
        this.doomedToDie = true;
        // 向上搜索安全位置
        /*for (int yOffset = 0;yOffset < 10; yOffset++) {
            BlockPos checkPos = pos.above(yOffset);
            if (level.getBlockState(checkPos.below()).isSolid() &&
                    level.getBlockState(checkPos).isAir()) {
                this.teleportTo(checkPos.getX() + 0.5, checkPos.getY(), checkPos.getZ() + 0.5);
                return;
            }
        }*/
    }

    public void remove(RemovalReason pReason) {
        int i = this.getSize();
        if (this.doomedToDie) {
            this.setRemoved(pReason);
            this.invalidateCaps();
            this.brain.clearMemories();
            return;
        }
        if (!this.level().isClientSide && i > 1 && this.isDeadOrDying()) {
            Component component = this.getCustomName();
            boolean flag = this.isNoAi();
            float f = (float)i / 4.0F;
            int j = i / 2;
            int k = 2 + this.random.nextInt(3);
            for (int l = 0;l < k;++l) {
                float f1 = ((float)(l % 2) - 0.5F) * f;
                float f2 = ((float)(l / 2) - 0.5F) * f;
                Slime slime = NoixmodAPIEntities.ENDER_SLIME.get().create(this.level());
                if (slime != null) {
                    if (this.isPersistenceRequired()) {
                        slime.setPersistenceRequired();
                    }
                    slime.setCustomName(component);
                    slime.setNoAi(flag);
                    slime.setInvulnerable(this.isInvulnerable());
                    slime.setSize(j, true);
                    slime.moveTo(this.getX() + (double)f1, this.getY() + 0.5D, this.getZ() + (double)f2,
                            this.random.nextFloat() * 360.0F, 0.0F);
                    this.level().addFreshEntity(slime);
                }
            }
        }
        this.setRemoved(pReason);
        this.invalidateCaps();
        this.brain.clearMemories();
    }

    protected void dropAllDeathLoot(DamageSource pDamageSource) {
        if (this.getSize() != 1) {
            return;
        }
        super.dropAllDeathLoot(pDamageSource);
    }

    public static boolean checkEnderSlimeSpawn(EntityType<EnderSlime> pSlime, LevelAccessor pLevel,
                                               MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        double distanceToOrigin = Math.sqrt((double)(pPos.getX() * pPos.getX() + pPos.getZ() * pPos.getZ()));
        if (distanceToOrigin < 900.0D) {
            return false;// 在主岛范围内，不生成
        }
        return Mob.checkMobSpawnRules(pSlime, pLevel, pSpawnType, pPos, pRandom);
    }

    protected ParticleOptions getParticleType()
    {
        return ParticleTypes.PORTAL;
    }

    protected int getJumpDelay()
    {
        return super.getJumpDelay() * 2;
    }

    @SuppressWarnings("deprecation")
    public float getLightLevelDependentMagicValue()
    {
        return 3.0F;
    }
}
