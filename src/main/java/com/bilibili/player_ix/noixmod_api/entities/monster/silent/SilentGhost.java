
package com.bilibili.player_ix.noixmod_api.entities.monster.silent;

import com.bilibili.player_ix.noixmod_api.entities.ai.control.FlyingVexMoveControl;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractGhost;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexServant;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class SilentGhost
extends AbstractGhost
implements Enemy, IFlagMob {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    public SilentGhost(EntityType<? extends SilentGhost> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 2;
        this.setHostile(true);
        this.moveControl = new FlyingVexMoveControl(this);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new VexServant.VexChargeAttackGoal<>(this));
        this.addTargetGoal();
    }

    protected PathNavigation createNavigation(Level pLevel) {
        var n = new FlyingPathNavigation(this, pLevel);
        n.setCanFloat(true);
        n.setCanPassDoors(true);
        return n;
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int i) {
        this.entityData.set(DATA_FLAGS, i);
    }

    private FlyingPathNavigation getDifNavigation() {
        return (FlyingPathNavigation)this.getNavigation();
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(SilentGhost.class, EntityDataSerializers.INT);
    }
}
