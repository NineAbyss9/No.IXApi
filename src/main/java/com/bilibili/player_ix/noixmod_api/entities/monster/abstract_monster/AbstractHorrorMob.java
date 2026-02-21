
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AbstractHorrorMob extends ApiPathfinderMob {
    public AbstractHorrorMob(EntityType<? extends AbstractHorrorMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public boolean canAttack(LivingEntity p_21171_) {
        if (p_21171_ instanceof AbstractHorrorMob) {
            return false;
        }
        return super.canAttack(p_21171_);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (NoixmodAPIDamageSource.sourceEntity(pSource) instanceof AbstractHorrorMob) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (NoixmodAPIDamageSource.sourceEntity(p_21240_) instanceof AbstractHorrorMob) {
            return;
        }
        super.actuallyHurt(p_21240_, p_21241_);
    }

    protected static class HorrorHurtByTargetGoal extends HurtByTargetGoal {
        public HorrorHurtByTargetGoal(PathfinderMob p_26039_, Class<?>... p_26040_) {
            super(p_26039_, p_26040_);
        }

        public boolean canUse() {
            if (this.targetMob instanceof AbstractHorrorMob) {
                return false;
            }
            return super.canUse();
        }

        protected boolean canAttack(@Nullable LivingEntity p_26151_, TargetingConditions p_26152_) {
            if (p_26151_ instanceof AbstractHorrorMob) {
                return false;
            }
            return super.canAttack(p_26151_, p_26152_);
        }
    }
}
