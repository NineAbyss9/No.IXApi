
package com.bilibili.player_ix.noixmod_api.entities.servant.nether;

import com.github.NineAbyss9.ix_api.api.mobs.ApiRangedAttackMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.AbstractSkeletonServant;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class WitherSkeletonServant
extends AbstractSkeletonServant
implements ApiRangedAttackMob {
    private final OwnerSummon ownerSummon = new OwnerSummon(this);
    public WitherSkeletonServant(EntityType<? extends WitherSkeletonServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        if (p_21684_.random.nextBoolean()) {
            this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
        } else {
            this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ApiRangedBowAttackGoal(this, 0.6,
                10, 20F));
        this.goalSelector.addGoal(2, new AttackGoal(this, 0.8));
        this.addTargetGoal();
        this.addBehaviorGoal(4, 0.6, 10F);
        this.goalSelector.addGoal(3, new FollowOwnerGoal<>(this, 1, 30F,
                10F, false));
        super.registerGoals();
    }

    public boolean fireImmune() {
        return true;
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        if (p_20122_.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        return super.isInvulnerableTo(p_20122_);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_SKELETON_DEATH;
    }

    public boolean doHurtTarget(Entity p_21372_) {
        if (p_21372_ instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.WITHER, Maths.toTick(3)));
        }
        return super.doHurtTarget(p_21372_);
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        Arrow arrow = new Arrow(this.level(), this);
        arrow.setEnchantmentEffectsFromEntity(this, pDistanceFactor);
        arrow.setOwner(this);
        arrow.setSecondsOnFire(8);
        arrow.setEffectsFromItem(stack);
        arrow.addEffect(new MobEffectInstance(MobEffects.WITHER, Maths.toTick(3)));
        return arrow;
    }

    public boolean burnInSun() {
        return false;
    }

    public void performRangedAttack(LivingEntity livingEntity, float v) {
        AbstractArrow arrow = this.getArrow(this.getProjectile(this.getItemInHand(ProjectileUtil
                .getWeaponHoldingHand(this, item -> item instanceof BowItem))), v);
        double x = ownerSummon.projectileDouble(livingEntity)[0];
        double y = ownerSummon.projectileDouble(livingEntity)[1];
        double z = ownerSummon.projectileDouble(livingEntity)[2];
        arrow.shoot(x, y, z, 2F, 0.8F);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat()
                * 0.4f + 0.8f));
        this.level().addFreshEntity(arrow);
    }

    @Nullable
    public SoundEvent getStepSound() {
        return SoundEvents.WITHER_SKELETON_STEP;
    }

    private static class AttackGoal
    extends ApiMeleeAttackGoal {
        public AttackGoal(PathfinderMob finder, double speed) {
            super(finder, speed, Maths.square(2.5));
        }

        public boolean canUse() {
            if (this.checkItem()) {
                return false;
            }
            return super.canUse();
        }

        public boolean canContinueToUse() {
            if (this.checkItem()) {
                return false;
            }
            return super.canContinueToUse();
        }

        private boolean checkItem() {
            return this.mob.getMainHandItem().is(Items.BOW);
        }
    }
}
