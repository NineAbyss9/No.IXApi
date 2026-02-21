
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.util.IXList;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIAttributes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Flagman extends AbstractIllager {
    public Flagman(EntityType<? extends Flagman> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 1;
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_AXE));
        this.setItemSlot(EquipmentSlot.HEAD, Raid.getLeaderBannerInstance());
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FlagmanMeleeAttackGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void applyRaidBuffs(int i, boolean b) {}

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

    public IllagerArmPose getArmPose() {
        if (this.isAggressive()) {
            return IllagerArmPose.ATTACKING;
        }
        return IllagerArmPose.CROSSED;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NoixmodAPIAttributes.baseAttributes(4, 0.37000034523423, 0)
                .add(Attributes.MAX_HEALTH, 24).add(Attributes.FOLLOW_RANGE, 42);
    }

    public static void initialize() {
        if (NoixmodAPIMainConfig.FlagmanJoinRaids.get())
            Raid.RaiderType.create("ApiFlagman", NoixmodAPIEntities.FLAGMAN.get(),
                    IXList.raidCount(NoixmodAPIMainConfig.FlagmanRaidCount.get()));
    }

    private static class FlagmanMeleeAttackGoal extends MeleeAttackGoal {
        public FlagmanMeleeAttackGoal(PathfinderMob p_25552_) {
            super(p_25552_, 1.1,
                    false);
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            return 4;
        }
    }
}
