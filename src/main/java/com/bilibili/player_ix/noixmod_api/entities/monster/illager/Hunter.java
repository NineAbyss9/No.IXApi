
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.IXList;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.ApiSpellcaster;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Hunter
extends ApiSpellcaster {
    public Hunter(EntityType<? extends Hunter> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 5;
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NoixmodAPIItems.AXE_OF_HUNTER.get()));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1, Maths.square(2.5)));
        OwnableMob.addBehaviorGoals(this, 4, 0.8, 10F, true, true);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    @Nullable
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
        if (this.isCelebrating()) {
            return IllagerArmPose.CELEBRATING;
        }
        return IllagerArmPose.CROSSED;
    }

    /*
    public void summonWolf() {

    }*/

    public boolean doHurtTarget(Entity p_21372_) {
        if (p_21372_ instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.TETANUS.get(), Maths.toTick(3), this.level()
                    .getDifficulty().getId()));
            if (!living.level().isClientSide && !living.getMobType().equals(MobType.UNDEAD)) {
                EntityEventHandler.broadcastEntityEvent(living, 4);
            }
        }
        return super.doHurtTarget(p_21372_);
    }

    public static void init() {
        if (NoixmodAPIMainConfig.HunterCanJoinRaid.get()) {
            List<? extends Integer> list = NoixmodAPIMainConfig.HunterRaidCount.get();
            Raid.RaiderType.create("APIHunter", NoixmodAPIEntities.HUNTER.get(),
                    IXList.raidCount(list));
        }
    }
}
