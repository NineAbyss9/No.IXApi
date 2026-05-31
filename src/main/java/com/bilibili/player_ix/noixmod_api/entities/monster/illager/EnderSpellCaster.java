
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import com.bilibili.player_ix.noixmod_api.magic.end.EndermanSpell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EnderSpellCaster
extends APISpellcaster {
    public EnderSpellCaster(EntityType<? extends EnderSpellCaster> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 10;
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    public boolean canCastSpell()
    {
        return true;
    }

    private class SummonSpellGoal extends UseSpellGoal {
        protected void castSpell() {
            new EndermanSpell().castSpell((ServerLevel)EnderSpellCaster.this.level(), EnderSpellCaster.this);
        }

        protected int getCastingTime() {
            return 0;
        }

        protected int getCastingInterval() {
            return 0;
        }

        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.DARK;
        }
    }

    private class AttackSpellGoal extends UseSpellGoal {
        protected void castSpell() {

        }

        protected int getCastingTime() {
            return 0;
        }

        protected int getCastingInterval() {
            return 0;
        }

        protected @Nullable SoundEvent getPrepareSound() {
            return null;
        }

        protected IllagerSpellType getSpellType() {
            return null;
        }
    }
}
