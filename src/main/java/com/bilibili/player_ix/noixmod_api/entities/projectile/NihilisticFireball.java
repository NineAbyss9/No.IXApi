
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import org.NineAbyss9.util.Option;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class NihilisticFireball
extends AbstractHurtingProjectile
implements Nihilistic {
    public double radius = 2D;
    public float damage = 8f;
    private boolean isMoveDown;
    private double speedModifier;
    public NihilisticFireball(EntityType<? extends NihilisticFireball> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public NihilisticFireball(Level level, LivingEntity living, double d, double dou, double o) {
        super(NoixmodAPIEntities.NIHILISTIC_FIREBALL.get(), living, d, dou, o, level);
    }

    public NihilisticFireball(double v, double v1, double v2, double v3, double v4, double v5, Level level) {
        super(NoixmodAPIEntities.NIHILISTIC_FIREBALL.get(), v, v1, v2, v3, v4, v5, level);
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.NIHILISTIC_FIREBALL.get();
    }

    public void addAdditionalSaveData(CompoundTag p_36848_) {
        super.addAdditionalSaveData(p_36848_);
    }

    public void readAdditionalSaveData(CompoundTag p_36844_) {
        super.readAdditionalSaveData(p_36844_);
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public void setDeltaMovement(Vec3 p_20257_) {
        if (this.isMoveDown()) {
            super.setDeltaMovement(new Vec3(0, this.speedModifier, 0));
        } else {
            super.setDeltaMovement(p_20257_);
        }
    }

    public void tick() {
        super.tick();
        if (this.tickCount % 75 == 0) {
            MobUtils.rangeHurt(4, 4, 4, this, this.getDs(), this.damage);
            this.playSound(SoundEvents.GENERIC_EXPLODE);
            if (this.level() instanceof ServerLevel world) {
                world.sendParticles(ParticleTypes.WITCH, this.getX(), this.getY() + 0.5, this.getZ(), 10,
                        1, 1, 1, 0.1);
                for (int i = 0; i < 2; ++i) {
                    NihilisticFire fire = (NoixmodAPIEntities.NIHILISTIC_FIRE.get()).create(world);
                    if (fire == null) continue;
                    if (!(this.getOwner() instanceof LivingEntity)) continue;
                    fire.setOwner((LivingEntity)this.getOwner());
                    fire.moveTo(this.getX() + this.random.nextDouble(), this.getY(), this.getZ() +
                            this.random.nextDouble());
                    world.addFreshEntity(fire);
                }
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        return !p_20122_.is(DamageTypes.GENERIC_KILL);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public DamageSource getDs() {
        return this.damageSources().indirectMagic(Optional.ofNullable(this.getOwner()).orElse(this), this.getOwner());
    }

    public void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        MobUtils.rangeHurt(4, 4, 4, this, this.getDs(), this.damage);
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1f, 1f);
        for (int i = 0; i < 2; ++i) {
            NihilisticFire fire = (NoixmodAPIEntities.NIHILISTIC_FIRE.get()).create(this.level());
            if (fire == null) continue;
            if (!(this.getOwner() instanceof LivingEntity)) continue;
            fire.setOwner((LivingEntity) this.getOwner());
            fire.moveTo(this.getX() + this.random.nextDouble(), this.getY(), this.getZ() +
                    this.random.nextDouble());
            this.level().addFreshEntity(fire);
        }
        if (this.level() instanceof ServerLevel) {
            double d = this.random.nextGaussian() * 0.1;
            WorldUtil.sendParticles(NoixmodAPIParticleTypes.PURPLE_ATTACK.get(), this, 12, d);
        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (pEntity instanceof LivingEntity living && !MobUtils.canHurt(living, this)) {
            return false;
        }
        return super.canHitEntity(pEntity);
    }

    public void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        if (entity instanceof LivingEntity living && MobUtils.canHurt(living, this)) {
            living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.NIHILISTIC.get(), 40), living);
            super.onHitEntity(pResult);
            this.discard();
        }
    }

    public void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.discard();
    }

    protected ParticleOptions getTrailParticle() {
        return Option.of(NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get()).ifOrElse(
                level().random.nextFloat() <= 0.01, NoixmodAPIParticleTypes.DARK_SPELL.get());
    }

    public boolean isMoveDown() {
        return this.isMoveDown;
    }

    public void setMoveDown(boolean flag) {
        this.isMoveDown = flag;
    }

    public void setSpeed(double speed) {
        this.speedModifier = speed;
    }
}
