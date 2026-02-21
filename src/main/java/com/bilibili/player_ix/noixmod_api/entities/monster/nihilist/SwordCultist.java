
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SwordCultist extends Nihilist implements Enemy {
    public SwordCultist(EntityType<SwordCultist> type, Level level) {
        super(type, level);
        this.xpReward = 5;
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1.2,
                Maths.square(1.5)));
        OwnableMob.addBehaviorGoals(this, 5, 0.8, 10F, true, true);
        this.targetSelector.addGoal(0, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Nihilist.class).setAlertOthers());
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.tickCount % 90 == 0) {
            this.heal(1f);
        }
        if (this.level().isClientSide) {
            if (this.level().random.nextBoolean()) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5),
                        this.getRandomY(), this.getRandomZ(0.8), 0, 0, 0);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if (!p_21372_.level().isClientSide) {
            WorldUtil.sendParticles(NoixmodAPIParticleTypes.PURPLE_FLAME.get(), p_21372_,
                    9, 0, 2, 0, this.randomUtil.nextGaussian() * 0.3);
        }
        return super.doHurtTarget(p_21372_);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        float damage = Math.min(10f, pAmount);
        if (this.randomUtil.nextInt(3) == 0) {
            if (!this.level().isClientSide) {
                WorldUtil.sendParticles(NoixmodAPIParticleTypes.PURPLE_FLAME.get(),
                        this, 4, 1, 2, 1, 0);
            }
            this.playSound(SoundEvents.FIRE_EXTINGUISH, 0.75f, 0.5F);
            return false;
        }
        return super.hurt(pSource, damage);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NoixmodAPISounds.CULTIST_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return NoixmodAPISounds.CULTIST_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NoixmodAPISounds.CULTIST_DEATH.get();
    }

    @Override
    public NihilistArmPose getArmPose() {
        if (this.isAggressive()) {
            return NihilistArmPose.ATTACKING;
        }
        return NihilistArmPose.CROSSED;
    }
}
