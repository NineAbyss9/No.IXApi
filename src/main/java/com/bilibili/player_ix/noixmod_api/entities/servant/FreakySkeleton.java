
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.api.mobs.NihilitySummonedMobs;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FreakySkeleton extends NihilitySummonedMobs implements Ownable, RangedAttackMob {
    public FreakySkeleton(EntityType<? extends FreakySkeleton> e, Level l) {
        super(e, l);
        this.setNoGravity(true);
        this.moveControl = new NihilityGhastMoveControl(this);
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return EntityType.WITHER_SKELETON.getDefaultLootTable();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public void aiStep() {
        super.aiStep();
        if (this.tickCount %1200 == 0) {
            this.discard();
        }
    }

    public FreakySkeleton(PlayMessages.SpawnEntity packet, Level world) {
        this(NoixmodAPIEntities.FREAKY_SKELETON.get(), world);
    }

    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.WITHER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    protected float getSoundVolume() {
        return 0.5f;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(0, new ApiOwnerTargetGoal(this));
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new GhastLookGoal(this));
        this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this,
                Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ARMOR, 2)
                .add(Attributes.FLYING_SPEED, 1)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.FOLLOW_RANGE, 72)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.15);
    }

    public void performRangedAttack(LivingEntity livingEntity, float v) {
        double d0 = this.getX(this.getX());
        double d1 = this.getY(this.getY());
        double d2 = this.getZ(this.getZ());
        double d3 = livingEntity.getX() - d0;
        double d5 = livingEntity.getZ() - d2;
        double d4 = livingEntity.getY() - d1;
        WitherSkull witherskull = new WitherSkull(this.level(), this, d3, d4 + 0.5, d5);
        witherskull.setOwner(this);
        witherskull.setDangerous(false);
        witherskull.setPosRaw(d0, d1, d2);
        this.level().addFreshEntity(witherskull);
    }

    static class NihilityGhastMoveControl
            extends MoveControl {
        private final FreakySkeleton ghast;
        private int floatDuration;

        public NihilityGhastMoveControl(FreakySkeleton $$0) {
            super($$0);
            this.ghast = $$0;
        }

        @Override
        public void tick() {
            if (this.operation != MoveControl.Operation.MOVE_TO) {
                return;
            }
            if (this.floatDuration-- <= 0) {
                this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
                Vec3 $$0 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                double $$1 = $$0.length();
                if (this.canReach($$0 = $$0.normalize(), Mth.ceil($$1))) {
                    this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add($$0.scale(0.1)));
                } else {
                    this.operation = MoveControl.Operation.WAIT;
                }
            }
        }

        private boolean canReach(Vec3 $$0, int $$1) {
            AABB $$2 = this.ghast.getBoundingBox();
            for (int $$3 = 1; $$3 < $$1; ++$$3) {
                $$2 = $$2.move($$0);
                if (this.ghast.level().noCollision(this.ghast, $$2)) continue;
                return false;
            }
            return true;
        }
    }

    static class GhastLookGoal
            extends Goal {
        private final FreakySkeleton ghast;

        public GhastLookGoal(FreakySkeleton $$0) {
            this.ghast = $$0;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vec3 $$0 = this.ghast.getDeltaMovement();
                this.ghast.setYRot(-((float)Mth.atan2($$0.x(), $$0.z())) * 57.295776f);
                this.ghast.yBodyRot = this.ghast.getYRot();
            } else {
                LivingEntity $$1 = this.ghast.getTarget();
                if ($$1.distanceToSqr(this.ghast) < 4096.0) {
                    double $$3 = $$1.getX() - this.ghast.getX();
                    double $$4 = $$1.getZ() - this.ghast.getZ();
                    this.ghast.setYRot(-((float)Mth.atan2($$3, $$4)) * 57.295776f);
                    this.ghast.yBodyRot = this.ghast.getYRot();
                }
            }
        }
    }

    static class GhastShootFireballGoal
            extends Goal {
        private final FreakySkeleton ghast;
        public int chargeTime;

        public GhastShootFireballGoal(FreakySkeleton $$0) {
            this.ghast = $$0;
        }

        @Override
        public boolean canUse() {
            return this.ghast.getTarget() != null;
        }

        @Override
        public void start() {
            this.chargeTime = 0;
        }

        @Override
        public void stop() {
            this.ghast.setAggressive(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity $$0 = this.ghast.getTarget();
            if ($$0 == null) {
                return;
            }
            double $$1 = 64.0;
            if ($$0.distanceToSqr(this.ghast) < 4096.0 && this.ghast.hasLineOfSight($$0)) {
                Level $$2 = this.ghast.level();
                ++this.chargeTime;
                if (this.chargeTime == 10) {
                    Vec3 $$4 = this.ghast.getViewVector(1.0f);
                    double $$5 = $$0.getX() - (this.ghast.getX() + $$4.x * 4.0);
                    double $$6 = $$0.getY(0.5) - (0.5 + this.ghast.getY(0.5));
                    double $$7 = $$0.getZ() - (this.ghast.getZ() + $$4.z * 4.0);
                    this.ghast.playSound(SoundEvents.WITHER_SHOOT, 1, 1f);
                    WitherSkull $$8 = new WitherSkull($$2, this.ghast, $$5, $$6, $$7);
                    $$8.setPos(this.ghast.getX() + $$4.x * 4.0, this.ghast.getY(0.5) + 0.5, $$8.getZ() + $$4.z * 4.0);
                    $$2.addFreshEntity($$8);
                    this.chargeTime = -30;
                }
            } else if (this.chargeTime > 0) {
                --this.chargeTime;
            }
            this.ghast.setAggressive(this.chargeTime > 10);
        }
    }

    private static class RandomFloatAroundGoal extends Goal {
        private final FreakySkeleton ghast;

        public RandomFloatAroundGoal(FreakySkeleton p_32783_) {
            this.ghast = p_32783_;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl $$0 = this.ghast.getMoveControl();
            if (!$$0.hasWanted()) {
                return true;
            } else {
                double $$1 = $$0.getWantedX() - this.ghast.getX();
                double $$2 = $$0.getWantedY() - this.ghast.getY();
                double $$3 = $$0.getWantedZ() - this.ghast.getZ();
                double $$4 = $$1 * $$1 + $$2 * $$2 + $$3 * $$3;
                return $$4 < 1.0 || $$4 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource $$0 = this.ghast.getRandom();
            double $$1 = this.ghast.getX() + (double)(($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double $$2 = this.ghast.getY() + (double)(($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double $$3 = this.ghast.getZ() + (double)(($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.ghast.getMoveControl().setWantedPosition($$1, $$2, $$3, 1.0);
        }
    }
}
