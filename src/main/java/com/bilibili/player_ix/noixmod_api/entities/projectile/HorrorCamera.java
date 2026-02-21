
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class HorrorCamera extends Entity {
    private static final EntityDataAccessor<Integer> DATA_LIFE;
    public HorrorCamera(EntityType<HorrorCamera> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public void tick() {
        if (!NoixmodAPIMainConfig.HorrorMode.get()) {
            this.discard();
        }
        super.tick();
        if (this.getLife() > 0) {
            this.setLife(this.getLife() - 1);
        }
    }

    public Component getDisplayName() {
        return Component.empty();
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_LIFE, 90);
    }

    public int getLife() {
        return this.entityData.get(DATA_LIFE);
    }

    public void setLife(int life) {
        this.entityData.set(DATA_LIFE, life);
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {}

    protected void addAdditionalSaveData(CompoundTag compoundTag) {}

    static {
        DATA_LIFE = SynchedEntityData.defineId(HorrorCamera.class, EntityDataSerializers.INT);
    }
}
