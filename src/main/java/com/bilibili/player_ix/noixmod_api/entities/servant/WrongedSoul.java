
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.github.NineAbyss9.ix_api.api.mobs.IProjectile;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.api.mobs.NihilitySummonedMobs;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticFire;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.ObjectUtil;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WrongedSoul
extends NihilitySummonedMobs
implements IProjectile {
    private int lifeTicks = 0;
    @Nullable
    private Vec3 chargePos = null;
    public boolean stay = false;
    private float damage;
    public WrongedSoul(EntityType<WrongedSoul> e, Level l) {
        super(e, l);
        this.noPhysics = true;
        this.damage = getDamage();
    }

    public void setDamage(float f) {
        this.damage = f;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public boolean isStay() {
        return this.stay;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new OwnableTargetGoal<>(this, true));
    }

    public boolean isHostile() {
        return super.isHostile() || this.isUnowned();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
                                        MobSpawnType pReason, @Nullable SpawnGroupData p_21437_,
                                        @Nullable CompoundTag pDataTag) {
        pLevel.getLevel().sendParticles(ParticleTypes.SOUL, this.getX(), this.getY() + 1, this.getZ(),
                12, 1, 0, 1, 0);
        pLevel.getLevel().sendParticles(NoixmodAPIParticleTypes.SUMMON_PARTICLE.get(),
                this.getX(), this.getY() + 1, this.getZ(), 12, 0, 0,
                0, 0);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, p_21437_, pDataTag);
    }

    public int getLifeTick() {
        return this.lifeTicks;
    }

    public void setLifeTick(int i) {
        this.lifeTicks = i;
    }

    public void setState(int flag) {
        if (flag == 1) {
            this.stay = true;
        }
        if (flag == 2) {
            this.setAggressive(true);
        }
        if (flag != 1 && flag != 2) {
            throw new ObjectUtil.UnsupportedTypeException("WrongedSoul");
        }
    }

    public void setChasing(Vec3 vec3) {
        this.chargePos = vec3;
    }

    public float getDamage() {
        return NoixmodAPIMainConfig.HorrorMode.get() ? 19F : 12F;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("Stay", this.stay);
        tag.putBoolean("isChasing", this.isAggressive());
        if (this.chargePos != null) {
            tag.put("ChangePos", Vec9.createVec3Tag(this.chargePos, "ChangePos"));
        }
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        this.stay = tag.getBoolean("Stay");
        this.setChasing(Vec9.readVec3Tag(tag, "ChangePos"));
        super.readAdditionalSaveData(tag);
    }

    public void tick() {
        super.tick();
        ++this.lifeTicks;
        this.setYRot(this.getYHeadRot());
        this.yBodyRot = this.getYRot();
        LivingEntity lie = this.getTarget();
        if (this.isAggressive()) {
            if (this.chargePos != null) {
                this.setDeltaMovement(this.chargePos);
            }
            HitResult hitResult;
            if ((hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity)).getType() !=
                    HitResult.Type.MISS) {
                this.onHit(hitResult);
            }
        }
        if (lie != null) {
            this.getLookControl().setLookAt(lie, 30f, this.getMaxHeadXRot());
        }
        if (this.level().isClientSide()) {
            this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get(), this.getRandomX(0.5),
                    this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.15, 0);
        }
        if (this.isStay()) {
            if (this.getLifeTick() < 140) {
                this.setDeltaMovement(new Vec3(0, 0.05, 0));
            }
            if (this.getLifeTick() > 140 && this.getLifeTick() < 200) {
                this.setDeltaMovement(Vec3.ZERO);
            }
            if (this.getTarget() != null) {
                if (this.getLifeTick() == 140) {
                    double x = this.getTarget().getX();
                    double y = this.getTarget().getY();
                    double z = this.getTarget().getZ();
                    this.teleportTo(x, y, z);
                }
            }
            if (this.getLifeTick() >= 200) {
                this.explosion();
            }
        } else {
            if (this.getLifeTick() < 50) {
                this.setDeltaMovement(new Vec3(0, 0.2, 0));
            }
            if (this.getLifeTick() == 50) {
                if (lie != null) {
                    this.playSound(SoundEvents.RAVAGER_ROAR);
                    double q = NoixmodAPIMainConfig.HorrorMode.get() ? 0.4 : 0.3;
                    Vec3 vec3 = this.getVec3(lie, q);
                    this.setChasing(vec3);
                    this.setAggressive(true);
                }
            }
            if (this.getLifeTick() > 300) {
                this.damage();
                this.discard();
            }
        }
    }

    private Vec3 getVec3(LivingEntity lie, double q) {
        double xDPower = this.getX() - lie.getX();
        double yDPower = this.getY() - lie.getY();
        double zDPower = this.getZ() - lie.getZ();
        double d = Math.sqrt(xDPower * xDPower + yDPower * yDPower + zDPower * zDPower);
        double xPower = -(xDPower / d * 5.0 * q);
        double yPower = -(yDPower / d * 5.0 * q);
        double zPower = -(zDPower / d * 5.0 * q);
        return new Vec3(xPower, yPower, zPower);
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return null;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return null;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypes.GENERIC_KILL) && !(pSource.is(DamageTypes.PLAYER_ATTACK))) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WrongedSoul.createPathAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.FOLLOW_RANGE, 100)
                .add(Attributes.MAX_HEALTH, 10);
    }

    public void onHit(HitResult result) {
        if (result instanceof EntityHitResult hitResult && this.isAggressive()) {
            this.onHitEntity(hitResult);
        }
        if (result instanceof BlockHitResult block && this.isAggressive()) {
            this.onHitBlock(block);
        }
        for (int i = 0; i < 2; ++i) {
            double r = this.random.nextDouble() * Maths.trueOrFalse();
            double r1 = this.random.nextDouble() * Maths.trueOrFalse();
            NihilisticFire fire = (NoixmodAPIEntities.NIHILISTIC_FIRE.get()).create(this.level());
            if (fire != null) {
                fire.setOwner(ownerOrThis(this, this));
                fire.moveTo(this.getX() + r, this.getY(), this.getZ() + r1);
                this.level().addFreshEntity(fire);
            }
        }
        this.damage();
    }

    public void onHitEntity(EntityHitResult pResult) {
        if (pResult.getEntity() instanceof LivingEntity lie) {
            if (!this.level().isClientSide()) {
                ServerLevel level = (ServerLevel)this.level();
                level.sendParticles(NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get(),
                        this.getRandomX(0.6), this.getRandomY(), this.getRandomZ(0.6),
                9, 0, 0, 0, 0);
            }
            if (this.canHitEntity(lie)) {
                pResult.getEntity().push(this);
                pResult.getEntity().hurt(this.damageSources().indirectMagic(this, this.getOwner()),
                        12f);
                this.damage();
            }
        }
    }

    public void onHitBlock(BlockHitResult pResult) {
        this.damage();
    }

    public boolean canHitEntity(Entity pEntity) {
        return !(pEntity instanceof Nihilistic) && !(pEntity == this.getOwner());
    }

    protected void damage() {
        double d = this.random.nextGaussian() * 0.2;
        double d1 = this.random.nextGaussian() * 0.2;
        double d2 = this.random.nextGaussian() * 0.2;
        MobUtils.rangeHurtAndFire(4, 0.25, 4, this, this.damageSources().indirectMagic(this,
                this.getOwner()), this.damage, 2);
        if (this.level() instanceof ServerLevel) {
            WorldUtil.getServerLevel(this).sendParticles(NoixmodAPIParticleTypes.PURPLE_ATTACK.get(), this.getX(),
                    this.getY() + 0.25, this.getZ(), 50, d, d1, d2, 0.25);
        }
        this.discard();
    }

    protected void explosion() {
        MobUtils.rangeHurt(5, 5, 5, this, this.damageSources().starve(), this.damage);
        if (this.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.5, this.getZ(),
                    50, 0, 0, 0, 0.5);
        }
        this.discard();
    }

    public boolean canAttack(LivingEntity livingEntity) {
        return livingEntity.canBeSeenAsEnemy() && MobUtils.canHurt(livingEntity, this);
    }

    public boolean isOnFire() {
        return this.isAggressive();
    }
}
