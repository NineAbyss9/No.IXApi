
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.api.APISpells;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class NihilisticMessenger
extends SpellcasterNihilist {
    public NihilisticMessenger(EntityType<NihilisticMessenger> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(0, new LookAtLordGoal(this));
    }

    @Nullable
    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public int getExperienceReward() {
        return 99;
    }

    private class SummonSpellGoal
    extends UseSpellGoalA {

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
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.UNKNOWN;
        }
    }
}
