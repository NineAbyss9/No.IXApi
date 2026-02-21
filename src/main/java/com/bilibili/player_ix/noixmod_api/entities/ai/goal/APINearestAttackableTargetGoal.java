
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.function.Predicate;

public class APINearestAttackableTargetGoal<T extends LivingEntity>
extends TargetGoal {
    protected final Class<T>[] targetType;
    protected final int randomInterval;
    @Nullable
    protected LivingEntity target;
    protected TargetingConditions targetConditions;

    @SafeVarargs
    public APINearestAttackableTargetGoal(Mob p_26060_, boolean p_26062_, Class<T>... c) {
        this(p_26060_, 10, p_26062_, false, null, c);
    }

    @SafeVarargs
    public APINearestAttackableTargetGoal(Mob p_199891_, boolean p_199893_, Predicate<LivingEntity> p_199894_,
                                          Class<T>... c) {
        this(p_199891_, 10, p_199893_, false, p_199894_, c);
    }

    @SafeVarargs
    public APINearestAttackableTargetGoal(Mob p_26064_, boolean p_26066_, boolean p_26067_, Class<T>... c) {
        this(p_26064_, 10, p_26066_, p_26067_, null, c);
    }

    @SafeVarargs
    public APINearestAttackableTargetGoal(Mob p_26053_, int p_26055_, boolean p_26056_, boolean p_26057_, @Nullable
    Predicate<LivingEntity> p_26058_, Class<T>... classes) {
        super(p_26053_, p_26056_, p_26057_);
        this.targetType = classes;
        this.randomInterval = reducedTickDelay(p_26055_);
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(p_26058_);
    }

    public boolean canUse() {
        if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
            return false;
        } else {
            this.findTarget();
            return this.target != null;
        }
    }

    protected AABB getTargetSearchArea(double p_26069_) {
        return this.mob.getBoundingBox().inflate(p_26069_, 4.0, p_26069_);
    }

    protected void findTarget() {
        Iterator<Class<T>> iterator = Arrays.stream(this.targetType).iterator();
        while (this.target == null && iterator.hasNext()) {
            if (iterator.next() != Player.class && iterator.next() != ServerPlayer.class) {
                this.target = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(iterator.next(),
                                this.getTargetSearchArea(this.getFollowDistance()), (p_148152_) -> true), this.targetConditions,
                        this.mob, this.mob.getX(),
                        this.mob.getEyeY(), this.mob.getZ());
                iterator.remove();
            } else {
                this.target = this.mob.level().getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(),
                        this.mob.getZ());
                iterator.remove();
            }
        }
    }

    public void start() {
        this.mob.setTarget(this.target);
        super.start();
    }

    public void setTarget(@Nullable LivingEntity p_26071_) {
        this.target = p_26071_;
    }
}
