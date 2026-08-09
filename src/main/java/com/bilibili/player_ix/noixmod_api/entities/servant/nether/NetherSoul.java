
package com.bilibili.player_ix.noixmod_api.entities.servant.nether;

import com.bilibili.player_ix.noixmod_api.api.entity.IVex;
import com.bilibili.player_ix.noixmod_api.entities.ai.control.FlyingVexMoveControl;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexArcher;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexServant;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIAttributes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class NetherSoul
extends OwnableMob
implements IFlagMob, IVex {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private int attackTick;
    public AnimationState attacking = new AnimationState();
    public AnimationState attack = new AnimationState();
    public NetherSoul(EntityType<? extends NetherSoul> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 3;
        this.setHostile();
        this.moveControl = new FlyingVexMoveControl(this);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        /*this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1) {
            public void start() {
                super.start();
                NetherSoul.this.setFlag(2);
            }
        });*/
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new VexServant.VexChargeAttackGoal<>(this));
        this.goalSelector.addGoal(8, new VexArcher.VexRandomMoveGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, LivingEntity.class, 20.0F));
    }

    protected void addTargetGoals() {
        super.addTargetGoals();
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new OwnableTargetGoal<>(this, true));
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.setNoGravity(true);
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackTick > 0) {
            this.attackTick--;
        }
        /*if (this.attackTick == 0) {
            this.setFlag(this.getTarget() == null ? 1 : 0);
        }*/
        if (this.tickCount % 10 != 0) {
            return;
        }
        if (this.level().isClientSide) {
            if (!this.isUnowned()) {
                this.level().addParticle(NoixmodAPIParticleTypes.RED_SKULL.get(), this.getRandomX(0.9),
                        this.getRandomY(), this.getRandomZ(0.9), 0, 0, 0);
            }
            /*if (this.isAggressive()) {
                if (this.attackTick <= 0) {
                    this.setFlag(2);
                }
            }*/
        }
    }

    /*protected PathNavigation createNavigation(Level pLevel) {
        var n = new FlyingPathNavigation(this, pLevel);
        n.setCanFloat(true);
        n.setCanPassDoors(true);
        return n;
    }*/

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (!this.isVehicle() && !pPlayer.isSecondaryUseActive() && this.isOwnedBy(pPlayer)) {
            if (!this.level().isClientSide) {
                pPlayer.startRiding(this);
                this.setHostile(false);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        } else if (stack.is(Items.DIAMOND) && this.isUnowned()) {
            if (!this.level().isClientSide) {
                this.setOwner(pPlayer);
                this.setTarget((LivingEntity)null);
                this.setAggressive(false);
                stack.shrink(1);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(pPlayer, pHand);
    }

    public SoundEvent getChargeSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    public boolean isInvulnerableTo(DamageSource pSource) {
        if (pSource.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        return super.isInvulnerableTo(pSource);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case 0 -> {
                        this.attack.stop();
                        this.attacking.stop();
                    }
                    case 1 -> {
                        this.attack.stop();
                        this.attacking.stop();
                        this.attacking.startIfStopped(this.tickCount);
                    }
                    case 2 -> {
                        this.attack.stop();
                        this.attacking.stop();
                        this.attack.startIfStopped(this.tickCount);
                    }
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int flag) {
        this.entityData.set(DATA_FLAGS, flag);
    }

    public int getAniTick() {
        return this.attackTick;
    }

    public void setAniTick(int attackTick) {
        this.attackTick = attackTick;
    }

    public boolean doHurtTarget(Entity pEntity) {
        if (pEntity instanceof LivingEntity entity) {
            this.attackTick = 10;
            this.setFlag(2);
            entity.setSecondsOnFire(5);
        }
        return super.doHurtTarget(pEntity);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity instanceof Player ? (Player)entity : (LivingEntity)null;
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
                super.travel(new Vec3(passenger.xxa, passenger.yya, passenger.zza));
            }
            if ((f1 = (float)Math.sqrt((d1 = this.getX() - this.xo) * d1 + (d0 = this.getZ() - this.zo) * d0) * 4.0F) > 1.0F) {
                f1 = 1.0f;
            }
            this.walkAnimation.setSpeed(this.walkAnimation.speed() + (f1 - this.walkAnimation.speed()) * 0.4F);
            this.walkAnimation.speed(this.walkAnimation.speed() + this.walkAnimation.speed());
            this.calculateEntityAnimation(true);
            return;
        }
        super.travel(pTravelVector);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NoixmodAPIAttributes.baseAttributes(3, 0.44004457064136, 0.5)
                .add(Attributes.ARMOR, 4).add(Attributes.FOLLOW_RANGE, 56);
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(NetherSoul.class, EntityDataSerializers.INT);
    }
}
