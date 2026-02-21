
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.ApiSpellcaster;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.projectile.WaterTrap;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WaterWarlock
extends ApiSpellcaster
implements Enemy {
    public final OwnerSummon ownerSummon = new OwnerSummon(this);
    public WaterWarlock(EntityType<? extends WaterWarlock> e, Level l) {
        super(e, l);
        this.xpReward = 5;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new SummonSpellGoal());
        this.goalSelector.addGoal(1, new DamageSpellGoal());
        this.goalSelector.addGoal(2, new AttackSpellGoal());
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.5f));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class,
                false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractGolem.class,
                false));
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    public void waterDamage() {
        if (this.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.FALLING_WATER, this.getX(), this.getY() + 0.1, this.getZ(), 50,
                    0, 0, 0,  this.random.nextGaussian() * 0.3);
        }
        MobUtils.rangeHurt(6, 6, 6, this, this.damageSources()
                .indirectMagic(this, this), 6f);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    public IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return IllagerArmPose.SPELLCASTING;
        }
        return IllagerArmPose.CROSSED;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Nihilist.createPathAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.15)
                .add(Attributes.ATTACK_KNOCKBACK, 1);
    }

    private class SummonSpellGoal extends UseSpellGoal {
        protected void castSpell() {
            if (!WaterWarlock.this.level().isClientSide){
                ISpell spell = Spells.DROWNED.get();
                spell.castSpell((ServerLevel)WaterWarlock.this.level(), WaterWarlock.this);
            }
        }

        @Override
        protected int getCastingTime() {
            return 80;
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.WATER;
        }
    }

    private class AttackSpellGoal extends UseSpellGoal {
        WaterWarlock warlock = WaterWarlock.this;

        @Override
        protected void castSpell() {
            LivingEntity $$0 = warlock.getTarget();
            assert $$0 != null;
            double $$1 = Math.min($$0.getY(), warlock.getY());
            double $$2 = Math.max($$0.getY(), warlock.getY()) + 2.0;
            float $$3 = (float) Mth.atan2($$0.getZ() - warlock.getZ(), $$0.getX() - warlock.getX());
            if (warlock.distanceToSqr($$0) < 6.0) {
                for (int $$4 = 0; $$4 < 27; ++$$4) {
                    float $$5 = $$3 + (float) $$4 * (float) Math.PI * 0.4f;
                    this.createSpellEntity(warlock.getX() + (double) Mth.cos($$5) * 1.5, warlock.getZ() + (double) Mth.sin($$5) * 1.5, $$1, $$2, $$5, 0);
                }
                for (int $$6 = 0; $$6 < 6; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 2.0f / 8.0f + 1.2566371f;
                    this.createSpellEntity(warlock.getX() + (double) Mth.cos($$7) * 2.5, warlock.getZ() + (double) Mth.sin($$7) * 2.5, $$1, $$2, $$7, 3);
                }
                for (int $$6 = 0; $$6 < 9; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 3.0f / 8.0f + 1.2566371f;
                    this.createSpellEntity(warlock.getX() + (double) Mth.cos($$7) * 3.0, warlock.getZ() + (double) Mth.sin($$7) * 3.0, $$1, $$2, $$7, 3);
                }
                for (int $$6 = 0; $$6 < 12; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 4.0f / 8.0f + 1.2566371f;
                    this.createSpellEntity(warlock.getX() + (double) Mth.cos($$7) * 3.5, warlock.getZ() + (double) Mth.sin($$7) * 3.5, $$1, $$2, $$7, 3);
                }
            } else {
                float radius = 0.25f;
                for (int $$8 = 0; $$8 < 30; ++$$8) {
                    double $$9 = 1.25 * (double) ($$8 + 1);
                    float left = $$3 + radius;
                    float right = $$3 - radius;
                    int $$10 = 3 * $$8;
                    this.createSpellEntity(warlock.getX() + (double) Mth.cos($$3) * $$9, warlock.getZ() + (double) Mth.sin($$3) * $$9, $$1, $$2, $$3, $$10);
                    this.createSpellEntity(warlock.getX() + (double) Mth.cos(left) * $$9, warlock.getZ() + (double) Mth.sin(left) * $$9, $$1, $$2, left, $$10);
                    this.createSpellEntity(warlock.getX() + (double) Mth.cos(right) * $$9, warlock.getZ() + (double) Mth.sin(right) * $$9, $$1, $$2, right, $$10);
                }
            }
        }

        private void createSpellEntity(double $$0, double $$1, double $$2, double $$3, float $$4, int $$5) {
            BlockPos $$6 = BlockPos.containing($$0, $$3, $$1);
            boolean $$7 = false;
            double $$8 = 0.0;
            do {
                VoxelShape $$12;
                BlockPos $$9 = $$6.below();
                BlockState $$10 = warlock.level().getBlockState($$9);
                if (!$$10.isFaceSturdy(warlock.level(), $$9, Direction.UP)) continue;
                if (!warlock.level().isEmptyBlock($$6) && !($$12 = warlock.level().getBlockState($$6).getCollisionShape(warlock.level(), $$6)).isEmpty()) {
                    $$8 = $$12.max(Direction.Axis.Y);
                }
                $$7 = true;
                break;
            } while (($$6 = $$6.below()).getY() >= Mth.floor($$2) - 1);
            if ($$7 && warlock.getHealth() > 25) {
                warlock.level().addFreshEntity(new WaterTrap(warlock.level(), $$0, (double) $$6.getY() + $$8, $$1, $$4, $$5, warlock));
            }
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 120;
        }

        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.WATER;
        }
    }

    private class DamageSpellGoal
    extends UseSpellGoal {

        @Override
        protected void castSpell() {
            WaterWarlock.this.waterDamage();
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 140;
        }

        @Nullable
        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.WATER;
        }
    }
}
