
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.ApiSpellcaster;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SoulMaster
extends ApiSpellcaster {
    public SoulMaster(EntityType<? extends SoulMaster> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 15;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    protected class SummonSpellGoal
    extends UseSpellGoal {

        protected void castSpell() {

        }

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
            return IllagerSpellType.WATER;
        }
    }
}
