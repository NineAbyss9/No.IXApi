
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.IXList;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.ApiSpellcaster;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Drunkenness extends ApiSpellcaster {
    public Drunkenness(EntityType<? extends Drunkenness> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 5;
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
        this.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(NoixmodAPIItems.WINE.get()));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1, 9));
        OwnableMob.addBehaviorGoals(this, 4, 0.6, 5F, false, false);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void tick() {
        super.tick();
        int integer = this.getTicksUsingItem();
        if (this.getHealth() <= 20 && this.isAlive()) {
            this.startUsingItem(InteractionHand.OFF_HAND);
        }
        if (integer >= 20) {
            this.completeUsingItem();
        }
    }

    protected void completeUsingItem() {
        if (this.getUseItem().is(NoixmodAPIItems.WINE.get())) {
            if (this.isAlive()) {
                this.setHealth(this.getMaxHealth());
            }
        }
        super.completeUsingItem();
    }

    public boolean isAttackable() {
        return !this.isUsingItem();
    }

    public boolean isPickable() {
        return !this.isUsingItem();
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (!this.isUsingItem()) {
            super.actuallyHurt(p_21240_, p_21241_);
        }
    }

    public boolean canCastSpell() {
        return false;
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

    public static void initialize() {
        if (NoixmodAPIMainConfig.DrunkennessJoinRaids.get())
            Raid.RaiderType.create("ApiDrunkenness", NoixmodAPIEntities.DRUNKENNESS.get(), IXList
                    .raidCount(NoixmodAPIMainConfig.DrunkennessRaidCount.get()));
    }
}
