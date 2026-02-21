
package com.bilibili.player_ix.noixmod_api.entities.servant.animal;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IAgeableMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSpiderServant
extends OwnableMob
implements IAgeableMob {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    public AbstractSpiderServant(EntityType<? extends AbstractSpiderServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public void makeStuckInBlock(BlockState p_33796_, Vec3 p_33797_) {
        if (!p_33796_.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(p_33796_, p_33797_);
        }
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    public void setClimbing(boolean p_33820_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_33820_) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 &= -2;
        }
        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, Maths.ZERO_BYTE);
    }

    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.5F;
    }

    protected PathNavigation createNavigation(Level p_33802_) {
        return new WallClimberNavigation(this, p_33802_);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33814_) {
        return SoundEvents.SPIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Nullable
    public SoundEvent getStepSound() {
        return SoundEvents.SPIDER_STEP;
    }

    protected void playStepSound(BlockPos p_33804_, BlockState p_33805_) {
        if (this.getStepSound() != null) {
            this.playSound(this.getStepSound());
        }
    }

    public IAgeableMob getBreedMob() {
        return null;
    }

    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(AbstractSpiderServant.class, EntityDataSerializers.BYTE);
    }

    protected static class SpiderAttackGoal
    extends ApiMeleeAttackGoal {
        public SpiderAttackGoal(PathfinderMob finder, double speed) {
            super(finder, speed);
        }

        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            return Maths.square(1.5);
        }
    }
}
