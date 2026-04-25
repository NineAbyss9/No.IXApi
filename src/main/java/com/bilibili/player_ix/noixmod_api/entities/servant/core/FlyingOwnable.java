
package com.bilibili.player_ix.noixmod_api.entities.servant.core;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public abstract class FlyingOwnable
extends OwnableMob {
    protected FlyingOwnable(EntityType<? extends FlyingOwnable> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {
    }

    public boolean onClimbable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public static class GhastFlyingMoveControl
            extends MoveControl {
        private int floatDuration;

        public GhastFlyingMoveControl(Mob p_24983_) {
            super(p_24983_);
        }

        public void tick() {
            super.tick();
            if (!this.hasWanted()) {
                return;
            }
            if (this.floatDuration-- <= 0) {
                this.floatDuration += this.mob.getRandom().nextInt(5) + 2;
                Vec3 $$0 = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(),
                        this.wantedZ - this.mob.getZ());
                double $$1 = $$0.length();
                if (this.canReach($$0 = $$0.normalize(), Mth.ceil($$1))) {
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().add($$0.scale(0.1)));
                } else {
                    this.operation = MoveControl.Operation.WAIT;
                }
            }
        }

        protected boolean canReach(Vec3 $$0, int $$1) {
            AABB $$2 = this.mob.getBoundingBox();
            for (int $$3 = 1; $$3 < $$1; ++$$3) {
                $$2 = $$2.move($$0);
                if (this.mob.level().noCollision(this.mob, $$2)) continue;
                return false;
            }
            return true;
        }
    }

    public static class GhastRandomMoveGoal
            extends Goal {
        protected final PathfinderMob mob;

        public GhastRandomMoveGoal(PathfinderMob m) {
            this.mob = m;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl $$0 = this.mob.getMoveControl();
            if (!$$0.hasWanted()) {
                return true;
            } else {
                double $$1 = $$0.getWantedX() - this.mob.getX();
                double $$2 = $$0.getWantedY() - this.mob.getY();
                double $$3 = $$0.getWantedZ() - this.mob.getZ();
                double $$4 = $$1 * $$1 + $$2 * $$2 + $$3 * $$3;
                return $$4 < 1.0 || $$4 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            super.start();
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                RandomSource $$0 = this.mob.getRandom();
                double $$1 = this.mob.getX() + (double) (($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double $$2 = this.mob.getY() + (double) (($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double $$3 = this.mob.getZ() + (double) (($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.mob.getMoveControl().setWantedPosition($$1, $$2, $$3, 5);
            } else {
                this.mob.getMoveControl().setWantedPosition(target.getRandomX(0.8), target.getY()
                        + 3, target.getRandomZ(0.8), 5);
            }
        }
    }

    public static class GhastLookGoal extends Goal {
        private final PathfinderMob ghast;

        public GhastLookGoal(PathfinderMob $$0) {
            this.ghast = $$0;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return true;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vec3 $$0 = this.ghast.getDeltaMovement();
                this.ghast.setYRot(-((float) Mth.atan2($$0.x(), $$0.z())) * 57.295776f);
            } else {
                LivingEntity $$1 = this.ghast.getTarget();
                if ($$1.distanceToSqr(this.ghast) < 4096.0) {
                    double $$3 = $$1.getX() - this.ghast.getX();
                    double $$4 = $$1.getZ() - this.ghast.getZ();
                    this.ghast.getLookControl().setLookAt($$1, 20F, this.ghast.getMaxHeadXRot());
                    this.ghast.setYRot(-((float) Mth.atan2($$3, $$4)) * 57.295776f);
                }
            }
            this.ghast.yBodyRot = this.ghast.getYRot();
        }
    }
}
