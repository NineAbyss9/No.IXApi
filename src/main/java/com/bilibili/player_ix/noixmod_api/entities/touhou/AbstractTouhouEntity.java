
package com.bilibili.player_ix.noixmod_api.entities.touhou;

import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class AbstractTouhouEntity
extends ApiPathfinderMob
implements IFlagMob
{
    protected static final EntityDataAccessor<Integer> DATA_FLAGS;
    protected static final EntityDataAccessor<Integer> ANIM_TICK;
    public AbstractTouhouEntity(EntityType<? extends AbstractTouhouEntity> type, Level level)
    {
        super(type, level);
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
        this.entityData.define(ANIM_TICK, 0);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey)
    {
        if (pKey.equals(DATA_FLAGS)) {
            this.onFlagSync(this.getFlag());
        }
        super.onSyncedDataUpdated(pKey);
    }

    public int getFlag() {return this.entityData.get(DATA_FLAGS);}
    public void setFlag(int i) {this.entityData.set(DATA_FLAGS, i);}
    public int getAniTick() {return this.entityData.get(ANIM_TICK);}
    public void setAniTick(int aniTick) {this.entityData.set(ANIM_TICK, aniTick);}
    protected void onFlagSync(int flag) {}
    protected List<AnimationState> allAnims = null;
    public List<AnimationState> getAllAnimations() {return allAnims;}

    static {
        DATA_FLAGS = SynchedEntityData.defineId(AbstractTouhouEntity.class, EntityDataSerializers.INT);
        ANIM_TICK = SynchedEntityData.defineId(AbstractTouhouEntity.class, EntityDataSerializers.INT);
    }
}
