
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.ApiIllagerBoss;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.monster.MoonKiller;
import com.bilibili.player_ix.noixmod_api.entities.servant.SuicideZombie;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIAttributes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.List;

public class EvokerIllager
extends SpellcasterIllager
implements ApiIllagerBoss, ApiPoseMob {
    private static final EntityDataAccessor<Integer> DATA_SHIELD_TICK;
    private final Predicate<Entity> NO_RAVAGER_AND_ALIVE = entity -> {
        if (entity instanceof Mob mob) {
            return mob.getTarget() == this || this.getTarget() == mob;
        }
        return entity.isAlive();
    };
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.WHITE, ServerBossEvent.BossBarOverlay.PROGRESS);

    public EvokerIllager(EntityType<? extends EvokerIllager> $$0, Level $$1) {
        super($$0, $$1);
        this.setMaxUpStep(1f);
        this.xpReward = 100;
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
    }

    public EvokerIllager(PlayMessages.SpawnEntity packet, Level world) {
        this(NoixmodAPIEntities.EVOKER_ILLAGER.get(), world);
        packet.getEntity();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SHIELD_TICK, 0);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ShieldTick", this.getShieldTick());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("ShieldTick")) {
            this.setShieldTick(tag.getInt("ShieldTick"));
        }
        super.readAdditionalSaveData(tag);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SpellcasterCastingSpellGoal());
        this.goalSelector.addGoal(0, new SummonSpellGoal());
        this.goalSelector.addGoal(1, new DamageSpellGoal());
        this.goalSelector.addGoal(1, new HealSpellGoal());
        this.goalSelector.addGoal(2, new SummonSuicideZombieSpellGoal());
        this.goalSelector.addGoal(2, new FireBallSpellGoal());
        this.goalSelector.addGoal(2, new AttackSpellGoal());
        this.goalSelector.addGoal(2, new AttackSpell1Goal());
        this.goalSelector.addGoal(3, new AttackSpell2Goal());
        this.goalSelector.addGoal(4, new RiderSpellGoal());
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LivingEntity.class, 12.0f));
        this.targetSelector.addGoal(8, new HurtByTargetGoal(this, Raider.class));
        this.targetSelector.addGoal(9, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    @Override
    public ApiPose getPoses() {
        if (this.isCastingSpell()) {
            return ApiPose.SPELL_CASTING;
        }
        if (this.isAggressive()) {
            return ApiPose.ATTACKING;
        }
        return ApiPose.CROSSED;
    }

    public static void init() {
        if (NoixmodAPIMainConfig.EvokerIllagerRaid.get()) {
            Raid.RaiderType.create("evoker_illager", NoixmodAPIEntities.EVOKER_ILLAGER.get(),
                    new int[]{0, 0, 0, 0, 0, 0, 0, 1});
        }
    }

    private class DamageSpellGoal
    extends SpellcasterUseSpellGoal {
        public DamageSpellGoal() {
        }

        private void strongKnockback(LivingEntity living) {
            double d = NoixmodAPIAttributes.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE, living);
            double d0 = living.getX() - getX();
            double d1 = living.getZ() - getZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            living.push(d0 / d2 * 4.0, 1.3 - d, d0 / d2 * 4.0);
        }

        protected void performSpellCasting() {
            assert EvokerIllager.this.getTarget() != null;
            List<LivingEntity> $$0 = EvokerIllager.this.level().getEntitiesOfClass(LivingEntity.class, EvokerIllager.this.getBoundingBox().inflate(8),
                    NO_RAVAGER_AND_ALIVE);
            for (LivingEntity livingEntity : $$0) {
                if (!(livingEntity instanceof AbstractIllager)) {
                    livingEntity.hurt(EvokerIllager.this.damageSources().magic(), 12.0f);
                }
                this.strongKnockback(livingEntity);
            }
            double r = Mth.nextDouble(RandomSource.create(), -20, 20);
            double r1 = Mth.nextDouble(RandomSource.create(), -20, 20);
            ((ServerLevel) EvokerIllager.this.level()).sendParticles(ParticleTypes.POOF, EvokerIllager.this.getX(), EvokerIllager.this.getY() + 1.0, EvokerIllager.this.getZ(), 100, 1.0, 1.0, 1.0, 0.25);
            ((ServerLevel) EvokerIllager.this.level()).sendParticles(ParticleTypes.PORTAL, EvokerIllager.this.getX(), EvokerIllager.this.getY() + 1.0, EvokerIllager.this.getZ(), 60, 1.0, 1.0, 1.0, 0.01);
            EvokerIllager.this.randomTeleport(EvokerIllager.this.getTarget().getX() + r, EvokerIllager.this.getTarget().getY() + 1, EvokerIllager.this.getTarget().getZ() + r1, false);
            ((ServerLevel)EvokerIllager.this.level()).sendParticles(ParticleTypes.EFFECT, EvokerIllager.this.getX(), EvokerIllager.this.getY() + 1.0, EvokerIllager.this.getZ(), 400, 1.0, 1.0, 1.0, 0.25);
            setShieldTick(100);
            EvokerIllager.this.removeAllEffects();
        }

        protected int getCastingTime() {
            return 50;
        }

        protected int getCastingInterval() {
            return 300;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected IllagerSpell getSpell() {
            return IllagerSpell.BLINDNESS;
        }

        @Override
        public boolean canUse() {
            if (EvokerIllager.this.getTarget() == null) {
                return false;
            }
            if (!EvokerIllager.this.closerThan(EvokerIllager.this.getTarget(), 6)) {
                return false;
            }
            if (EvokerIllager.this.level().isClientSide()) {
                return false;
            }
            return super.canUse();
        }
    }

    protected class AttackSpell2Goal
    extends SpellcasterUseSpellGoal {
        AttackSpell2Goal() {
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            return EvokerIllager.this.getHealth() > 150;
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity $$0 = EvokerIllager.this.getTarget();
            assert $$0 != null;
            double $$1 = Math.min($$0.getY(), EvokerIllager.this.getY());
            double $$2 = Math.max($$0.getY(), EvokerIllager.this.getY()) + 1.0;
            float $$3 = (float) Mth.atan2($$0.getZ() - EvokerIllager.this.getZ(), $$0.getX() - EvokerIllager.this.getX());
            if (EvokerIllager.this.distanceToSqr($$0) < 9.0) {
                for (int $$4 = 0; $$4 < 5; ++$$4) {
                    float $$5 = $$3 + (float) $$4 * (float) Math.PI * 0.4f;
                    this.createSpellEntity(EvokerIllager.this.getX() + (double) Mth.cos($$5) * 1.5, EvokerIllager.this.getZ() + (double) Mth.sin($$5) * 1.5, $$1, $$2, $$5, 0);
                }
                for (int $$6 = 0; $$6 < 8; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 2.0f / 8.0f + 1.2566371f;
                    this.createSpellEntity(EvokerIllager.this.getX() + (double) Mth.cos($$7) * 2.5, EvokerIllager.this.getZ() + (double) Mth.sin($$7) * 2.5, $$1, $$2, $$7, 3);
                }
            } else {
                for (int $$8 = 0; $$8 < 16; ++$$8) {
                    double $$9 = 1.25 * (double) ($$8 + 1);
                    this.createSpellEntity(EvokerIllager.this.getX() + (double) Mth.cos($$3) * $$9, EvokerIllager.this.getZ() + (double) Mth.sin($$3) * $$9, $$1, $$2, $$3, $$8);
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
                BlockState $$10 = EvokerIllager.this.level().getBlockState($$9);
                if (!$$10.isFaceSturdy(EvokerIllager.this.level(), $$9, Direction.UP)) continue;
                if (!EvokerIllager.this.level().isEmptyBlock($$6) && !($$12 = (EvokerIllager.this.level().getBlockState($$6)).getCollisionShape(EvokerIllager.this.level(), $$6)).isEmpty()) {
                    $$8 = $$12.max(Direction.Axis.Y);
                }
                $$7 = true;
                break;
            } while (($$6 = $$6.below()).getY() >= Mth.floor($$2) - 1);
            if ($$7) {
                EvokerIllager.this.level().addFreshEntity(new EvokerFangs(EvokerIllager.this.level(), $$0, (double) $$6.getY() + $$8, $$1, $$4, $$5, EvokerIllager.this));
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.FANGS;
        }
    }

    protected class AttackSpell1Goal
    extends SpellcasterUseSpellGoal {
        AttackSpell1Goal() {
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            return EvokerIllager.this.getHealth() != 0;
        }

        @Override
        protected void performSpellCasting() {
            if (EvokerIllager.this.getHealth() != 0) {
                EvokerIllager.this.setHealth(EvokerIllager.this.getHealth() + 10);
            }
            LivingEntity $$0 = EvokerIllager.this.getTarget();
            assert $$0 != null;
            double $$1 = Math.min($$0.getY(), EvokerIllager.this.getY());
            double $$2 = Math.max($$0.getY(), EvokerIllager.this.getY()) + 2.0;
            float $$3 = (float) Mth.atan2($$0.getZ() - EvokerIllager.this.getZ(), $$0.getX() - EvokerIllager.this.getX());
            if (EvokerIllager.this.distanceToSqr($$0) > 9.0) {
                for (int $$4 = 0; $$4 < 39; ++$$4) {
                    float $$5 = $$3 + (float) $$4 * (float) Math.PI * 0.4f;
                    this.createSpellEntity(EvokerIllager.this.getTarget().getX() + (double) Mth.cos($$5) * 1.5, EvokerIllager.this.getTarget().getZ() + (double) Mth.sin($$5) * 1.5, $$1, $$2, $$5, 0);
                }
                for (int $$6 = 0; $$6 < 5; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 2.0f / 8.0f + 1.2566371f;
                    this.createSpellEntity(EvokerIllager.this.getTarget().getX() + (double) Mth.cos($$7) * 2.5, EvokerIllager.this.getTarget().getZ() + (double) Mth.sin($$7) * 2.5, $$1, $$2, $$7, 3);
                }
                for (int $$6 = 0; $$6 < 8; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 3.0f / 8.0f + 2.2566371f;
                    this.createSpellEntity(EvokerIllager.this.getTarget().getX() + (double) Mth.cos($$7) * 3.0, EvokerIllager.this.getTarget().getZ() + (double) Mth.sin($$7) * 3.0, $$1, $$2, $$7, 3);
                }
                for (int $$6 = 0; $$6 < 11; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 4.0f / 8.0f + 3.2566371f;
                    this.createSpellEntity(EvokerIllager.this.getTarget().getX() + (double) Mth.cos($$7) * 3.5, EvokerIllager.this.getTarget().getZ() + (double) Mth.sin($$7) * 3.5, $$1, $$2, $$7, 3);
                }
                for (int $$6 = 0; $$6 < 14; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 5.0f / 8.0f + 4.2566371f;
                    this.createSpellEntity(EvokerIllager.this.getTarget().getX() + (double) Mth.cos($$7) * 4.0, EvokerIllager.this.getTarget().getZ() + (double) Mth.sin($$7) * 4.0, $$1, $$2, $$7, 3);
                }
                for (int $$6 = 0; $$6 < 1; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 0.0f / 8.0f + 1.0f;
                    this.createSpellEntity(EvokerIllager.this.getTarget().getX(), EvokerIllager.this.getTarget().getZ(), $$1, $$2, $$7, 3);
                }
            } else {
                float radius = 0.25f;
                for (int $$8 = 0; $$8 < 30; ++$$8) {
                    double $$9 = 1.25 * (double) ($$8 + 1);
                    float left = $$3 + radius;
                    float right = $$3 - radius;
                    this.createSpellEntity(EvokerIllager.this.getX() + (double) Mth.cos($$3) * $$9, EvokerIllager.this.getZ() + (double) Mth.sin($$3) * $$9, $$1, $$2, $$3, $$8);
                    this.createSpellEntity(EvokerIllager.this.getX() + (double) Mth.cos(left) * $$9, EvokerIllager.this.getZ() + (double) Mth.sin(left) * $$9, $$1, $$2, left, $$8);
                    this.createSpellEntity(EvokerIllager.this.getX() + (double) Mth.cos(right) * $$9, EvokerIllager.this.getZ() + (double) Mth.sin(right) * $$9, $$1, $$2, right, $$8);
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
                BlockState $$10 = EvokerIllager.this.level().getBlockState($$9);
                if (!$$10.isFaceSturdy(EvokerIllager.this.level(), $$9, Direction.UP)) continue;
                if (!EvokerIllager.this.level().isEmptyBlock($$6) && !($$12 = EvokerIllager.this.level().getBlockState($$6).getCollisionShape(EvokerIllager.this.level(), $$6)).isEmpty()) {
                    $$8 = $$12.max(Direction.Axis.Y);
                }
                $$7 = true;
                break;
            } while (($$6 = $$6.below()).getY() >= Mth.floor($$2) - 1);
            if ($$7) {
                EvokerIllager.this.level().addFreshEntity(new EvokerFangs(EvokerIllager.this.level(), $$0, (double) $$6.getY() + $$8, $$1, $$4, $$5, EvokerIllager.this));
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.FANGS;
        }
    }

    protected class AttackSpellGoal
    extends SpellcasterUseSpellGoal {
        AttackSpellGoal() {
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            return EvokerIllager.this.getHealth() < 150;
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity $$0 = EvokerIllager.this.getTarget();
            assert $$0 != null;
            double $$1 = Math.min($$0.getY(), EvokerIllager.this.getY());
            double $$2 = Math.max($$0.getY(), EvokerIllager.this.getY()) + 2.0;
            float $$3 = (float) Mth.atan2($$0.getZ() - EvokerIllager.this.getZ(), $$0.getX() - EvokerIllager.this.getX());
            $$0.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
            if (EvokerIllager.this.distanceToSqr($$0) < 9.0) {
                for (int $$4 = 0; $$4 < 38; ++$$4) {
                    float $$5 = $$3 + (float) $$4 * (float) Math.PI * 0.4f;
                    WorldUtil.createSpellEntities(EvokerIllager.this.getX() + (double) Mth.cos($$5) * 1.5, EvokerIllager.this.getZ() + (double) Mth.sin($$5) * 1.5, $$1, $$2, $$5, 0, EvokerIllager.this, EvokerIllager.this.level());
                }
                for (int $$6 = 0; $$6 < 5; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 2.0f / 8.0f + 1.2566371f;
                    WorldUtil.createSpellEntities(EvokerIllager.this.getX() + (double) Mth.cos($$7) * 2.5, EvokerIllager.this.getZ() + (double) Mth.sin($$7) * 2.5, $$1, $$2, $$7, 3, EvokerIllager.this, EvokerIllager.this.level());
                }
                for (int $$6 = 0; $$6 < 8; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 3.0f / 8.0f + 2.2566371f;
                    WorldUtil.createSpellEntities(EvokerIllager.this.getX() + (double) Mth.cos($$7) * 3.0, EvokerIllager.this.getZ() + (double) Mth.sin($$7) * 3.0, $$1, $$2, $$7, 3, EvokerIllager.this, EvokerIllager.this.level());
                }
                for (int $$6 = 0; $$6 < 11; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 4.0f / 8.0f + 3.2566371f;
                    WorldUtil.createSpellEntities(EvokerIllager.this.getX() + (double) Mth.cos($$7) * 3.5, EvokerIllager.this.getZ() + (double) Mth.sin($$7) * 3.5, $$1, $$2, $$7, 3, EvokerIllager.this, EvokerIllager.this.level());
                }
                for (int $$6 = 0; $$6 < 14; ++$$6) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 5.0f / 8.0f + 4.2566371f;
                    WorldUtil.createSpellEntities(EvokerIllager.this.getX() + (double) Mth.cos($$7) * 4.0, EvokerIllager.this.getZ() + (double) Mth.sin($$7) * 4.0, $$1, $$2, $$7, 3, EvokerIllager.this, EvokerIllager.this.level());
                }
            } else {
                float radius = 0.25f;
                for (int $$8 = 0; $$8 < 30; ++$$8) {
                    double $$9 = 1.25 * (double) ($$8 + 1);
                    float left = $$3 + radius;
                    float right = $$3 - radius;
                    WorldUtil.createSpellEntities(EvokerIllager.this.getX() + (double) Mth.cos($$3) * $$9, EvokerIllager.this.getZ() + (double) Mth.sin($$3) * $$9, $$1, $$2, $$3, $$8, EvokerIllager.this, EvokerIllager.this.level());
                    WorldUtil.createSpellEntities(EvokerIllager.this.getX() + (double) Mth.cos(left) * $$9, EvokerIllager.this.getZ() + (double) Mth.sin(left) * $$9, $$1, $$2, left, $$8, EvokerIllager.this, EvokerIllager.this.level());
                    WorldUtil.createSpellEntities(EvokerIllager.this.getX() + (double) Mth.cos(right) * $$9, EvokerIllager.this.getZ() + (double) Mth.sin(right) * $$9, $$1, $$2, right, $$8, EvokerIllager.this, EvokerIllager.this.level());
                }
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.FANGS;
        }
    }

    private class SummonSpellGoal
    extends SpellcasterUseSpellGoal {
        private final TargetingConditions vexCountTargeting;

        SummonSpellGoal() {
            this.vexCountTargeting = TargetingConditions.forNonCombat().range(16.0).ignoreLineOfSight().ignoreInvisibilityTesting();
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            int $$0 = EvokerIllager.this.level().getNearbyEntities(Vex.class, this.vexCountTargeting, EvokerIllager.this, EvokerIllager.this.getBoundingBox().inflate(16.0)).size();
            return EvokerIllager.this.random.nextInt(8) + 1 > $$0;
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
        protected void performSpellCasting() {
            ServerLevel $$0 = (ServerLevel) EvokerIllager.this.level();
            for (int $$1 = 0; $$1 < 5; ++$$1) {
                BlockPos $$2 = EvokerIllager.this.blockPosition().offset(-2 + EvokerIllager.this.random.nextInt(5), 1, -2 + EvokerIllager.this.random.nextInt(5));
                Vex $$3 = EntityType.VEX.create(EvokerIllager.this.level());
                if ($$3 == null) continue;
                $$3.moveTo($$2, 0.0f, 0.0f);
                $$3.finalizeSpawn($$0, EvokerIllager.this.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED, null, null);
                $$3.setOwner(EvokerIllager.this);
                $$3.setBoundOrigin($$2);
                $$3.setLimitedLife(25 * (30 + EvokerIllager.this.random.nextInt(90)));
                $$0.addFreshEntityWithPassengers($$3);
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.SUMMON_VEX;
        }
    }

    private class HealSpellGoal extends SpellcasterUseSpellGoal {
        HealSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            return EvokerIllager.this.getHealth() != EvokerIllager.this.getMaxHealth();
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 600;
        }

        @Override
        protected void performSpellCasting() {
            if (EvokerIllager.this.getHealth() != 0) {
                EvokerIllager.this.setHealth(EvokerIllager.this.getHealth() + 10);
            }
        }

        @Override
        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return null;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.WOLOLO;
        }
    }

    private class RiderSpellGoal
    extends SpellcasterUseSpellGoal {
        private final TargetingConditions vexCountTargeting;

        RiderSpellGoal() {
            this.vexCountTargeting = TargetingConditions.forNonCombat().range(16.0).ignoreLineOfSight().ignoreInvisibilityTesting();
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            int $$0 = EvokerIllager.this.level().getNearbyEntities(AbstractIllager.class, this.vexCountTargeting, EvokerIllager.this, EvokerIllager.this.getBoundingBox().inflate(16.0)).size();
            return EvokerIllager.this.random.nextInt(8) + 1 > $$0;
        }

        @Override
        protected int getCastingTime() {
            return 80;
        }

        @Override
        protected int getCastingInterval() {
            return 480;
        }

        @Override
        protected void performSpellCasting() {
            if (!level().isClientSide) {
                for (int $$1 = 0; $$1 < 4; ++$$1) {
                    ServerLevel $$0 = (ServerLevel) EvokerIllager.this.level();
                    int i = EvokerIllager.this.random.nextInt(3);
                    switch (i) {
                        case 0 : {
                            BlockPos $$2 = EvokerIllager.this.blockPosition().offset(-2 + EvokerIllager.this.random.nextInt(5)
                                    , 1, -2 + EvokerIllager.this.random.nextInt(5));
                            Evoker $$3 = EntityType.EVOKER.create(EvokerIllager.this.level());
                            if ($$3 == null) continue;
                            $$3.moveTo($$2, 0.0f, 0.0f);
                            $$3.finalizeSpawn($$0, EvokerIllager.this.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED, null, null);
                            $$3.setTarget(EvokerIllager.this.getTarget());
                            $$0.addFreshEntityWithPassengers($$3);
                            break;
                        }
                        case 1 : {
                            BlockPos $$2 = EvokerIllager.this.blockPosition().offset(-2 + EvokerIllager.this.random.nextInt(5), 1, -2 + EvokerIllager.this.random.nextInt(5));
                            Vindicator $$3 = EntityType.VINDICATOR.create(EvokerIllager.this.level());
                            if ($$3 == null) continue;
                            $$3.moveTo($$2, 0.0f, 0.0f);
                            $$3.finalizeSpawn($$0, EvokerIllager.this.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED, null, null);
                            $$3.setTarget(EvokerIllager.this.getTarget());
                            $$0.addFreshEntityWithPassengers($$3);
                            break;
                        }
                        case 2 : {
                            BlockPos $$2 = EvokerIllager.this.blockPosition().offset(-2 + EvokerIllager.this.random.nextInt(5), 1, -2 + EvokerIllager.this.random.nextInt(5));
                            Pillager $$3 = EntityType.PILLAGER.create(EvokerIllager.this.level());
                            if ($$3 == null) continue;
                            $$3.moveTo($$2, 0.0f, 0.0f);
                            $$3.finalizeSpawn($$0, EvokerIllager.this.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED, null, null);
                            $$3.setTarget(EvokerIllager.this.getTarget());
                            $$0.addFreshEntityWithPassengers($$3);
                            break;
                        }
                        default: {
                            EvokerIllager.this.heal(1f);
                            break;
                        }
                    }
                    if (EvokerIllager.this.getHealth() != 0) {
                        EvokerIllager.this.setHealth(EvokerIllager.this.getHealth() + 5);
                    }
                }
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.SUMMON_VEX;
        }
    }

    private class FireBallSpellGoal
    extends SpellcasterUseSpellGoal {

        @Override
        protected void performSpellCasting() {
            LivingEntity $$0 = EvokerIllager.this.getTarget();
            assert $$0 != null;
            Level $$2 = EvokerIllager.this.level();
            Vec3 $$4 = EvokerIllager.this.getViewVector(1.0f);
            double $$5 = $$0.getX() - (EvokerIllager.this.getX() + $$4.x * 4.0);
            double $$6 = $$0.getY(0.5) - (0.5 + EvokerIllager.this.getY(0.5));
            double $$7 = $$0.getZ() - (EvokerIllager.this.getZ() + $$4.z * 4.0);
            LargeFireball $$8 = new LargeFireball($$2, EvokerIllager.this, $$5, $$6, $$7, 4);
            $$8.setPos(EvokerIllager.this.getX() + $$4.x * 2.0, EvokerIllager.this.getY(0.5) + 0.5, $$8.getZ() + $$4.z * 2.0);
            $$2.addFreshEntity($$8);
            EvokerIllager.this.playSound(SoundEvents.GHAST_SHOOT);
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return NoixmodAPISounds.EVOKER_ILLAGER_SHOOT_FIREBALL.get();
        }

        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.WOLOLO;
        }
    }

    private class SummonSuicideZombieSpellGoal
    extends SpellcasterUseSpellGoal {

        @Override
        protected void performSpellCasting() {
            if (!EvokerIllager.this.level().isClientSide()) {
                ServerLevel level = WorldUtil.getServerLevel(EvokerIllager.this);
                SuicideZombie zombie = new SuicideZombie(NoixmodAPIEntities.SUICIDE_ZOMBIE.get(), level);
                zombie.setOwner(EvokerIllager.this);
                BlockPos pos = EvokerIllager.this.blockPosition().offset(Maths.randomInt(3), 0, Maths.randomInt(3));
                zombie.moveTo(pos, 0, 0);
                level.addFreshEntity(zombie);
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return Maths.toTick(25);
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected IllagerSpell getSpell() {
            return IllagerSpell.SUMMON_VEX;
        }
    }

    protected float getDamageAfterArmorAbsorb(DamageSource $$0, float $$1) {
        $$1 = super.getDamageAfterArmorAbsorb($$0, $$1);
        if ($$0.getEntity() == this) {
            $$1 = 0.0f;
        }
        if (this.hasShield()) {
            $$1 *= 0.5f;
        }
        if ($$0.is(DamageTypes.MAGIC)) {
            $$1 *= 0.1f;
        }
        if ($$0.is(DamageTypes.INDIRECT_MAGIC)) {
            $$1 *= 0.1f;
        }
        if ($$0.is(DamageTypes.FALL)) {
            $$1 *= 0.0f;
        }
        if ($$1 > 20 && !$$0.is(DamageTypes.GENERIC_KILL)) {
            $$1 = 20.0f;
        }
        return $$1;
    }

    public int getAmbientSoundInterval() {
        return 200;
    }

    public boolean hurt(DamageSource p_37849_, float p_37850_) {
        Entity entity = p_37849_.getEntity();
        Entity in = p_37849_.getDirectEntity();
        if (entity == this || in == this) {
            return false;
        }
        if (entity instanceof MoonKiller || in instanceof MoonKiller) {
            return false;
        }
        Entity pEntity = in == null ? entity : in;
        if (pEntity instanceof Ownable ownable) {
            if (ownable.getOwner() == this) {
                return false;
            }
        }
        if (pEntity instanceof Vex vex && vex.getOwner() == this) {
            return false;
        }
        List<AbstractIllager> illagers = this.level().getEntitiesOfClass(AbstractIllager.class, this.getBoundingBox().inflate(64));
        illagers.forEach(this::target);
        return super.hurt(p_37849_, p_37850_);
    }

    private void target(AbstractIllager illager) {
        if (illager != this && illager.getTarget() == null) {
            illager.setTarget(this.getTarget());
        }
    }

    @Override
    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.isAggressive()) {
            return AbstractIllager.IllagerArmPose.ATTACKING;
        }
        if (this.isCastingSpell()) {
            return AbstractIllager.IllagerArmPose.SPELLCASTING;
        }
        return AbstractIllager.IllagerArmPose.CROSSED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.35D;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return NoixmodAPISounds.EVOKER_ILLAGER_IDLE.get();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return NoixmodAPISounds.EVOKER_ILLAGER_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return NoixmodAPISounds.EVOKER_ILLAGER_DEATH.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public void applyRaidBuffs(int num, boolean logic) {
    }

    @Override
    public boolean isAlliedTo(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity == this) {
            return true;
        }
        return super.isAlliedTo(entity);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            if (this.hasShield()) {
                this.level().addParticle(ParticleTypes.EFFECT, this.getRandomX(1), this.getY(),
                        this.getRandomZ(1), 0, 0, 0);
            }
        } else {
            if (this.tickCount % 20 == 0) {
                this.heal(1f);
            }
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void tick() {
        super.tick();
        if (this.getShieldTick() > 0) {
            setShieldTick(getShieldTick() - 1);
        }
    }

    public int getShieldTick() {
        return this.entityData.get(DATA_SHIELD_TICK);
    }

    public void setShieldTick(int tick) {
        this.entityData.set(DATA_SHIELD_TICK, tick);
    }

    private boolean hasShield() {
        return getShieldTick() > 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EvokerIllager.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).
                add(Attributes.MAX_HEALTH, 250).add(Attributes.ARMOR, 12)
                .add(Attributes.ARMOR_TOUGHNESS, 4)
                .add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.FOLLOW_RANGE, 72)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75);
    }

    static {
        DATA_SHIELD_TICK = SynchedEntityData.defineId(EvokerIllager.class, EntityDataSerializers.INT);
    }
}
