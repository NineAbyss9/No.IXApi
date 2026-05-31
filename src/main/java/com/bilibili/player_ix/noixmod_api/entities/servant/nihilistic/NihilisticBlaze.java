
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import com.github.NineAbyss9.ix_api.api.mobs.NihilitySummonedMobs;
import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticFireball;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class NihilisticBlaze
extends NihilitySummonedMobs {
    private float allowedHeightOffset = 0.5F;
    private int nextHeightOffsetChangeTick;
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    public NihilisticBlaze(EntityType<? extends NihilisticBlaze> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    public void aiStep() {
        super.aiStep();
        if (!this.onGround() && this.getDeltaMovement().y < 0.0) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
        }
        if (this.level().isClientSide) {
            if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                this.level().playLocalSound(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5,
                        SoundEvents.BLAZE_BURN, this.getSoundSource(), 1.0F + this.random.nextFloat(),
                        this.random.nextFloat() * 0.7F + 0.3F, false);
            }
            this.level().addParticle(NoixmodAPIParticleTypes.SMALL_FIRE.get(), this.getRandomX(0.5),
                    this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
        }
    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.BLAZE.getDefaultLootTable();
    }

    protected void customServerAiStep() {
        --this.nextHeightOffsetChangeTick;
        if (this.nextHeightOffsetChangeTick <= 0) {
            this.nextHeightOffsetChangeTick = 100;
            this.allowedHeightOffset = (float)this.random.triangle(0.5, 6.891);
        }
        LivingEntity $$0 = this.getTarget();
        if ($$0 != null && $$0.getEyeY() > this.getEyeY() + (double)this.allowedHeightOffset && this.canAttack($$0)) {
            Vec3 $$1 = this.getDeltaMovement();
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, (0.3 - $$1.y) * 0.3, 0.0));
            this.hasImpulse = true;
        }
        super.customServerAiStep();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.EXPLOSION)) {
            return false;
        }
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public float getVoicePitch() {
        return 0.25f;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_32235_) {
        return SoundEvents.BLAZE_HURT;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new FollowOwnerGoal<>(this, 0.7,
                30F, 10F, false));
        this.goalSelector.addGoal(4, new BlazeAttackGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0,
                0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetGoal();
        this.targetSelector.addGoal(0, new OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    protected void targetGoal() {
        this.targetSelector.addGoal(0, new OwnableTargetGoal<>(this, true));
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NihilisticBlaze.createPathAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 128).add(Attributes.KNOCKBACK_RESISTANCE, 0.35);
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(NihilisticBlaze.class, EntityDataSerializers.BYTE);
    }

    protected static class BlazeAttackGoal extends Goal {
        private final NihilisticBlaze blaze;
        private int attackStep;
        private int attackTime;
        private int lastSeen;

        public BlazeAttackGoal(NihilisticBlaze p_32247_) {
            this.blaze = p_32247_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity $$0 = this.blaze.getTarget();
            return $$0 != null && $$0.isAlive() && this.blaze.canAttack($$0);
        }

        public void start() {
            this.attackStep = 0;
        }

        public void stop() {
            this.blaze.setCharged(false);
            this.lastSeen = 0;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            --this.attackTime;
            LivingEntity $$0 = this.blaze.getTarget();
            if ($$0 != null) {
                boolean $$1 = this.blaze.getSensing().hasLineOfSight($$0);
                if ($$1) {
                    this.lastSeen = 0;
                } else {
                    ++this.lastSeen;
                }
                double $$2 = this.blaze.distanceToSqr($$0);
                if ($$2 < 4.0) {
                    if (!$$1) {
                        return;
                    }
                    if (this.attackTime <= 0) {
                        this.attackTime = 20;
                        this.blaze.doHurtTarget($$0);
                    }
                    this.blaze.getMoveControl().setWantedPosition($$0.getX(), $$0.getY(), $$0.getZ(), 1.0D);
                } else if ($$2 < this.getFollowDistance() * this.getFollowDistance() && $$1) {
                    double $$3 = $$0.getX() - this.blaze.getX();
                    double $$4 = $$0.getY(0.5D) - this.blaze.getY(0.5D);
                    double $$5 = $$0.getZ() - this.blaze.getZ();
                    if (this.attackTime <= 0) {
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            this.attackTime = 60;
                            this.blaze.setCharged(true);
                        } else if (this.attackStep <= 4) {
                            this.attackTime = 6;
                        } else {
                            this.attackTime = 100;
                            this.attackStep = 0;
                            this.blaze.setCharged(false);
                        }
                        if (this.attackStep > 1) {
                            if (!this.blaze.isSilent()) {
                                this.blaze.level().levelEvent(null, 1018, this.blaze.blockPosition(), 0);
                            }
                            for(int $$7 = 0; $$7 < 1; ++$$7) {
                                NihilisticFireball $$8 = new NihilisticFireball(this.blaze.level(), this.blaze, $$3, $$4, $$5);
                                $$8.setPos($$8.getX(), this.blaze.getY(0.5) + 0.5, $$8.getZ());
                                $$8.setOwner(this.blaze.getOwner());
                                this.blaze.level().addFreshEntity($$8);
                            }
                        }
                    }
                    this.blaze.getLookControl().setLookAt($$0, 10.0F, 10.0F);
                } else if (this.lastSeen < 5) {
                    this.blaze.getMoveControl().setWantedPosition($$0.getX(), $$0.getY(), $$0.getZ(), 1.0);
                }
            }
        }
        private double getFollowDistance() {
            return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }

    public boolean isOnFire() {
        return this.isCharged() && !WorldUtil.isRainingAt(this);
    }

    private boolean isCharged() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    private void setCharged(boolean p_32241_) {
        byte $$1 = this.entityData.get(DATA_FLAGS_ID);
        if (p_32241_) {
            $$1 = (byte)($$1 | 1);
        } else {
            $$1 &= -2;
        }
        this.entityData.set(DATA_FLAGS_ID, $$1);
    }
}
