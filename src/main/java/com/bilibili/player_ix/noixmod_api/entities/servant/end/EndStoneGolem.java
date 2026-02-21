
package com.bilibili.player_ix.noixmod_api.entities.servant.end;

import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.util.UnmodifiableList;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.NoAttackMeleeGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public class EndStoneGolem
extends AbstractEndServant
implements IFlagMob {
    public final AnimationState attack = new AnimationState();
    public final AnimationState ground = new AnimationState();
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    public EndStoneGolem(EntityType<? extends EndStoneGolem> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new NoAttackMeleeGoal(this, 1, Math.PI));
        this.addBehaviorGoal(4, 1, 10F, false, false);
        this.addTargetGoal();
    }

    public void aiStep() {
        super.aiStep();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        this.stopAllAnimations();
                        this.attack.startIfStopped(tickCount);
                        break;
                    }
                    case 2: {
                        this.stopAllAnimations();
                        this.ground.startIfStopped(tickCount);
                        break;
                    }
                    default: {
                        NoixmodAPI.LOGGER.warn("Unknown flag in EndStoneGolem");
                        this.resetFlag();
                        break;
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

    private List<AnimationState> animations() {
        return UnmodifiableList.of(attack, ground);
    }

    private void stopAllAnimations() {
        for (AnimationState state : animations()) {
            state.stop();
        }
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(EndStoneGolem.class, EntityDataSerializers.INT);
    }
}
