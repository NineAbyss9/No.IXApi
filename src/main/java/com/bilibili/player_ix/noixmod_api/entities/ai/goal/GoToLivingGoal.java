
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.List;

/**A goal.
 *
 * @author Player_IX*/
public class GoToLivingGoal extends Goal {
    protected final PathfinderMob mob;
    protected final double speedModifier;
    protected final Class<? extends LivingEntity> targetType;
    @Nullable
    protected LivingEntity targetEntity;
    public GoToLivingGoal(PathfinderMob finder, double speed, Class<? extends LivingEntity> type) {
        this.mob = finder;
        this.speedModifier = speed;
        this.targetType = type;
    }

    public boolean canUse() {
        if (this.mob.getMoveControl().hasWanted()) {
            return false;
        }
        return this.checkCanUse();
    }

    public void start() {
        if (this.targetEntity != null && this.targetEntity != this.mob) {
            this.mob.getNavigation().moveTo(this.targetEntity, this.speedModifier);
        }
        this.stop();
    }

    public boolean canContinueToUse() {
        return false;
    }

    public boolean checkCanUse() {
        List<? extends LivingEntity> list = this.mob.level().getEntitiesOfClass(this.targetType, this.mob.getBoundingBox()
                .inflate(90), living -> living != this.mob);
        if (list.isEmpty()) {
            return false;
        } else {
            for (LivingEntity living : list) {
                if (living instanceof Mob target) {
                    if (target.getTarget() != this.mob) {
                        this.targetEntity = target;
                        break;
                    }
                } else {
                    this.targetEntity = living;
                    break;
                }
            }
            return this.speedModifier > 0;
        }
    }
}
