
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.NoAttackMeleeGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TheAnger
extends AbstractHorrorMob {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    public TheAnger(EntityType<? extends TheAnger> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new NoAttackMeleeGoal(this, 1.0));
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(TheAnger.class, EntityDataSerializers.INT);
    }
}
