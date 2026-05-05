
package com.bilibili.player_ix.noixmod_api.entities.monster.horror;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.MeleeGoal;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TheAnger
extends AbstractHorrorMob
implements IFlagMob
{
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private static final EntityDataAccessor<Integer> DATA_ANIM_TICK;
    public AnimationState idle = new AnimationState();
    public static final int ATTACK= 1;
    private final Int2ObjectArrayMap<AnimationState> states;
    public TheAnger(EntityType<? extends TheAnger> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.states = new Int2ObjectArrayMap<>();
        for (int i = 1;i < 5;i++) {
            this.states.put(i, new AnimationState());
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
        this.entityData.define(DATA_ANIM_TICK, 0);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey)
    {
        if (pKey.equals(DATA_FLAGS)) {
            if (this.level().isClientSide) {
                if (this.getFlag() > 0)
                    this.states.get(this.getFlag()).startIfStopped(tickCount);
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    protected void clientAiStep() {
        this.idle.startIfStopped(tickCount);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeGoal(this, 1.0));
    }

    public int getLevel()
    {
        return 2;
    }

    public int getFlag()
    {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int i)
    {
        this.entityData.set(DATA_FLAGS, i);
    }

    private List<AnimationState> stateList = null;

    private List<AnimationState> getAllAnims()
    {
        if (stateList == null) {
            List<AnimationState> list = new ArrayList<>();
            for (var v : this.states.int2ObjectEntrySet())
            {
                list.add(v.getValue());
            }
            stateList = list;
        }
        return stateList;
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(TheAnger.class, EntityDataSerializers.INT);
        DATA_ANIM_TICK = SynchedEntityData.defineId(TheAnger.class, EntityDataSerializers.INT);
    }
}
