
package com.bilibili.player_ix.noixmod_api.entities.servant.core;

import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractZombieServant
extends OwnableMob {
    protected static final AttributeModifier BABY_SPEED = new AttributeModifier(
            "1Player_IX2-931-Zombie-BabySpeed", 0.2, AttributeModifier.Operation.ADDITION);
    public AbstractZombieServant(EntityType<? extends AbstractZombieServant> entityType, Level level) {
        super(entityType, level);
        if (ThreadLocalRandom.current().nextFloat() <= 0.05F) {
            this.setBaby(true);
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new FollowOwnerGoal<>(this, 1.0,
                15F, 4F, false));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
    }

    protected PathNavigation createNavigation(Level pLevel) {
        GroundPathNavigation base = new GroundPathNavigation(this, pLevel);
        base.setAvoidSun(this.shouldBurn());
        return base;
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0 : -0.45;
    }

    protected float getStandingEyeHeight(Pose p_34313_, EntityDimensions p_34314_) {
        return this.isBaby() ? 0.93F : 1.74F;
    }

    public boolean isBaby() {
        return this.entityData.get(DATA_BABY);
    }

    public void setBaby(boolean b) {
        super.setBaby(b);
        AttributeInstance instance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (instance == null) {
            return;
        }
        if (b) {
            instance.addTransientModifier(BABY_SPEED);
        } else {
            instance.removeModifier(BABY_SPEED);
        }
    }

    public void tick() {
        super.tick();
        if (this.getParticleChance()) {
            this.makeParticle();
        }
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        this.playSound(SoundEvents.ZOMBIE_STEP);
    }

    protected boolean getParticleChance() {
        return ThreadLocalRandom.current().nextFloat() < 0.1F;
    }

    @Nullable
    public abstract ParticleOptions getAmbientParticle();

    public void makeParticle() {
        if (!this.level().isClientSide) {
            return;
        }
        if (this.getAmbientParticle() == null) {
            return;
        }
        double x = this.getRandomX(0.45);
        double y = this.getRandomY() - 0.01;
        double z = this.getRandomZ(0.45);
        this.level().addParticle(this.getAmbientParticle(), x, y, z, 0, 0, 0);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        if (ThreadLocalRandom.current().nextInt(5) == 0) {
            ItemStack stack;
            if (ThreadLocalRandom.current().nextBoolean()) {
                stack = ItemStacks.of(Items.IRON_SWORD);
            } else {
                stack = ItemStacks.of(Items.STONE_AXE);
            }
            this.setItemInHand(InteractionHand.MAIN_HAND, stack);
        }
    }

    public boolean shouldBurn() {
        return true;
    }

    public void affect(LivingEntity living) {
    }

    public void aiStep() {
        super.aiStep();
        if (this.tickCount % 10 != 0) return;
        MobUtils.burnInTheSun(this.shouldBurn(), this, 8);
    }

    protected static class AttackGoal
    extends ApiMeleeAttackGoal {
        protected final AbstractZombieServant zombieServant;
        protected int raiseArmTicks;
        public AttackGoal(AbstractZombieServant servant, double speed) {
            super(servant, speed);
            this.zombieServant = servant;
        }

        public void start() {
            super.start();
            this.raiseArmTicks = 0;
        }

        public void tick() {
            ++this.raiseArmTicks;
            this.zombieServant.setAggressive(this.raiseArmTicks >= 5 && this.ticksUntilNextAttack <
                    this.getAttackCooldown() / 2);
            super.tick();
        }

        public boolean use() {
            LivingEntity target = this.zombieServant.getTarget();
            if (target == null) {
                return false;
            }
            if (this.zombieServant.getMainHandItem().is(Items.TRIDENT)) {
                return this.zombieServant.closerThan(target, 4) &&
                        (target.isInWaterOrRain() || !this.zombieServant.level().isDay());
            }
            return true;
        }

        protected int getAttackCooldown() {
            return 15;
        }

        public boolean canUse() {
            return super.canUse() && this.use();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (!(p_25558_ <= d0) || this.ticksUntilNextAttack > 0) {
                return;
            }
            this.zombieServant.affect(p_25557_);
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(p_25557_);
        }
    }
}
