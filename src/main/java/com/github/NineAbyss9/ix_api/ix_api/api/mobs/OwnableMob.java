
package com.github.NineAbyss9.ix_api.ix_api.api.mobs;

import com.github.NineAbyss9.ix_api.ix_api.api.Synchronizer;
import com.github.NineAbyss9.ix_api.ix_api.util.ItemUtil;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.ObjectUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.scores.Team;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class OwnableMob
extends ApiPathfinderMob
implements Ownable {
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UNIQUE_ID;
    private static final EntityDataAccessor<Integer> DATA_OWNER_ID;
    public static int DEFAULT_LIFE_TICKS = 20 * (Maths.random.nextInt(30) + 90);
    protected int lifeTicks = DEFAULT_LIFE_TICKS;
    protected int targetCooldown;
    protected boolean isHostile;
    protected OwnableData ownableData = new OwnableData(this);
    protected OwnableMob(EntityType<? extends OwnableMob> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER_ID, -1);
        this.entityData.define(DATA_OWNER_UNIQUE_ID, Optional.empty());
    }

    public OwnableData getOwnableData() {
        return ownableData;
    }

    public boolean canAttack(LivingEntity p_21171_) {
        if (!MobUtils.canHurt(p_21171_, this)) {
            return false;
        }
        return super.canAttack(p_21171_);
    }

    protected void addBehaviorGoal(int i, double speed, float range) {
        this.addBehaviorGoal(i, speed, range, true);
    }

    protected void addBehaviorGoal(int i, double speed, float range, boolean Float) {
        this.addBehaviorGoal(i, speed, range, Float, false);
    }

    protected void addBehaviorGoal(int i, double speed, float range, boolean Float, boolean Water) {
        OwnableMob.addBehaviorGoals(this, i, speed, range, Float, Water);
    }

    public static void addBehaviorGoals(PathfinderMob mob, int i, double speed, float range, boolean Float, boolean Water) {
        if (Float) {
            mob.goalSelector.addGoal(i, new FloatGoal(mob));
        }
        mob.goalSelector.addGoal(i, new LookAtPlayerGoal(mob, LivingEntity.class, range));
        if (Water) {
            mob.goalSelector.addGoal(i, new WaterAvoidingRandomStrollGoal(mob, speed));
        } else {
            mob.goalSelector.addGoal(i, new RandomStrollGoal(mob, speed));
        }
    }

    protected void addTargetGoal(boolean flag) {
        if (flag) {
            this.targetSelector.addGoal(0, new OwnableTargetGoal<>(this, true));
            this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
        } else {
            this.addTargetGoal();
        }
    }

    protected void addTargetGoal() {
        this.addTargetGoal(1);
    }

    protected void addTargetGoal(int i) {
        this.targetSelector.addGoal(i, new HurtByTargetGoal(this).setAlertOthers());
        this.addTargetGoal(true);
    }

    public void setTargets() {
        this.setTargets(100);
    }

    public void setTargets(int cooldown) {
        if (this.getTarget() instanceof Mob mob && mob.getTarget() == null) {
            mob.setTarget(this);
            if (cooldown > 0) {
                this.targetCooldown = cooldown;
            }
        }
    }

    public static void setTargetByOwner(Ownable ownable) {
        if (ownable instanceof Mob mob) {
            if (ownable.getOwner() != null && ownable.getOwner() instanceof Mob b) {
                if (b.getTarget() != null && MobUtils.canHurt(b.getTarget(), mob)) {
                    mob.setTarget(b.getTarget());
                }
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity entity = pSource.getEntity();
        if (!MobUtils.canHurt(this, entity)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    protected void customServerAiStep() {
        if (this.hasLife()) {
            --this.lifeTicks;
            if (this.getLifeTick() <= 0) {
                this.discard();
            }
        }
        if (this.targetCooldown > 0) {
            --this.targetCooldown;
        }
        super.customServerAiStep();
    }

    public void tick() {
        super.tick();
        this.ownableTick();
    }

    public void aiStep() {
        super.aiStep();
        if (targetCooldown <= 0) {
            this.setTargets();
        }
    }

    public void die(DamageSource pDamageSource) {
        this.onSync(new Synchronizer("Die"));
        super.die(pDamageSource);
    }

    protected void registerGoals() {
        this.addTargetGoals();
    }

    protected void addTargetGoals() {
        this.targetSelector.addGoal(0, new ApiOwnerTargetGoal(this));
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (this.ownableData.nextFlag(pPlayer, pHand)) {
            this.ownableData.nextFlag();
            return InteractionResult.SUCCESS;
        } else if (this.isFood(stack)) {
            this.onSync(new Synchronizer("FoodHeal"));
            ItemUtil.shrink(stack, pPlayer);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(pPlayer, pHand);
    }

    public void onSync(Synchronizer synchronizer) {
        if (synchronizer.getMessage().equals("FoodHeal")) {
            this.heal(this.getHealAmount());
            if (!this.level().isClientSide) {
                ParticleUtil.addParticleAroundSelf(this, ParticleTypes.TOTEM_OF_UNDYING, 10);
            }
        } else if (synchronizer.getMessage().equals("Die")) {
            if (!this.level().isClientSide && this.level().getGameRules()
                    .getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
                this.getOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
            }
        }
    }

    public boolean isPersistenceRequired() {
        return super.isPersistenceRequired() || this.getOwner() != null;
    }

    protected float getHealAmount() {
        return 3.0F;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    public int getOwnerId() {
        return this.entityData.get(DATA_OWNER_ID);
    }

    public void setOwnerId(int id) {
        this.entityData.set(DATA_OWNER_ID, id);
    }

    public boolean isHostile() {
        return this.getOwner() instanceof Enemy || this.isHostile;
    }

    public void setHostile(boolean b) {
        this.isHostile = b;
    }

    @Nullable
    public Team getTeam() {
        LivingEntity living = this.getOwner();
        if (living != null && !this.areBothOwner(living)) {
            return living.getTeam();
        }
        return super.getTeam();
    }

    public static Predicate<Entity> ownablePredicate() {
        return lie -> lie instanceof Ownable;
    }

    public static LivingEntity ownerOrThis(Ownable own, LivingEntity This) {
        return own.getOwner() == null ? This : own.getOwner();
    }

    public static LivingEntity ownerOrThis(Ownable own) {
        return ownerOrThis(own, (LivingEntity)own);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        this.addOwnableAdditionalSaveData(tag);
        this.ownableData.addOwnableAdditionalSaveData(tag);
        if (this.hasLife()) {
            tag.putInt("LifeTicks", this.getLifeTick());
        }
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        this.readOwnableAdditionalSaveData(tag);
        this.ownableData.readOwnableAdditionalSaveData(tag);
        this.setLifeTick(tag.getInt("LifeTicks"));
        super.readAdditionalSaveData(tag);
    }

    public float getSpeed() {
        if (this.ownableData.isStaying() && !this.isAggressive()) {
            return 0.0F;
        }
        return super.getSpeed();
    }

    public int getLifeTick() {
        return this.lifeTicks;
    }

    public void setLifeTick(int i) {
        this.lifeTicks = i;
    }

    static {
        DATA_OWNER_ID = SynchedEntityData.defineId(OwnableMob.class, EntityDataSerializers.INT);
        DATA_OWNER_UNIQUE_ID = SynchedEntityData.defineId(OwnableMob.class,
                EntityDataSerializers.OPTIONAL_UUID);
    }

    public static class OwnerHurtTargetGoal<T extends Mob & Ownable>
    extends TargetGoal {
        protected final T mob;
        private int timestamp;
        private LivingEntity ownerLastHurt;

        public OwnerHurtTargetGoal(T ownable) {
            super(ownable, false);
            this.mob = ownable;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        public void start() {
            super.start();
            this.mob.setTarget(this.ownerLastHurt);
            LivingEntity $$0 = this.mob.getOwner();
            if ($$0 != null) {
                this.timestamp = $$0.getLastHurtMobTimestamp();
            }
        }

        public boolean canUse() {
            if (this.mob.getOwner() != null) {
                LivingEntity $$0 = this.mob.getOwner();
                if ($$0 == null) {
                    return false;
                } else {
                    this.ownerLastHurt = $$0.getLastHurtMob();
                    int $$1 = $$0.getLastHurtMobTimestamp();
                    return $$1 != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT);
                }
            } else {
                return false;
            }
        }
    }

    public static class FollowOwnerGoal<T extends PathfinderMob & Ownable> extends Goal {
        protected final T tamable;
        protected LivingEntity owner;
        protected final LevelReader level;
        protected final double speedModifier;
        protected final PathNavigation navigation;
        protected int timeToRecalcPath;
        protected final float stopDistance;
        protected final float startDistance;
        protected float oldWaterCost;
        protected final boolean canFly;
        protected final float teleportRange;
        protected final boolean targetTP;

        public FollowOwnerGoal(T entity, double speedModifier, float startDistance, float stopDistance, boolean canFly) {
            this(entity, speedModifier, startDistance, stopDistance, canFly, 144, true);
        }

        public FollowOwnerGoal(T entity, double speedModifier, float startDistance, float stopDistance, boolean canFly,
                               float tpRange) {
            this(entity, speedModifier, startDistance, stopDistance, canFly, tpRange, true);
        }

        public FollowOwnerGoal(T entity, double speedModifier, float startDistance, float stopDistance, boolean canFly,
                               float tpRange, boolean targetTp) {
            this.tamable = entity;
            this.level = entity.level();
            this.speedModifier = speedModifier;
            this.navigation = entity.getNavigation();
            this.startDistance = startDistance;
            this.stopDistance = stopDistance;
            this.canFly = canFly;
            this.teleportRange = tpRange;
            this.targetTP = targetTp;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity tamableOwner = this.tamable.getOwner();
            if (tamableOwner == null) {
                return false;
            } else if (tamableOwner.isSpectator()) {
                return false;
            } else if (this.tamable.getOwnableData().isStaying()) {
                return false;
            } else if (this.unableToMove()) {
                return false;
            } else if (this.tamable.isInWall()) {
                this.owner = tamableOwner;
                return true;
            } else if (this.checkRange(tamableOwner)) {
                return false;
            } else if (this.tamable.getTarget() != null && !this.targetTP) {
                return false;
            } else {
                this.owner = tamableOwner;
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else if (this.unableToMove()) {
                return false;
            } else if (this.tamable.getTarget() != null) {
                return false;
            } else {
                return !(this.tamable.distanceToSqr(this.owner) <= this.stopDistance * this.stopDistance);
            }
        }

        protected boolean checkRange(LivingEntity pOwner) {
            if (this.tamable.getOwnableData().isFollowing()) {
                return this.tamable.closerThan(pOwner, stopDistance);
            } else {
                return this.tamable.closerThan(pOwner, startDistance);
            }
        }

        protected boolean unableToMove() {
            return this.tamable.isPassenger() || this.tamable.isLeashed();
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.tamable.getPathfindingMalus(BlockPathTypes.WATER);
            this.tamable.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.tamable.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
        }

        public void tick() {
            if (this.canFly) {
                if (this.tamable.distanceToSqr(this.owner) >= Maths.square(12)) {
                    this.tamable.setTarget(null);
                    this.teleportToOwner();
                }
            } else {
                this.tamable.getLookControl().setLookAt(this.owner, 10.0F, this.tamable.getMaxHeadXRot());
                if (--this.timeToRecalcPath <= 0) {
                    this.timeToRecalcPath = this.adjustedTickDelay(10);
                    if (this.tamable.distanceToSqr(this.owner) >= this.teleportRange) {
                        this.teleportToOwner();
                    } else {
                        this.navigation.moveTo(this.owner, this.speedModifier);
                    }
                }
            }
        }

        private void teleportToOwner() {
            BlockPos $$0 = this.owner.blockPosition();
            for(int $$1 = 0; $$1 < 10; ++$$1) {
                int $$2 = this.randomIntInclusive(-3, 3);
                int $$3 = this.randomIntInclusive(-1, 1);
                int $$4 = this.randomIntInclusive(-3, 3);
                boolean $$5 = this.maybeTeleportTo($$0.getX() + $$2, $$0.getY() + $$3,
                        $$0.getZ() + $$4);
                if ($$5) {
                    return;
                }
            }
        }

        private boolean maybeTeleportTo(int p_25304_, int p_25305_, int p_25306_) {
            if (Math.abs((double)p_25304_ - this.owner.getX()) < 2.0 && Math.abs(p_25306_ - this.owner.getZ()) < 2.0) {
                return false;
            } else if (!this.canTeleportTo(new BlockPos(p_25304_, p_25305_, p_25306_))) {
                return false;
            } else {
                this.tamable.moveTo(p_25304_ + 0.5, p_25305_, p_25306_ + 0.5, this.tamable.getYRot(),
                        this.tamable.getXRot());
                this.navigation.stop();
                return true;
            }
        }

        private boolean canTeleportTo(BlockPos p_25308_) {
            BlockPathTypes $$1 = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, p_25308_.mutable());
            if ($$1 != BlockPathTypes.WALKABLE) {
                return false;
            } else {
                BlockState $$2 = this.level.getBlockState(p_25308_.below());
                if (!this.canFly && $$2.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos $$3 = p_25308_.subtract(this.tamable.blockPosition());
                    return this.level.noCollision(this.tamable, this.tamable.getBoundingBox().move($$3));
                }
            }
        }

        private int randomIntInclusive(int p_25301_, int p_25302_) {
            return this.tamable.getRandom().nextInt(p_25302_ - p_25301_ + 1) + p_25301_;
        }
    }

    public static class OwnableTargetGoal<T extends Mob & Ownable>
    extends NearestAttackableTargetGoal<LivingEntity> {
        protected final T ownable;
        public OwnableTargetGoal(T p_26060_, boolean p_26062_, Predicate<LivingEntity> p_26063) {
            super(p_26060_, LivingEntity.class, p_26062_, p_26063);
            this.ownable = p_26060_;
        }

        public OwnableTargetGoal(T entity, boolean flag) {
            this(entity, flag, living -> MobUtils.ownableCanHurt(living, entity));
        }
    }

    public static class OwnableHurtByTargetGoal
    extends HurtByTargetGoal {
        protected final Ownable ownable;
        public OwnableHurtByTargetGoal(PathfinderMob pathfinderMob, Class<?>... clazz) {
            super(pathfinderMob, clazz);
            this.ownable = (Ownable)pathfinderMob;
        }

        public boolean canUse() {
            if (ObjectUtil.nonnullEquals(this.ownable.getOwner(), targetMob)) {
                return false;
            }
            return super.canUse();
        }

        protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
            if (ObjectUtil.nonnullEquals(pPotentialTarget, this.ownable.getOwner())) {
                return false;
            }
            return super.canAttack(pPotentialTarget, pTargetPredicate);
        }

        protected void alertOther(Mob pMob, LivingEntity pTarget) {
            if (MobUtils.canHurt(pMob, pTarget)) {
                pMob.setTarget(pTarget);
            }
        }
    }
}
