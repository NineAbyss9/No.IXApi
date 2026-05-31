
package com.bilibili.player_ix.noixmod_api.entities.servant.ice;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.github.NineAbyss9.ix_api.api.annotation.ServerOnly;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.MeleeGoal;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class Yeti
extends IceServant
implements IFlagMob {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private static final EntityDataAccessor<Integer> DATA_STATUS;
    private static final EntityDataAccessor<Integer> DATA_ANIM_TICK;
    public static final int POSE = 0;
    public static final int ATTACK = 2;
    public static final int ATTACK2 = 3;
    public static final int ATTACK3 = 4;
    public static final int SNOWBALL = 5;
    public static final int MOVE_TO_STRIDE = 6;
    public static final int STRIDE_TO_RUN = 7;
    public static final int RUN_TO_MOVE = 8;
    public static final int HIDE = 9;
    public static final int HIDE_POSE = 10;
    public AnimationState pose = new AnimationState();
    public AnimationState idle = new AnimationState();
    public AnimationState attack = new AnimationState();
    public AnimationState attack2 = new AnimationState();
    public AnimationState attack3 = new AnimationState();
    public AnimationState snowball = new AnimationState();
    public AnimationState moveToStride = new AnimationState();
    public AnimationState strideToRun = new AnimationState();
    public AnimationState runToMove = new AnimationState();
    public AnimationState hide = new AnimationState();
    public AnimationState hidePose = new AnimationState();
    private List<AnimationState> animationStates = null;

    public Yeti(EntityType<? extends Yeti> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
        this.entityData.define(DATA_ANIM_TICK, 0);
        this.entityData.define(DATA_STATUS, 3);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new FollowOwnerGoal<>(this, 1.0D,
                20.0F, 4.0F, false));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 12.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.addTargetGoal();
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive()) {
            if (isFlag(POSE)) handleStatusAndFlag();
            //if (this.getTarget() != null) MobUtils.forceLook(this, this.getTarget());
            if (isFlag(ATTACK)) attack();
            else if (isFlag(ATTACK2)) attack2();
            else if (isFlag(ATTACK3)) attack3();
            else if (isFlag(STRIDE_TO_RUN)) strideToRun();
            else if (isFlag(RUN_TO_MOVE)) runToMove();
            else if (isFlag(MOVE_TO_STRIDE)) moveToStride();
            else if (isFlag(HIDE)) hide();
            if (this.tickCount % 20 == 0 && this.level().getBlockState(blockPosition()).is(BlockTags.SNOW)) {
                this.heal(0.5F);
            }
        }
        this.setYRot(this.getYHeadRot());
    }

    protected void clientAiStep() {
        this.idle.startIfStopped(tickCount);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case POSE: {
                        this.stopAllAnimations();
                        this.pose.startIfStopped(tickCount);
                        break;
                    }
                    case ATTACK: {
                        this.stopAllAnimations();
                        this.attack.startIfStopped(tickCount);
                        break;
                    }
                    case ATTACK2: {
                        this.stopAllAnimations();
                        this.attack2.startIfStopped(tickCount);
                        break;
                    }
                    case ATTACK3: {
                        this.stopAllAnimations();
                        this.attack3.startIfStopped(tickCount);
                    }
                    case SNOWBALL: {
                        this.stopAllAnimations();
                        this.snowball.startIfStopped(tickCount);
                    }
                    case MOVE_TO_STRIDE: {
                        this.stopAllAnimations();
                        this.moveToStride.startIfStopped(tickCount);
                    }
                    case STRIDE_TO_RUN: {
                        stopAllAnimations();
                        strideToRun.startIfStopped(tickCount);
                    }
                    case RUN_TO_MOVE: {
                        stopAllAnimations();
                        runToMove.startIfStopped(tickCount);
                    }
                    case HIDE: {
                        this.stopAllAnimations();
                        this.hide.startIfStopped(tickCount);
                        break;
                    }
                    case HIDE_POSE: {
                        this.stopAllAnimations();
                        this.pose.stop();
                        this.hidePose.startIfStopped(tickCount);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            } else {
                if (this.getFlag() > HIDE_POSE || this.getFlag() < 0) {
                    NoixmodAPI.LOGGER.warn("Can't handle synchedData in {}", this.getClass().getSimpleName());
                    this.resetFlag();
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void attack() {
        increaseAniTick();
        if (this.aniTickEquals(13)) {
            MobUtils.areaAttack(this, 3.5F, 0, 90F, this.getAttackDamage(),
                    0F, 15, this.damageSources().mobAttack(this), false,
                    entity -> {
                        this.knockback(entity);
                        this.freeze(entity);
                        this.heal(2.0F);
                    }, false);
        }
        if (this.aniTick(20)) {
            resetState();
        }
    }

    public void attack2() {
        increaseAniTick();
        if (this.aniTickEquals(13)) {
            MobUtils.areaAttack(this, 4.0F, 3F, 90F, this.getAttackDamage()
                            * 1.4F,
                    0.01F, 15, this.damageSources().mobAttack(this), false,
                    entity -> {
                        this.knockback(entity);
                        this.freeze(entity);
                        this.heal(4F);
                    }, false);
            Vec3 vector = this.getLookAngle();
            ParticleUtil.sendParticles(serverLevel(), new BlockParticleOption(ParticleTypes.BLOCK,
                            level().getBlockState(blockPosition().relative(this.getDirection()).below())), position()
                            .add(vector.x * 2D
                                    , 0D, vector.z * 2D
                            )
                    , 20, 0.5, 0.5, 0.5,
                    0.1);
        }
        if (this.aniTick(20)) {
            resetState();
        }
    }

    public void attack3() {
        increaseAniTick();
        if (this.aniTickEquals(11)) {
            MobUtils.areaAttack(this, 4F, 3F, 90F, this.getAttackDamage()
                            * 1.05F,
                    0F, 15, this.damageSources().mobAttack(this), false,
                    entity -> {
                        this.knockback(entity);
                        this.freeze(entity);
                        this.heal(3F);
                    }, false);
        }
        if (this.aniTick(20)) {
            resetState();
        }
    }

    public void snowball() {
        increaseAniTick();
        if (this.aniTickEquals(13)) {
            MobUtils.areaAttack(this, 3F, 3F, 90F, this.getAttackDamage(),
                    0F, 10, this.damageSources().mobAttack(this), false,
                    entity -> {
                        this.knockback(entity);
                        this.freeze(entity);
                    }, false);
            Vec3 vector = this.getLookAngle();
            ParticleUtil.sendParticles(serverLevel(), new BlockParticleOption(ParticleTypes.BLOCK,
                            level().getBlockState(blockPosition().relative(this.getDirection()))), position()
                            .add(vector.x * 1.2D
                                    , 0D, vector.z * 1.2D
                            )
                    , 20, 0.25, 0.25, 0.25,
                    0.1);
        }
        if (this.aniTick(28)) {
            resetState();
        }
    }

    public void runToMove() {
        increaseAniTick();
        if (this.aniTick(3)) {
            resetState();
        }
    }

    public void moveToStride() {
        increaseAniTick();
        if (aniTickEquals(3)) {
            setStatus(2);
        }
    }

    public void strideToRun() {
        increaseAniTick();
        if (aniTickEquals(3)) {
            setStatus(2);
        }
    }

    public void hide() {
        increaseAniTick();
        if (this.aniTick(20)) {
            setFlag(HIDE_POSE);
            resetAniTick();
        }
    }

    public float getAttackDamage() {
        return (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
    }

    public void attack(AABB pRange) {
        for (LivingEntity livingEntity : this.level().getEntitiesOfClass(LivingEntity.class, pRange, this::canAttack)) {
            this.doHurtTarget(livingEntity);
        }
    }

    public void knockback(Entity pEntity) {
        double d0 = pEntity.getX() - x();
        double d1 = pEntity.getZ() - z();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        pEntity.push(d0 / d2 * 2.D, 0.18D, d1 / d2 * 2.D);
    }

    @ServerOnly
    public void freeze(LivingEntity pTarget) {
        super.freeze(pTarget);
        ParticleUtil.sendParticles(serverLevel(), ParticleTypes.SNOWFLAKE, pTarget.position()
                .add(0D, 1D, 0D), 4, 0.5, 0.5, 0.5, 0.1);
    }

    public void playSound(SoundEvent pSound, float pVolume, float pPitch) {
        if (this.isFlag(HIDE) || this.isFlag(HIDE_POSE)) return;
        super.playSound(pSound, pVolume, pPitch);
    }

    public boolean doHurtTarget(Entity pEntity) {
        if (super.doHurtTarget(pEntity) && pEntity instanceof LivingEntity) {
            this.knockback(pEntity);
            this.freeze((LivingEntity)pEntity);
            return true;
        }
        return false;
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int i) {
        this.setFlag(i, true);
    }

    public void setFlag(int i, boolean changeStatus) {
        this.entityData.set(DATA_FLAGS, i);
        if (!changeStatus) return;
        if (i == HIDE || i == HIDE_POSE) {
            this.setStatus(3);
        } else if (i == STRIDE_TO_RUN) {
            this.setStatus(2);
        } else if (i == MOVE_TO_STRIDE) {
            this.setStatus(1);
        } else {
            if (getStatus() == 0) return;
            this.setStatus(0);
        }
    }

    public int getAniTick() {
        return this.entityData.get(DATA_ANIM_TICK);
    }

    public void setAniTick(int aniTick) {
        this.entityData.set(DATA_ANIM_TICK, aniTick);
    }

    public List<AnimationState> allAnims() {
        if (animationStates == null)
            animationStates = List.of(attack2, attack3, snowball, runToMove, strideToRun,
                    moveToStride, hide, attack, hidePose);
        return animationStates;
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.isVehicle() && !pPlayer.isSecondaryUseActive()
            && this.isOwnedBy(pPlayer)) {
            if (!this.level().isClientSide) {
                pPlayer.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity instanceof Player ? (Player)entity : null;
    }

    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        this.setRot(pPlayer.getYRot(), pPlayer.getXRot() * 0.5F);
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.yHeadRot = pPlayer.yHeadRot;
    }

    public void travel(Vec3 pTravelVector) {
        Entity entity = this.getControllingPassenger();
        if (this.isVehicle()) {
            double d0;
            double d1;
            float f1;
            if (entity instanceof LivingEntity passenger) {
                this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float forward = passenger.zza;
                float strafe = passenger.xxa;
                super.travel(new Vec3(strafe, 0.0, forward));
            }
            if ((f1 = (float)Math.sqrt((d1 = this.getX() - this.xo) * d1 + (d0 = this.getZ() - this.zo) * d0) * 4.0f) > 1.0f) {
                f1 = 1.0f;
            }
            this.walkAnimation.setSpeed(this.walkAnimation.speed() + (f1 - this.walkAnimation.speed()) * 0.4f);
            this.walkAnimation.speed(this.walkAnimation.speed() + this.walkAnimation.speed());
            this.calculateEntityAnimation(true);
            return;
        }
        super.travel(pTravelVector);
    }

    public void stopAllAnimations() {
        this.allAnims().forEach(AnimationState::stop);
    }

    private static AttributeModifier HIDE_SPEED = new AttributeModifier("Yeti hide speed", 0.6D,
                AttributeModifier.Operation.MULTIPLY_BASE);

    private static AttributeModifier RUN_SPEED =
        new AttributeModifier("Yeti speed boost", 1.2D,
                AttributeModifier.Operation.MULTIPLY_BASE);

    private static AttributeModifier STRIDE_SPEED =
            new AttributeModifier("Yeti stride speed", 1.1D,
                    AttributeModifier.Operation.MULTIPLY_BASE);

    public void handleStatusAndFlag() {
        float chance = this.getRandomUtil().nextFloat();
        if (this.canAttack()) {
            if (chance < 0.6F) {
                setFlag(ATTACK);
            } else if (chance < 0.8F) {
                setFlag(ATTACK2);
            } else {
                setFlag(ATTACK3);
            }
        }
        LivingEntity entity = this.getTarget();
        AttributeInstance instance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (entity == null) {
            this.setStatus(0);
            if (this.tickCount % 10 == 0) {
                if (instance.hasModifier(RUN_SPEED))
                    instance.removeModifier(RUN_SPEED);
                if (instance.hasModifier(STRIDE_SPEED))
                    instance.removeModifier(STRIDE_SPEED);
                if (instance.hasModifier(HIDE_SPEED))
                    instance.removeModifier(HIDE_SPEED);
            }
            return;
        }
        if (this.closerThan(entity, 10D)) {
            this.setStatus(2);
            if (this.tickCount % 10 == 0) {
                if (!instance.hasModifier(RUN_SPEED))
                    instance.addTransientModifier(RUN_SPEED);
                if (instance.hasModifier(STRIDE_SPEED))
                    instance.removeModifier(STRIDE_SPEED);
                if (instance.hasModifier(HIDE_SPEED))
                    instance.removeModifier(HIDE_SPEED);
            }
        } else if (this.closerThan(entity, 30D)) {
            this.setStatus(1);
            if (this.tickCount % 10 == 0) {
                if (!instance.hasModifier(STRIDE_SPEED))
                    instance.addTransientModifier(STRIDE_SPEED);
                if (instance.hasModifier(RUN_SPEED))
                    instance.removeModifier(RUN_SPEED);
                if (instance.hasModifier(HIDE_SPEED))
                    instance.removeModifier(HIDE_SPEED);
            }
        } else {
            this.setStatus(3);
            if (this.tickCount % 10 == 0) {
                if (!instance.hasModifier(HIDE_SPEED))
                    instance.addTransientModifier(HIDE_SPEED);
                if (instance.hasModifier(STRIDE_SPEED))
                    instance.removeModifier(STRIDE_SPEED);
                if (instance.hasModifier(RUN_SPEED))
                    instance.removeModifier(RUN_SPEED);
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_FIRE)) pAmount *= 2.0F;
        return super.hurt(pSource, pAmount);
    }

    /**
     * 1->Stride
     * 2->Run
     * 3->Hide
     */
    public int getStatus() {
        return this.entityData.get(DATA_STATUS);
    }

    /**
     * 1->Stride
     * 2->Run
     * 3->Hide
     */
    public void setStatus(int status) {
        this.entityData.set(DATA_STATUS, status);
    }

    public boolean canAttack() {
        LivingEntity target = this.getTarget();
        return target != null && target.isAlive() && this.closerThan(target, 3D);
    }

    public void setTargets() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.ATTACK_DAMAGE, 14D)
                .add(Attributes.MAX_HEALTH, 75D)
                .add(Attributes.ARMOR, 8D)
                .add(Attributes.FOLLOW_RANGE, 128D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(Yeti.class, EntityDataSerializers.INT);
        DATA_STATUS = SynchedEntityData.defineId(Yeti.class, EntityDataSerializers.INT);
        DATA_ANIM_TICK = SynchedEntityData.defineId(Yeti.class, EntityDataSerializers.INT);
    }
}
