
package com.bilibili.player_ix.noixmod_api.entities.servant.animal;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.List;

public class SpiderQueenServant
extends AgeableAnimalServant {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected int attackTick;
    protected static final EntityDataAccessor<Integer> DATA_BOSS_FLAGS;
    public final AnimationState attack = new AnimationState();
    public SpiderQueenServant(EntityType<? extends SpiderQueenServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BOSS_FLAGS, 0);
    }

    public void aiStep() {
        super.aiStep();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_BOSS_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        this.stopAllAnimations();
                        this.attack.startIfStopped(this.tickCount);
                        break;
                    }
                    default: {
                        LOGGER.warn("Can't handle boss flags in SpiderQueenServant");
                        break;
                    }
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public int getFlag() {
        return this.entityData.get(DATA_BOSS_FLAGS);
    }

    protected void setFlag(int flag) {
        this.entityData.set(DATA_BOSS_FLAGS, flag);
    }

    protected List<AnimationState> allAnimations() {
        return ImmutableList.of(this.attack);
    }

    protected void stopAllAnimations() {
        for (AnimationState state : allAnimations()) {
            state.stop();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.FOLLOW_RANGE, 72).add(Attributes.ARMOR, 4)
                .add(Attributes.ATTACK_DAMAGE, 9).add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 200);
    }

    static {
        DATA_BOSS_FLAGS = SynchedEntityData.defineId(SpiderQueenServant.class, EntityDataSerializers.INT);
    }
}
