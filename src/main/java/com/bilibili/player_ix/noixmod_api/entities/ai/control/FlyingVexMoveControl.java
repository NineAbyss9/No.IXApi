
package com.bilibili.player_ix.noixmod_api.entities.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class FlyingVexMoveControl extends MoveControl {
    public FlyingVexMoveControl(Mob pMob) {
        super(pMob);
    }

    public void tick() {
        if (this.hasWanted()) {
            Vec3 $$0 = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(),
                    this.wantedZ - this.mob.getZ());
            double $$1 = $$0.length();
            if ($$1 < this.mob.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.5));
            } else {
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().add($$0.scale(
                        this.speedModifier * 0.05 / $$1)));
                if (this.mob.getTarget() == null) {
                    Vec3 $$2 = this.mob.getDeltaMovement();
                    this.mob.setYRot(-((float) Mth.atan2($$2.x(), $$2.z())) * 57.295776F);
                } else {
                    double $$3 = this.mob.getTarget().getX() - this.mob.getX();
                    double $$4 = this.mob.getTarget().getZ() - this.mob.getZ();
                    this.mob.setYRot(-((float)Mth.atan2($$3, $$4)) * 57.295776F);
                }
                this.mob.yBodyRot = this.mob.getYRot();
            }
        }
    }
}
