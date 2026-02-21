
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.AbstractZombieServant;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GraveGhost
extends AbstractZombieServant
implements Enemy {
    public GraveGhost(EntityType<? extends GraveGhost> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = XP_REWARD_SMALL;
        this.setBaby(false);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(p_21684_.getDifficulty().equals(Difficulty.HARD)
               ? Items.DIAMOND_SWORD : Items.IRON_SWORD));
    }

    @Override
    public boolean isHostile() {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource p_21239_) {
        return SoundEvents.HUSK_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    @Override
    public ParticleOptions getAmbientParticle() {
        return ParticleTypes.SOUL;
    }

    @Override
    public float getVoicePitch() {
        return 0.9F;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1, false, false));
        this.addTargetGoal();
    }

    @NotNull
    public static AttributeSupplier.Builder createAttributes() {
        return GraveGhost.createPathAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.ARMOR, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 3);
    }
}
