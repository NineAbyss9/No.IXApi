
package com.bilibili.player_ix.noixmod_api.entities.servant.ice;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public class Yeti
extends IceServant
implements IFlagMob {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private static final EntityDataAccessor<Integer> DATA_ANIM_TICK;
    public AnimationState idle = new AnimationState();
    public AnimationState hide = new AnimationState();
    public AnimationState attack = new AnimationState();
    public Yeti(EntityType<? extends Yeti> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    public void aiStep() {
        super.aiStep();
    }

    protected void clientAiStep() {
        this.idle.startIfStopped(tickCount);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case 1:{
                        this.stopAllAnimations();
                        this.hide.startIfStopped(tickCount);
                        break;
                    }
                    case 2:{
                        this.stopAllAnimations();
                        this.attack.startIfStopped(tickCount);
                        break;
                    }
                    default:{
                        break;
                    }
                }
            } else {
                if (this.getFlag() > 5) {
                    NoixmodAPI.LOGGER.warn("Can't handle synchedData in {}", this.getClass().getSimpleName());
                    this.setFlag(0);
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void knockback(Entity pEntity) {
        double d0 = pEntity.getX() - x();
        double d1 = pEntity.getZ() - z();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        pEntity.push(d0 / d2 * 2.D, 0.18D, d1 / d2 * 2.D);
    }

    public boolean doHurtTarget(Entity pEntity) {
        if (super.doHurtTarget(pEntity)) {
            this.knockback(pEntity);
            return true;
        }
        return false;
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int i) {
        this.entityData.set(DATA_FLAGS, i);
    }

    public int getAniTick() {
        return this.entityData.get(DATA_ANIM_TICK);
    }

    public void setAniTick(int aniTick) {
        this.entityData.set(DATA_ANIM_TICK, aniTick);
    }

    public List<AnimationState> allAnims() {
        return List.of(hide, attack);
    }

    public void stopAllAnimations() {
        this.allAnims().forEach(AnimationState::stop);
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(Yeti.class, EntityDataSerializers.INT);
        DATA_ANIM_TICK = SynchedEntityData.defineId(Yeti.class, EntityDataSerializers.INT);
    }
}
