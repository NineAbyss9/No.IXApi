
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ICreeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ApiSwellGoal<T extends Mob & ICreeper>
extends Goal {
    private final T creeper;
    @Nullable
    private LivingEntity target;

    public ApiSwellGoal(T pCreeper) {
        this.creeper = pCreeper;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        LivingEntity $$0 = this.creeper.getTarget();
        return this.creeper.getSwellDir() > 0 || $$0 != null && this.creeper.distanceToSqr($$0) < 9.0;
    }

    public void start() {
        this.creeper.getNavigation().stop();
        this.target = this.creeper.getTarget();
    }

    public void stop() {
        this.target = null;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.target == null) {
            this.creeper.setSwellDir(-1);
        } else if (this.creeper.distanceToSqr(this.target) > 49.0) {
            this.creeper.setSwellDir(-1);
        } else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
            this.creeper.setSwellDir(-1);
        } else {
            this.creeper.setSwellDir(1);
        }
    }
}
