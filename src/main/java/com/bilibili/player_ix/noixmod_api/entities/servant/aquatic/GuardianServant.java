
package com.bilibili.player_ix.noixmod_api.entities.servant.aquatic;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class GuardianServant
extends OwnableMob {
    private static final EntityDataAccessor<Boolean> DATA_ID_MOVING;
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET;
    private float clientSideTailAnimation;
    private float clientSideTailAnimationO;
    private float clientSideTailAnimationSpeed;
    private float clientSideSpikesAnimation;
    private float clientSideSpikesAnimationO;
    @Nullable
    private LivingEntity clientSideCachedAttackTarget;
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    @Nullable
    protected RandomStrollGoal randomStrollGoal;
    public final Guardian guardian=null;
    public GuardianServant(EntityType<? extends OwnableMob> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new GuardianMoveControl(this);
        this.clientSideTailAnimation = this.random.nextFloat();
        this.clientSideTailAnimationO = this.clientSideTailAnimation;
    }

    protected void registerGoals() {
        MoveTowardsRestrictionGoal $$0 = new MoveTowardsRestrictionGoal(this, 1.0);
        this.randomStrollGoal = new RandomStrollGoal(this, 1.0, 80);
        this.goalSelector.addGoal(4, new GuardianAttackGoal(this));
        this.goalSelector.addGoal(5, $$0);
        this.goalSelector.addGoal(7, this.randomStrollGoal);
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.randomStrollGoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        $$0.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,
                LivingEntity.class, 10, true, false,
                new GuardianAttackSelector(this)));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0).add(
                Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE,
                16.0).add(Attributes.MAX_HEALTH, 30.0);
    }

    protected PathNavigation createNavigation(Level p_32846_) {
        return new WaterBoundPathNavigation(this, p_32846_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_MOVING, false);
        this.entityData.define(DATA_ID_ATTACK_TARGET, 0);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    public boolean isMoving() {
        return this.entityData.get(DATA_ID_MOVING);
    }

    public void setMoving(boolean p_32862_) {
        this.entityData.set(DATA_ID_MOVING, p_32862_);
    }

    public int getAttackDuration() {
        return 80;
    }

    public void setActiveAttackTarget(int p_32818_) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, p_32818_);
    }

    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    public int getAttackTarget() {
        return this.entityData.get(DATA_ID_ATTACK_TARGET);
    }

    @Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity $$0 = this.level().getEntity(this.getAttackTarget());
                if ($$0 instanceof LivingEntity) {
                    this.clientSideCachedAttackTarget = (LivingEntity)$$0;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (DATA_ID_ATTACK_TARGET.equals(pKey)) {
            this.clientSideAttackTime = 0;
            this.clientSideCachedAttackTarget = null;
        }
    }

    public int getAmbientSoundInterval() {
        return 160;
    }

    protected SoundEvent getAmbientSound() {
        return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_AMBIENT
                : SoundEvents.GUARDIAN_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource p_32852_) {
        return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_HURT : SoundEvents.GUARDIAN_HURT_LAND;
    }

    protected SoundEvent getDeathSound() {
        return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_DEATH : SoundEvents.GUARDIAN_DEATH_LAND;
    }

    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    protected float getStandingEyeHeight(Pose p_32843_, EntityDimensions p_32844_) {
        return p_32844_.height * 0.5F;
    }

    public float getWalkTargetValue(BlockPos p_32831_, LevelReader p_32832_) {
        return p_32832_.getFluidState(p_32831_).is(FluidTags.WATER) ? 10.0F + p_32832_
                .getPathfindingCostFromLightLevels(p_32831_) : super.getWalkTargetValue(p_32831_, p_32832_);
    }

    public void aiStep() {
        if (this.isAlive()) {
            if (this.level().isClientSide) {
                this.clientSideTailAnimationO = this.clientSideTailAnimation;
                Vec3 $$1;
                if (!this.isInWater()) {
                    this.clientSideTailAnimationSpeed = 2.0F;
                    $$1 = this.getDeltaMovement();
                    if ($$1.y > 0.0 && this.clientSideTouchedGround && !this.isSilent()) {
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), this.getFlopSound(),
                                this.getSoundSource(), 1.0F, 1.0F, false);
                    }
                    this.clientSideTouchedGround = $$1.y < 0.0 && this.level().loadedAndEntityCanStandOn(
                            this.blockPosition().below(), this);
                } else if (this.isMoving()) {
                    if (this.clientSideTailAnimationSpeed < 0.5F) {
                        this.clientSideTailAnimationSpeed = 4.0F;
                    } else {
                        this.clientSideTailAnimationSpeed += (0.5F - this.clientSideTailAnimationSpeed) * 0.1F;
                    }
                } else {
                    this.clientSideTailAnimationSpeed += (0.125F - this.clientSideTailAnimationSpeed) * 0.2F;
                }
                this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
                this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
                if (!this.isInWaterOrBubble()) {
                    this.clientSideSpikesAnimation = this.random.nextFloat();
                } else if (this.isMoving()) {
                    this.clientSideSpikesAnimation += (0.0F - this.clientSideSpikesAnimation) * 0.25F;
                } else {
                    this.clientSideSpikesAnimation += (1.0F - this.clientSideSpikesAnimation) * 0.06F;
                }
                if (this.isMoving() && this.isInWater()) {
                    $$1 = this.getViewVector(0.0F);
                    for(int $$2 = 0; $$2 < 2; ++$$2) {
                        this.level().addParticle(ParticleTypes.BUBBLE, this.getRandomX(
                    0.5) - $$1.x * 1.5, this.getRandomY() - $$1.y * 1.5,
                                this.getRandomZ(0.5) - $$1.z * 1.5,
                                0.0, 0.0, 0.0);
                    }
                }
                if (this.hasActiveAttackTarget()) {
                    if (this.clientSideAttackTime < this.getAttackDuration()) {
                        ++this.clientSideAttackTime;
                    }
                    LivingEntity $$3 = this.getActiveAttackTarget();
                    if ($$3 != null) {
                        this.getLookControl().setLookAt($$3, 90.0F, 90.0F);
                        this.getLookControl().tick();
                        double $$4 = this.getAttackAnimationScale(0.0F);
                        double $$5 = $$3.getX() - this.getX();
                        double $$6 = $$3.getY(0.5) - this.getEyeY();
                        double $$7 = $$3.getZ() - this.getZ();
                        double $$8 = Math.sqrt($$5 * $$5 + $$6 * $$6 + $$7 * $$7);
                        $$5 /= $$8;
                        $$6 /= $$8;
                        $$7 /= $$8;
                        double $$9 = this.random.nextDouble();
                        while($$9 < $$8) {
                            $$9 += 1.8 - $$4 + this.random.nextDouble() * (1.7 - $$4);
                            this.level().addParticle(ParticleTypes.BUBBLE, this.getX() + $$5 * $$9,
                                    this.getEyeY() + $$6 * $$9, this.getZ() + $$7 * $$9,
                                    0.0, 0.0, 0.0);
                        }
                    }
                }
            }
            if (this.isInWaterOrBubble()) {
                this.setAirSupply(300);
            } else if (this.onGround()) {
                this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F
                        - 1.0F) * 0.4F, 0.5, (this.random.nextFloat() * 2.0F - 1.0F) * 0.4F));
                this.setYRot(this.random.nextFloat() * 360.0F);
                this.setOnGround(false);
                this.hasImpulse = true;
            }
            if (this.hasActiveAttackTarget()) {
                this.setYRot(this.yHeadRot);
            }
        }
        super.aiStep();
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.GUARDIAN_FLOP;
    }

    public float getTailAnimation(float p_32864_) {
        return Mth.lerp(p_32864_, this.clientSideTailAnimationO, this.clientSideTailAnimation);
    }

    public float getSpikesAnimation(float p_32866_) {
        return Mth.lerp(p_32866_, this.clientSideSpikesAnimationO, this.clientSideSpikesAnimation);
    }

    public float getAttackAnimationScale(float p_32813_) {
        return ((float)this.clientSideAttackTime + p_32813_) / (float)this.getAttackDuration();
    }

    public float getClientSideAttackTime() {
        return this.clientSideAttackTime;
    }

    public boolean checkSpawnObstruction(LevelReader p_32829_) {
        return p_32829_.isUnobstructed(this);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.level().isClientSide) {
            return false;
        } else {
            if (!this.isMoving() && !pSource.is(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
                    && !pSource.is(DamageTypes.THORNS)) {
                Entity var4 = pSource.getDirectEntity();
                if (var4 instanceof LivingEntity $$2) {
                    $$2.hurt(this.damageSources().thorns(this), 2.0F);
                }
            }
            if (this.randomStrollGoal != null) {
                this.randomStrollGoal.trigger();
            }
            return super.hurt(pSource, pAmount);
        }
    }

    public int getMaxHeadXRot() {
        return 180;
    }

    public void travel(Vec3 p_32858_) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(0.1F, p_32858_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (!this.isMoving() && this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(p_32858_);
        }
    }

    static {
        DATA_ID_MOVING = SynchedEntityData.defineId(GuardianServant.class, EntityDataSerializers.BOOLEAN);
        DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(GuardianServant.class, EntityDataSerializers.INT);
    }

    private static class GuardianMoveControl extends MoveControl {
        private final GuardianServant guardian;

        public GuardianMoveControl(GuardianServant p_32886_) {
            super(p_32886_);
            this.guardian = p_32886_;
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO && !this.guardian.getNavigation().isDone()) {
                Vec3 $$0 = new Vec3(this.wantedX - this.guardian.getX(), this.wantedY
                        - this.guardian.getY(), this.wantedZ - this.guardian.getZ());
                double $$1 = $$0.length();
                double $$2 = $$0.x / $$1;
                double $$3 = $$0.y / $$1;
                double $$4 = $$0.z / $$1;
                float $$5 = (float)(Mth.atan2($$0.z, $$0.x) * 57.2957763671875) - 90.0F;
                this.guardian.setYRot(this.rotlerp(this.guardian.getYRot(), $$5, 90.0F));
                this.guardian.yBodyRot = this.guardian.getYRot();
                float $$6 = (float)(this.speedModifier * this.guardian.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float $$7 = Mth.lerp(0.125F, this.guardian.getSpeed(), $$6);
                this.guardian.setSpeed($$7);
                double $$8 = Math.sin(this.guardian.tickCount + this.guardian.getId() * 0.5) * 0.05;
                double $$9 = Math.cos(this.guardian.getYRot() * 0.017453292F);
                double $$10 = Math.sin(this.guardian.getYRot() * 0.017453292F);
                double $$11 = Math.sin(this.guardian.tickCount + this.guardian.getId() * 0.75) * 0.05;
                this.guardian.setDeltaMovement(this.guardian.getDeltaMovement().add($$8 * $$9,
                        $$11 * ($$10 + $$9) * 0.25 + (double)$$7 * $$3 * 0.1, $$8 * $$10));
                LookControl $$12 = this.guardian.getLookControl();
                double $$13 = this.guardian.getX() + $$2 * 2.0;
                double $$14 = this.guardian.getEyeY() + $$3 / $$1;
                double $$15 = this.guardian.getZ() + $$4 * 2.0;
                double $$16 = $$12.getWantedX();
                double $$17 = $$12.getWantedY();
                double $$18 = $$12.getWantedZ();
                if (!$$12.isLookingAtTarget()) {
                    $$16 = $$13;
                    $$17 = $$14;
                    $$18 = $$15;
                }
                this.guardian.getLookControl().setLookAt(Mth.lerp(0.125, $$16, $$13), Mth.lerp(
                        0.125, $$17, $$14), Mth.lerp(0.125, $$18, $$15), 10.0F, 40.0F);
                this.guardian.setMoving(true);
            } else {
                this.guardian.setSpeed(0.0F);
                this.guardian.setMoving(false);
            }
        }
    }

    static class GuardianAttackGoal extends Goal {
        private final GuardianServant guardian;
        private int attackTime;
        private final boolean elder;

        public GuardianAttackGoal(GuardianServant p_32871_) {
            this.guardian = p_32871_;
            this.elder = p_32871_ instanceof ElderGuardianServant;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity $$0 = this.guardian.getTarget();
            return $$0 != null && $$0.isAlive();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.elder || this.guardian.getTarget() != null &&
                    this.guardian.distanceToSqr(this.guardian.getTarget()) > 9.0);
        }

        public void start() {
            this.attackTime = -10;
            this.guardian.getNavigation().stop();
            LivingEntity $$0 = this.guardian.getTarget();
            if ($$0 != null) {
                this.guardian.getLookControl().setLookAt($$0, 90.0F, 90.0F);
            }
            this.guardian.hasImpulse = true;
        }

        public void stop() {
            this.guardian.setActiveAttackTarget(0);
            this.guardian.setTarget(null);
            if (this.guardian.randomStrollGoal != null) {
                this.guardian.randomStrollGoal.trigger();
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = this.guardian.getTarget();
            if ($$0 != null) {
                this.guardian.getNavigation().stop();
                this.guardian.getLookControl().setLookAt($$0, 90.0F, 90.0F);
                if (!this.guardian.hasLineOfSight($$0)) {
                    this.guardian.setTarget(null);
                } else {
                    ++this.attackTime;
                    if (this.attackTime == 0) {
                        this.guardian.setActiveAttackTarget($$0.getId());
                        if (!this.guardian.isSilent()) {
                            this.guardian.level().broadcastEntityEvent(this.guardian, (byte)21);
                        }
                    } else if (this.attackTime >= this.guardian.getAttackDuration()) {
                        float $$1 = 1.0F;
                        if (this.guardian.level().getDifficulty() == Difficulty.HARD) {
                            $$1 += 2.0F;
                        }
                        if (this.elder) {
                            $$1 += 2.0F;
                        }
                        $$0.hurt(this.guardian.damageSources().indirectMagic(this.guardian, this.guardian), $$1);
                        $$0.hurt(this.guardian.damageSources().mobAttack(this.guardian), (float)this.guardian.getAttributeValue
                                (Attributes.ATTACK_DAMAGE));
                        this.guardian.setTarget(null);
                    }
                    super.tick();
                }
            }
        }
    }

    private static class GuardianAttackSelector implements Predicate<LivingEntity> {
        private final GuardianServant guardian;

        public GuardianAttackSelector(GuardianServant p_32879_) {
            this.guardian = p_32879_;
        }

        public boolean test(@Nullable LivingEntity p_32881_) {
            return (p_32881_ instanceof Player || p_32881_ instanceof Squid || p_32881_ instanceof Axolotl)
                    && p_32881_.distanceToSqr(this.guardian) > 9.0;
        }
    }
}
