
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.AbstractUseSpellGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiAvoidTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.NormalCastingSpellGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EvokerServant
extends OwnableIllager {
    public EvokerServant(EntityType<? extends EvokerServant> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new NormalCastingSpellGoal(this));
        this.goalSelector.addGoal(2, new SummonSpellGoal(this));
        this.goalSelector.addGoal(3, new AttackSpellGoal(this));
        this.goalSelector.addGoal(4, new ApiAvoidTargetGoal(this, 8.0F,
                0.8, 1));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class,
                10.0F));
    }

    public ApiPose getPoses() {
        if (this.isCastingSpell()) {
            return ApiPose.SPELL_CASTING;
        }
        return ApiPose.CROSSED;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_32654_) {
        return SoundEvents.EVOKER_HURT;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    public boolean canCastSpell() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.FOLLOW_RANGE, 56).add(Attributes.MAX_HEALTH, 24);
    }

    private static class SummonSpellGoal extends AbstractUseSpellGoal {
        EvokerServant servant;
        SummonSpellGoal(EvokerServant finder) {
            super(finder);
            servant = finder;
        }

        protected void castSpell() {
            for (int i = 0; i < 3;i++) {
                if (!mob.level().isClientSide) {
                    OwnableMob ownableMob;
                    ServerLevel serverLevel = (ServerLevel) mob.level();
                    boolean flag = mob.getRandom().nextBoolean();
                    if (flag) {
                        ownableMob = new VexArcher(NoixmodAPIEntities.VEX_ARCHER.get(), serverLevel);
                    } else {
                        ownableMob = new VexServant(NoixmodAPIEntities.VEX_SERVANT.get(), serverLevel);
                    }
                    ownableMob.setOwner(ownerOrThis(servant, servant));
                    ownableMob.moveTo(servant.position().add(Vec9.of(Maths.randomInteger(2), 0,
                            Maths.randomInteger(2))));
                    serverLevel.addFreshEntity(ownableMob);
                    ParticleUtil.spawnAnim(ownableMob);
                }
            }
        }

        protected int getCastingTime() {
            return 100;
        }

        protected int getCastingInterval() {
            return 340;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.RANGE;
        }
    }

    private static class AttackSpellGoal extends AbstractUseSpellGoal {
        EvokerServant evokerServant;
        AttackSpellGoal(EvokerServant servant) {
            super(servant);
            evokerServant = servant;
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 100;
        }

        protected void castSpell() {
            LivingEntity livingentity = evokerServant.getTarget();
            if (livingentity != null) {
                double d0 = Math.min(livingentity.getY(), evokerServant.getY());
                double d1 = Math.max(livingentity.getY(), evokerServant.getY()) + 1.0;
                float f = (float) Mth.atan2(livingentity.getZ() - evokerServant.getZ(), livingentity.getX()
                        - evokerServant.getX());
                int k;
                if (evokerServant.distanceToSqr(livingentity) < 9.0) {
                    float f2;
                    for (k = 0;k < 5;++k) {
                        f2 = f + (float) k * 3.1415927F * 0.4F;
                        this.createSpellEntity(evokerServant.getX() + Mth.cos(f2) * 1.5,
                                evokerServant.getZ() + Mth.sin(f2) * 1.5, d0, d1, f2, 0);
                    }

                    for (k = 0;k < 8;++k) {
                        f2 = f + k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
                        this.createSpellEntity(evokerServant.getX() + Mth.cos(f2) * 2.5,
                                evokerServant.getZ() + (double) Mth.sin(f2) * 2.5, d0, d1, f2, 3);
                    }
                } else {
                    for (k = 0;k < 16;++k) {
                        double d2 = 1.25 * (double) (k + 1);
                        this.createSpellEntity(evokerServant.getX() + Mth.cos(f) * d2,
                                evokerServant.getZ() + Mth.sin(f) * d2, d0, d1, f, k);
                    }
                }
            }
        }

        private void createSpellEntity(double p_32673_, double p_32674_, double p_32675_, double p_32676_,
                                       float p_32677_, int p_32678_) {
            BlockPos blockpos = BlockPos.containing(p_32673_, p_32676_, p_32674_);
            boolean flag = false;
            double d0 = 0.0;
            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = evokerServant.level().getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(evokerServant.level(), blockpos1, Direction.UP)) {
                    if (!evokerServant.level().isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = evokerServant.level().getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(evokerServant.level(), blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }
                    flag = true;
                    break;
                }
                blockpos = blockpos.below();
            } while(blockpos.getY() >= Mth.floor(p_32675_) - 1);
            if (flag) {
                evokerServant.level().addFreshEntity(new EvokerFangs(evokerServant.level(), p_32673_,
                        blockpos.getY() + d0, p_32674_, p_32677_, p_32678_, evokerServant));
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.ATTACK;
        }
    }
}
