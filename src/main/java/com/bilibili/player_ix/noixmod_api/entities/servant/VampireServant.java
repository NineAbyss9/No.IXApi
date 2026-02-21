
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.api.mobs.ApiVillager;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class VampireServant
extends OwnableMob
implements ApiPoseMob {
    public VampireServant(EntityType<? extends VampireServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1.0));
        addBehaviorGoal(5, 0.8, 12f, true, true);
        targetSelector.addGoal(1, new OwnableTargetGoal<>(this, false));
        targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        MobUtils.burnInTheSun(true, this, 3);
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack stackIn = pPlayer.getItemInHand(pHand);
        if (stackIn.is(NoixmodAPIItems.BLOOD_BOTTLE.get())) {
            this.heal(this.getMaxHealth() / 2f);
            ItemUtil.shrink(stackIn, pPlayer);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if (p_21372_ instanceof LivingEntity entity) {
            if (entity instanceof ApiVillager || entity instanceof AbstractVillager) {
                if (!entity.level().isClientSide) {
                    EntityEventHandler.broadcastEntityEvent(entity, 4);
                }
                entity.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.DESIRE_FOR_BLOOD.get(), 400));
            }
        }
        return super.doHurtTarget(p_21372_);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SOUL_ESCAPE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.EMPTY;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SOUL_ESCAPE;
    }

    @Override
    public ApiPose getPoses() {
        if (this.isAggressive()) {
            if (this.getMainHandItem().is(Items.BOW)) {
                return ApiPose.BOW_AND_ARROW;
            }
            return ApiPose.ATTACKING;
        }
        return ApiPose.CROSSED;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.ARMOR, 2)
                .add(Attributes.FOLLOW_RANGE, 64);
    }
}
