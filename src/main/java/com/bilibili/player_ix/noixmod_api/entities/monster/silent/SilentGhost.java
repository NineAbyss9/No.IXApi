
package com.bilibili.player_ix.noixmod_api.entities.monster.silent;

import com.bilibili.player_ix.noixmod_api.api.entity.IVex;
import com.bilibili.player_ix.noixmod_api.entities.ai.control.FlyingVexMoveControl;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractGhost;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.IHorror;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexArcher;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexServant;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class SilentGhost
extends AbstractGhost
implements Enemy, IFlagMob, IHorror, IVex
{
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    public SilentGhost(EntityType<? extends SilentGhost> pType, Level pLevel) {
        super(pType, pLevel);
        this.xpReward = 2;
        this.setHostile(true);
        this.moveControl = new FlyingVexMoveControl(this);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.setNoGravity(true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new VexServant.VexChargeAttackGoal<>(this));
        this.goalSelector.addGoal(4, new VexArcher.VexRandomMoveGoalNoOwner(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, LivingEntity.class, 20.0F));
        this.addTargetGoal();
    }

    /*protected PathNavigation createNavigation(Level pLevel) {
        var n = new FlyingPathNavigation(this, pLevel);
        n.setCanFloat(true);
        n.setCanPassDoors(true);
        return n;
    }*/

    public SoundEvent getChargeSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int i) {
        this.entityData.set(DATA_FLAGS, i);
    }

    public int getLevel()
    {
        return 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 42.0D).add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(SilentGhost.class, EntityDataSerializers.INT);
    }
}
