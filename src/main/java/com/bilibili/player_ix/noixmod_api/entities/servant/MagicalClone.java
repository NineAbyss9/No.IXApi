
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.util.ObjectUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class MagicalClone
extends OwnableMob {
    private int invTicks = 120;
    public MagicalClone(EntityType<? extends MagicalClone> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(NoixmodAPIItems.MAGICAL_SWORD.get()));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1));
        this.addBehaviorGoal(4, 0.8, 14F);
        this.goalSelector.addGoal(4, new FollowOwnerGoal<>(this, 1,
                10, 2, false));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
        this.addTargetGoal(2);
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return ObjectUtil.FALSE;
    }

    public boolean doHurtTarget(Entity p_21372_) {
        if (p_21372_ instanceof LivingEntity lie) {
            if (lie.isAlive() && lie.hurtDuration <= 0 && lie.getHealth() > 5) {
                lie.setHealth(lie.getHealth() - 5);
            }
        }
        return super.doHurtTarget(p_21372_);
    }

    public void tick() {
        super.tick();
        if (invTicks > 0) {
            --this.invTicks;
        }
    }

    public void aiStep() {
        super.aiStep();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.invTicks > 0) {
            return false;
        } else {
            this.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE);
            this.spawnAnim();
            this.discard();
        }
        return false;
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
    }

    public float getLightLevelDependentMagicValue() {
        return 1f;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EXPERIENCE_ORB_PICKUP;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return OwnableMob.createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.FOLLOW_RANGE,
                        128).add(Attributes.MAX_HEALTH, 20).add(Attributes.ATTACK_DAMAGE, 5);
    }
}
