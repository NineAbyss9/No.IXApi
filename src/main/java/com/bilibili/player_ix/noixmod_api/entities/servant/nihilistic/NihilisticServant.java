
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.github.NineAbyss9.ix_api.api.mobs.ApiMobType;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.control.FlyingVexMoveControl;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

import static com.github.NineAbyss9.ix_api.api.mobs.OwnableMob.DEFAULT_LIFE_TICKS;

public class NihilisticServant
extends OwnableNihilist
implements Nihilistic {
    private int lifeTicks = DEFAULT_LIFE_TICKS;
    private static final EntityDataAccessor<Boolean> IS_DANGEROUS = SynchedEntityData
            .defineId(NihilisticServant.class, EntityDataSerializers.BOOLEAN);
    public NihilisticServant(EntityType<? extends NihilisticServant> $$0, Level $$1) {
        super($$0, $$1);
        this.xpReward = this.isHostile() ? 5 : 0;
        this.moveControl = new FlyingVexMoveControl(this);
        this.setDangerous(java.util.concurrent.ThreadLocalRandom.current().nextFloat() <= 0.05f);
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(0, new ApiOwnerTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnableMob.OwnerHurtTargetGoal<>(this));
        this.goalSelector.addGoal(4, new NihilisticServantChargeAttackGoal());
        this.goalSelector.addGoal(8, new StollGoal());
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.targetSelector.addGoal(1, new OwnableMob.OwnableTargetGoal<>(this, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        super.registerGoals();
    }

    public MobType getMobType() {
        return ApiMobType.NIHILISTIC_UNDEAD;
    }

    public void tick() {
        super.tick();
        this.noPhysics = true;
        this.setNoGravity(true);
        if (this.getLifeTick() <= 0) {
            if (this.tickCount % 20 == 0) {
                if (!this.hurt(this.damageSources().starve(), 1f)) {
                    this.discard();
                }
            }
        }
        if (this.level().isClientSide && this.level().isRaining()) {
            this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5), this.getRandomY(),
                    this.getRandomZ(0.5), Maths.randomBetween(this.random,
                            -0.15f, 0.15f), 0.1,
                    Maths.randomBetween(this.random, -0.15f, 0.15f));
        }
    }

    public void spawnAnim() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                double d3 = 10.0;
                this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(1.0) - d0 * d3,
                        this.getRandomY() - d1 * d3, this.getRandomZ(1.0) - d2 * d3, d0, d1, d2);
            }
        } else {
            this.level().broadcastEntityEvent(this, (byte)20);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType
            pReason, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        this.populateDefaultEquipmentEnchantments(pLevel.getRandom(), pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, p_21437_, pDataTag);
    }

    public void handleLifeTicks() {
        this.setLifeTick(Maths.toTick(60) + Maths.toTick(this.getRandom().nextInt(5)));
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_217055_, DifficultyInstance p_217056_) {
        if (p_217056_.getDifficulty().getId() > 1) {
            if (p_217055_.nextFloat() <= 0.05F) {
                this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
            }
        }
    }

    protected void populateDefaultEquipmentEnchantments(RandomSource p_217063_, DifficultyInstance p_217064_) {
        if (p_217064_.isHard()) {
            if (p_217063_.nextBoolean()) {
                this.getMainHandItem().enchant(Enchantments.SHARPNESS, p_217063_.nextInt(2) + 1);
            }
        }
    }

    public int getLifeTick() {
        return this.lifeTicks;
    }

    public void setLifeTick(int ticks) {
        this.lifeTicks = ticks;
    }

    public boolean dying() {
        return this.getLifeTick() < 120;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_DANGEROUS, false);
    }

    public boolean isDangerous() {
        return this.entityData.get(IS_DANGEROUS);
    }

    public void setDangerous(boolean b) {
        this.entityData.set(IS_DANGEROUS, b);
    }

    public NihilistArmPose getArmPose() {
        if (this.isAggressive()) {
            return NihilistArmPose.ZOMBIE_ATTACKING;
        } else if (!this.getLord().isEmpty()) {
            return NihilistArmPose.SPELL_CASTING;
        }
        return NihilistArmPose.CROSSED;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.STRAY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NihilisticServant.createMonsterAttributes().add(Attributes.ARMOR, 2)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 7).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.FLYING_SPEED, 1);
    }

    private class StollGoal
    extends Goal {
        public StollGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !NihilisticServant.this.moveControl.hasWanted() && NihilisticServant
                    .this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            super.tick();
            LivingEntity owner = NihilisticServant.this.getOwner();
            BlockPos pos = Objects.requireNonNullElse(owner, NihilisticServant.this).blockPosition();
            for (int i = 0; i < 3; ++i) {
                BlockPos position = pos.offset(java.util.concurrent.ThreadLocalRandom.current().nextInt(15) - 7, java.util.concurrent.ThreadLocalRandom.current().nextInt(11) - 5,
                        java.util.concurrent.ThreadLocalRandom.current().nextInt(15) - 7);
                if (NihilisticServant.this.level().isEmptyBlock(position)) {
                    if (!NihilisticServant.this.moveControl.hasWanted()) {
                        NihilisticServant.this.moveControl.setWantedPosition(position.getX(), position.getY(), position.getZ(), 0.25);
                        if (NihilisticServant.this.getTarget() == null) {
                            NihilisticServant.this.lookControl.setLookAt(position.getX(), position.getY(), position.getZ());
                        }
                    }
                    break;
                }
            }
        }
    }

    private class NihilisticServantChargeAttackGoal extends Goal
    {
        public NihilisticServantChargeAttackGoal()
        {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse()
        {
            LivingEntity $$0 = NihilisticServant.this.getTarget();
            if ($$0 != null && $$0.isAlive() && !NihilisticServant.this.getMoveControl().hasWanted() && NihilisticServant
                    .this.random.nextInt(reducedTickDelay(7)) == 0) {
                return NihilisticServant.this.distanceToSqr($$0) > 4.0;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse()
        {
            return NihilisticServant.this.getMoveControl().hasWanted() && NihilisticServant.this.getTarget() != null && NihilisticServant
                    .this.getTarget().isAlive();
        }

        public void start()
        {
            LivingEntity $$0 = NihilisticServant.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                NihilisticServant.this.moveControl.setWantedPosition($$1.x, $$1.y, $$1.z, 1.0);
            }
            NihilisticServant.this.setAggressive(true);
        }

        public void stop()
        {
            NihilisticServant.this.setAggressive(false);
        }

        public boolean requiresUpdateEveryTick()
        {
            return true;
        }

        public void tick()
        {
            LivingEntity $$0 = NihilisticServant.this.getTarget();
            if ($$0 == null)
                return;
            if (NihilisticServant.this.getBoundingBox().intersects($$0.getBoundingBox())) {
                if (NihilisticServant.this.isDangerous() || NihilisticServant.this.dying()) {
                    NihilisticServant.this.level().explode(NihilisticServant.this, NihilisticServant.this.getX(), NihilisticServant
                            .this.getY(), NihilisticServant.this.getZ(), 3.0F, false, Level.ExplosionInteraction.MOB);
                    ParticleUtil.sendParticles(NihilisticServant.this.serverLevel(), ParticleTypes.LARGE_SMOKE,
                            NihilisticServant.this.position(), 30, 0.0D, 0.0D, 0.0D, 0.15D);
                    for (LivingEntity entity : NihilisticServant.this.level().getEntitiesOfClass(LivingEntity.class,
                            NihilisticServant.this.getBoundingBox().inflate(4), NihilisticServant.this::canAttack)) {
                        entity.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.NIHILISTIC.get(),
                                40, 0), entity);
                    }
                    if (NihilisticServant.this.getOwner() != null) {
                        NihilisticServant.this.getOwner().heal(3.0F);
                        NihilisticServant.this.setLifeTick(0);
                        if (NihilisticServant.this.getOwner() instanceof Apostle apostle && apostle.getCancelHealTick() < Maths.toTick(2)) {
                            apostle.setCancelHealTick(Maths.toTick(2));
                        }
                    }
                    NihilisticServant.this.discard();
                } else {
                    NihilisticServant.this.doHurtTarget($$0);
                    NihilisticServant.this.heal(1F);
                    if (NihilisticServant.this.getOwner() != null) {
                        NihilisticServant.this.getOwner().heal(1f);
                    }
                }
                NihilisticServant.this.setAggressive(false);
            } else {
                double $$1 = NihilisticServant.this.distanceToSqr($$0);
                if ($$1 < 9.0) {
                    Vec3 $$2 = $$0.getEyePosition();
                    NihilisticServant.this.getMoveControl().setWantedPosition($$2.x, $$2.y - 1, $$2.z, 1.0);
                }
            }
        }
    }
}
