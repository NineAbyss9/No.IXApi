
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import com.bilibili.player_ix.noixmod_api.magic.illager.SelfFangsSpell;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FangsDancer extends APISpellcaster {
    private static final int DEFAULT_DANCE_TIME = Maths.toTick(6);
    private static final EntityDataAccessor<Integer> DATA_DANCE_TIME;
    public FangsDancer(EntityType<FangsDancer> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 15;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DANCE_TIME, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new AttackGoal());
        this.goalSelector.addGoal(2, new RangedAttackGoal());
        this.goalSelector.addGoal(3, new DanceSpellGoal());
        OwnableMob.addBehaviorGoals(this, 6, 0.8, 12F, true, true);
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
        super.registerGoals();
    }

    public void aiStep() {
        super.aiStep();
        /*if (danceTime == 100) {
            for (int i = 0; i < 99; i++) {

            }
        }*/
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getDanceTime() > 0) {
            this.setDanceTime(getDanceTime() - 1);
        }
    }

    private void spawnFangs() {
        var target = this.getTarget();
        if (target != null) {
            EvokerFangs fangs = EntityType.EVOKER_FANGS.create(level());
            if (fangs != null) {
                fangs.setOwner(this);
                fangs.moveTo(blockPosition(), 0, 0);
                level().addFreshEntity(fangs);
            }
        }
    }

    private boolean isDancing() {
        return getDanceTime() > 0;
    }

    private int getDanceTime() {
        return this.entityData.get(DATA_DANCE_TIME);
    }

    private void setDanceTime(int time) {
        this.entityData.set(DATA_DANCE_TIME, time);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.EVOKER_HURT;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    static {
        DATA_DANCE_TIME = SynchedEntityData.defineId(FangsDancer.class, EntityDataSerializers.INT);
    }

    private class AttackGoal extends UseSpellGoal {
        AttackGoal() {
        }

        protected void castSpell() {
            LivingEntity livingentity = FangsDancer.this.getTarget();
            assert livingentity != null : "The target of " + FangsDancer.this.getClass().getSimpleName() + " is NULL";
            double d0 = Math.min(livingentity.getY(), FangsDancer.this.getY());
            double d1 = Math.max(livingentity.getY(), FangsDancer.this.getY()) + 1.0D;
            float f = (float)Mth.atan2(livingentity.getZ() - FangsDancer.this.getZ(), livingentity.getX() - FangsDancer.this.getX());
            if (FangsDancer.this.distanceToSqr(livingentity) < 9.0D) {
                for(int i = 0; i < 5; ++i) {
                    float f1 = f + i * Maths.PI * 0.4F;
                    this.createSpellEntity(FangsDancer.this.getX() + Mth.cos(f1) * 1.5D, FangsDancer.this.getZ() + Mth.sin(f1) * 1.5D, d0,
                            d1, f1, 0);
                }
                for(int k = 0; k < 8; ++k) {
                    float f2 = f + k * Maths.PI * 2.0F / 8.0F + 1.2566371F;
                    this.createSpellEntity(FangsDancer.this.getX() + Mth.cos(f2) * 2.5D, FangsDancer.this.getZ() + Mth.sin(f2)
                            * 2.5D, d0, d1, f2, 3);
                }
            } else {
                for(int count = 0; count < 19; ++count) {
                    double d2 = 1.25D * (count + 1);
                    this.createSpellEntity(FangsDancer.this.getX() + Mth.cos(f) * d2, FangsDancer.this.getZ() + Mth.sin(f) * d2, d0, d1, f, count);
                }
            }
        }

        private void createSpellEntity(double pX, double pZ, double pMinY, double pMaxY, float pYRot, int pWarmupDelay) {
            BlockPos blockpos = BlockPos.containing(pX, pMaxY, pZ);
            boolean flag = false;
            double d0 = 0.0D;
            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = FangsDancer.this.level().getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(FangsDancer.this.level(), blockpos1, Direction.UP)) {
                    if (!FangsDancer.this.level().isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = FangsDancer.this.level().getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(FangsDancer.this.level(), blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }
                    flag = true;
                    break;
                }
                blockpos = blockpos.below();
            } while(blockpos.getY() >= Mth.floor(pMinY) - 1);
            if (flag) {
                FangsDancer.this.level().addFreshEntity(new EvokerFangs(FangsDancer.this.level(), pX, blockpos.getY() + d0, pZ,
                        pYRot, pWarmupDelay, FangsDancer.this));
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 140;
        }

        @Nullable
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.ATTACK;
        }
    }

    private class RangedAttackGoal extends UseSpellGoal {

        protected void castSpell() {
            FangsDancer.this.spawnFangs();
            if (level() instanceof ServerLevel level) {
                SelfFangsSpell spell = new SelfFangsSpell();
                spell.castSpell(level, FangsDancer.this);
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 300;
        }

        @Nullable
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.UNKNOWN;
        }
    }

    private class DanceSpellGoal extends UseSpellGoal {
        private DanceSpellGoal() {
        }

        public void start() {
            super.start();
            FangsDancer.this.setDanceTime(DEFAULT_DANCE_TIME);
        }

        protected void castSpell() {
            spawnFangs();
        }

        public void stop() {
            super.stop();
        }

        protected int getCastingTime() {
            return 80;
        }

        protected int getCastingInterval() {
            return 800;
        }

        @Nullable
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_WOLOLO;
        }

        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.DARK;
        }

        public boolean canUse() {
            if (FangsDancer.this.isDancing()) {
                return false;
            }
            if (FangsDancer.this.level().getRandom().nextFloat() > 0.25f) {
                return false;
            }
            return super.canUse();
        }
    }
}
