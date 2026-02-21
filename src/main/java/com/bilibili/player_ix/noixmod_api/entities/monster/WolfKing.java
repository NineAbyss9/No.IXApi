
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.github.NineAbyss9.ix_api.api.mobs.ApiBoss;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.mod.APIMonster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class WolfKing
extends APIMonster
implements ApiBoss {
    protected int summonWolfTicks;
    public WolfKing(EntityType<? extends WolfKing> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 30;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new WolfKingAttackGoal(this));
        OwnableMob.addBehaviorGoals(this, 5, 0.7, 10F, true, false);
    }

    private static class WolfKingAttackGoal
    extends ApiMeleeAttackGoal {
        public WolfKingAttackGoal(WolfKing king) {
            super(king, 1, false, false);
        }

        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            super.checkAndPerformAttack(p_25557_, p_25558_);
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            return Maths.square(1.5);
        }
    }
}
