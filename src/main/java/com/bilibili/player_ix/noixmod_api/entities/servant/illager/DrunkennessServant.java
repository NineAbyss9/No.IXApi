
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class DrunkennessServant
extends OwnableIllager
{
    public DrunkennessServant(EntityType<? extends DrunkennessServant> entityType, Level level)
    {
        super(entityType, level);
    }

    protected void registerGoals() {
        super.registerGoals();
        OwnableMob.addBehaviorGoals(this, 4, 0.6, 5F, false, false);
    }

    protected void addAttackGoal()
    {
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1, 9));
    }

    public void tick() {
        super.tick();
        int integer = this.getTicksUsingItem();
        if (this.getHealth() <= 20 && this.isAlive() && !this.getOffhandItem().isEmpty()) {
            this.startUsingItem(InteractionHand.OFF_HAND);
        }
        if (integer >= 19) {
            this.completeUsingItem();
        }
    }

    protected void completeUsingItem() {
        if (this.getUseItem().is(NoixmodAPIItems.WINE.get())) {
            if (this.isAlive()) {
                this.setHealth(this.getMaxHealth());
            }
        }
        this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
    }

    public boolean isAttackable() {
        return !this.isUsingItem();
    }

    public boolean isPickable() {
        return !this.isUsingItem();
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (isUsingItem()) return;
        super.actuallyHurt(p_21240_, p_21241_);
    }

    protected void populateDefaultItems()
    {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
        this.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(NoixmodAPIItems.WINE.get()));
    }

    public ApiPose getPoses()
    {
        if (isAggressive()) {
            return ApiPose.ATTACKING;
        }
        return ApiPose.CROSSED;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.VINDICATOR_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }
}
