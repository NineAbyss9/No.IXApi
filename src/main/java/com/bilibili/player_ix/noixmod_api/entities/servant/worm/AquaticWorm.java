
package com.bilibili.player_ix.noixmod_api.entities.servant.worm;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.mod.APIMonster;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class AquaticWorm
extends AbstractWorm {
    public AquaticWorm(EntityType<? extends AbstractWorm> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 2;
        this.setPathfindingMalus(BlockPathTypes.WATER, 1);
        this.moveControl = new AquaticWormMoveControl(this);
        this.refreshDimensions();
    }

    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    public void travel(Vec3 p_27490_) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, p_27490_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(p_27490_);
        }
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isInWater() && ThreadLocalRandom.current().nextFloat() <= 0.05F) {
            this.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP);
            this.level().addParticle(ParticleTypes.BUBBLE, this.getX(), this.getY(), this.getZ(), 0,
                    0.1, 0);
        }
    }

    public void aiStep() {
        super.aiStep();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1.2, false, false));
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 2, 40));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 5f));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,
                LivingEntity.class, true, this::canAttack));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractWorm.createPathAttributes().add(Attributes.MAX_HEALTH, 16)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.FOLLOW_RANGE, 64)
                .add(ForgeMod.SWIM_SPEED.get(), 1);
    }

    public void makeWormParticle() {
        if (!this.level().isClientSide) {
            WorldUtil.sendParticles(NoixmodAPIParticleTypes.WORM_PARTICLE.get(), this, 15,
                    this.random.nextGaussian() * 0.3);
            WorldUtil.sendParticles(ParticleTypes.BUBBLE, this, 6, 0);
        }
    }

    public static void init() {
        MobUtils.registerSpawn(NoixmodAPIEntities.AQUATIC_WORM.get(), SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR, (entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) ->
        APIMonster.checkAPIMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource)
                && NoixmodAPIMainConfig.AquaticWormWillSpawn.get() && randomSource.nextDouble() <= 0.05);
    }

    @Nullable
    public AbstractWorm getBreedMob() {
        return NoixmodAPIEntities.AQUATIC_WORM.get().create(this.level());
    }

    private class AquaticWormMoveControl
    extends MoveControl {
        public AquaticWormMoveControl(AquaticWorm p_24983_) {
            super(p_24983_);
        }

            public void tick() {
            if (AquaticWorm.this.isInWater()) {
                AquaticWorm.this.setDeltaMovement(AquaticWorm.this.getDeltaMovement().add(0, 0.0025, 0));
            }
            if (this.hasWanted() && !AquaticWorm.this.getNavigation().isDone()) {
                double dx = this.wantedX - AquaticWorm.this.getX();
                double dy = this.wantedY - AquaticWorm.this.getY();
                double dz = this.wantedZ - AquaticWorm.this.getZ();
                float f = (float) (Mth.atan2(dz, dx) * (180 / Math.PI)) - 90;
                float f1 = (float) (this.speedModifier * AquaticWorm.this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                AquaticWorm.this.setYRot(this.rotlerp(AquaticWorm.this.getYRot(), f, 10));
                AquaticWorm.this.yBodyRot = AquaticWorm.this.getYRot();
                AquaticWorm.this.yHeadRot = AquaticWorm.this.getYRot();
                if (AquaticWorm.this.isInWater()) {
                    AquaticWorm.this.setSpeed((float)AquaticWorm.this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    float f2 = -(float) (Mth.atan2(dy, (float) Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));
                    f2 = Mth.clamp(Mth.wrapDegrees(f2), -85, 85);
                    AquaticWorm.this.setXRot(this.rotlerp(AquaticWorm.this.getXRot(), f2, 5));
                    float f3 = Mth.cos(AquaticWorm.this.getXRot() * (float) (Math.PI / 180.0));
                    AquaticWorm.this.setZza(f3 * f1);
                    AquaticWorm.this.setYya((float) (f1 * dy));
                } else {
                    AquaticWorm.this.setSpeed(f1 * 0.5F);
                }
            } else {
                AquaticWorm.this.setSpeed(0);
                AquaticWorm.this.setYya(0);
                AquaticWorm.this.setZza(0);
            }
            AquaticWorm.this.refreshDimensions();
        }
    }
}
