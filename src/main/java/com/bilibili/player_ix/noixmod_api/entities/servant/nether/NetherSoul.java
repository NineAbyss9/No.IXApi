
package com.bilibili.player_ix.noixmod_api.entities.servant.nether;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIAttributes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class NetherSoul extends OwnableMob implements Enemy {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private int attackTick;
    public AnimationState attacking = new AnimationState();
    public AnimationState attack = new AnimationState();
    public NetherSoul(EntityType<? extends NetherSoul> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 3;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1) {
            public void start() {
                super.start();
                NetherSoul.this.setFlag(2);
            }
        });
        this.addBehaviorGoal(4, 0.8, 12F);
        this.targetSelector.addGoal(2, new OwnableHurtByTargetGoal(this, NetherSoul.class));
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackTick > 0) {
            this.attackTick--;
        }
        if (this.level().isClientSide) {
            if (!this.isUnowned()) {
                this.level().addParticle(NoixmodAPIParticleTypes.RED_SKULL.get(), this.getRandomX(0.9),
                        this.getRandomY(), this.getRandomZ(0.9), 0, 0, 0);
            }
            if (this.isAggressive()) {
                if (this.attackTick <= 0) {
                    this.setFlag(2);
                }
            }
        }
    }

    public void setAggressive(boolean p_21562_) {
        if (!p_21562_) {
            setFlag(0);
        }
        super.setAggressive(p_21562_);
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        if (p_20122_.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        return super.isInvulnerableTo(p_20122_);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case 0 :{
                        this.attack.stop();
                        this.attacking.stop();
                        break;
                    }
                    case 1: {
                        this.attacking.stop();
                        this.attack.startIfStopped(this.tickCount);
                        break;
                    }
                    case 2: {
                        this.attack.stop();
                        this.attacking.startIfStopped(this.tickCount);
                        break;
                    }
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    private int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    private void setFlag(int flag) {
        this.entityData.set(DATA_FLAGS, flag);
    }

    public boolean isHostile() {
        return true;
    }

    public boolean doHurtTarget(Entity p_21372_) {
        if (p_21372_ instanceof LivingEntity entity) {
            this.attackTick = 10;
            this.setFlag(1);
            entity.setSecondsOnFire(6);
        }
        return super.doHurtTarget(p_21372_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NoixmodAPIAttributes.baseAttributes(3, 0.24004457064136, 0.2)
                .add(Attributes.ARMOR, 4).add(Attributes.FOLLOW_RANGE, 70);
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(NetherSoul.class, EntityDataSerializers.INT);
    }
}
