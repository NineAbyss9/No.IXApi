
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class FrostMage
extends APISpellcaster {
    private final ServerBossEvent bossEvent;
    public FrostMage(EntityType<FrostMage> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 30;
        this.bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE,
                BossEvent.BossBarOverlay.PROGRESS);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(2, new SummonSpellGoal());
        OwnableMob.addBehaviorGoals(this, 4, 0.7, 15F, true, true);
    }

    public void aiStep() {
        super.aiStep();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void startSeenByPlayer(ServerPlayer p_20119_) {
        super.startSeenByPlayer(p_20119_);
        this.bossEvent.addPlayer(p_20119_);
    }

    public void stopSeenByPlayer(ServerPlayer p_20174_) {
        super.stopSeenByPlayer(p_20174_);
        this.bossEvent.removePlayer(p_20174_);
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    private class SummonSpellGoal extends UseSpellGoal {
        protected void castSpell() {

        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 600;
        }

        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.RANGE;
        }
    }
}
