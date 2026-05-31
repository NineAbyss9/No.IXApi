
package com.bilibili.player_ix.noixmod_api.entities.servant.core;

import com.github.NineAbyss9.ix_api.api.mobs.ApiRangedAttackMob;
import com.github.NineAbyss9.ix_api.api.mobs.MobUtils;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class AbstractSkeletonServant
extends OwnableMob
implements ApiRangedAttackMob
{
    public AbstractSkeletonServant(EntityType<? extends AbstractSkeletonServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public void aiStep()
    {
        super.aiStep();
        if (!this.level().isClientSide) {
            MobUtils.burnInTheSun(this.burnInSun(), this, 4);
        }
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

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        Arrow arrow = new Arrow(this.level(), this);
        arrow.setEnchantmentEffectsFromEntity(this, pDistanceFactor);
        arrow.setOwner(this);
        arrow.setEffectsFromItem(stack);
        return arrow;
    }

    public boolean burnInSun() {
        return true;
    }

    @Nullable
    public SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.SKELETON_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }
}
