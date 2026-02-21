
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class AbstractSkeletonServant
extends OwnableMob {
    public AbstractSkeletonServant(EntityType<? extends AbstractSkeletonServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    protected PathNavigation createNavigation(Level pLevel) {
        GroundPathNavigation base = new GroundPathNavigation(this, pLevel);
        base.setAvoidSun(this.burnInSun());
        return base;
    }

    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        if (this.getStepSound() != null) {
            this.playSound(this.getStepSound());
        }
    }

    public boolean burnInSun() {
        return true;
    }

    @Nullable
    public SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }
}
