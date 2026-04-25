
package com.bilibili.player_ix.noixmod_api.entities.villager;

import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.projectile.VillagerFangs;
import com.bilibili.player_ix.noixmod_api.entities.villager.trades.ApiVillagerTrades;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class Exorcist
extends VillagerFighter {
    public Exorcist(EntityType<? extends Exorcist> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new FighterHealSpellGoal(9));
        this.goalSelector.addGoal(2, new KnockbackSpellGoal());
        this.goalSelector.addGoal(2, new AttackSpellGoal());
        this.goalSelector.addGoal(3, new FireSpellGoal());
        OwnableMob.addBehaviorGoals(this, 5, 0.8, 12F, true, true);
        super.registerGoals();
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, VillagerFighter.class)
                .setAlertOthers());
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return new Exorcist(NoixmodAPIEntities.EXORCIST.get(), serverLevel);
    }

    @Nullable
    protected VillagerTrades.ItemListing[] getTradeLists() {
        return ApiVillagerTrades.EXORCIST_TRADES;
    }

    public void aiStep() {
        super.aiStep();
        if (this.tickCount % 20 == 0) {
            this.heal(1F);
        }
    }

    private class FireSpellGoal
    extends UseSpellGoal {

        @Override
        protected void performSpellCasting() {
            LivingEntity living = Exorcist.this.getTarget();
            if (living != null) {
                List<LivingEntity> list = level().getEntitiesOfClass(LivingEntity.class, living.getBoundingBox().inflate(4),
                        target -> MobUtils.canHurt(target, Exorcist.this));
                for (LivingEntity target : list) {
                    target.hurt(damageSources().indirectMagic(Exorcist.this, Exorcist.this), 6.0f);
                }
                if (!level().isClientSide()) {
                    EntityEventHandler.broadcastEntityEvent(living, 2);
                }
                living.playSound(SoundEvents.FIRE_EXTINGUISH);
            }
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.FIRE;
        }
    }

    private class AttackSpellGoal
    extends UseSpellGoal {

        @Override
        protected void performSpellCasting() {
            LivingEntity livingentity = Exorcist.this.getTarget();
            if (livingentity != null) {
                double d0 = Math.min(livingentity.getY(), Exorcist.this.getY());
                double d1 = Math.max(livingentity.getY(), Exorcist.this.getY()) + 1.0;
                float f = (float)Mth.atan2(livingentity.getZ() - Exorcist.this.getZ(), livingentity.getX() -
                        Exorcist.this.getX());
                int k;
                for (int i = 0; i < 9; ++i) {
                    float f2;
                    for (k = 0; k < 5; ++k) {
                        f2 = f + (float) k * 3.1415927F * 0.4F;
                        this.createSpellEntity(livingentity.getX() + (double) Mth.cos(f2) * 1.5,
                                livingentity.getZ() + (double) Mth.sin(f2) * 1.5, d0, d1, f2, 0);
                    }
                    for (k = 0; k < 8; ++k) {
                        f2 = f + (float) k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
                        this.createSpellEntity(livingentity.getX() + (double) Mth.cos(f2) * 2.5,
                                livingentity.getZ() + (double) Mth.sin(f2) * 2.5, d0, d1, f2, 3);
                    }
                    this.createSpellEntity(livingentity.getX(), livingentity.getZ(), d0, d1, 0, 5);
                }
            }
        }

        private void createSpellEntity(double p_32673_, double p_32674_, double p_32675_, double p_32676_, float
                p_32677_, int p_32678_) {
            BlockPos blockpos = BlockPos.containing(p_32673_, p_32676_, p_32674_);
            boolean flag = false;
            double d0 = 0.0;
            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = Exorcist.this.level().getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(Exorcist.this.level(), blockpos1, Direction.UP)) {
                    if (!Exorcist.this.level().isEmptyBlock(blockpos)) {
                        BlockState state = Exorcist.this.level().getBlockState(blockpos);
                        VoxelShape voxelshape = state.getCollisionShape(Exorcist.this.level(), blockpos);
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
                VillagerFangs fangs = new VillagerFangs(NoixmodAPIEntities.VILLAGER_FANGS.get(), Exorcist.this.level());
                fangs.setPos(p_32673_, blockpos.getY() + d0, p_32674_);
                fangs.setOwner(Exorcist.this);
                fangs.setYRot(p_32677_ * 57.295776F);
                fangs.setWarmupDelayTicks(p_32678_);
                Exorcist.this.level().addFreshEntity(fangs);
            }
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 300;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.POTION;
        }
    }

    private class KnockbackSpellGoal extends UseSpellGoal {

        @Override
        protected void performSpellCasting() {
            double x = Exorcist.this.getX();
            double y = Exorcist.this.getY();
            double z = Exorcist.this.getZ();
            MobUtils.rangeHurt(4, 4, 4, Exorcist.this, Exorcist.this.damageSources().indirectMagic(
                    Exorcist.this, Exorcist.this
            ), 10F);
            if (!Exorcist.this.level().isClientSide()) EntityEventHandler.broadcastEntityEvent(Exorcist.this, 2);
            MobUtils.push(4, 4, 4, Exorcist.this, x / z * 1.2, 0.1, y / z * 1.2);
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 200;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.VILLAGER_NO;
        }

        @Override
        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.FIRE;
        }

        @Override
        public boolean canUse() {
            LivingEntity living = Exorcist.this.getTarget();
            if (living == null || Exorcist.this.distanceToSqr(living) > Maths.square(3)) {
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity living = Exorcist.this.getTarget();
            if (living == null || Exorcist.this.distanceToSqr(living) > Maths.square(3)) {
                return false;
            }
            return super.canContinueToUse();
        }
    }
}
