
package com.bilibili.player_ix.noixmod_api.entities.villager;

import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.projectile.VillagerFangs;
import com.bilibili.player_ix.noixmod_api.entities.villager.trades.ApiVillagerTrades;
import com.bilibili.player_ix.noixmod_api.magic.villager.VillagerGolemSpell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VillagerEvoker
extends VillagerFighter {
    public VillagerEvoker(EntityType<VillagerEvoker> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new RegenSpellGoal());
        this.goalSelector.addGoal(2, new SummonSpellGoal());
        this.goalSelector.addGoal(3, new AttackSpellGoal());
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 0.5));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1f));
        this.targetSelector.addGoal(3, new VillagerFighterHurtByTargetGoal(this));
    }

    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return new VillagerEvoker(NoixmodAPIEntities.VILLAGER_EVOKER.get(), this.level());
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 90.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.KNOCKBACK_RESISTANCE,
                        0.27).add(Attributes.ATTACK_DAMAGE, 15.0)
                .add(Attributes.FOLLOW_RANGE, 64);
    }

    @Nullable
    protected VillagerTrades.ItemListing[] getTradeLists() {
        return ApiVillagerTrades.EVOKER_TRADES;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private class RegenSpellGoal extends FighterHealSpellGoal {
        public RegenSpellGoal() {
            super(4);
        }

        protected void performSpellCasting() {
            List<VillagerFighter> fighters = VillagerEvoker.this.level().getEntitiesOfClass(VillagerFighter.class,
                    getBoundingBox().inflate(16, 4, 16));
            if (!fighters.isEmpty()) {
                for (VillagerFighter fighter : fighters) {
                    fighter.heal(4f);
                    fighter.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 2));
                }
            }
            super.performSpellCasting();
        }

        protected int getCastingInterval() {
            return 600;
        }
    }

    private class SummonSpellGoal extends UseSpellGoal {
        VillagerEvoker evoker = VillagerEvoker.this;

        SummonSpellGoal() {}

        protected void performSpellCasting() {
            if (level() instanceof ServerLevel level) {
                VillagerGolemSpell spell = new VillagerGolemSpell();
                spell.castSpell(level, evoker);
            }
        }

        public boolean canUse() {
            if (isBaby()) {
                return false;
            }
            return super.canUse();
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 500;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.RANGE;
        }
    }
    
    private class AttackSpellGoal extends UseSpellGoal {
        VillagerEvoker evoker = VillagerEvoker.this;
        
        public AttackSpellGoal() {
        }

        protected void performSpellCasting() {
            LivingEntity $$0 = evoker.getTarget();
            if ($$0 != null) {
                double $$1 = Math.min($$0.getY(), evoker.getY());
                double $$2 = Math.max($$0.getY(), evoker.getY()) + 2.0;
                float $$3 = (float)Mth.atan2($$0.getZ() - evoker.getZ(), $$0.getX() - evoker.getX());
                if (evoker.distanceToSqr($$0) < 9.0) {
                    for (int $$4 = 0; $$4 < 38; ++$$4) {
                        float $$5 = $$3 + $$4 * Maths.CLOSER_PI * 0.4f;
                        this.createSpellEntity(evoker.getX() + Mth.cos($$5) * 1.5, evoker.getZ() + Mth.sin($$5) * 1.5, $$1, $$2, $$5, 0);
                    }
                    for (int $$6 = 0; $$6 < 5; ++$$6) {
                        float $$7 = $$3 + $$6 * Maths.CLOSER_PI * 2.0f / 8.0f + 1.2566371f;
                        this.createSpellEntity(evoker.getX() + Mth.cos($$7) * 2.5, evoker.getZ() + Mth.sin($$7) * 2.5, $$1, $$2, $$7, 1);
                    }
                    for (int $$6 = 0; $$6 < 8; ++$$6) {
                        float $$7 = $$3 + $$6 * Maths.CLOSER_PI * 3.0f / 8.0f + 2.2566371f;
                        this.createSpellEntity(evoker.getX() + Mth.cos($$7) * 3.0, evoker.getZ() + Mth.sin($$7) * 3.0,
                                $$1, $$2, $$7, 2);
                    }
                    for (int $$6 = 0; $$6 < 11; ++$$6) {
                        float $$7 = $$3 + (float) $$6 * (float) Math.PI * 4.0f / 8.0f + 3.2566371f;
                        this.createSpellEntity(evoker.getX() + Mth.cos($$7) * 3.5, evoker.getZ() + Mth.sin($$7) * 3.5,
                                $$1, $$2, $$7, 3);
                    }
                    for (int $$6 = 0; $$6 < 14; ++$$6) {
                        float $$7 = $$3 + $$6 * Maths.CLOSER_PI * 5.0f / 8.0f + 4.2566371f;
                        this.createSpellEntity(evoker.getX() + Mth.cos($$7) * 4.0, evoker.getZ() + Mth.sin($$7)
                                * 4.0, $$1, $$2, $$7, 4);
                    }
                } else {
                    float radius = 0.25f;
                    for (int $$8 = 0; $$8 < 30; ++$$8) {
                        double $$9 = 1.25 * (double) ($$8 + 1);
                        float left = $$3 + radius;
                        float right = $$3 - radius;
                        this.createSpellEntity(evoker.getX() + Mth.cos($$3) * $$9, evoker.getZ() + Mth.sin($$3) * $$9,
                                $$1, $$2, $$3, $$8);
                        this.createSpellEntity(evoker.getX() + Mth.cos(left) * $$9, evoker.getZ() + Mth.sin(left) * $$9,
                                $$1, $$2, left, $$8);
                        this.createSpellEntity(evoker.getX() + Mth.cos(right) * $$9, evoker.getZ() + Mth.sin(right) * $$9,
                                $$1, $$2, right, $$8);
                    }
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
                BlockState $$10 = level().getBlockState($$9);
                if (!$$10.isFaceSturdy(level(), $$9, Direction.UP)) continue;
                if (!level().isEmptyBlock($$6) && !($$12 = level().getBlockState($$6)
                        .getCollisionShape(level(), $$6)).isEmpty()) {
                    $$8 = $$12.max(Direction.Axis.Y);
                }
                $$7 = true;
                break;
            } while (($$6 = $$6.below()).getY() >= Mth.floor($$2) - 1);
            if ($$7) {
                VillagerFangs trap = new VillagerFangs(NoixmodAPIEntities.VILLAGER_FANGS.get(), evoker.level());
                trap.setPos($$0,$$6.getY() + $$8, $$1);
                trap.setYRot($$4 * 57.295776F);
                trap.setWarmupDelayTicks($$5);
                trap.setOwner(VillagerEvoker.this);
                level().addFreshEntity(trap);
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 120;
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            return getTarget() != null && !isBaby();
        }

        protected  SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.POTION;
        }
    }
}
