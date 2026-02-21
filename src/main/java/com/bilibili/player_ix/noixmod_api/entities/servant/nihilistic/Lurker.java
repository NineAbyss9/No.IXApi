
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiMobType;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.NihilitySummonedMobs;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class Lurker
extends NihilitySummonedMobs {
    public AnimationState attacking;
    public Lurker(EntityType<Lurker> e, Level l) {
        super(e, l);
        this.setMaxUpStep(3);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for(int $$0 = 0; $$0 < 2; ++$$0) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5), this.getRandomY(),
                        this.getRandomZ(0.5), 0.0, 0.0, 0.0);
            }
        }
        this.attacking.animateWhen(this.isAggressive(), this.tickCount);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.HUSK_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.EXPLOSION)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public void aiStep() {
        super.aiStep();
    }

    public Lurker(PlayMessages.SpawnEntity packet, Level world) {
        this(NoixmodAPIEntities.LURKER.get(), world) ;
    }

    public MobType getMobType() {
        return ApiMobType.NIHILISTIC_UNDEAD;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 0.5, false, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.25));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new OwnableTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Lurker.createPathAttributes().add(Attributes.MAX_HEALTH, 100).add(Attributes.ARMOR, 4)
                .add(Attributes.MOVEMENT_SPEED, 1).add(Attributes.FOLLOW_RANGE, 100)
                .add(Attributes.ATTACK_DAMAGE, 20)
                .add(Attributes.MOVEMENT_SPEED, 1);
    }

    {
        this.attacking = new AnimationState();
    }
}
