
package com.bilibili.player_ix.noixmod_api.entities.servant.sculk;

import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.AbstractZombieServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPITags;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SculkZombie
extends AbstractZombieServant {
    public SculkZombie(EntityType<? extends SculkZombie> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        goalSelector.addGoal(1, new AttackGoal(this, 1));
        super.registerGoals();
        addTargetGoal();
    }

    protected void addTargetGoal() {
        targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
        targetSelector.addGoal(1, new OwnableTargetGoal<>(this, false));
    }

    public void affect(LivingEntity living) {
        if (!this.level().isClientSide) {
            ParticleUtil.sendParticles((ServerLevel)this.level(), ParticleTypes.SCULK_SOUL, living.position(), 2,
                    0.3, 0.5, 0.3, 0);
            living.hurt(this.damageSources().indirectMagic(this, this), 0.5F);
        }
    }

    public boolean shouldBurn() {
        return false;
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(NoixmodAPITags.SCULKS);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.HUSK_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    public ParticleOptions getAmbientParticle() {
        return ParticleTypes.SCULK_SOUL;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.ATTACK_DAMAGE, 5.5).add(Attributes.MOVEMENT_SPEED, 0.255)
                .add(Attributes.MAX_HEALTH, 30).add(Attributes.ARMOR, 4).add(Attributes.FOLLOW_RANGE, 56)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.1);
    }
}
