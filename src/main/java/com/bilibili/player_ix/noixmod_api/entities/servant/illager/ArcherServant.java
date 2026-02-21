
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.ApiRangedAttackMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ArcherServant
extends OwnableIllager
implements ApiRangedAttackMob {
    public ArcherServant(EntityType<? extends ArcherServant> entityType, Level level) {
        super(entityType, level);
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStacks.of(Items.BOW));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ApiRangedBowAttackGoal(this, 1.0, 20,
                20F));
        this.addBehaviorGoal(5, 0.8, 10F);
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        Arrow arrow = new Arrow(this.level(), this);
        arrow.setEffectsFromItem(stack);
        arrow.setEnchantmentEffectsFromEntity(this, pDistanceFactor);
        arrow.setCritArrow(this.level().random.nextInt(3) == 0);
        return arrow;
    }

    public void performRangedAttack(LivingEntity livingEntity, float v) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil
                .getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrow arrow = this.getArrow(itemstack, v);
        if (this.getMainHandItem().getItem() instanceof BowItem bow) {
            arrow = bow.customArrow(arrow);
        }
        double $$4 = livingEntity.getX() - this.getX();
        double $$5 = livingEntity.getY(0.5) - this.getY(0.5);
        double $$6 = livingEntity.getZ() - this.getZ();
        float speed = 2.2f;
        arrow.shoot($$4, $$5, $$6, speed, 0.8f);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f
                / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity(arrow);
    }

    public ApiPose getPoses() {
        if (this.isAggressive() && this.isHolding(Items.BOW)) {
            return ApiPose.BOW_AND_ARROW;
        }
        return super.getPoses();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.PILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    public static AttributeSupplier createAttributes() {
        return createPathAttributes().add(Attributes.MAX_HEALTH, 24)
                .add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.39)
                .add(Attributes.FOLLOW_RANGE, 56).build();
    }
}
