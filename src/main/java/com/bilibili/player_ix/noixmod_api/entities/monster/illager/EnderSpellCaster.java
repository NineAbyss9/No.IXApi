
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderSpellCaster
extends APISpellcaster {
    public EnderSpellCaster(EntityType<EnderSpellCaster> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 10;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @NotNull
    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    private class SummonSpellGoal extends UseSpellGoal {

        @Override
        protected void castSpell() {

        }

        @Override
        protected int getCastingTime() {
            return 0;
        }

        @Override
        protected int getCastingInterval() {
            return 0;
        }

        @Nullable
        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.DARK;
        }
    }
}
