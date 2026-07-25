
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.bilibili.player_ix.noixmod_api.entities.boss.NihilisticWitherBoss;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIDamageSource;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.client.particle.CircleParticleOption;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NihilisticWitherSkull extends AbstractHurtingProjectile {
    public NihilisticWitherSkull(EntityType<? extends NihilisticWitherSkull> p_37598_, Level p_37599_) {
        super(p_37598_, p_37599_);
    }

    public NihilisticWitherSkull(Level p_37609_, LivingEntity p_37610_, double p_37611_, double p_37612_, double p_37613_) {
        super(NoixmodAPIEntities.NIHILISTIC_WITHER_SKULL.get(), p_37610_, p_37611_, p_37612_, p_37613_, p_37609_);
    }

    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (this.level().isClientSide) {
            return;
        }
        Entity entity = pResult.getEntity();
        if (entity instanceof NihilisticWitherBoss boss) {
            boss.skullHurt();
        }
        Entity entity1 = this.getOwner();
        boolean flag = entity.hurt(NoixmodAPIDamageSource.nihilityOwner(this), 8.0F);
        LivingEntity living;
        if (entity1 instanceof LivingEntity) {
            living = (LivingEntity)entity1;
            if (flag) {
                if (entity.isAlive()) {
                    living.heal(0.25f);
                    this.doEnchantDamageEffects(living, entity);
                } else {
                    living.heal(6.5f);
                }
            }
        }
        if (flag && entity instanceof LivingEntity livingEntity) {
            int i = 2;
            if (this.level().getDifficulty() == Difficulty.NORMAL) {
                i = 4;
            } else if (this.level().getDifficulty() == Difficulty.HARD) {
                i = 6;
            }
            livingEntity.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.NIHILISTIC.get(), 20 * i,
                    0), livingEntity);
        }
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (this.level().isClientSide) {
            return;
        }
        BlockPos pos = pResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)pResult)
                .getEntity().blockPosition() : (pResult.getType() == HitResult.Type.BLOCK ?
                ((BlockHitResult)pResult).getBlockPos() : this.blockPosition());
        ParticleUtil.sendParticles(((ServerLevel)this.level()), new CircleParticleOption(0,
                        0, 0, 6F, 0.3F), Vec9.of(pos).add(0.0, 0.03, 0.0), 1,
                0, 0, 0, 0);
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, false,
                Level.ExplosionInteraction.MOB);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.5),
                living -> MobUtils.canHurt(living, this));
        if (list.isEmpty()) {
            this.discard();
        } else {
            for (LivingEntity living : list) {
                living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.NIHILISTIC.get(), 30, 0));
                living.hurt(NoixmodAPIDamageSource.nihilityOwner(this), 4);
            }
            this.discard();
        }
    }

    protected boolean canHitEntity(Entity p_36842_) {
        if (p_36842_ instanceof LivingEntity living && !MobUtils.canHurt(living, this)) {
            return false;
        }
        return super.canHitEntity(p_36842_);
    }

    public boolean isOnFire() {
        return false;
    }

    protected boolean shouldBurn() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!(pSource.getEntity() instanceof Player))
            return false;
        else {
            this.markHurt();
            Entity entity = pSource.getEntity();
            if (entity == null) {
                return false;
            }
            if (this.level().isClientSide) {
                return true;
            }
            Vec3 vec3 = entity.getLookAngle();
            this.setDeltaMovement(vec3);
            this.xPower = vec3.x * 0.1D;
            this.yPower = vec3.y * 0.1D;
            this.zPower = vec3.z * 0.1D;
            this.setOwner(entity);
            return true;
        }
    }
}
