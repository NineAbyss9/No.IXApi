
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class NoAttackMeleeGoal
extends ApiMeleeAttackGoal {
    public NoAttackMeleeGoal(PathfinderMob finder, double speed, double range) {
        super(finder, speed, range);
    }

    public NoAttackMeleeGoal(PathfinderMob finder, double speed) {
        this(finder, speed, 1.0);
    }

    protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
    }
}
