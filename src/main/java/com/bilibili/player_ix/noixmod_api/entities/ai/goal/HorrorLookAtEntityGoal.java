
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class HorrorLookAtEntityGoal extends Goal {
    protected final Mob mob;
    @Nullable
    protected Player lookAt;
    private int lookTime;
    private final boolean onlyHorizontal;
    protected final Class<? extends Player> lookAtType;
    protected final TargetingConditions lookAtContext;

    public HorrorLookAtEntityGoal(Mob p_25520_) {
        this(p_25520_, Player.class, 999F, false);
    }

    public HorrorLookAtEntityGoal(Mob p_148118_, Class<? extends Player> p_148119_, float p_148120_, boolean p_148122_) {
        this.mob = p_148118_;
        this.lookAtType = p_148119_;
        this.onlyHorizontal = p_148122_;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        if (p_148119_ == Player.class) {
            this.lookAtContext = TargetingConditions.forNonCombat().range(p_148120_).selector((p_25531_) -> EntitySelector.notRiding(p_148118_).test(p_25531_));
        } else {
            this.lookAtContext = TargetingConditions.forNonCombat().range(p_148120_);
        }
    }

    @Override
    public boolean canUse() {
        if (!NoixmodAPIMainConfig.HorrorMode.get()) {
            return false;
        }
        if (this.lookAtType == Player.class) {
            this.lookAt = this.mob.level().getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            this.lookAt = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate(999), (p_148124_) -> true), this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
        return this.lookAt != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (!NoixmodAPIMainConfig.HorrorMode.get()) {
            return false;
        }
        if (this.lookAt == null) {
            return false;
        }
        if (!this.lookAt.isAlive()) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    @Override
    public void start() {
        this.lookTime = this.adjustedTickDelay(40 + this.mob.getRandom().nextInt(40));
    }

    @Override
    public void stop() {
        this.lookAt = null;
    }

    @Override
    public void tick() {
        if (this.lookAt != null && this.lookAt.isAlive()) {
            double $$0 = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
            this.mob.getLookControl().setLookAt(this.lookAt.getX(), $$0, this.lookAt.getZ());
            --this.lookTime;
        }
    }
}
