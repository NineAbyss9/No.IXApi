
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.api.mobs.ApiRangedAttackMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class DrownedServant
extends AbstractZombieServant
implements ApiRangedAttackMob {
    protected boolean searchingForLand;
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;
    public DrownedServant(EntityType<? extends DrownedServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.setMaxUpStep(1.2f);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0);
        this.moveControl = new DrownedServantMoveControl(this);
        this.waterNavigation = new WaterBoundPathNavigation(this, this.level());
        this.groundNavigation = new GroundPathNavigation(this, this.level());
        this.populateDefaultEquipmentSlots(p_21684_.getRandom(), p_21684_.getCurrentDifficultyAt(this.blockPosition()));
        this.populateDefaultEquipmentEnchantments(p_21684_.getRandom(), p_21684_
                .getCurrentDifficultyAt(this.blockPosition()));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new DrownedGoToWaterGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal(this, 1));
        this.goalSelector.addGoal(2, new ThrowTridentGoal(this));
        this.goalSelector.addGoal(5, new DrownedGoToBeachGoal(this));
        this.goalSelector.addGoal(6, new SwimUpGoal(this, this.level().getSeaLevel()));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, DrownedServant.class).setAlertOthers());
        this.targetSelector.addGoal(2, new OwnableTargetGoal<>(this, true));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal<>(this));
    }

    public void tick() {
        super.tick();
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_217055_, DifficultyInstance p_217056_) {
        if (p_217055_.nextFloat() > 0.5F) {
            int $$2 = p_217055_.nextInt(16);
            if ($$2 < 10) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
        }
    }

    protected void populateDefaultEquipmentEnchantments(RandomSource p_217063_, DifficultyInstance p_217064_) {
        if (p_217063_.nextInt(10) <= 2) {
            EnchantmentHelper.enchantItem(p_217063_, this.getMainHandItem(), 3, false);
        }
    }

    public ParticleOptions getAmbientParticle() {
        return ParticleTypes.FALLING_WATER;
    }

    public void affect(LivingEntity living) {
        living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.WET.get(), Maths.toTick(15),
                this.level().getDifficulty().getId()));
    }

    public boolean doHurtTarget(Entity p_21372_) {
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        MobEffectInstance effect = null;
        if (p_21372_ instanceof LivingEntity living) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), living.getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);
            effect = living.getEffect(NoixmodAPIMobEffects.WET.get());
        }
        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            p_21372_.setSecondsOnFire(i * 4);
        }
        if (effect != null) {
            f += effect.getAmplifier();
        }
        boolean flag = p_21372_.hurt(this.damageSources().mobAttack(this), f);
        if (flag) {
            if (f1 > 0.0F && p_21372_ instanceof LivingEntity living) {
                living.knockback(f1 * 0.5F, Mth.sin(this.getYRot() * 0.017453292F),
                        -Mth.cos(this.getYRot() * 0.017453292F));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }
            if (p_21372_ instanceof Player player) {
                this.maybeDisableShield(player, this.getMainHandItem(), player.isUsingItem() ?
                        player.getUseItem() : ItemStack.EMPTY);
            }
            this.doEnchantDamageEffects(this, p_21372_);
            this.setLastHurtMob(p_21372_);
        }
        return flag;
    }

    public void maybeDisableShield(Player p_21425_, ItemStack p_21426_, ItemStack p_21427_) {
        if (!p_21426_.isEmpty() && !p_21427_.isEmpty() && p_21426_.getItem() instanceof AxeItem
                && p_21427_.is(Items.SHIELD)) {
            float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                p_21425_.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.level().broadcastEntityEvent(p_21425_, (byte)30);
            }
        }
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.DROWNED_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.DROWNED_HURT;
    }

    @Nullable
    protected  SoundEvent getDeathSound() {
        return SoundEvents.DROWNED_DEATH;
    }

    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        this.playSound(SoundEvents.DROWNED_STEP);
    }

    public float getVoicePitch() {
        return 0.95F;
    }

    public boolean isPushedByFluid(FluidType type) {
        return !this.isSwimming();
    }

    public void travel(Vec3 p_32394_) {
        if (this.isControlledByLocalInstance() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(0.01F, p_32394_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(p_32394_);
        }
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        return new ThrownTrident(this.level(), this, stack);
    }

    public void performRangedAttack(LivingEntity p_32356_, float v) {
        ThrownTrident $$2 = (ThrownTrident)this.getArrow(this.getMainHandItem(), 0);
        double $$3 = p_32356_.getX() - this.getX();
        double $$4 = p_32356_.getY(1D / 3D) - $$2.getY();
        double $$5 = p_32356_.getZ() - this.getZ();
        double $$6 = Math.sqrt($$3 * $$3 + $$5 * $$5);
        $$2.shoot($$3, $$4 + $$6 * 0.2, $$5, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity($$2);
    }

    public void updateSwimming() {
        if (!this.level().isClientSide()) {
            if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigation;
                this.setSwimming(false);
            }
        }
    }

    public boolean isVisuallySwimming() {
        return this.isSwimming();
    }

    public void setSearchingForLand(boolean p_32399_) {
        this.searchingForLand = p_32399_;
    }

    public boolean wantsToSwim() {
        if (this.searchingForLand) {
            return true;
        } else {
            LivingEntity a = this.getTarget();
            return a != null && a.isInWaterOrRain();
        }
    }

    protected boolean closeToNextPos() {
        Path $$0 = this.getNavigation().getPath();
        if ($$0 != null) {
            BlockPos $$1 = $$0.getTarget();
            double $$2 = this.distanceToSqr($$1.getX(), $$1.getY(), $$1.getZ());
            return $$2 < 4.0;
        }
        return false;
    }

    protected static class DrownedGoToWaterGoal extends Goal {
        private final PathfinderMob mob;
        private double wantedX;
        private double wantedY;
        private double wantedZ;
        private final double speedModifier;
        private final Level level;

        public DrownedGoToWaterGoal(PathfinderMob p_32425_) {
            this.mob = p_32425_;
            this.speedModifier = 1;
            this.level = p_32425_.level();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (!this.level.isDay()) {
                return false;
            } else if (this.mob.isInWater()) {
                return false;
            } else {
                Vec3 $$0 = this.getWaterPos();
                if ($$0 == null) {
                    return false;
                } else {
                    this.wantedX = $$0.x;
                    this.wantedY = $$0.y;
                    this.wantedZ = $$0.z;
                    return true;
                }
            }
        }

        public boolean canContinueToUse() {
            return !this.mob.getNavigation().isDone();
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }

        @Nullable
        private Vec3 getWaterPos() {
            RandomSource $$0 = this.mob.getRandom();
            BlockPos $$1 = this.mob.blockPosition();
            for(int $$2 = 0; $$2 < 10; ++$$2) {
                BlockPos $$3 = $$1.offset($$0.nextInt(20) - 10, 2 - $$0.nextInt(8),
                        $$0.nextInt(20) - 10);
                if (this.level.getBlockState($$3).is(Blocks.WATER)) {
                    return Vec3.atBottomCenterOf($$3);
                }
            }
            return null;
        }
    }

    protected static class DrownedGoToBeachGoal extends MoveToBlockGoal {
        private final DrownedServant drowned;

        public DrownedGoToBeachGoal(DrownedServant p_32409_) {
            super(p_32409_, 1.0, 8, 2);
            this.drowned = p_32409_;
        }

        public boolean canUse() {
            return super.canUse() && !this.drowned.level().isDay() && this.drowned.isInWater() && this.drowned.getY() >= (double)(this.drowned.level().getSeaLevel() - 3);
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        protected boolean isValidTarget(LevelReader p_32413_, BlockPos p_32414_) {
            BlockPos $$2 = p_32414_.above();
            return p_32413_.isEmptyBlock($$2) && p_32413_.isEmptyBlock($$2.above()) &&
                    p_32413_.getBlockState(p_32414_).entityCanStandOn(p_32413_, p_32414_, this.drowned);
        }

        public void start() {
            this.drowned.setSearchingForLand(false);
            this.drowned.navigation = this.drowned.groundNavigation;
            super.start();
        }
    }

    protected static class ThrowTridentGoal
    extends RangedAttackGoal {
        protected final DrownedServant servant;

        public ThrowTridentGoal(DrownedServant servant) {
            super(servant, 1.0, 40, 12F);
            this.servant = servant;
        }

        public boolean canUse() {
            LivingEntity living = this.servant.getTarget();
            if (living == null) {
                return false;
            }
            if (this.servant.distanceToSqr(living) < Maths.square(4)) {
                return false;
            }
            return super.canUse() && this.servant.getMainHandItem().is(Items.TRIDENT);
        }

        public boolean canContinueToUse() {
            LivingEntity living = this.servant.getTarget();
            if (living == null) {
                return false;
            }
            if (this.servant.distanceToSqr(living) < Maths.square(4)) {
                return false;
            }
            return super.canContinueToUse();
        }

        public void start() {
            super.start();
            this.servant.setAggressive(true);
            this.servant.startUsingItem(InteractionHand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.servant.stopUsingItem();
            this.servant.setAggressive(false);
            this.servant.setTarget(null);
        }
    }

    protected static class DrownedServantMoveControl
    extends MoveControl {
        protected final DrownedServant drowned;

        public DrownedServantMoveControl(DrownedServant drownedServant) {
            super(drownedServant);
            this.drowned = drownedServant;
        }

        public void tick() {
            LivingEntity $$0 = this.drowned.getTarget();
            if (this.drowned.wantsToSwim() && this.drowned.isInWater()) {
                if ($$0 != null && $$0.getY() > this.drowned.getY() || this.drowned.searchingForLand) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement()
                            .add(0.0, 0.002, 0.0));
                }
                if (this.operation != Operation.MOVE_TO || this.drowned.getNavigation().isDone()) {
                    this.drowned.setSpeed(0.0F);
                    return;
                }
                double $$1 = this.wantedX - this.drowned.getX();
                double $$2 = this.wantedY - this.drowned.getY();
                double $$3 = this.wantedZ - this.drowned.getZ();
                double $$4 = Math.sqrt($$1 * $$1 + $$2 * $$2 + $$3 * $$3);
                $$2 /= $$4;
                float $$5 = (float)(Mth.atan2($$3, $$1) * 57.2957763671875) - 90.0F;
                this.drowned.setYRot(this.rotlerp(this.drowned.getYRot(), $$5, 90.0F));
                this.drowned.yBodyRot = this.drowned.getYRot();
                float $$6 = (float)(this.speedModifier * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float $$7 = Mth.lerp(0.125F, this.drowned.getSpeed(), $$6);
                this.drowned.setSpeed($$7);
                this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add($$7 * $$1 * 0.005,
                        $$7 * $$2 * 0.1, $$7 * $$3 * 0.005));
            } else {
                if (!this.drowned.onGround()) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0,
                            -0.008, 0.0));
                }
                super.tick();
            }
        }
    }

    protected static class SwimUpGoal
    extends Goal {
        protected final DrownedServant servant;
        protected final int seaLevel;
        protected boolean stuck;

        public SwimUpGoal(DrownedServant drownedServant, int seaLevel) {
            this.servant = drownedServant;
            this.seaLevel = seaLevel;
        }

        public boolean canUse() {
            return !this.servant.level().isDay() && this.servant.isInWater() && this.servant.getY() < (double)(this.seaLevel - 2);
        }

        public boolean canContinueToUse() {
            return this.canUse() && !this.stuck;
        }

        public void start() {
            super.start();
            this.servant.setSearchingForLand(true);
            this.stuck = false;
        }

        public void tick() {
            if (this.servant.getY() < (double)(this.seaLevel - 1) && (this.servant.getNavigation().isDone()
                    || this.servant.closeToNextPos())) {
                Vec3 $$0 = DefaultRandomPos.getPosTowards(this.servant, 4, 8,
                        new Vec3(this.servant.getX(), this.seaLevel - 1, this.servant.getZ()), 1.6);
                if ($$0 == null) {
                    this.stuck = true;
                    return;
                }
                this.servant.getNavigation().moveTo($$0.x, $$0.y, $$0.z, 1);
            }
        }

        public void stop() {
            super.stop();
            this.servant.setSearchingForLand(false);
        }
    }
}
