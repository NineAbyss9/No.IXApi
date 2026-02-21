
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiBoss;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.SpellCasterMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.AbstractUseSpellGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.NormalCastingSpellGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.TuberMob;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class WhiteDeath
extends TuberMob
implements ApiBoss, ApiPoseMob, SpellCasterMob {
    private ApiSpells.ApiSpell spell;
    protected static final EntityDataAccessor<Integer> SPELL_TICKS;
    public WhiteDeath(EntityType<? extends WhiteDeath> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new NormalCastingSpellGoal(this));
        this.goalSelector.addGoal(1, new SummonSpellGoal(this));
        this.goalSelector.addGoal(4, new DeathAttackGoal(this));
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, TuberMob.class).setAlertOthers());
    }

    public boolean wouldHaveOwner() {
        return false;
    }

    public boolean isHostile() {
        return true;
    }

    public void affect(LivingEntity livingEntity) {

    }

    public ApiPose getPoses() {
        if (this.isCastingSpell()) {
            return ApiPose.SPELL_CASTING;
        }
        if (this.isAggressive()) {
            return ApiPose.ATTACKING;
        }
        return ApiPose.CROSSED;
    }

    public int getExperienceReward() {
        return 40;
    }

    public int getSpellTick() {
        return this.entityData.get(SPELL_TICKS);
    }

    public void setSpellType(ApiSpells.ApiSpell spell) {
        this.spell = spell;
    }

    public void setSpellTick(int tick) {
        this.entityData.set(SPELL_TICKS, tick);
    }

    public boolean isCastingSpell() {
        return this.getSpellTick() > 0;
    }

    @Nullable
    public SoundEvent getCastSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    static {
        SPELL_TICKS = SynchedEntityData.defineId(WhiteDeath.class, EntityDataSerializers.INT);
    }

    private static class DeathAttackGoal
    extends ApiMeleeAttackGoal {
        protected final WhiteDeath death;
        public DeathAttackGoal(WhiteDeath whiteDeath) {
            super(whiteDeath, 1, false, false);
            this.death = whiteDeath;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(p_25557_);
                this.death.affect(p_25557_);
            }
        }
    }

    private static class SummonSpellGoal extends AbstractUseSpellGoal {
        public SummonSpellGoal(SpellCasterMob finder) {
            super(finder);
        }

        @Override
        protected void castSpell() {

        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 200;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return null;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.RANGE;
        }
    }
}
