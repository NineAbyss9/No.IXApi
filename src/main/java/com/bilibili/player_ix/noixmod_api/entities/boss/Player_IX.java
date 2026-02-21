
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.Prototype;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.api.entity.IX;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Prototype(prototype = "bilibili@Player_IX")
public class Player_IX extends SpellcasterNihilist implements IX {
    protected int stopTimeTicks;
    protected static final EntityDataAccessor<Float> IX_HP;
    public Player_IX(EntityType<Player_IX> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IX_HP, 999F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(6, new FloatGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Nihilist.class).setAlertOthers());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        this.unsetRemoved();
        super.tick();
        for (Mob mob : this.targets()) {
            mob.setTarget(null);
            mob.setAggressive(false);
        }
    }

    public List<Mob> targets() {
        return this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(64), mob ->
                mob.getTarget() != null && mob != this);
    }

    public void theWorld() {
        this.setStopTimeTicks(Maths.toTick(9));
    }

    private void setStopTimeTicks(int ticks) {
        this.stopTimeTicks = ticks;
    }

    public int getStopTimeTicks() {
        return this.stopTimeTicks;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(@Nonnull DamageSource p_20122_) {
        return true;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    protected void actuallyHurt(@Nonnull DamageSource p_21240_, float p_21241_) {
    }

    @Override
    public final boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void remove(@Nonnull RemovalReason p_276115_) {
    }

    @Override
    public void onRemovedFromWorld() {
    }

    @Override
    public NihilistArmPose getArmPose() {
        if (this.getTarget() != null) {
            return NihilistArmPose.SPELL_CASTING;
        }
        return NihilistArmPose.CROSSED;
    }

    @Nullable
    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public Entity ixSelf() {
        return this;
    }

    static {
        IX_HP = SynchedEntityData.defineId(Player_IX.class, EntityDataSerializers.FLOAT);
    }
}
