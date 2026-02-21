
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.NihilitySummonedMobs;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticFireball;
import com.bilibili.player_ix.noixmod_api.entities.servant.WrongedSoul;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class NihilisticGhast
extends NihilitySummonedMobs {
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING =
            SynchedEntityData.defineId(NihilisticGhast.class, EntityDataSerializers.BOOLEAN);
    public float explosionPower = 12F;
    private int cooldown = 0;

    public boolean removeWhenFarAway(double d) {
        return d > Maths.square(100) && this.getOwner() == null;
    }

    public void travel(Vec3 p_20818_) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, p_20818_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8));
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
                float f1 = 0.163F / (f * f * f);
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

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getCooldown() > 0) {
            --this.cooldown;
        }
    }

    public void tick() {
        if (this.isCharging()) {
            this.cooldown = Maths.toTick(1);
        }
        super.tick();
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public boolean onClimbable() {
        return false;
    }

    protected void checkFallDamage(double p_20809_, boolean p_20810_, BlockState p_20811_, BlockPos p_20812_) {
    }

    public NihilisticGhast(EntityType<? extends NihilisticGhast> $$0, Level $$1) {
        super($$0, $$1);
        this.xpReward = 5;
        this.moveControl = new NihilityGhastMoveControl(this);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new FollowOwnerGoal<>(this, 1,
                30, 10, true));
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new GhastLookGoal(this));
        this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(2, new OwnableTargetGoal<>(this, false));
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return super.hurt(pSource, pAmount);
    }

    public boolean canAttack(LivingEntity $$0) {
        return super.canAttack($$0);
    }

    public void aiStep() {
        super.aiStep();
    }

    public boolean isCharging() {
        return this.entityData.get(DATA_IS_CHARGING);
    }

    public void setCharging(boolean $$0) {
        this.entityData.set(DATA_IS_CHARGING, $$0);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_CHARGING, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FOLLOW_RANGE, 1000.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.GHAST_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource $$0) {
        return SoundEvents.GHAST_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.GHAST_DEATH;
    }

    protected float getSoundVolume() {
        return 3.0f;
    }

    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putFloat("ExplosionPower", this.explosionPower);
    }

    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("ExplosionPower")) {
            this.explosionPower = $$0.getFloat("ExplosionPower");
        }
    }

    protected float getStandingEyeHeight(Pose $$0, EntityDimensions $$1) {
        return 1.3F;
    }

    private static class NihilityGhastMoveControl
    extends MoveControl {
        private final NihilisticGhast ghast;
        private int floatDuration;

        public NihilityGhastMoveControl(NihilisticGhast $$0) {
            super($$0);
            this.ghast = $$0;
        }

        public void tick() {
            if (!this.hasWanted()) {
                return;
            }
            if (this.floatDuration-- <= 0) {
                this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
                Vec3 $$0 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(),
                        this.wantedZ - this.ghast.getZ());
                double $$1 = $$0.length();
                if (this.ghast.cooldown <= 0) {
                    if (this.canReach($$0 = $$0.normalize(), Mth.ceil($$1))) {
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add($$0.scale(0.1)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 $$0, int $$1) {
            AABB $$2 = this.ghast.getBoundingBox();
            for (int $$3 = 1; $$3 < $$1; ++$$3) {
                $$2 = $$2.move($$0);
                if (this.ghast.level().noCollision(this.ghast, $$2)) continue;
                return false;
            }
            return true;
        }
    }

    private static class GhastLookGoal
    extends Goal {
        private final NihilisticGhast ghast;

        public GhastLookGoal(NihilisticGhast $$0) {
            this.ghast = $$0;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return true;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vec3 $$0 = this.ghast.getDeltaMovement();
                this.ghast.setYRot(-((float)Mth.atan2($$0.x(), $$0.z())) * 57.295776f);
            } else {
                LivingEntity $$1 = this.ghast.getTarget();
                if ($$1.distanceToSqr(this.ghast) < 4096.0) {
                    double $$3 = $$1.getX() - this.ghast.getX();
                    double $$4 = $$1.getZ() - this.ghast.getZ();
                    this.ghast.getLookControl().setLookAt($$1, 20F, this.ghast.getMaxHeadXRot());
                    this.ghast.setYRot(-((float)Mth.atan2($$3, $$4)) * 57.295776f);
                }
            }
            this.ghast.yBodyRot = this.ghast.getYRot();
        }
    }

    private static class GhastShootFireballGoal
    extends Goal {
        private final NihilisticGhast ghast;
        public int chargeTime;

        public GhastShootFireballGoal(NihilisticGhast $$0) {
            this.ghast = $$0;
        }

        public boolean canUse() {
            return this.ghast.getTarget() != null;
        }

        public void start() {
            this.chargeTime = 0;
        }

        public void stop() {
            this.ghast.setCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

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
                    ServerLevel level = (ServerLevel)this.ghast.level();
                    double x = this.ghast.getX();
                    double y = this.ghast.getY();
                    double z = this.ghast.getZ();
                    if (this.ghast.random.nextFloat() <= 0.1f) {
                        if (!this.ghast.isSilent()) {
                            this.ghast.playSound(SoundEvents.GHAST_AMBIENT);
                        }
                        WrongedSoul soul = new WrongedSoul(NoixmodAPIEntities.WRONGED_SOUL.get(), this.ghast.level());
                        soul.setDamage(this.ghast.explosionPower);
                        soul.setOwner(this.ghast);
                        soul.setTarget(this.ghast.getTarget());
                        soul.moveTo(x, y, z);
                        WorldUtil.nullableFinalizeSpawn(soul, level, level.getCurrentDifficultyAt(this.ghast.blockPosition()),
                                MobSpawnType.MOB_SUMMONED);
                        $$2.addFreshEntity(soul);
                    } else {
                        LivingEntity target = this.ghast.getTarget();
                        if (target != null) {
                            this.ghast.playSound(SoundEvents.GHAST_SHOOT);
                            double d1 = target.getX() - this.ghast.getX();
                            double d2 = target.getY(0.5) - this.ghast.getY(0.5);
                            double d3 = target.getZ() - this.ghast.getZ();
                            NihilisticFireball ball = new NihilisticFireball(this.ghast.level(), this.ghast, d1, d2, d3);
                            ball.setOwner(this.ghast);
                            ball.setDamage(this.ghast.explosionPower);
                            ball.setPosRaw(ball.getX(), this.ghast.getY(0.5) + 0.25, ball.getZ());
                            level.addFreshEntity(ball);
                        }
                    }
                    this.chargeTime = -40;
                }
            } else if (this.chargeTime > 0) {
                --this.chargeTime;
            }
            this.ghast.setCharging(this.chargeTime > 10);
        }
    }

   private static class RandomFloatAroundGoal extends Goal {
        private final NihilisticGhast ghast;

        public RandomFloatAroundGoal(NihilisticGhast p_32783_) {
            this.ghast = p_32783_;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl $$0 = this.ghast.getMoveControl();
            if (this.ghast.getCooldown() > 0) {
                return false;
            }
            if (!$$0.hasWanted()) {
                return true;
            } else {
                double $$1 = $$0.getWantedX() - this.ghast.getX();
                double $$2 = $$0.getWantedY() - this.ghast.getY();
                double $$3 = $$0.getWantedZ() - this.ghast.getZ();
                double $$4 = $$1 * $$1 + $$2 * $$2 + $$3 * $$3;
                return $$4 < 1.0 || $$4 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            LivingEntity target = this.ghast.getTarget();
            if (target == null) {
                RandomSource $$0 = this.ghast.getRandom();
                double $$1 = this.ghast.getX() + (double) (($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double $$2 = this.ghast.getY() + (double) (($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double $$3 = this.ghast.getZ() + (double) (($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.ghast.getMoveControl().setWantedPosition($$1, $$2, $$3, 0.25);
            } else {
                double x = target.getRandomX(1);
                double y = target.getRandomY() + 4;
                double z = target.getRandomZ(1);
                this.ghast.getMoveControl().setWantedPosition(x, y, z, 0.25);
            }
        }
    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.GHAST.getDefaultLootTable();
    }
}
