
package com.bilibili.player_ix.noixmod_api.entities.friendly;

import com.github.NineAbyss9.ix_api.api.ApiSpells;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.AbstractUseSpellGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.NormalCastingSpellGoal;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.bilibili.player_ix.noixmod_api.magic.illager.TargetFangsSpell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgePlayer;
import org.jetbrains.annotations.Nullable;

public class FriendlyEvoker extends FriendlyCaster {
    public FriendlyEvoker(EntityType<FriendlyEvoker> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new NormalCastingSpellGoal(this));
        this.goalSelector.addGoal(1, new SummonSpellGoal(this));
        this.goalSelector.addGoal(2, new AttackSpellGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                if (this.targetMob instanceof IForgePlayer) {
                    return false;
                }
                if (this.targetMob instanceof AbstractFriendlyMob) {
                    return false;
                }
                return super.canUse();
            }
        }.setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                false, living -> living instanceof Enemy));
    }

    private static class SummonSpellGoal extends AbstractUseSpellGoal {
        public SummonSpellGoal(FriendlyEvoker finder) {
            super(finder);
        }

        @Override
        protected void castSpell() {
            if (!this.mob.level().isClientSide) {
                ISpell iSpell = Spells.VEX_ARCHER.get();
                iSpell.castSpell((ServerLevel)this.mob.level(), this.mob);
            }
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected int getCastingInterval() {
            return 480;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.RANGE;
        }
    }

    private static class AttackSpellGoal extends AbstractUseSpellGoal {
        public AttackSpellGoal(FriendlyEvoker finder) {
            super(finder);
        }

        @Override
        protected void castSpell() {
            if (!this.mob.level().isClientSide) {
                Spell apiSpell = new TargetFangsSpell();
                apiSpell.castSpell((ServerLevel)this.mob.level(), this.mob);
            }
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.ATTACK;
        }
    }
}
