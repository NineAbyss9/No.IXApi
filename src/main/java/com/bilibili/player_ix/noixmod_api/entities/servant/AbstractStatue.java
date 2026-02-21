
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractStatue
extends OwnableMob {
    public AbstractStatue(EntityType<? extends AbstractStatue> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.blocksBuilding = true;
    }

    @Override
    public void push(@NotNull Entity p_21294_) {
    }

    @Override
    public void push(double p_20286_, double p_20287_, double p_20288_) {
    }

    @Override
    public void move(@NotNull MoverType p_19973_, @NotNull Vec3 p_19974_) {
    }

    @Override
    protected boolean canRide(@NotNull Entity p_20339_) {
        return false;
    }

    @Override
    public boolean startRiding(@NotNull Entity p_20330_) {
        return false;
    }

    @Override
    public boolean startRiding(@NotNull Entity p_21396_, boolean p_21397_) {
        return false;
    }

    @Override
    public boolean canRiderInteract() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
}
