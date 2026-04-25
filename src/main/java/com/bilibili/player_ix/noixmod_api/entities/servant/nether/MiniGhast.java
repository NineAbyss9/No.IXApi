
package com.bilibili.player_ix.noixmod_api.entities.servant.nether;

import com.bilibili.player_ix.noixmod_api.entities.projectile.LittleFireball;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.FlyingOwnable;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MiniGhast
extends FlyingOwnable {
    private static final EntityDataAccessor<Boolean> CHARGING;

    public MiniGhast(EntityType<? extends MiniGhast> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.moveControl = new GhastFlyingMoveControl(this);
    }

    public boolean canAttack(LivingEntity p_21171_) {
        return MobUtils.canHurt(p_21171_, this);
    }

    public boolean fireImmune() {
        return true;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_FIRE)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHARGING, false);
    }

    public void travel(Vec3 p_20818_) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, p_20818_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.800000011920929));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, p_20818_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            } else {
                BlockPos ground = this.getBlockPosBelowThatAffectsMyMovement();
                float f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }
                float f1 = 0.16277137F / (f * f * f);
                f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }
                this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, p_20818_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(f));
            }
        }
        this.calculateEntityAnimation(false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new FollowOwnerGoal<>(this, 1, 30, 10, true));
        this.goalSelector.addGoal(5, new GhastRandomMoveGoal(this));
        this.goalSelector.addGoal(7, new GhastLookGoal(this));
        this.goalSelector.addGoal(7, new ShootFireballGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GHAST_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.GHAST_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GHAST_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return 1.25f;
    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.GHAST.getDefaultLootTable();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return FlyingOwnable.createPathAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED,
                        0.5).add(Attributes.FOLLOW_RANGE, 100).add(Attributes.FLYING_SPEED, 8);
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public void setCharging(boolean b) {
        this.entityData.set(CHARGING, b);
    }

    private static class ShootFireballGoal
            extends Goal {
        private final MiniGhast ghast;
        public int chargeTime;

        public ShootFireballGoal(MiniGhast miniGhast) {
            this.ghast = miniGhast;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            return this.ghast.getTarget() != null;
        }

        @Override
        public void start() {
            super.start();
            this.chargeTime = 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.ghast.setCharging(false);
        }

        @Override
        public void tick() {
            LivingEntity $$0 = this.ghast.getTarget();
            if ($$0 == null) {
                return;
            }
            if ($$0.distanceToSqr(this.ghast) < 4096.0 && this.ghast.hasLineOfSight($$0)) {
                Level $$2 = this.ghast.level();
                ++this.chargeTime;
                if (this.chargeTime == 10 && !this.ghast.isSilent()) {
                    $$2.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
                }
                if (this.chargeTime == 20) {
                    if (!this.ghast.isSilent()) {
                        $$2.levelEvent(null, 1016, this.ghast.blockPosition(), 0);
                    }
                    this.chargeTime = -40;
                    LivingEntity target = this.ghast.getTarget();
                    if (target != null) {
                        double d1 = target.getX() - this.ghast.getX();
                        double d2 = target.getY(0.25) - this.ghast.getY(0.25);
                        double d3 = target.getZ() - this.ghast.getZ();
                        LittleFireball fireBall = new LittleFireball(this.ghast.level(), this.ghast, d1, d2, d3);
                        fireBall.setPosRaw(fireBall.getX(), this.ghast.getY() + 0.25, fireBall.getZ());
                        fireBall.setOwner(this.ghast);
                        this.ghast.level().addFreshEntity(fireBall);
                    }
                }
            } else if (this.chargeTime > 0) {
                --this.chargeTime;
            }
            this.ghast.setCharging(this.chargeTime > 10);
        }
    }

    static {
        CHARGING = SynchedEntityData.defineId(MiniGhast.class, EntityDataSerializers.BOOLEAN);
    }
}
