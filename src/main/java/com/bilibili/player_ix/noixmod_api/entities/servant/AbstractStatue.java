
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractStatue
extends OwnableMob {
    public AbstractStatue(EntityType<? extends AbstractStatue> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.blocksBuilding = true;
    }

    public void push(Entity p_21294_) {
    }

    public void push(double p_20286_, double p_20287_, double p_20288_) {
    }

    public void move(MoverType p_19973_, Vec3 p_19974_) {
    }

    protected boolean canRide(Entity p_20339_) {
        return false;
    }

    public boolean startRiding(Entity p_20330_) {
        return false;
    }

    public boolean startRiding(Entity p_21396_, boolean p_21397_) {
        return false;
    }

    public boolean canBeCollidedWith() {
        return true;
    }
}
