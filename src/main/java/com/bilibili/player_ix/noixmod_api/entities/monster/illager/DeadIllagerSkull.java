
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

public class DeadIllagerSkull extends Raider implements Enemy, Ownable {
    @Nullable
    private BlockPos boundOrigin;
    private boolean aCodeBoolean;
    private short trueDeathTime = 0;
    @Nullable
    protected LivingEntity owner;
    @Nullable
    protected UUID ownerUUID;
    protected int lifeTicks;
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID
            = SynchedEntityData.defineId(DeadIllagerSkull.class, EntityDataSerializers.BYTE);
    public DeadIllagerSkull(EntityType<DeadIllagerSkull> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.lifeTicks = 20 * (50 + p_21684_.random.nextInt(20));
        this.moveControl = new VexMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new VexChargeAttackGoal());
        this.goalSelector.addGoal(8, new VexRandomMoveGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, LivingEntity.class, 10F));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(3, new OwnableMob.OwnableTargetGoal<>(this, true));
    }

    @Override
    public int getExperienceReward() {
        return 4;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, Maths.ZERO_BYTE);
    }

    @Override
    public void applyRaidBuffs(int i, boolean b) {}

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    protected float getStandingEyeHeight(Pose p_260180_, EntityDimensions p_260049_) {
        return p_260049_.height - 0.28125F;
    }

    public double getMyRidingOffset() {
        return 0.4;
    }

    public void readAdditionalSaveData(CompoundTag p_34008_) {
        super.readAdditionalSaveData(p_34008_);
        if (p_34008_.contains("BoundX")) {
            this.boundOrigin = new BlockPos(p_34008_.getInt("BoundX"), p_34008_.getInt("BoundY"), p_34008_.getInt("BoundZ"));
        }
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EMPTY;
    }

    public void addAdditionalSaveData(CompoundTag p_34015_) {
        super.addAdditionalSaveData(p_34015_);
        p_34015_.putShort("trueDeathTime", this.getTrueDeathTime());
        if (this.boundOrigin != null) {
            p_34015_.putInt("BoundX", this.boundOrigin.getX());
            p_34015_.putInt("BoundY", this.boundOrigin.getY());
            p_34015_.putInt("BoundZ", this.boundOrigin.getZ());
        }
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.setNoGravity(true);
        if (MobUtils.isHalfHealth(this) && !this.aCodeBoolean && this.isAlive()) {
            this.aCodeBoolean = true;
            this.playAngrySound();
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Maths.toTick(3), 0));
        }
        this.lifeTicks--;
        if (this.getLifeTick() <= 0) {
            this.hurt(this.damageSources().starve(), 1);
        }
        if (this.checker()) {
            for (ItemStack stack : this.getAllSlots()) {
                if (!Objects.equals(stack.getEquipmentSlot(), EquipmentSlot.HEAD)) {
                    stack.setCount(0);
                }
            }
        }
    }

    private boolean checker() {
        return !this.getMainHandItem().isEmpty() || !this.getOffhandItem().isEmpty() ||
                !this.getItemBySlot(EquipmentSlot.FEET).isEmpty() || !this.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ||
                !this.getItemBySlot(EquipmentSlot.LEGS).isEmpty();
    }

    @Override
    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public int getLifeTick() {
        return lifeTicks;
    }

    public void setLifeTick(int lifeTicks) {
        this.lifeTicks = lifeTicks;
    }

    public short getTrueDeathTime() {
        return this.trueDeathTime;
    }

    @Override
    protected void tickDeath() {
        this.trueDeathTime++;
        if (this.trueDeathTime >= 20) {
            if (!this.isRemoved()) {
                if (this.level().isClientSide()) {
                    this.spawnAnim();
                } else {
                    this.remove(RemovalReason.KILLED);
                }
            }
        }
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if (p_21375_ == 60) {
            if (!this.level().isClientSide()) {
                this.makeDeathParticle();
            }
        } else {
            super.handleEntityEvent(p_21375_);
        }
    }

    @Override
    public void move(MoverType p_19973_, Vec3 p_19974_) {
        super.move(p_19973_, p_19974_);
        this.checkInsideBlocks();
    }

    public void makeDeathParticle() {
        if (this.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }
    }

    private boolean getVexFlag() {
        int $$1 = this.entityData.get(DATA_FLAGS_ID);
        return ($$1 & 1) != 0;
    }

    private void setVexFlag(boolean p_33991_) {
        int $$2 = this.entityData.get(DATA_FLAGS_ID);
        if (p_33991_) {
            $$2 |= 1;
        } else {
            $$2 &= ~1;
        }
        this.entityData.set(DATA_FLAGS_ID, (byte)($$2 & 255));
    }

    public boolean isCharging() {
        return this.getVexFlag();
    }

    public void setCharging(boolean p_34043_) {
        this.setVexFlag(p_34043_);
    }

    protected void playAngrySound() {
        this.playSound(SoundEvents.RAVAGER_ROAR);
    }

    public void setItemSlot(EquipmentSlot p_21416_, ItemStack p_21417_) {
        super.setItemSlot(p_21416_, p_21417_);
    }

    public void setItemInHand(InteractionHand p_21009_, ItemStack p_21010_) {
        super.setItemInHand(p_21009_, p_21010_);
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOrigin) {
        this.boundOrigin = boundOrigin;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SOUL_ESCAPE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.SOUL_ESCAPE;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.SOUL_ESCAPE;
    }

    @Nullable
    public Team getTeam() {
        LivingEntity owner = this.getOwner();
        if (owner != null && !this.areBothOwner(owner)) {
            return owner.getTeam();
        }
        return super.getTeam();
    }

    @SuppressWarnings("deprecation")
    public float getLightLevelDependentMagicValue() {
        return 1f;
    }

    private class VexMoveControl extends MoveControl {
        public VexMoveControl(DeadIllagerSkull p_34062_) {
            super(p_34062_);
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 $$0 = new Vec3(this.wantedX - DeadIllagerSkull.this.getX(), this.wantedY - DeadIllagerSkull.this.getY(), this.wantedZ - DeadIllagerSkull.this.getZ());
                double $$1 = $$0.length();
                if ($$1 < DeadIllagerSkull.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    DeadIllagerSkull.this.setDeltaMovement(DeadIllagerSkull.this.getDeltaMovement().scale(0.5));
                } else {
                    DeadIllagerSkull.this.setDeltaMovement(DeadIllagerSkull.this.getDeltaMovement().add($$0.scale(
                            this.speedModifier * 0.05 / $$1)));
                    if (DeadIllagerSkull.this.getTarget() == null) {
                        Vec3 $$2 = DeadIllagerSkull.this.getDeltaMovement();
                        DeadIllagerSkull.this.setYRot(-((float) Mth.atan2($$2.x(), $$2.z())) * 57.295776F);
                    } else {
                        double $$3 = DeadIllagerSkull.this.getTarget().getX() - DeadIllagerSkull.this.getX();
                        double $$4 = DeadIllagerSkull.this.getTarget().getZ() - DeadIllagerSkull.this.getZ();
                        DeadIllagerSkull.this.setYRot(-((float)Mth.atan2($$3, $$4)) * 57.295776F);
                    }
                    DeadIllagerSkull.this.yBodyRot = DeadIllagerSkull.this.getYRot();
                }
            }
        }
    }

    private class VexChargeAttackGoal extends Goal {
        public VexChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity $$0 = DeadIllagerSkull.this.getTarget();
            if ($$0 != null && $$0.isAlive() && !DeadIllagerSkull.this.getMoveControl().hasWanted() && DeadIllagerSkull.this.random.nextInt(reducedTickDelay(7)) == 0) {
                return DeadIllagerSkull.this.distanceToSqr($$0) > 4.0;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return DeadIllagerSkull.this.getMoveControl().hasWanted() && DeadIllagerSkull.this.isCharging() && DeadIllagerSkull.this.getTarget() != null && DeadIllagerSkull.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity $$0 = DeadIllagerSkull.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                DeadIllagerSkull.this.moveControl.setWantedPosition($$1.x, $$1.y, $$1.z, 1.0);
            }
            DeadIllagerSkull.this.setCharging(true);
            DeadIllagerSkull.this.playSound(SoundEvents.RAVAGER_ROAR, 1.0F, 1.0F);
        }

        public void stop() {
            DeadIllagerSkull.this.setCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = DeadIllagerSkull.this.getTarget();
            if ($$0 != null) {
                if (DeadIllagerSkull.this.getBoundingBox().intersects($$0.getBoundingBox())) {
                    DeadIllagerSkull.this.doHurtTarget($$0);
                    DeadIllagerSkull.this.setCharging(false);
                } else {
                    double $$1 = DeadIllagerSkull.this.distanceToSqr($$0);
                    if ($$1 < 9.0) {
                        Vec3 $$2 = $$0.getEyePosition();
                        DeadIllagerSkull.this.moveControl.setWantedPosition($$2.x, $$2.y, $$2.z, 1.0);
                    }
                }

            }
        }
    }

    private class VexRandomMoveGoal extends Goal {
        public VexRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !DeadIllagerSkull.this.getMoveControl().hasWanted() && DeadIllagerSkull.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos $$0 = DeadIllagerSkull.this.getBoundOrigin();
            if ($$0 == null) {
                $$0 = DeadIllagerSkull.this.blockPosition();
            }
            for(int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = $$0.offset(DeadIllagerSkull.this.random.nextInt(15) - 7, DeadIllagerSkull.this.random.nextInt(11) - 5, DeadIllagerSkull.this.random.nextInt(15) - 7);
                if (DeadIllagerSkull.this.level().isEmptyBlock($$2)) {
                    DeadIllagerSkull.this.moveControl.setWantedPosition((double)$$2.getX() + 0.5, (double)$$2.getY() + 0.5, (double)$$2.getZ() + 0.5, 0.25);
                    if (DeadIllagerSkull.this.getTarget() == null) {
                        DeadIllagerSkull.this.getLookControl().setLookAt((double)$$2.getX() + 0.5, (double)$$2.getY() + 0.5, (double)$$2.getZ() + 0.5, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }
}
