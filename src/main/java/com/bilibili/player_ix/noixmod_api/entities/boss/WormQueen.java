
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.api.mobs.ApiBoss;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.servant.worm.AbstractWorm;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class WormQueen
extends AbstractWorm
implements ApiBoss {
    private static final EntityDataAccessor<Byte> BOSS_PHASE;
    public WormQueen(EntityType<? extends WormQueen> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 300;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.addBehaviorGoal(5, 0.8, 10F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOSS_PHASE, Maths.ONE_BYTE);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putByte("BossPhase", this.getPhase());
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("BossPhase")) {
            this.setPhase(tag.getByte("BossPhase"));
        }
        super.readAdditionalSaveData(tag);
    }

    public void tick() {
        super.tick();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return super.hurt(pSource, pAmount);
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (this.getHealth() - p_21241_ <= 0 && this.needChangePhase() && this.isAlive()) {
            this.setHealth(this.getMaxHealth());
            this.setPhase((byte)(this.getPhase() + 1));
        }
        super.actuallyHurt(p_21240_, p_21241_);
    }

    public boolean needChangePhase() {
        return this.getPhase() == 1 || this.getPhase() == 2;
    }

    public byte getPhase() {
        return this.entityData.get(BOSS_PHASE);
    }

    public void setPhase(byte b) {
        this.entityData.set(BOSS_PHASE, b);
    }

    public boolean wouldHaveOwner() {
        return false;
    }

    public boolean isHostile() {
        return true;
    }

    static {
        BOSS_PHASE = SynchedEntityData.defineId(WormQueen.class, EntityDataSerializers.BYTE);
    }
}
