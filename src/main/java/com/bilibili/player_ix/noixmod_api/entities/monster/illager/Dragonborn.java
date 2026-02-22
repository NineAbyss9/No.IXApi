
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.api.mobs.ApiIllagerBoss;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Dragonborn
extends APISpellcaster
implements ApiIllagerBoss {
    protected final ServerBossEvent bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private static final EntityDataAccessor<Integer> DATA_BOSS_FLAGS;
    private static final EntityDataAccessor<Byte> DATA_PHASE;
    public Dragonborn(EntityType<Dragonborn> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BOSS_FLAGS, 0);
        this.entityData.define(DATA_PHASE, Maths.ONE_BYTE);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public int getExperienceReward() {
        return 20;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer p_20119_) {
        super.startSeenByPlayer(p_20119_);
        this.bossEvent.addPlayer(p_20119_);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer p_20174_) {
        super.stopSeenByPlayer(p_20174_);
        this.bossEvent.removePlayer(p_20174_);
    }



    public byte getPhase() {
        return this.entityData.get(DATA_PHASE);
    }

    public void setPhase(int i) {
        this.entityData.set(DATA_PHASE, (byte)i);
    }

    public boolean isSecondPhase() {
        return this.getPhase() == 2;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isSecondPhase()) {
            return this.getCelebrateSound();
        }
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource p_33034_) {
        if (this.isSecondPhase()) {
            return SoundEvents.ENDER_DRAGON_HURT;
        }
        return SoundEvents.EVOKER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDER_DRAGON_DEATH;
    }

    @NotNull
    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    static {
        DATA_BOSS_FLAGS = SynchedEntityData.defineId(Dragonborn.class, EntityDataSerializers.INT);
        DATA_PHASE = SynchedEntityData.defineId(Dragonborn.class, EntityDataSerializers.BYTE);
    }

    private abstract class SummonSpellGoal extends UseSpellGoal {

    }
}
