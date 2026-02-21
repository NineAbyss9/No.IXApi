
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;

public class ApiAvoidTargetGoal extends AvoidEntityGoal<LivingEntity> {
    public ApiAvoidTargetGoal(PathfinderMob p_25033_, float p_25035_, double p_25036_, double p_25037_) {
        super(p_25033_, LivingEntity.class, p_25035_, p_25036_, p_25037_, living -> living != null &&
                EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(living));
    }

    public boolean canUse() {
        if (this.toAvoid instanceof Mob target) {
            if (target.getTarget() == this.mob)
                return super.canUse();
            return false;
        } else
            return super.canUse() && this.toAvoid instanceof Player;
    }
}
