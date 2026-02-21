
package com.bilibili.player_ix.noixmod_api.entities.servant.worm;

import com.github.NineAbyss9.ix_api.api.mobs.NihilityMobs;
import com.github.NineAbyss9.ix_api.api.mobs.NihilitySummonedMobs;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.projectile.SmokeTrap;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class FreakyWorm extends NihilitySummonedMobs {
    private static final Predicate<Entity> NO_RAVAGER_AND_ALIVE = (p_33346_) -> p_33346_.isAlive()
            && !(p_33346_ instanceof FreakyWorm);
    public static final EntityDataAccessor<Integer> ROAR_COOL_DOWN =
            SynchedEntityData.defineId(FreakyWorm.class, EntityDataSerializers.INT);

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ROAR_COOL_DOWN, 0);
    }

    public int getRoarCoolDown() {
        return this.entityData.get(ROAR_COOL_DOWN);
    }

    public void setRoarCoolDown(int nt) {
        this.entityData.set(ROAR_COOL_DOWN, nt);
    }

    public void tick() {
        super.tick();
        if (this.getRoarCoolDown() > 0) {
            this.setRoarCoolDown(this.getRoarCoolDown() - 1);
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RoarGoal());
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1,
                false));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 20f));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class,
                false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractGolem.class,
                false));
    }

    public FreakyWorm(EntityType<? extends FreakyWorm> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
        this.setMaxUpStep(1f);
    }

    public void aiStep() {
        super.aiStep();
        if (this.getOwner() != null && this.getOwner() instanceof Mob mob && mob.getTarget() != null) {
            this.setTarget(mob.getTarget());
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        if (pSource.getEntity() == this.getOwner()) {
            return false;
        }
        if (pSource.getEntity() instanceof NihilitySummonedMobs mob && mob.getOwner() == this.getOwner()) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public int getAmbientSoundInterval() {
        return this.random.nextInt(100);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    protected float getSoundVolume() {
        return 0.5f;
    }

    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 1.0f, 0.25f);
    }

    private void strongKnockback(Entity p_33340_) {
        double d0 = p_33340_.getX() - this.getX();
        double d1 = p_33340_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        p_33340_.push(d0 / d2 * 4.0, 0.2, d1 / d2 * 4.0);
    }

    public void Roar() {
        if (this.isAlive()) {
            List<LivingEntity> var1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0), NO_RAVAGER_AND_ALIVE);
            for (LivingEntity livingentity : var1) {
                if ((!(livingentity instanceof Nihilist) && !(livingentity instanceof NihilityMobs))) {
                    livingentity.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 6.0F);
                    this.strongKnockback(livingentity);
                }
            }
            for (int i = 0; i < 40; ++i) {
                double d0 = this.random.nextGaussian() * 0.2;
                double d1 = this.random.nextGaussian() * 0.2;
                double d2 = this.random.nextGaussian() * 0.2;
                ((ServerLevel)this.level()).sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 25, d0, d1, d2, 0.25);
            }
            ServerLevel level = (ServerLevel) this.level();
            for (int $$0 = 0; $$0 < 8; ++$$0) {
                SmokeTrap trap = (SmokeTrap) ((EntityType<?>) NoixmodAPIEntities.SMOKE_TRAP.get()).create(this.level());
                BlockPos pos = this.blockPosition().offset(-2 + this.random.nextInt(8), 1, -2 + this.random.nextInt(8));
                if (trap != null) {
                    trap.moveTo(pos, 0, 0);
                    trap.setOwner(this);
                    level.addFreshEntity(trap);
                }
            }
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40).add(Attributes.FOLLOW_RANGE,
                        100.0).add(Attributes.ARMOR, 4).add(Attributes.ATTACK_KNOCKBACK, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 10).add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    class RoarGoal extends Goal {
        public FreakyWorm worm = FreakyWorm.this;
        private int RoarTicks;

        public RoarGoal() {
            super();
        }

        @Override
        public void start() {
            super.start();
            this.RoarTicks = 60;
        }

        @Override
        public void tick() {
            super.tick();
            --this.RoarTicks;
            if (--this.RoarTicks == 0) {
                worm.Roar();
                this.stop();
            }
        }

        @Override
        public void stop() {
            super.stop();
            worm.setRoarCoolDown(600);
        }

        @Override
        public boolean canContinueToUse() {
            if (worm.getRoarCoolDown() != 0) {
                return false;
            }
            if (worm.getTarget() == null) {
                return false;
            }
            return worm.getTarget() != null;
        }

        @Override
        public boolean canUse() {
            if (worm.getTarget() == null) {
                return false;
            }
            if (worm.distanceToSqr(worm.getTarget()) > 8) {
                return false;
            }
            return worm.getRoarCoolDown() == 0;
        }
    }

    class LookGoal extends Goal {
        FreakyWorm worm = FreakyWorm.this;

        LookGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = worm.getTarget();
            if (target != null) {
                worm.lookAt(target, 100, 100);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return worm.getTarget() != null;
        }

        @Override
        public boolean canUse() {
            return worm.getTarget() != null;
        }
    }
}
