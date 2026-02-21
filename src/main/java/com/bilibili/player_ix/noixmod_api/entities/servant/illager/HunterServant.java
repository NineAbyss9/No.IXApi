
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HunterServant
extends OwnableIllager {
    public HunterServant(EntityType<? extends HunterServant> entityType, Level level) {
        super(entityType, level);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NoixmodAPIItems.AXE_OF_HUNTER.get()));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.addBehaviorGoal(4, 0.8, 10F);
    }

    protected void addAttackGoal() {
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1, Maths.square(2.5)));
    }

    public int getExperienceReward() {
        if (!isHostile())
            return 0;
        return super.getExperienceReward();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.VINDICATOR_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    public ApiPose getPoses() {
        if (this.isAggressive()) {
            return ApiPose.ATTACKING;
        }
        return ApiPose.CROSSED;
    }

    public boolean doHurtTarget(Entity pEntity) {
        if (pEntity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.TETANUS.get(), Maths.toTick(3), this.level()
                    .getDifficulty().getId()));
            if (!living.level().isClientSide && !living.getMobType().equals(MobType.UNDEAD)) {
                EntityEventHandler.broadcastEntityEvent(living, 4);
            }
        }
        return super.doHurtTarget(pEntity);
    }
}
