
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.api.mobs.NihilitySummonedMobs;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;

public class Golem
extends NihilitySummonedMobs
implements ApiPoseMob {
    public Golem(EntityType<? extends Golem> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 9;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1, 4));
        this.addBehaviorGoal(4, 0.6, 20f);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnableTargetGoal<>(this, false));
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide && this.level().random.nextBoolean()) {
            this.clientLevel().addParticle(NoixmodAPIParticleTypes.DARK_SPELL.get(),
                    this.getRandomX(1), this.getRandomY(), this.getRandomZ(1),
                    0, 0, 0);
        }
    }

    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        if (this.getOwner() != null) {
            this.getOwner().heal(9f);
        }
        return super.killedEntity(p_216988_, p_216989_);
    }

    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    public ApiPose getPoses() {
        if (this.isAggressive()) {
            return ApiPose.ATTACKING;
        }
        return ApiPose.NATURAL;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Golem.createPathAttributes().add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_KNOCKBACK, 1).add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.25786787867);
    }
}
