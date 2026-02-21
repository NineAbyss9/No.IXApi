
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class ApiMeleeAttackGoal extends Goal {
    protected final PathfinderMob mob;
    protected final double speedModifier;
    protected double pathedTargetX;
    protected double pathedTargetY;
    protected double pathedTargetZ;
    protected int ticksUntilNextPathRecalculation;
    protected int ticksUntilNextAttack;
    protected long lastCanUseCheck;
    protected int failedPathFindingPenalty = 0;
    protected final boolean canPenalize = false;
    protected final boolean p_1145;
    protected final boolean p_1146;
    protected final float p_1147;
    protected final double attackRange;

    public ApiMeleeAttackGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_, boolean p_25555_, float exp,
                              double range) {
        this.mob = p_25552_;
        this.speedModifier = p_25553_;
        this.p_1145 = p_25554_;
        this.p_1146 = p_25555_;
        this.p_1147 = exp;
        this.attackRange = range;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public ApiMeleeAttackGoal(PathfinderMob mob, double d, boolean p_1145, boolean p_1146) {
        this(mob, d, p_1145, p_1146, 2f, Math.PI);
    }

    public ApiMeleeAttackGoal(PathfinderMob mob, double d, boolean b, boolean f, float exp) {
        this(mob, d, b, f, exp, Math.PI);
    }

    public ApiMeleeAttackGoal(PathfinderMob finder, double speed) {
        this(finder, speed, false, false);
    }

    public ApiMeleeAttackGoal(PathfinderMob finder, double speed, double range) {
        this(finder, speed, false, false, 0F, range);
    }

    public boolean canUse() {
        long i = this.mob.level().getGameTime();
        if (i - this.lastCanUseCheck < 20L) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.mob.getTarget();
            Path path;
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (this.canPenalize) {
                if (--this.ticksUntilNextPathRecalculation <= 0) {
                    path = this.mob.getNavigation().createPath(livingentity, 0);
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                    return path != null;
                } else {
                    return true;
                }
            } else {
                path = this.mob.getNavigation().createPath(livingentity, 0);
                if (path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(),
                            livingentity.getY(), livingentity.getZ());
                }
            }
        }
    }

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
        }
    }

    public void start() {
        LivingEntity living = this.mob.getTarget();
        if (living != null) {
            this.mob.getMoveControl().setWantedPosition(living.getX(), living.getY(), living.getZ(), this.speedModifier);
        }
        this.mob.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {
        LivingEntity livingentity = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.mob.setTarget(null);
        }
        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            this.mob.getLookControl().setLookAt(livingentity, 30.0F, this.mob.getMaxHeadXRot());
            double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(livingentity);
            double p_1147 = this.mob.distanceToSqr(livingentity);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 &&
                    (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || livingentity
                            .distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 ||
                            this.mob.getRandom().nextFloat() < 0.05F)) {
                this.pathedTargetX = livingentity.getX();
                this.pathedTargetY = livingentity.getY();
                this.pathedTargetZ = livingentity.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                if (this.canPenalize) {
                    this.ticksUntilNextPathRecalculation += this.failedPathFindingPenalty;
                    if (this.mob.getNavigation().getPath() != null) {
                        Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                        if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.asVec3()) < 0.6) {
                            this.failedPathFindingPenalty = 0;
                        } else {
                            this.failedPathFindingPenalty += 4;
                        }
                    } else {
                        this.failedPathFindingPenalty += 4;
                    }
                }
                if (d0 > 1024.0) {
                    this.ticksUntilNextPathRecalculation += 4;
                } else if (d0 > 256.0) {
                    this.ticksUntilNextPathRecalculation += 2;
                }
                if (p_1146 && p_1147 < Maths.square(this.attackRange)) {
                    this.mob.level().explode(this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.p_1147,
                            Level.ExplosionInteraction.MOB);
                    ((ServerLevel)this.mob.level()).sendParticles(ParticleTypes.LARGE_SMOKE, this.mob.getX(), this.mob.getY(),
                            this.mob.getZ(), 15, 0, 0, 0, 0.35);
                    this.mob.discard();
                }
                if (!this.mob.getNavigation().moveTo(livingentity, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }
                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingentity, d0);
        }
    }

    protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
        double d0 = this.getAttackReachSqr(p_25557_);
        if (p_25558_ <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(p_25557_);
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.getAttackCooldown();
    }

    protected int getAttackCooldown() {
        return p_1145 ? (int)((this.mob.getHealth() / this.mob.getMaxHealth()) * 10f) : reducedTickDelay(20);
    }

    protected double getAttackReachSqr(LivingEntity p_25556_) {
        if (this.attackRange != Math.PI) {
            return this.attackRange;
        }
        return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + p_25556_.getBbWidth();
    }
}