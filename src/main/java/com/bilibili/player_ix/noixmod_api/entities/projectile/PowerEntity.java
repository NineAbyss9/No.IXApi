
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.api.mobs.effect.EffectInstance;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PowerEntity
extends OwnableMob {
    public PowerEntity(EntityType<? extends PowerEntity> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 5;
        this.setNoGravity(true);
        this.setLifeTick(Maths.toTick(10));
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.POWER_ENTITY.get();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getLifeTick() <= 0) {
            if (this.getOwner() instanceof Apostle apostle) {
                apostle.setArrowDamagePlus();
            } else {
                double x = this.random.nextGaussian() * 0.3;
                double y = this.random.nextGaussian() * 0.3;
                double z = this.random.nextGaussian() * 0.3;
                LivingEntity entity = this.getOwner();
                if (entity != null) {
                    this.getOwner().addEffect(EffectInstance.create(MobEffects.DAMAGE_BOOST, Maths.toTick(20),
                            1));
                    this.getOwner().addEffect(EffectInstance.create(MobEffects.DAMAGE_RESISTANCE, Maths.toTick(20),
                            1));
                    if (entity.level() instanceof ServerLevel level) {
                        level.sendParticles(ParticleTypes.SOUL, entity.getX(), entity.getY(), entity.getZ(), 15, x, y, z, x);
                    }
                }
            }
        }
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            double x = this.random.nextGaussian() * 0.2;
            double y = this.random.nextGaussian() * 0.2;
            double z = this.random.nextGaussian() * 0.2;
            this.level().addParticle(ParticleTypes.SOUL, this.getX(), this.getY(), this.getZ(), x, y, z);
        }
        this.setDeltaMovement(new Vec3(0, 0, 0));
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.SOUL_ESCAPE;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.SOUL_ESCAPE;
    }

    public void die(DamageSource p_21014_) {
        if (this.getOwner() != null) {
            if (this.getLastDamageSource() != null) {
                this.getOwner().hurt(this.getLastDamageSource(), 4);
            } else {
                this.getOwner().hurt(this.damageSources().starve(), 4);
            }
        } else {
            Entity entity = p_21014_.getEntity() == null ? p_21014_.getDirectEntity() : p_21014_.getEntity();
            if (entity instanceof LivingEntity living) {
                if (living instanceof Apostle apostle) {
                    apostle.setArrowDamagePlus();
                    apostle.setTraits(this.random.nextInt(10));
                } else {
                    living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Maths.toTick(20), 1));
                    living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Maths.toTick(20), 0));
                }
            }
        }
        super.die(p_21014_);
    }

    public boolean hasLife() {
        return TRUE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 120);
    }
}
