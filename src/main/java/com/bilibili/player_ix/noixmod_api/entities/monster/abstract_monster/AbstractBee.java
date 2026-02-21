
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.github.NineAbyss9.ix_api.api.mobs.ApiNeutralMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.APIBreedGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.animal.AgeableAnimalServant;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("all")
public abstract class AbstractBee
extends AgeableAnimalServant
implements ApiNeutralMob {
    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME;
    private static final UniformInt PERSISTENT_ANGER_TIME;
    @Nullable
    private UUID persistentAngerTarget;
    private float rollAmount;
    private float rollAmountO;
    private int timeSinceSting;
    int ticksWithoutNectarSinceExitingHive;
    private int stayOutOfHiveCountdown;
    private int numCropsGrownSincePollination;
    int remainingCooldownBeforeLocatingNewHive;
    int remainingCooldownBeforeLocatingNewFlower;
    @Nullable
    BlockPos savedFlowerPos;
    @Nullable
    BlockPos hivePos;
    AbstractBee.BeePollinateGoal beePollinateGoal;
    AbstractBee.BeeGoToHiveGoal goToHiveGoal;
    private BeeGoToKnownFlowerGoal goToKnownFlowerGoal;
    private int underWaterTicks;
    private Bee bee;
    public AbstractBee(EntityType<? extends AbstractBee> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
        this.lookControl = new BeeLookControl(this);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    public float getWalkTargetValue(BlockPos p_27788_, LevelReader p_27789_) {
        return p_27789_.getBlockState(p_27788_).isAir() ? 10.0F : 0.0F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BeeAttackGoal(this, 1.399999976158142,
                true));
        this.goalSelector.addGoal(1, new BeeEnterHiveGoal(this));
        this.goalSelector.addGoal(2, new APIBreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25,
                Ingredient.of(ItemTags.FLOWERS), false));
        this.beePollinateGoal = new BeePollinateGoal(this);
        this.goalSelector.addGoal(4, this.beePollinateGoal);
        //this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new BeeLocateHiveGoal(this));
        this.goToHiveGoal = new BeeGoToHiveGoal(this);
        this.goalSelector.addGoal(5, this.goToHiveGoal);
        this.goToKnownFlowerGoal = new BeeGoToKnownFlowerGoal(this);
        this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
        this.goalSelector.addGoal(7, new BeeGrowCropGoal(this));
        this.goalSelector.addGoal(8, new BeeWanderGoal(this));
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.targetSelector.addGoal(1, (new BeeHurtByOtherGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new BeeBecomeAngryTargetGoal(this));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public void addAdditionalSaveData(CompoundTag p_27823_) {
        super.addAdditionalSaveData(p_27823_);
        if (this.hasHive()) {
            p_27823_.put("HivePos", NbtUtils.writeBlockPos(this.getHivePos()));
        }
        if (this.hasSavedFlowerPos()) {
            p_27823_.put("FlowerPos", NbtUtils.writeBlockPos(this.getSavedFlowerPos()));
        }
        p_27823_.putBoolean("HasNectar", this.hasNectar());
        p_27823_.putBoolean("HasStung", this.hasStung());
        p_27823_.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
        p_27823_.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
        p_27823_.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);
        this.addPersistentAngerSaveData(p_27823_);
    }

    public void readAdditionalSaveData(CompoundTag p_27793_) {
        this.hivePos = null;
        if (p_27793_.contains("HivePos")) {
            this.hivePos = NbtUtils.readBlockPos(p_27793_.getCompound("HivePos"));
        }
        this.savedFlowerPos = null;
        if (p_27793_.contains("FlowerPos")) {
            this.savedFlowerPos = NbtUtils.readBlockPos(p_27793_.getCompound("FlowerPos"));
        }
        super.readAdditionalSaveData(p_27793_);
        this.setHasNectar(p_27793_.getBoolean("HasNectar"));
        this.setHasStung(p_27793_.getBoolean("HasStung"));
        this.ticksWithoutNectarSinceExitingHive = p_27793_.getInt("TicksSincePollination");
        this.stayOutOfHiveCountdown = p_27793_.getInt("CannotEnterHiveTicks");
        this.numCropsGrownSincePollination = p_27793_.getInt("CropsGrownSincePollination");
        this.readPersistentAngerSaveData(this.level(), p_27793_);
    }

    public boolean doHurtTarget(Entity p_27722_) {
        boolean flag = p_27722_.hurt(this.damageSources().sting(this),
                (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.doEnchantDamageEffects(this, p_27722_);
            if (p_27722_ instanceof LivingEntity living) {
                living.setStingerCount(living.getStingerCount() + 1);
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 18;
                }
                if (i > 0) {
                    living.addEffect(new MobEffectInstance(MobEffects.POISON, i * 20, 0),
                            this);
                }
            }
            this.setHasStung(true);
            this.stopBeingAngry();
            this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
        }
        return flag;
    }

    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
            for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.spawnFluidParticle(this.level(), this.getX() - 0.30000001192092896,
                        this.getX() + 0.30000001192092896, this.getZ() - 0.30000001192092896,
                        this.getZ() + 0.30000001192092896, this.getY(0.5),
                        ParticleTypes.FALLING_NECTAR);
            }
        }
        this.updateRollAmount();
    }

    public void spawnFluidParticle(Level p_27780_, double p_27781_, double p_27782_, double p_27783_,
                                   double p_27784_, double p_27785_, ParticleOptions p_27786_) {
        p_27780_.addParticle(p_27786_, Mth.lerp(p_27780_.random.nextDouble(), p_27781_, p_27782_), p_27785_,
                Mth.lerp(p_27780_.random.nextDouble(), p_27783_, p_27784_), 0.0, 0.0, 0.0);
    }

    public void pathfindRandomlyTowards(BlockPos p_27881_) {
        Vec3 vec3 = Vec3.atBottomCenterOf(p_27881_);
        int i = 0;
        BlockPos blockpos = this.blockPosition();
        int j = (int)vec3.y - blockpos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }
        int k = 6;
        int l = 8;
        int i1 = blockpos.distManhattan(p_27881_);
        if (i1 < 15) {
            k = i1 / 2;
            l = i1 / 2;
        }
        Vec3 vec31 = AirRandomPos.getPosTowards(this, k, l, i, vec3, 0.3141592741012573);
        if (vec31 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vec31.x, vec31.y, vec31.z, 1.0);
        }
    }

    @Nullable
    public BlockPos getSavedFlowerPos() {
        return this.savedFlowerPos;
    }

    public boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    public void setSavedFlowerPos(BlockPos p_27877_) {
        this.savedFlowerPos = p_27877_;
    }

    @VisibleForDebug
    public int getTravellingTicks() {
        return Math.max(this.goToHiveGoal.travellingTicks, this.goToKnownFlowerGoal.travellingTicks);
    }

    @VisibleForDebug
    public List<BlockPos> getBlacklistedHives() {
        return this.goToHiveGoal.blacklistedTargets;
    }

    public boolean isTiredOfLookingForNectar() {
        return this.ticksWithoutNectarSinceExitingHive > 3600;
    }

    public boolean wantsToEnterHive() {
        if (this.stayOutOfHiveCountdown <= 0 && !this.beePollinateGoal.isPollinating() && !this.hasStung()
                && this.getTarget() == null) {
            boolean flag = this.isTiredOfLookingForNectar() || this.level().isRaining() || this.level().isNight()
                    || this.hasNectar();
            return flag && !this.isHiveNearFire();
        } else {
            return false;
        }
    }

    public void setStayOutOfHiveCountdown(int p_27916_) {
        this.stayOutOfHiveCountdown = p_27916_;
    }

    public float getRollAmount(float p_27936_) {
        return Mth.lerp(p_27936_, this.rollAmountO, this.rollAmount);
    }

    public void updateRollAmount() {
        this.rollAmountO = this.rollAmount;
        if (this.isRolling()) {
            this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
        } else {
            this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
        }
    }

    protected void customServerAiStep() {
        boolean flag = this.hasStung();
        if (this.isInWaterOrBubble()) {
            ++this.underWaterTicks;
        } else {
            this.underWaterTicks = 0;
        }
        if (this.underWaterTicks > 20) {
            this.hurt(this.damageSources().drown(), 1.0F);
        }
        if (flag) {
            ++this.timeSinceSting;
            if (this.timeSinceSting % 5 == 0 && this.random.nextInt(Mth.clamp(1200 -
                    this.timeSinceSting, 1, 1200)) == 0) {
                this.hurt(this.damageSources().generic(), this.getHealth());
            }
        }
        if (!this.hasNectar()) {
            ++this.ticksWithoutNectarSinceExitingHive;
        }
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), false);
        }
    }

    public void resetTicksWithoutNectarSinceExitingHive() {
        this.ticksWithoutNectarSinceExitingHive = 0;
    }

    public boolean isHiveNearFire() {
        if (this.hivePos == null) {
            return false;
        } else {
            BlockEntity blockentity = this.level().getBlockEntity(this.hivePos);
            return blockentity instanceof BeehiveBlockEntity entity && entity.isFireNearby();
        }
    }

    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int p_27795_) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, p_27795_);
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID p_27791_) {
        this.persistentAngerTarget = p_27791_;
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    private boolean doesHiveHaveSpace(BlockPos p_27885_) {
        BlockEntity blockentity = this.level().getBlockEntity(p_27885_);
        if (blockentity instanceof BeehiveBlockEntity) {
            return !((BeehiveBlockEntity)blockentity).isFull();
        } else {
            return false;
        }
    }

    @VisibleForDebug
    public boolean hasHive() {
        return this.hivePos != null;
    }

    @Nullable
    @VisibleForDebug
    public BlockPos getHivePos() {
        return this.hivePos;
    }

    @VisibleForDebug
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    protected void sendDebugPackets() {
        super.sendDebugPackets();
        //DebugPackets.sendBeeInfo(this);
    }

    public int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }

    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = 0;
    }

    void incrementNumCropsGrownSincePollination() {
        ++this.numCropsGrownSincePollination;
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.stayOutOfHiveCountdown > 0) {
                --this.stayOutOfHiveCountdown;
            }
            if (this.remainingCooldownBeforeLocatingNewHive > 0) {
                --this.remainingCooldownBeforeLocatingNewHive;
            }
            if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
                --this.remainingCooldownBeforeLocatingNewFlower;
            }
            boolean flag = this.isAngry() && !this.hasStung() && this.getTarget() != null && this.getTarget()
                    .distanceToSqr(this) < 4.0;
            this.setRolling(flag);
            if (this.tickCount % 20 == 0 && !this.isHiveValid()) {
                this.hivePos = null;
            }
        }
    }

    public boolean isHiveValid() {
        if (!this.hasHive()) {
            return false;
        } else if (this.isTooFarAway(this.hivePos)) {
            return false;
        } else {
            BlockEntity blockentity = this.level().getBlockEntity(this.hivePos);
            return blockentity instanceof BeehiveBlockEntity;
        }
    }

    public boolean hasNectar() {
        return this.getFlag(8);
    }

    public void setHasNectar(boolean p_27920_) {
        if (p_27920_) {
            this.resetTicksWithoutNectarSinceExitingHive();
        }
        this.setFlag(8, p_27920_);
    }

    public boolean hasStung() {
        return this.getFlag(4);
    }

    private void setHasStung(boolean p_27926_) {
        this.setFlag(4, p_27926_);
    }

    private boolean isRolling() {
        return this.getFlag(2);
    }

    private void setRolling(boolean p_27930_) {
        this.setFlag(2, p_27930_);
    }

    boolean isTooFarAway(BlockPos p_27890_) {
        return !this.closerThan(p_27890_, 32);
    }

    private void setFlag(int p_27833_, boolean p_27834_) {
        if (p_27834_) {
            this.entityData.set(DATA_FLAGS_ID, (byte)((Byte)this.entityData.get(DATA_FLAGS_ID) | p_27833_));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)((Byte)this.entityData.get(DATA_FLAGS_ID) & ~p_27833_));
        }

    }

    private boolean getFlag(int p_27922_) {
        return (this.entityData.get(DATA_FLAGS_ID) & p_27922_) != 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes
                .FLYING_SPEED, 0.6000000238418579).add(Attributes.MOVEMENT_SPEED,
                0.30000001192092896).add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    protected PathNavigation createNavigation(Level p_27815_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_27815_) {
            public boolean isStableDestination(BlockPos p_27947_) {
                return !this.level.getBlockState(p_27947_.below()).isAir();
            }
            public void tick() {
                if (!beePollinateGoal.isPollinating()) {
                    super.tick();
                }
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public boolean isFood(ItemStack p_27895_) {
        return p_27895_.is(ItemTags.FLOWERS);
    }

    boolean isFlowerValid(BlockPos p_27897_) {
        return this.level().isLoaded(p_27897_) && this.level().getBlockState(p_27897_).is(BlockTags.FLOWERS);
    }

    protected void playStepSound(BlockPos p_27820_, BlockState p_27821_) {
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource p_27845_) {
        return SoundEvents.BEE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    public Bee getBreedOffspring(ServerLevel p_148760_, AgeableMob p_148761_) {
        return (Bee)EntityType.BEE.create(p_148760_);
    }

    protected float getStandingEyeHeight(Pose p_27804_, EntityDimensions p_27805_) {
        return p_27805_.height * 0.5F;
    }

    protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_) {
    }

    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % TICKS_PER_FLAP == 0;
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    public void dropOffNectar() {
        this.setHasNectar(false);
        this.resetNumCropsGrownSincePollination();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            if (!this.level().isClientSide) {
                this.beePollinateGoal.stopPollinating();
            }

            return super.hurt(pSource, pAmount);
        }
    }

    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    private void jumpInLiquidInternal() {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0,
                0.01, 0.0));
    }

    public void jumpInFluid(FluidType type) {
        this.jumpInLiquidInternal();
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
    }

    boolean closerThan(BlockPos p_27817_, int p_27818_) {
        return p_27817_.closerThan(this.blockPosition(), p_27818_);
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(AbstractBee.class, EntityDataSerializers.BYTE);
        DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(AbstractBee.class, EntityDataSerializers.INT);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    }

    static class BeeLookControl extends LookControl {
        protected final AbstractBee bee;
        BeeLookControl(AbstractBee p_28059_) {
            super(p_28059_);
            this.bee = p_28059_;
        }

        public void tick() {
            if (!this.bee.isAngry()) {
                super.tick();
            }
        }

        protected boolean resetXRotOnTick() {
            return !this.bee.beePollinateGoal.isPollinating();
        }
    }

    class BeeAttackGoal extends MeleeAttackGoal {
        BeeAttackGoal(PathfinderMob p_27960_, double p_27961_, boolean p_27962_) {
            super(p_27960_, p_27961_, p_27962_);
        }

        public boolean canUse() {
            return super.canUse() && isAngry() && !hasStung();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && isAngry() && !hasStung();
        }
    }

    static class BeeEnterHiveGoal extends BaseBeeGoal {
        BeeEnterHiveGoal(AbstractBee bee) {
            super(bee);
        }

        public boolean canBeeUse() {
            if (this.bee.hasHive() && this.bee.wantsToEnterHive() && this.bee.hivePos.closerToCenterThan(
                    this.bee.position(), 2.0)) {
                BlockEntity blockentity = this.bee.level().getBlockEntity(this.bee.hivePos);
                if (blockentity instanceof BeehiveBlockEntity beehiveblockentity) {
                    if (!beehiveblockentity.isFull()) {
                        return true;
                    }
                    this.bee.hivePos = null;
                }
            }
            return false;
        }

        public boolean canBeeContinueToUse() {
            return false;
        }

        public void start() {
            BlockEntity blockentity = this.bee.level().getBlockEntity(this.bee.hivePos);
            if (blockentity instanceof BeehiveBlockEntity beehiveblockentity) {
                beehiveblockentity.addOccupant(this.bee, this.bee.hasNectar());
            }
        }
    }

    static class BeePollinateGoal extends BaseBeeGoal {
        private final Predicate<BlockState> VALID_POLLINATION_BLOCKS = (p_28074_) -> {
            if (p_28074_.hasProperty(BlockStateProperties.WATERLOGGED) && p_28074_.getValue(
                    BlockStateProperties.WATERLOGGED)) {
                return false;
            } else if (p_28074_.is(BlockTags.FLOWERS)) {
                if (p_28074_.is(Blocks.SUNFLOWER)) {
                    return p_28074_.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        };
        private int successfulPollinatingTicks;
        private int lastSoundPlayedTick;
        private boolean pollinating;
        @Nullable
        private Vec3 hoverPos;
        private int pollinatingTicks;

        BeePollinateGoal(AbstractBee bee) {
            super(bee);
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canBeeUse() {
            if (this.bee.remainingCooldownBeforeLocatingNewFlower > 0) {
                return false;
            } else if (this.bee.hasNectar()) {
                return false;
            } else if (this.bee.level().isRaining()) {
                return false;
            } else {
                Optional<BlockPos> optional = this.findNearbyFlower();
                if (optional.isPresent()) {
                    this.bee.savedFlowerPos = optional.get();
                    this.bee.navigation.moveTo(this.bee.savedFlowerPos.getX() + 0.5,
                            this.bee.savedFlowerPos.getY() + 0.5, this.bee.savedFlowerPos.getZ() + 0.5,
                            1.2000000476837158);
                    return true;
                } else {
                    this.bee.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.bee.random, 20,
                            60);
                    return false;
                }
            }
        }

        public boolean canBeeContinueToUse() {
            if (!this.pollinating) {
                return false;
            } else if (!this.bee.hasSavedFlowerPos()) {
                return false;
            } else if (this.bee.level().isRaining()) {
                return false;
            } else if (this.hasPollinatedLongEnough()) {
                return this.bee.random.nextFloat() < 0.2F;
            } else if (this.bee.tickCount % 20 == 0 && !this.bee.isFlowerValid(this.bee.savedFlowerPos)) {
                this.bee.savedFlowerPos = null;
                return false;
            } else {
                return true;
            }
        }

        private boolean hasPollinatedLongEnough() {
            return this.successfulPollinatingTicks > 400;
        }

        boolean isPollinating() {
            return this.pollinating;
        }

        void stopPollinating() {
            this.pollinating = false;
        }

        public void start() {
            this.successfulPollinatingTicks = 0;
            this.pollinatingTicks = 0;
            this.lastSoundPlayedTick = 0;
            this.pollinating = true;
            this.bee.resetTicksWithoutNectarSinceExitingHive();
        }

        public void stop() {
            if (this.hasPollinatedLongEnough()) {
                this.bee.setHasNectar(true);
            }
            this.pollinating = false;
            this.bee.navigation.stop();
            this.bee.remainingCooldownBeforeLocatingNewFlower = 200;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            ++this.pollinatingTicks;
            if (this.pollinatingTicks > 600) {
                this.bee.savedFlowerPos = null;
            } else {
                Vec3 vec3 = Vec3.atBottomCenterOf(this.bee.savedFlowerPos).add(0.0,
                        0.6000000238418579, 0.0);
                if (vec3.distanceTo(this.bee.position()) > 1.0) {
                    this.hoverPos = vec3;
                    this.setWantedPos();
                } else {
                    if (this.hoverPos == null) {
                        this.hoverPos = vec3;
                    }
                    boolean flag = this.bee.position().distanceTo(this.hoverPos) <= 0.1;
                    boolean flag1 = true;
                    if (!flag && this.pollinatingTicks > 600) {
                        this.bee.savedFlowerPos = null;
                    } else {
                        if (flag) {
                            boolean flag2 = this.bee.random.nextInt(25) == 0;
                            if (flag2) {
                                this.hoverPos = new Vec3(vec3.x() + this.getOffset(), vec3.y(),
                                        vec3.z() + this.getOffset());
                                this.bee.navigation.stop();
                            } else {
                                flag1 = false;
                            }
                            this.bee.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
                        }
                        if (flag1) {
                            this.setWantedPos();
                        }
                        ++this.successfulPollinatingTicks;
                        if (this.bee.random.nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                            this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                            this.bee.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }

        private void setWantedPos() {
            this.bee.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(),
                    0.3499999940395355);
        }

        private float getOffset() {
            return (this.bee.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

        private Optional<BlockPos> findNearbyFlower() {
            return this.findNearestBlock(this
                    .VALID_POLLINATION_BLOCKS);
        }

        private Optional<BlockPos> findNearestBlock(Predicate<BlockState> p_28076_) {
            BlockPos blockpos = this.bee.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for(int i = 0;(double)i <= 5.0;i = i > 0 ? -i : 1 - i) {
                for(int j = 0;(double)j < 5.0;++j) {
                    for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            blockpos$mutableblockpos.setWithOffset(blockpos, k, i - 1, l);
                            if (blockpos.closerThan(blockpos$mutableblockpos, 5.0) && p_28076_
                                    .test(this.bee.level().getBlockState(blockpos$mutableblockpos))) {
                                return Optional.of(blockpos$mutableblockpos);
                            }
                        }
                    }
                }
            }
            return Optional.empty();
        }
    }

    static class BeeLocateHiveGoal extends BaseBeeGoal {
        BeeLocateHiveGoal(AbstractBee f) {
            super(f);
        }

        public boolean canBeeUse() {
            return this.bee.remainingCooldownBeforeLocatingNewHive == 0 && !this.bee.hasHive()
                    && this.bee.wantsToEnterHive();
        }

        public boolean canBeeContinueToUse() {
            return false;
        }

        public void start() {
            this.bee.remainingCooldownBeforeLocatingNewHive = 200;
            List<BlockPos> list = this.findNearbyHivesWithSpace();
            if (!list.isEmpty()) {
                for (BlockPos blockpos : list) {
                    if (!this.bee.goToHiveGoal.isTargetBlacklisted(blockpos)) {
                        this.bee.hivePos = blockpos;
                        return;
                    }
                }
                this.bee.goToHiveGoal.clearBlacklist();
                this.bee.hivePos = list.get(0);
            }
        }

        private List<BlockPos> findNearbyHivesWithSpace() {
            BlockPos blockpos = this.bee.blockPosition();
            PoiManager poimanager = ((ServerLevel)this.bee.level()).getPoiManager();
            Stream<PoiRecord> stream = poimanager.getInRange((p_218130_) ->
                    p_218130_.is(PoiTypeTags.BEE_HOME), blockpos, 20, PoiManager.Occupancy.ANY);
            return stream.map(PoiRecord::getPos).filter(this.bee::doesHiveHaveSpace)
                    .sorted(Comparator.comparingDouble((p_148811_) -> p_148811_.distSqr(blockpos))).collect(Collectors.toList());
        }
    }

    @VisibleForDebug
    public static class BeeGoToHiveGoal extends BaseBeeGoal {
        int travellingTicks;
        final List<BlockPos> blacklistedTargets;
        @Nullable
        private Path lastPath;
        private int ticksStuck;

        BeeGoToHiveGoal(AbstractBee f) {
            super(f);
            this.travellingTicks = this.bee.level().random.nextInt(10);
            this.blacklistedTargets = Lists.newArrayList();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canBeeUse() {
            return this.bee.hivePos != null && !this.bee.hasRestriction() && this.bee.wantsToEnterHive()
                    && !this.hasReachedTarget(this.bee.hivePos) && this.bee.level().getBlockState(this.bee.hivePos)
                    .is(BlockTags.BEEHIVES);
        }

        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.start();
        }

        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            this.bee.navigation.stop();
            this.bee.navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (this.bee.hivePos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(600)) {
                    this.dropAndBlacklistHive();
                } else if (!this.bee.navigation.isInProgress()) {
                    if (!this.bee.closerThan(this.bee.hivePos, 16)) {
                        if (this.bee.isTooFarAway(this.bee.hivePos)) {
                            this.dropHive();
                        } else {
                            this.bee.pathfindRandomlyTowards(this.bee.hivePos);
                        }
                    } else {
                        boolean flag = this.pathfindDirectlyTowards(this.bee.hivePos);
                        if (!flag) {
                            this.dropAndBlacklistHive();
                        } else if (this.lastPath != null && this.bee.navigation.getPath().sameAs(this.lastPath)) {
                            ++this.ticksStuck;
                            if (this.ticksStuck > 60) {
                                this.dropHive();
                                this.ticksStuck = 0;
                            }
                        } else {
                            this.lastPath = this.bee.navigation.getPath();
                        }
                    }
                }
            }

        }

        private boolean pathfindDirectlyTowards(BlockPos p_27991_) {
            this.bee.navigation.setMaxVisitedNodesMultiplier(10.0F);
            this.bee.navigation.moveTo(p_27991_.getX(), p_27991_.getY(), p_27991_.getZ(), 1.0);
            return this.bee.navigation.getPath() != null && this.bee.navigation.getPath().canReach();
        }

        boolean isTargetBlacklisted(BlockPos p_27994_) {
            return this.blacklistedTargets.contains(p_27994_);
        }

        private void blacklistTarget(BlockPos p_27999_) {
            this.blacklistedTargets.add(p_27999_);
            while(this.blacklistedTargets.size() > 3) {
                this.blacklistedTargets.remove(0);
            }
        }

        void clearBlacklist() {
            this.blacklistedTargets.clear();
        }

        private void dropAndBlacklistHive() {
            if (this.bee.hivePos != null) {
                this.blacklistTarget(this.bee.hivePos);
            }

            this.dropHive();
        }

        private void dropHive() {
            this.bee.hivePos = null;
            this.bee.remainingCooldownBeforeLocatingNewHive = 200;
        }

        private boolean hasReachedTarget(BlockPos p_28002_) {
            if (this.bee.closerThan(p_28002_, 2)) {
                return true;
            } else {
                Path path = this.bee.navigation.getPath();
                return path != null && path.getTarget().equals(p_28002_) && path.canReach() && path.isDone();
            }
        }
    }

    public class BeeGoToKnownFlowerGoal extends BaseBeeGoal {
        int travellingTicks;

        BeeGoToKnownFlowerGoal(AbstractBee f) {
            super(f);
            this.travellingTicks = this.bee.level().random.nextInt(10);
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canBeeUse() {
            return this.bee.savedFlowerPos != null && !this.bee.hasRestriction() && this.wantsToGoToKnownFlower()
                    && this.bee.isFlowerValid(this.bee.savedFlowerPos) && !this.bee.closerThan(this.bee
                    .savedFlowerPos, 2);
        }

        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        public void start() {
            this.travellingTicks = 0;
            super.start();
        }

        public void stop() {
            this.travellingTicks = 0;
            navigation.stop();
            navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (savedFlowerPos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(600)) {
                    savedFlowerPos = null;
                } else if (!navigation.isInProgress()) {
                    if (isTooFarAway(savedFlowerPos)) {
                        savedFlowerPos = null;
                    } else {
                        pathfindRandomlyTowards(savedFlowerPos);
                    }
                }
            }
        }

        private boolean wantsToGoToKnownFlower() {
            return ticksWithoutNectarSinceExitingHive > 2400;
        }
    }

    class BeeGrowCropGoal extends BaseBeeGoal {
        BeeGrowCropGoal(AbstractBee f) {
            super(f);
        }

        public boolean canBeeUse() {
            if (getCropsGrownSincePollination() >= 10) {
                return false;
            } else if (random.nextFloat() < 0.3F) {
                return false;
            } else {
                return hasNectar() && isHiveValid();
            }
        }

        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        public void tick() {
            if (random.nextInt(this.adjustedTickDelay(30)) == 0) {
                for(int i = 1; i <= 2; ++i) {
                    BlockPos blockpos = blockPosition().below(i);
                    BlockState blockstate = level().getBlockState(blockpos);
                    Block block = blockstate.getBlock();
                    BlockState blockstate1 = null;
                    if (blockstate.is(BlockTags.BEE_GROWABLES)) {
                        if (block instanceof CropBlock cropblock) {
                            if (!cropblock.isMaxAge(blockstate)) {
                                blockstate1 = cropblock.getStateForAge(cropblock.getAge(blockstate) + 1);
                            }
                        } else {
                            int j;
                            if (block instanceof StemBlock) {
                                j = blockstate.getValue(StemBlock.AGE);
                                if (j < 7) {
                                    blockstate1 = blockstate.setValue(StemBlock.AGE, j + 1);
                                }
                            } else if (blockstate.is(Blocks.SWEET_BERRY_BUSH)) {
                                j = blockstate.getValue(SweetBerryBushBlock.AGE);
                                if (j < 3) {
                                    blockstate1 = blockstate.setValue(SweetBerryBushBlock.AGE, j + 1);
                                }
                            } else if (blockstate.is(Blocks.CAVE_VINES) || blockstate.is(Blocks.CAVE_VINES_PLANT)) {
                                ((BonemealableBlock)blockstate.getBlock()).performBonemeal((ServerLevel)level(),
                                        random, blockpos, blockstate);
                            }
                        }
                        if (blockstate1 != null) {
                            level().levelEvent(2005, blockpos, 0);
                            level().setBlockAndUpdate(blockpos, blockstate1);
                            incrementNumCropsGrownSincePollination();
                        }
                    }
                }
            }
        }
    }



    class BeeWanderGoal extends Goal {
        protected final AbstractBee bee;
        BeeWanderGoal(AbstractBee bee) {
            this.bee = bee;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return this.bee.navigation.isDone() && this.bee.random.nextInt(10) == 0;
        }

        public boolean canContinueToUse() {
            return navigation.isInProgress();
        }

        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                navigation.moveTo(navigation.createPath(BlockPos.containing(vec3), 1), 1.0);
            }
        }

        @Nullable
        private Vec3 findPos() {
            Vec3 vec3;
            if (isHiveValid() && !closerThan(hivePos, 22)) {
                Vec3 vec31 = Vec3.atCenterOf(hivePos);
                vec3 = vec31.subtract(position()).normalize();
            } else {
                vec3 = getViewVector(0.0F);
            }
            Vec3 vec32 = HoverRandomPos.getPos(bee, 8, 7, vec3.x, vec3.z,
                    1.5707964F, 3, 1);
            return vec32 != null ? vec32 : AirAndWaterRandomPos.getPos(bee, 8, 4,
                    -2, vec3.x, vec3.z, 1.5707963705062866);
        }
    }

    class BeeHurtByOtherGoal extends HurtByTargetGoal {
        BeeHurtByOtherGoal(AbstractBee p_28033_) {
            super(p_28033_, AbstractBee.class);
        }

        public boolean canContinueToUse() {
            return AbstractBee.this.isAngry() && super.canContinueToUse();
        }

        protected void alertOther(Mob p_28035_, LivingEntity p_28036_) {
            if (p_28035_ instanceof AbstractBee && this.mob.hasLineOfSight(p_28036_)) {
                p_28035_.setTarget(p_28036_);
            }
        }
    }

    protected static class BeeBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
        BeeBecomeAngryTargetGoal(AbstractBee p_27966_) {
            super(p_27966_, Player.class, 10, true, false, p_27966_::isAngryAt);
            Objects.requireNonNull(p_27966_);
        }

        public boolean canUse() {
            return this.beeCanTarget() && super.canUse();
        }

        public boolean canContinueToUse() {
            boolean flag = this.beeCanTarget();
            if (flag && this.mob.getTarget() != null) {
                return super.canContinueToUse();
            } else {
                this.targetMob = null;
                return false;
            }
        }

        private boolean beeCanTarget() {
            AbstractBee bee = (AbstractBee)this.mob;
            return bee.isAngry() && !bee.hasStung();
        }
    }

    protected static abstract class BaseBeeGoal extends Goal {
        protected final AbstractBee bee;
        BaseBeeGoal(AbstractBee bee) {
            this.bee = bee;
        }

        public abstract boolean canBeeUse();

        public abstract boolean canBeeContinueToUse();

        public boolean canUse() {
            return this.canBeeUse() && !this.bee.isAngry();
        }

        public boolean canContinueToUse() {
            return this.canBeeContinueToUse() && !this.bee.isAngry();
        }
    }
}