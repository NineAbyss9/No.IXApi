
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIDamageSource;
import com.github.NineAbyss9.ix_api.api.mobs.ApiMobType;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.api.mobs.NihilityMobs;
import com.github.NineAbyss9.ix_api.api.mobs.NihilitySummonedMobs;
import com.github.NineAbyss9.ix_api.util.Colors;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.client.particle.CircleParticleOption;
import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticWitherSkull;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.bilibili.player_ix.noixmod_api.magic.nether.LavaTrapSpell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NihilisticWither
extends NihilitySummonedMobs
implements PowerableMob, RangedAttackMob {
    public static final Float DATA_DAMAGE_CAPE = 12f;
    private static final EntityDataAccessor<Integer> DATA_TARGET_A;
    private static final EntityDataAccessor<Integer> DATA_TARGET_B;
    private static final EntityDataAccessor<Integer> DATA_TARGET_C;
    private static final List<EntityDataAccessor<Integer>> DATA_TARGETS;
    private static final EntityDataAccessor<Integer> DATA_ID_INV;
    private static final EntityDataAccessor<Integer> DATA_BACK_DAMAGE_TIME;
    private static final EntityDataAccessor<Integer> DATA_SHOOT_COOLDOWN;
    private final float[] xRotHeads = new float[2];
    private final float[] yRotHeads = new float[2];
    @SuppressWarnings("ALL")
    private final float[] xRotOHeads = new float[2];
    @SuppressWarnings("ALL")
    private final float[] yRotOHeads = new float[2];
    private final int[] nextHeadUpdate = new int[2];
    private final int[] idleHeadUpdates = new int[2];
    private static final ParticleOptions PURPLE_CIRCLE = new CircleParticleOption(Colors.DARK_PURPLE,
            9f, 0);
    private int hurtCooldown;
    private int destroyBlocksTick;
    private int summonCooldown;
    private float damageAmount = 0.0F;
    protected final ServerBossEvent bossEvent;
    private final TargetingConditions TARGETING_CONDITIONS;
    public NihilisticWither(EntityType<? extends NihilisticWither> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName().copy()
                .withStyle(ChatFormatting.DARK_PURPLE)
                , BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS).setCreateWorldFog(true);
        TARGETING_CONDITIONS = TargetingConditions.forCombat().range(20).selector(living -> {
            if (living instanceof Nihilistic) {
                return false;
            }
            return MobUtils.canHurt(living, this) && !ApiMobType.isNihilistic(living.getMobType());
        });
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TARGET_A, 0);
        this.entityData.define(DATA_TARGET_B, 0);
        this.entityData.define(DATA_TARGET_C, 0);
        this.entityData.define(DATA_ID_INV, 0);
        this.entityData.define(DATA_BACK_DAMAGE_TIME, 0);
        this.entityData.define(DATA_SHOOT_COOLDOWN, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new WitherDoNothingGoal());
        this.goalSelector.addGoal(2, new WitherRangedAttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1));
        this.goalSelector.addGoal(6, new FollowOwnerGoal<>(
                this, 0.8D, 20.0F, 8.0F, true));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class,
                12F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    protected void addTargetGoals() {
        super.addTargetGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, NihilityMobs.class) {
            protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
                if (pPotentialTarget == null) {
                    return false;
                }
                return NihilisticWither.this.canAttack(pPotentialTarget) && super.canAttack(pPotentialTarget, pTargetPredicate);
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this,
                LivingEntity.class, false, this::canAttack));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal<>(this));
    }

    public boolean isBoss() {
        return false;
    }

    public void addAdditionalSaveData(CompoundTag p_31485_) {
        super.addAdditionalSaveData(p_31485_);
        p_31485_.putInt("InvulnerableTicks", this.getInvulnerableTicks());
        p_31485_.putInt("BackDamageTime", this.getBackDamageTime());
    }

    public void readAdditionalSaveData(CompoundTag p_31474_) {
        super.readAdditionalSaveData(p_31474_);
        this.setInvulnerableTicks(p_31474_.getInt("InvulnerableTicks"));
        this.setBackDamageTime(p_31474_.getInt("BackDamageTime"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@Nullable Component pName) {
        super.setCustomName(pName);
        if (pName != null) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public void aiStep() {
        Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.7, 1.0);
        if (!this.level().isClientSide && this.getAlternativeTarget(0) > 0) {
            Entity entity = this.level().getEntity(this.getAlternativeTarget(0));
            if (entity != null) {
                double by = entity.getBoundingBox().getYsize();
                double d1 = entity.getY() + by;
                double d0 = vec3.y;
                if (this.getY() < entity.getY() + 1 || !this.isPowered() && this.getY() < d1 + 5.5) {
                    d0 = Math.max(0.0, d0);
                    d0 += 0.3D - d0 * 0.6000000238418579D;
                }
                vec3 = new Vec3(vec3.x, d0, vec3.z);
                Vec3 vec31 = new Vec3(entity.getX() - this.getX(), 0.0,
                        entity.getZ() - this.getZ());
                if (vec31.horizontalDistanceSqr() > 9.0) {
                    Vec3 vec32 = vec31.normalize();
                    vec3 = vec3.add(vec32.x * 0.3 - vec3.x * 0.6, 0.0, vec32.z * 0.3 - vec3.z * 0.6);
                }
            }
        }
        this.setDeltaMovement(vec3);
        if (vec3.horizontalDistanceSqr() > 0.05) {
            this.setYRot((float)Mth.atan2(vec3.z, vec3.x) * 57.295776F - 90.0F);
        }
        super.aiStep();
        if (this.hurtCooldown > 0) {
            --this.hurtCooldown;
        }
        int j;
        for (j = 0; j < 2; ++j) {
            this.yRotOHeads[j] = this.yRotHeads[j];
            this.xRotOHeads[j] = this.xRotHeads[j];
        }
        int i1;
        for (j = 0; j < 2; ++j) {
            i1 = this.getAlternativeTarget(j + 1);
            Entity entity1 = null;
            if (i1 > 0) {
                entity1 = this.level().getEntity(i1);
            }
            if (entity1 != null) {
                double d9 = this.getHeadX(j + 1);
                double d1 = this.getHeadY(j + 1);
                double d3 = this.getHeadZ(j + 1);
                double d4 = entity1.getX() - d9;
                double d5 = entity1.getEyeY() - d1;
                double d6 = entity1.getZ() - d3;
                double d7 = Math.sqrt(d4 * d4 + d6 * d6);
                float f = (float)(Mth.atan2(d6, d4) * 57.2957763671875) - 90.0F;
                float f1 = (float)(-(Mth.atan2(d5, d7) * 57.2957763671875));
                this.xRotHeads[j] = this.rotlerp(this.xRotHeads[j], f1, 40.0F);
                this.yRotHeads[j] = this.rotlerp(this.yRotHeads[j], f, 10.0F);
            } else {
                this.yRotHeads[j] = this.rotlerp(this.yRotHeads[j], this.yBodyRot, 10.0F);
            }
        }
        for (i1 = 0; i1 < 3; ++i1) {
            double d8 = this.getHeadX(i1);
            double d10 = this.getHeadY(i1);
            double d2 = this.getHeadZ(i1);
            this.level().addParticle(ParticleTypes.SMOKE, d8 + ThreadLocalRandom.current().nextGaussian() * 0.3,
                    d10 + ThreadLocalRandom.current().nextGaussian() * 0.3, d2 + ThreadLocalRandom.current()
                            .nextGaussian() * 0.3, 0.0, 0.0, 0.0);
        }
        if (this.isHalfHealth()) {
            if (this.level().isClientSide) {
                for (i1 = 0; i1 < 3; ++i1) {
                    double d8 = this.getHeadX(i1);
                    double d10 = this.getHeadY(i1);
                    double d2 = this.getHeadZ(i1);
                    this.level().addParticle(ParticleTypes.SMOKE, d8 + ThreadLocalRandom.current().nextGaussian() * 0.3D,
                            d10 + ThreadLocalRandom.current().nextGaussian() * 0.3D, d2 + ThreadLocalRandom.current()
                                    .nextGaussian() * 0.3D, 0.0D, 0.0D, 0.0D);
                    if (ThreadLocalRandom.current().nextInt(4) == 0) {
                        this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get(),
                                d8 + ThreadLocalRandom.current().nextGaussian() * 0.3D, d10 +
                                        ThreadLocalRandom.current().nextGaussian() * 0.3D, d2 + ThreadLocalRandom.current()
                                        .nextGaussian() * 0.3D, 0, 0, 0);
                    }
                }
            } else {
                if (this.tickCount % 80 == 0) {
                    ServerLevel level = (ServerLevel)this.level();
                    LavaTrapSpell spell = new LavaTrapSpell(2);
                    spell.castSpell(level, this);
                    ISpell trap = Spells.SMOKE_TRAP.get();
                    trap.castSpell(level, this);
                }
            }
        }
        if (this.level().isClientSide) {
            if (this.getInvulnerableTicks() > 0 || this.isGivingBackDamage()) {
                for (i1 = 0;i1 < 3;++i1) {
                    this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() +
                                    ThreadLocalRandom.current().nextGaussian(), this.getY() +
                                    ThreadLocalRandom.current().nextDouble() * 3.3D, this.getZ() +
                                    ThreadLocalRandom.current().nextGaussian(), 0.7D, 0.7D, 0.9D);
                }
            }
        }
    }

    protected void customServerAiStep() {
        int j1;
        if (this.cannotShoot()) {
            this.decreaseShootCooldown();
        }
        if (this.getBackDamageTime() == 0) {
            LivingEntity target = this.getTarget();
            if (this.damageAmount > 0.0F) {
                if (target != null) {
                    List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,
                            target.getBoundingBox().inflate(16), living -> MobUtils.canHurt(living, this));
                    if (!list.isEmpty()) {
                        for (LivingEntity living : list) {
                            living.hurt(NoixmodAPIDamageSource.nihility(this), this.damageAmount);
                        }
                        if (this.getInvulnerableTicks() <= 0 && !this.isSilent()) {
                            this.level().levelEvent(null, 1022, blockPosition(), 0);
                        }
                    }
                }
                if (!this.level().isClientSide) {
                    ParticleUtil.sendParticles((ServerLevel) this.level(), PURPLE_CIRCLE, this.position(), 1,
                            0, 0, 0, 0);
                }
            } else {
                if (target != null) {
                    this.hurt(NoixmodAPIDamageSource.nihility(this.level()), 20.0F);
                }
            }
            this.resetDamageAmount();
        }
        if (this.getBackDamageTime() <= 0) {
            this.setBackDamageTime(800);
        } else {
            if (!this.isPowered()) {
                this.setBackDamageTime(this.getBackDamageTime() - 1);
            }
        }
        if (this.getInvulnerableTicks() > 0) {
            j1 = this.getInvulnerableTicks() - 1;
            this.bossEvent.setProgress(1.0F - (float)j1 / 220.0F);
            if (j1 <= 0) {
                MobUtils.rangeHurt(4, 4, 4, this, NoixmodAPIDamageSource.nihility(this), 50.0F);
                if (!this.isSilent()) {
                    this.level().globalLevelEvent(1023, this.blockPosition(), 0);
                }
                if (!this.level().isClientSide) {
                    ParticleUtil.sendParticles((ServerLevel)this.level(), PURPLE_CIRCLE, this.position(),
                            1, 0, 0, 0, 0);
                }
            }
            this.setInvulnerableTicks(j1);
            if (this.tickCount % 10 == 0) {
                this.heal(this.getMaxHealth() / 20f);
            }
        } else {
            super.customServerAiStep();
            if (this.summonCooldown <= 0) {
                if (this.isHalfHealth()) {
                    ISpell spell = Spells.WITHER_SKELETON.get();
                    spell.castSpell(this.serverLevel(), this);
                    this.summonCooldown = 1200;
                }
            } else {
                this.summonCooldown--;
            }
            int i2;
            int j2;
            for (j1 = 1; j1 < 3; ++j1) {
                if (this.tickCount >= this.nextHeadUpdate[j1 - 1]) {
                    this.nextHeadUpdate[j1 - 1] = this.tickCount + 10 + ThreadLocalRandom.current().nextInt(10);
                    i2 = j1 - 1;
                    j2 = this.idleHeadUpdates[j1 - 1];
                    this.idleHeadUpdates[i2] = this.idleHeadUpdates[j1 - 1] + 1;
                    if (j2 > 15) {
                        double d0 = Mth.nextDouble(this.random, this.getX() - 10.0, this.getX() + 10.0);
                        double d1 = Mth.nextDouble(this.random, this.getY() - 5.0, this.getY() + 5.0);
                        double d2 = Mth.nextDouble(this.random, this.getZ() - 10.0, this.getZ() + 10.0);
                        this.performRangedAttack(j1 + 1, d0, d1, d2, true);
                        this.idleHeadUpdates[j1 - 1] = 0;
                    }
                    i2 = this.getAlternativeTarget(j1);
                    if (i2 > 0) {
                        LivingEntity livingentity = (LivingEntity)this.level().getEntity(i2);
                        if (livingentity != null && this.canAttack(livingentity) && this.distanceToSqr(livingentity) <= 900.0D
                                && this.hasLineOfSight(livingentity)) {
                            this.performRangedAttack(j1 + 1, livingentity);
                            this.nextHeadUpdate[j1 - 1] = this.tickCount + 40 + ThreadLocalRandom.current().nextInt(20);
                            this.idleHeadUpdates[j1 - 1] = 0;
                        } else {
                            this.setAlternativeTarget(j1, 0);
                        }
                    } else {
                        List<LivingEntity> list = this.level().getNearbyEntities(LivingEntity.class,
                                TARGETING_CONDITIONS, this, this.getBoundingBox()
                                        .inflate(20.0, 20.0, 20.0));
                        if (!list.isEmpty()) {
                            LivingEntity living = list.get(ThreadLocalRandom.current().nextInt(list.size()));
                            this.setAlternativeTarget(j1, living.getId());
                        }
                    }
                }
            }
            if (this.getTarget() != null) {
                this.setAlternativeTarget(0, this.getTarget().getId());
            } else {
                this.setAlternativeTarget(0, 0);
            }
            if (this.destroyBlocksTick > 0) {
                --this.destroyBlocksTick;
                if (this.destroyBlocksTick == 0 && ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                    j1 = Mth.floor(this.getY());
                    i2 = Mth.floor(this.getX());
                    j2 = Mth.floor(this.getZ());
                    boolean flag = false;
                    int j = -1;
                    while (true) {
                        if (j > 1) {
                            if (flag) {
                                this.level().levelEvent(null, 1022, this.blockPosition(), 0);
                            }
                            break;
                        }
                        for (int k2 = -1; k2 <= 1; ++k2) {
                            for (int k = 0; k <= 3; ++k) {
                                int l2 = i2 + j;
                                int l = j1 + k;
                                int i1 = j2 + k2;
                                BlockPos blockpos = new BlockPos(l2, l, i1);
                                BlockState blockstate = this.level().getBlockState(blockpos);
                                if (ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                                    flag = this.level().destroyBlock(blockpos, true, this) || flag;
                                }
                            }
                        }
                        ++j;
                    }
                }
            }
            if (this.tickCount % 20 == 0) {
                this.heal(1.0F);
                if (this.isOwned()) {
                    this.getOwner().heal(1f);
                }
            }
        }
    }

    private boolean isHalfHealth() {
        return MobUtils.isHalfHealth(this);
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        if (p_20122_.is(DamageTypeTags.IS_FALL)) {
            return true;
        }
        if (p_20122_.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        if (p_20122_.is(DamageTypeTags.IS_FREEZING)) {
            return true;
        }
        return super.isInvulnerableTo(p_20122_);
    }

    public void skullHurt() {
        this.setShootCooldown(100);
        this.setBackDamageTime(this.getBackDamageTime() + 100);
        super.hurt(NoixmodAPIDamageSource.nihility(this.level()), 20.0F);
        this.resetDamageAmount();
    }

    boolean checkSkull(Entity entity) {
        if (!(entity instanceof NihilisticWitherSkull skull))
        {
            return false;
        }
        return skull.getOwner() != this;
    }

    public boolean hurt(DamageSource pSource, float pAmount)
    {
        if (checkSkull(pSource.getDirectEntity())) {
            return super.hurt(pSource, pAmount);
        }
        if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(pSource, pAmount);
        }
        if (this.isGivingBackDamage()) {
            this.heal(pAmount);
        }
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (!pSource.is(DamageTypeTags.WITHER_IMMUNE_TO) && !(pSource.getEntity()
                instanceof NihilisticWither)) {
            if (this.getInvulnerableTicks() > 0 && !pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                return false;
            } else {
                Entity entity1;
                if (this.isPowered()) {
                    entity1 = pSource.getDirectEntity();
                    if (entity1 instanceof Projectile || pSource.is(DamageTypeTags.IS_PROJECTILE)) {
                        return false;
                    }
                }
                entity1 = pSource.getEntity();
                if (!(entity1 instanceof Player) && entity1 instanceof LivingEntity && ((LivingEntity) entity1).getMobType()
                        == this.getMobType()) {
                    return false;
                } else {
                    if (this.destroyBlocksTick <= 0) {
                        this.destroyBlocksTick = 20;
                    }
                    for (int i = 0; i < this.idleHeadUpdates.length; ++i) {
                        this.idleHeadUpdates[i] += 3;
                    }
                    return super.hurt(pSource, pAmount);
                }
            }
        } else {
            return false;
        }
    }

    protected void actuallyHurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            super.actuallyHurt(pSource, pAmount);
            return;
        }
        if (checkSkull(pSource.getDirectEntity())) {
            super.actuallyHurt(pSource, pAmount);
            return;
        }
        if (pAmount > DATA_DAMAGE_CAPE) {
            pAmount = DATA_DAMAGE_CAPE;
        }
        if (this.hurtCooldown <= 0) {
            this.hurtCooldown = 10;
            if (this.isGivingBackDamage() && this.getInvulnerableTicks() <= 0) {
                this.damageAmount += pAmount;
                return;
            }
            super.actuallyHurt(pSource, pAmount);
        }
    }

    public void heal(float pHealAmount) {
        if (this.cannotShoot()) {
            return;
        }
        super.heal(pHealAmount);
    }

    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        ParticleUtil.sendParticles(p_216988_, ParticleTypes.SMOKE, p_216989_.position(),
                20, 1, 1, 1, 0);
        return super.killedEntity(p_216988_, p_216989_);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType
            pReason, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag pDataTag) {
        this.handleSpawnEvent();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, p_21437_, pDataTag);
    }

    public void spawnAnim() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 40; ++i) {
                double d0 = ThreadLocalRandom.current().nextGaussian() * 0.02;
                double d1 = ThreadLocalRandom.current().nextGaussian() * 0.02;
                double d2 = ThreadLocalRandom.current().nextGaussian() * 0.02;
                double d3 = 10.0;
                this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(1.0) - d0 * d3,
                        this.getRandomY() - d1 * d3, this.getRandomZ(1.0) - d2 * d3, d0, d1, d2);
            }
        } else {
            this.level().broadcastEntityEvent(this, (byte)20);
        }
    }

    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {}

    public int getInvulnerableTicks() {
        return this.entityData.get(DATA_ID_INV);
    }

    public void setInvulnerableTicks(int p_31511_) {
        this.entityData.set(DATA_ID_INV, p_31511_);
    }

    /**If {@linkplain #getBackDamageTime()} > 600, return {@code true}, else, on Cooldown*/
    public boolean isGivingBackDamage() {
        return this.getBackDamageTime() <= 200;
    }

    public int getBackDamageTime() {
        return this.entityData.get(DATA_BACK_DAMAGE_TIME);
    }

    private void setBackDamageTime(int time) {
        this.entityData.set(DATA_BACK_DAMAGE_TIME, time);
    }

    public void handleSpawnEvent()
    {
        if (!this.isUnowned()) return;
        this.setInvulnerableTicks(220);
        this.bossEvent.setProgress(0.0F);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_31500_) {
        return SoundEvents.WITHER_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    public void makeStuckInBlock(BlockState p_31471_, Vec3 p_31472_) {}

    protected PathNavigation createNavigation(Level p_186262_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_186262_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public double getHeadX(int p_31515_) {
        if (p_31515_ <= 0) {
            return this.getX();
        } else {
            float f = (this.yBodyRot + (float)(180 * (p_31515_ - 1))) * 0.017453292F;
            float f1 = Mth.cos(f);
            return this.getX() + (double)f1 * 1.3;
        }
    }

    public double getHeadY(int p_31517_) {
        return p_31517_ <= 0 ? this.getY() + 3.0D : this.getY() + 2.2D;
    }

    public double getHeadZ(int p_31519_) {
        if (p_31519_ <= 0) {
            return this.getZ();
        } else {
            float f = (this.yBodyRot + (float)(180F * (p_31519_ - 1))) * 0.017453292F;
            return this.getZ() + Math.sin(f) * 1.3D;
        }
    }

    private float rotlerp(float p_31443_, float p_31444_, float p_31445_) {
        float f = Mth.wrapDegrees(p_31444_ - p_31443_);
        if (f > p_31445_) {
            f = p_31445_;
        }
        if (f < -p_31445_) {
            f = -p_31445_;
        }
        return p_31443_ + f;
    }

    public MobType getMobType() {
        return ApiMobType.NIHILISTIC_UNDEAD;
    }

    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    public float getHeadYRot(int p_31447_) {
        return this.yRotHeads[p_31447_];
    }

    public float getHeadXRot(int p_31481_) {
        return this.xRotHeads[p_31481_];
    }

    public boolean isPowered() {
        return this.getHealth() <= this.getMaxHealth() / 3.0D;
    }

    protected boolean canRide(Entity p_20339_) {
        return false;
    }

    public boolean canChangeDimensions() {
        return false;
    }

    public int getAlternativeTarget(int pId) {
        return this.entityData.get(DATA_TARGETS.get(pId));
    }

    public void setAlternativeTarget(int pHeadId, int pId) {
        this.entityData.set(DATA_TARGETS.get(pHeadId), pId);
    }

    public void resetDamageAmount() {
        this.damageAmount = 0.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NihilisticWither.createPathAttributes().add(Attributes.FOLLOW_RANGE, 140.0D)
                .add(Attributes.MAX_HEALTH, 600.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85D).add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D).add(Attributes.FLYING_SPEED, 0.6D);
    }

    public boolean cannotShoot() {
        return this.entityData.get(DATA_SHOOT_COOLDOWN) > 0;
    }

    public void setShootCooldown(int cooldown)
    {
        this.entityData.set(DATA_SHOOT_COOLDOWN, cooldown);
    }

    public void decreaseShootCooldown()
    {
        this.setShootCooldown(this.entityData.get(DATA_SHOOT_COOLDOWN) - 1);
    }

    private void performRangedAttack(int p_31458_, LivingEntity pTarget) {
        this.performRangedAttack(p_31458_, pTarget.getX(), pTarget.getY() +
                pTarget.getEyeHeight() * 0.5, pTarget.getZ(), p_31458_ == 0 && ThreadLocalRandom.current()
                .nextFloat() < 0.001F);
    }

    private void performRangedAttack(int p_31449_, double p_31450_, double p_31451_, double p_31452_,
                                     boolean p_31453_) {
        if (this.cannotShoot()) {
            return;
        }
        if (!this.isSilent()) {
            this.level().levelEvent(null, 1024, this.blockPosition(), 0);
        }
        double d0 = this.getHeadX(p_31449_);
        double d1 = this.getHeadY(p_31449_);
        double d2 = this.getHeadZ(p_31449_);
        double d3 = p_31450_ - d0;
        double d4 = p_31451_ - d1;
        double d5 = p_31452_ - d2;
        boolean flag = this.isHalfHealth() ? ThreadLocalRandom.current().nextBoolean() :
                ThreadLocalRandom.current().nextFloat() < 0.18F;
        if (flag) {
            NihilisticWitherSkull skull = new NihilisticWitherSkull(this.level(), this, d3, d4, d5);
            skull.setOwner(this);
            skull.setPosRaw(d0, d1, d2);
            this.level().addFreshEntity(skull);
        } else {
            WitherSkull witherskull = new WitherSkull(this.level(), this, d3, d4, d5);
            witherskull.setOwner(this);
            if (p_31453_) {
                witherskull.setDangerous(true);
            }
            witherskull.setPosRaw(d0, d1, d2);
            this.level().addFreshEntity(witherskull);
        }
    }

    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        this.performRangedAttack(0, pTarget);
    }

    public boolean canBeAffected(MobEffectInstance p_21197_) {
        return false;
    }

    static {
        DATA_TARGET_A = SynchedEntityData.defineId(NihilisticWither.class, EntityDataSerializers.INT);
        DATA_TARGET_B = SynchedEntityData.defineId(NihilisticWither.class, EntityDataSerializers.INT);
        DATA_TARGET_C = SynchedEntityData.defineId(NihilisticWither.class, EntityDataSerializers.INT);
        DATA_TARGETS = ImmutableList.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);
        DATA_ID_INV = SynchedEntityData.defineId(NihilisticWither.class, EntityDataSerializers.INT);
        DATA_BACK_DAMAGE_TIME = SynchedEntityData.defineId(NihilisticWither.class, EntityDataSerializers.INT);
        DATA_SHOOT_COOLDOWN = SynchedEntityData.defineId(NihilisticWither.class,
                EntityDataSerializers.INT);
    }

    private class WitherDoNothingGoal extends Goal {
        public WitherDoNothingGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        public boolean canUse() {
            return NihilisticWither.this.getInvulnerableTicks() > 0;
        }
    }

    private static class WitherRangedAttackGoal extends Goal {
        @Nullable
        private LivingEntity target;
        private int attackTime;
        private int seeTime;
        final NihilisticWither wither;
        public WitherRangedAttackGoal(NihilisticWither mob) {
            this.attackTime = -1;
            this.wither = mob;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity $$0 = this.wither.getTarget();
            if ($$0 == null || !$$0.isAlive()) {
                return false;
            } else {
                this.target = $$0;
                return true;
            }
        }

        public boolean canContinueToUse() {
            return this.target != null && (this.canUse() || this.target.isAlive() && !this.wither.getNavigation().isDone());
        }

        public void stop() {
            LivingEntity living = (LivingEntity)this.wither.level().getEntity(this.wither.getAlternativeTarget(0));
            if (living == null) {
                this.target = null;
            }
            this.seeTime = 0;
            this.attackTime = -1;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            double $$0 = this.wither.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            boolean $$1 = this.wither.getSensing().hasLineOfSight(this.target);
            if ($$1) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }
            float attackRadius = 30;
            float attackRadiusSqr = attackRadius * attackRadius;
            if (!($$0 > (double) attackRadiusSqr) && this.seeTime >= 5) {
                this.wither.getNavigation().stop();
            } else {
                this.wither.getNavigation().moveTo(this.target, 1);
            }
            this.wither.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            int attackIntervalMin = 10;
            int attackIntervalMax = 25;
            if (--this.attackTime == 0) {
                if (!$$1) {
                    return;
                }
                float $$2 = (float)Math.sqrt($$0) / attackRadius;
                float $$3 = Mth.clamp($$2, 0.1F, 1.0F);
                this.wither.performRangedAttack(this.target, $$3);
                this.attackTime = Mth.floor($$2 * (attackIntervalMax - attackIntervalMin) + attackIntervalMin);
            } else if (this.attackTime < 0) {
                this.attackTime = Mth.floor(Mth.lerp(Math.sqrt($$0) / attackRadius,
                        attackIntervalMin, attackIntervalMax));
            }
        }
    }
}
