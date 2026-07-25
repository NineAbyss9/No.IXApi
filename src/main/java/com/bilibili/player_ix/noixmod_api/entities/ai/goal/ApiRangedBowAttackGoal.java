
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class ApiRangedBowAttackGoal
extends Goal {
    protected final RangedAttackMob attackMob;
    protected final Mob mob;
    protected final double speedModifier;
    protected int attackIntervalMin;
    protected final float attackRadiusSqr;
    protected int attackTime = -1;
    protected int seeTime;
    protected boolean strafingClockwise;
    protected boolean strafingBackwards;
    protected int strafingTime = -1;
    @Nullable
    protected final Predicate<ItemStack> predicate;

    public ApiRangedBowAttackGoal(RangedAttackMob p_25773_, double p_25774_, int p_25776_, float p_25777_) {
        this(p_25773_, p_25774_, p_25776_, p_25777_, null);
    }

    public ApiRangedBowAttackGoal(RangedAttackMob p_25773_, double p_25774_, int p_25776_, float p_25777_,
                                  @Nullable Predicate<ItemStack> stackPredicate) {
        if (!(p_25773_ instanceof LivingEntity)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.attackMob = p_25773_;
            this.mob = (Mob)p_25773_;
            this.speedModifier = p_25774_;
            this.attackIntervalMin = p_25776_;
            this.attackRadiusSqr = p_25777_ * p_25777_;
            this.predicate = stackPredicate;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public boolean isHoldingBow() {
        return this.mob.isHolding((is) -> is.getItem() instanceof BowItem);
    }

    public boolean canUse() {
        if (this.predicate != null && !this.predicate.test(this.mob.getMainHandItem())) {
            return false;
        }
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive() && this.isHoldingBow();
    }

    public void start() {
        this.mob.setAggressive(true);
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingBow();
    }

    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            return;
        }
        double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
        boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
        boolean flag1 = this.seeTime > 0;
        if (flag != flag1) {
            this.seeTime = 0;
        }
        if (flag) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }
        this.stopOrStrafe(d0, livingentity);
        this.useItem(flag, livingentity);
    }

    public boolean checkSee() {
        return this.seeTime >= 20;
    }

    public void stopOrStrafe(double distance, LivingEntity livingentity) {
        if (distance <= this.attackRadiusSqr && this.checkSee()) {
            this.mob.getNavigation().stop();
            ++this.strafingTime;
        } else {
            this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
            this.strafingTime = -1;
        }
        if (this.strafingTime >= 20) {
            if (ThreadLocalRandom.current().nextFloat() < 0.3) {
                this.strafingClockwise = !this.strafingClockwise;
            }
            if (ThreadLocalRandom.current().nextFloat() < 0.3) {
                this.strafingBackwards = !this.strafingBackwards;
            }
            this.strafingTime = 0;
        }
        if (this.strafingTime > -1) {
            if (distance > this.attackRadiusSqr * 0.75F) {
                this.strafingBackwards = false;
            } else if (distance < this.attackRadiusSqr * 0.25F) {
                this.strafingBackwards = true;
            }
            this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
            this.mob.lookAt(livingentity, 30.0F, 30.0F);
        } else {
            this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
        }
    }

    public void useItem(boolean flag, LivingEntity target) {
        if (this.mob.isUsingItem()) {
            if (!flag && this.seeTime < -60) {
                this.mob.setAggressive(false);
                this.mob.setTarget(null);
                this.mob.stopUsingItem();
            } else if (flag) {
                int i = this.mob.getTicksUsingItem();
                if (i >= 30) {
                    this.mob.stopUsingItem();
                    this.attackMob.performRangedAttack(target, BowItem.getPowerForTime(i));
                    this.attackTime = this.attackIntervalMin;
                }
            }
        } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
            this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, (item) -> item instanceof BowItem));
        }
    }

    public void stop() {
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.stopUsingItem();
    }
}
