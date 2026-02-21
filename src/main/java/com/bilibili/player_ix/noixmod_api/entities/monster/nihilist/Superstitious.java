
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.api.mobs.ApiBoss;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Superstitious extends SpellcasterNihilist implements ApiBoss {
    protected boolean hasClone;
    protected int hurtCooldown = 0;
    public static final float DAMAGE_CAPE = 10F;
    protected OwnerSummon ownerSummon = new OwnerSummon(this);
    public Superstitious(EntityType<? extends Superstitious> type, Level world) {
        super(type, world);
        this.xpReward = 39;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new SummonSpellGoal());
        this.goalSelector.addGoal(2, new AttackGoal(this, 1));
        OwnableMob.addBehaviorGoals(this, 5, 0.8, 10F, true, true);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Nihilist.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false,
                living -> living.isAlive() && !(living instanceof Nihilistic)));
    }

    private List<SuperstitiousClone> clones() {
        return this.level().getEntitiesOfClass(SuperstitiousClone.class, this.getBoundingBox().inflate(365),
                clone -> clone.getOwner() == this);
    }

    @Override
    public final boolean isAttackable() {
        return this.clones().isEmpty();
    }

    @Override
    public final boolean isInvulnerable() {
        return !this.clones().isEmpty() || super.isInvulnerable();
    }

    @Override
    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {return false;}

    @Override
    public void forceAddEffect(MobEffectInstance p_147216_, @Nullable Entity p_147217_) {}

    @Override
    protected void tickEffects() {this.removeAllEffects();}

    @Override
    public final boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public final boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    @Override
    public void tick() {
        if (this.hurtCooldown > 0) {
            --this.hurtCooldown;
        }
        super.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (this.hurtCooldown <= 0) {
            this.hurtCooldown = 40;
            super.actuallyHurt(p_21240_, Math.min(p_21241_, DAMAGE_CAPE));
        }
    }

    @Override
    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        this.sendSystemMessage(Component.translatable("message.noixmodapi.destiny"));
        return super.killedEntity(p_216988_, p_216989_);
    }

    @Nullable
    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    private class AttackSpellGoal extends UseSpellGoalA {

        @Override
        protected void castSpell() {

        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 500;
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

        @Override
        public boolean canUse() {
            if (!clones().isEmpty()) {
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if (!clones().isEmpty()) {
                return false;
            }
            return super.canContinueToUse();
        }
    }

    private class SummonSpellGoal extends UseSpellGoalA {

        @Override
        protected void castSpell() {
            SuperstitiousClone clone = new SuperstitiousClone(NoixmodAPIEntities.SUPERSTITIOUS_CLONE.get(), Superstitious.this.level());
            ownerSummon.integerSummon(clone, 1);
            hasClone = true;
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        public boolean canUse() {
            if (clones().size() >= 3) {
                return false;
            }
            if (hasClone) {
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if (clones().size() >= 3) {
                return false;
            }
            return super.canContinueToUse();
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

    private static class AttackGoal extends ApiMeleeAttackGoal {
        final Superstitious superstitious;
        public AttackGoal(Superstitious finder, double speed) {
            super(finder, speed, Maths.square(3));
            this.superstitious = finder;
        }

        @Override
        public boolean canUse() {
            if (!superstitious.clones().isEmpty()) {
                return false;
            }
            return super.canUse();
        }
    }
}
