
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.api.mobs.ApiBoss;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.api.mobs.IShieldUser;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.particle.CircleParticleOption;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIAttributesConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.projectile.HeadHunterSword;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class HeadHunter extends Monster implements ApiBoss, IFlagMob {
    private int hurtCount;
    private int deathTick;
    private int expositionAttackTick;
    private int tpTick;
    protected final Random randomUtil;
    public AnimationState swordAttacking = new AnimationState();
    public AnimationState summoning = new AnimationState();
    public AnimationState exposition = new AnimationState();
    public AnimationState changingPhase = new AnimationState();
    public AnimationState shooting = new AnimationState();
    public AnimationState dying = new AnimationState();
    public AnimationState swordGroundAttack = new AnimationState();
    public AnimationState charge = new AnimationState();
    public AnimationState sword_ground_explode = new AnimationState();
    public AnimationState attack_explode = new AnimationState();
    private static final EntityDataAccessor<Integer> DATA_AVOID_COOLDOWN;
    private static final EntityDataAccessor<Integer> DATA_COOLDOWN;
    private static final EntityDataAccessor<Integer> DATA_BOSS_FLAG;
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK;
    private static final EntityDataAccessor<Byte> DATA_BOSS_PHASE;
    static final float DAMAGE_CAPE = NoixmodAPIAttributesConfig.headhunterDamageCap.get().floatValue();
    private final ServerBossEvent bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED,
            BossEvent.BossBarOverlay.PROGRESS);

    public HeadHunter(EntityType<? extends HeadHunter> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.randomUtil = new Random();
        this.xpReward = 100;
        this.setPersistenceRequired();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACK_TICK, 0);
        this.entityData.define(DATA_AVOID_COOLDOWN, 0);
        this.entityData.define(DATA_BOSS_FLAG, 99);
        this.entityData.define(DATA_BOSS_PHASE, Maths.ONE_BYTE);
        this.entityData.define(DATA_COOLDOWN, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SetSecondGoal(this));
        this.goalSelector.addGoal(2, new HunterMeleeAttackGoal(this));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 30F));
        this.goalSelector.addGoal(6, new FloatGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    @SuppressWarnings("deprecation")
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_,
                                        MobSpawnType p_21436_,
                                        @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        p_21434_.getLevel().sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(),
                75, 0, 0, 0, this.randomUtil.nextGaussian() * 0.5);
        p_21434_.getLevel().sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(),
                25, 0, 0, 0, this.randomUtil.nextGaussian() * 0.5);
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance,
                                        MobSpawnType type) {
        return this.finalizeSpawn(accessor, instance, type, null, null);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        /*  99: Idle
            0: SwordAttacking
            1: Sweeping
            2: Explosion
            3: SwordGroundAttack
            4: Death
            5: ChangingPhase
            6: ShootingArrow
         */
        if (DATA_BOSS_FLAG.equals(p_21104_)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case 99: {
                        break;
                    }
                    case 0: {
                        this.stopAllAnimations();
                        this.swordAttacking.startIfStopped(this.tickCount);
                        break;
                    }
                    case 1: {
                        this.stopAllAnimations();
                        this.summoning.startIfStopped(this.tickCount);
                        break;
                    }
                    case 2: {
                        this.stopAllAnimations();
                        this.exposition.startIfStopped(this.tickCount);
                        break;
                    }
                    case 3: {
                        this.stopAllAnimations();
                        this.swordGroundAttack.startIfStopped(this.tickCount);
                        break;
                    }
                    case 4: {
                        this.stopAllAnimations();
                        this.dying.startIfStopped(this.tickCount);
                        break;
                    }
                    case 5: {
                        this.stopAllAnimations();
                        this.changingPhase.start(this.tickCount);
                        break;
                    }
                    case 6: {
                        this.stopAllAnimations();
                        this.shooting.startIfStopped(this.tickCount);
                        break;
                    }
                    case 7: {
                        this.stopAllAnimations();
                        this.exposition.startIfStopped(this.tickCount);
                    }
                    case 8: {
                        this.stopAllAnimations();
                        this.summoning.start(this.tickCount);
                        break;
                    }
                    case 9: {
                        this.stopAllAnimations();
                        this.charge.startIfStopped(this.tickCount);
                        break;
                    }
                    case 10: {
                        stopAllAnimations();
                        sword_ground_explode.startIfStopped(tickCount);
                        break;
                    }
                    case 11: {
                        this.stopAllAnimations();
                        this.attack_explode.startIfStopped(tickCount);
                        break;
                    }
                    default: {
                        NoixmodAPI.LOGGER.warn("Can't handle bossFlag {} in HeadHunter.", this.getFlag());
                        break;
                    }
                }
            }
        }
        super.onSyncedDataUpdated(p_21104_);
    }

    public void tick() {
        super.tick();
        if (this.tickCount % 40 == 0 && this.hurtCount > 0) {
            this.hurtCount--;
        }
        int chance = this.getRandom().nextInt(23);
        LivingEntity target = this.getTarget();
        if (this.isOnCooldown()) {
            if (target != null && getAvoidCooldown() <= 0 && this.closerThan(target, 10)) {
                avoid();
            }
        } else {
            if (target != null && this.getFlag() != 5) {
                if (this.getFlag() == 99) {
                    if (this.isSecondPhase()) {
                        if (chance <= 3) {
                            this.setFlag(3);
                        } else if (chance < 14 && this.expositionAttackTick <= 0) {
                            this.setFlag(7);
                        } else if (chance < 8) {
                            this.setFlag(11);
                        } else if (chance < 12) {
                            this.setFlag(8);
                        } else if (chance < 16) {
                            this.setFlag(9);
                        } else if (chance < 20) {
                            setFlag(10);
                        } else if (this.closerThan(target, 4)) {
                            this.setFlag(0);
                        }
                    } else {
                        int a = this.randomUtil.nextInt(15);
                        if (a < 2 && this.closerThan(target, 4)) {
                            this.setFlag(0);
                        } else if (a <= 4) {
                            this.setFlag(6);
                        } else if (a <= 6) {
                            this.setFlag(8);
                        } else if (a <= 9) {
                            setFlag(10);
                        } else if (a <= 12) {
                            this.setFlag(9);
                        } else {
                            this.setFlag(11);
                        }
                    }
                }
            }
        }
        if (this.getFlag() == 0) {
            increaseAniTick();
            if (this.getAniTick() == 15) {
                if (target != null && this.distanceToSqr(target) <= Maths.square(4)) {
                    List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox()
                            .inflate(1, 2, 1), living -> MobUtils
                            .canHurt(living, this));
                    this.playAttackSound();
                    if (!entities.isEmpty()) {
                        for (LivingEntity living : entities) {
                            if (living.isBlocking()) {
                                IShieldUser.hurtShield(living, 6);
                                MobUtils.disableShield(1, 1, 1, living);
                            } else {
                                living.setHealth(target.getHealth() - living.getMaxHealth() / 15);
                                living.hurt(this.damageSources().mobAttack(this), 10);
                                this.heal(1f);
                            }
                        }
                    }
                }
            }
            if (this.getAniTick() >= 35) {
                this.resetAniTick();
                resetFlag();
            }
        }
        if (this.getFlag() == 3) {
            increaseAniTick();
            if (this.getAniTick() == 15 || this.getAniTick() == 30 || this.getAniTick() == 40) {
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(3, 0.2, 3),
                        living -> MobUtils.canHurt(living, this));
                playAttackSound();
                if (!list.isEmpty()) {
                    for (LivingEntity living : list) {
                        if (living.isBlocking()) {
                            IShieldUser.hurtShield(living, 5);
                            MobUtils.disableShield(1, 1, 1, living);
                        } else {
                            if (living.hurt(this.damageSources().mobAttack(this), 9)) {
                                living.setHealth(living.getHealth() - 12);
                            }
                        }
                    }
                }
                if (!this.level().isClientSide) {
                    ((ServerLevel)this.level()).sendParticles(new CircleParticleOption(0, 0, 0, 8, 0.1F)
                            , this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0,
                            0);
                }
            }
            if (this.getAniTick() == 40) {
                for (int i = 0;i < 5;i++) {
                    HeadHunterSword sword = new HeadHunterSword(NoixmodAPIEntities.HHS.get(), this.level());
                    sword.setOwner(this);
                    sword.setOwnerUUID(this.getUUID());
                    sword.moveTo(this.blockPosition().offset(i, 0, i), 0, 0);
                    this.level().addFreshEntity(sword);
                }
                for (int i = 0;i < 5;i++) {
                    HeadHunterSword sword = new HeadHunterSword(NoixmodAPIEntities.HHS.get(), this.level());
                    sword.setOwner(this);
                    sword.setOwnerUUID(this.getUUID());
                    sword.moveTo(this.blockPosition().offset(-i, 0, -i), 0, 0);
                    this.level().addFreshEntity(sword);
                }
                for (int i = 0;i < 5;i++) {
                    HeadHunterSword sword = new HeadHunterSword(NoixmodAPIEntities.HHS.get(), this.level());
                    sword.setOwner(this);
                    sword.setOwnerUUID(this.getUUID());
                    sword.moveTo(this.blockPosition().offset(i, 0, 0), 0, 0);
                    this.level().addFreshEntity(sword);
                }
                for (int i = 0;i < 5;i++) {
                    HeadHunterSword sword = new HeadHunterSword(NoixmodAPIEntities.HHS.get(), this.level());
                    sword.setOwner(this);
                    sword.setOwnerUUID(this.getUUID());
                    sword.moveTo(this.blockPosition().offset(-i, 0, 0), 0, 0);
                    this.level().addFreshEntity(sword);
                }
                for (int i = 0;i < 5;i++) {
                    HeadHunterSword sword = new HeadHunterSword(NoixmodAPIEntities.HHS.get(), this.level());
                    sword.setOwner(this);
                    sword.setOwnerUUID(this.getUUID());
                    sword.moveTo(this.blockPosition().offset(0, 0, i), 0, 0);
                    this.level().addFreshEntity(sword);
                }
                for (int i = 0;i < 5;i++) {
                    HeadHunterSword sword = new HeadHunterSword(NoixmodAPIEntities.HHS.get(), this.level());
                    sword.setOwner(this);
                    sword.setOwnerUUID(this.getUUID());
                    sword.moveTo(this.blockPosition().offset(0, 0, -i), 0, 0);
                    this.level().addFreshEntity(sword);
                }
                this.resetAniTick();
                resetFlag();
            }
        }
        if (this.getFlag() == 5) {
            increaseAniTick();
            if (this.getAniTick() > 40 && this.getAniTick() < 90) {
                if (this.tickCount % 10 == 0) {
                    this.playAttackSound();
                    List<LivingEntity> livingEntities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox()
                                    .inflate(5),
                            living -> MobUtils.canHurt(living, this));
                    if (!livingEntities.isEmpty()) {
                        for (LivingEntity entity : livingEntities) {
                            if (entity.isAlive()) {
                                if (entity.isBlocking()) {
                                    IShieldUser.hurtShield(entity, 10);
                                    MobUtils.disableShield(1, 1, 1, entity);
                                } else {
                                    entity.setHealth(entity.getHealth() - 10);
                                    entity.hurt(this.damageSources().indirectMagic(this,
                                            this), 10);
                                }
                            }
                        }
                    }
                }
            }
            if (this.getAniTick() >= 120) {
                this.resetAniTick();
                this.setBossPhase(2);
                resetFlag();
            }
        }
        if (this.getFlag() == 6) {
            increaseAniTick();
            if (target != null) {
                this.lookControl.setLookAt(target);
            }
            if (this.getAniTick() == 10) {
                this.shootArrow();
            }
            if (this.getAniTick() == 15) {
                this.shootArrow();
            }
            if (this.getAniTick() == 20) {
                this.shootArrow();
            }
            if (this.getAniTick() == 30) {
                this.resetAniTick();
                resetFlag();
            }
        }
        if (this.getFlag() == 7) {
            increaseAniTick();
            if (this.getAniTick() == 30) {
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(10), living -> MobUtils.canHurt(living, this));
                for (LivingEntity living : list) {
                    if (living.isBlocking()) {
                        IShieldUser.hurtShield(living, 5);
                        MobUtils.disableShield(2, 2, 2, living);
                    } else {
                        living.setHealth(living.getHealth() - living.getMaxHealth() / 2);
                        living.hurt(this.damageSources().starve(), 10);
                        this.setHealth(this.getHealth() + this.getMaxHealth() / 2);
                    }
                }
                if (!this.level().isClientSide) {
                    ((ServerLevel)this.level()).sendParticles(new CircleParticleOption(0, 0, 0, 8, 0.1F)
                            , this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
                    ParticleUtil.sendParticles((ServerLevel)this.level(), ParticleTypes.LARGE_SMOKE,
                            this.position(), 120, 8, 8, 8, 0);
                }
            }
            if (this.getAniTick() >= 60) {
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox()
                                .inflate(8, 0.2, 8),
                        living -> !living.isAlive() || living.getHealth() <= 1);
                for (LivingEntity living : list) {
                    level().broadcastEntityEvent(living, (byte)3);
                }
                this.expositionAttackTick = 3000;
                this.resetAniTick();
                resetFlag();
            }
        }
        if (this.getFlag() == 8) {
            increaseAniTick();
            if (getAniTick() == 10) {
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(
                        6, 0.3, 6
                ), living -> MobUtils.canHurt(living, this));
                for (LivingEntity living : list) {
                    if (living.isAlive()) {
                        this.heal(isSecondPhase() ? 4f : 2f);
                    }
                    living.setHealth(living.getHealth() - Maths.healthLessThan(living, 10, 40));
                    living.hurt(this.damageSources().mobAttack(this), 10);
                }
                if (!this.level().isClientSide) {
                    ParticleUtil.sendParticles((ServerLevel)this.level(), ParticleTypes.LARGE_SMOKE,
                            this.position(), 100, 6, 0, 6, 0);
                    if (isSecondPhase()) {
                        playExplodeSound();
                        ParticleUtil.explode(getServerLevel(), position());
                    }
                }
                for (int p_1145_ = 0;p_1145_ < 4;p_1145_++) {
                    int power = isSecondPhase() ? 2 : 1;
                    LargeFireball ball = new LargeFireball(this.level(), this, 0.0,
                            -0.1, 0.0, power);
                    int p_1146 = 0;
                    int p_1147 = 0;
                    if (p_1145_ == 1) {
                        p_1146 = 1;
                    } else if (p_1145_ == 2) {
                        p_1146 = -1;
                    } else if (p_1145_ == 3) {
                        p_1147 = 1;
                    } else {
                        p_1147 = -1;
                    }
                    ball.setOwner(this);
                    BlockPos pos = this.blockPosition();
                    if (target != null) {
                        pos = target.blockPosition();
                    }
                    ball.moveTo(pos.offset(p_1146, 5, p_1147), 0, 0);
                    this.level().addFreshEntity(ball);
                }
            }
            if (getAniTick() >= 50) {
                this.resetAniTick();
                this.setFlag(99);
            }
        }
        if (this.getFlag() == 9) {
            increaseAniTick();
            if (getAniTick() == 43) {
                if (!level().isClientSide) {
                    playExplodeSound();
                    ParticleUtil.sendParticles(getServerLevel(), ParticleTypes.EXPLOSION_EMITTER, position(), 1,
                            0, 0, 0, 0);
                    ParticleUtil.sendParticles(getServerLevel(), ParticleTypes.LAVA, position(), 12,
                            1, 1, 1, 1);
                    List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(
                            5), entity -> MobUtils.canHurt(entity, this));
                    if (!entities.isEmpty()) {
                        for (LivingEntity living : entities) {
                            float headHunter$tick$9 = living.getHealth() - Maths.healthLessThan(living, 4, 200);
                            living.setHealth(headHunter$tick$9);
                            living.hurt(this.damageSources().explosion(this, this), 12);
                        }
                    }
                    for (int k = 0;k < 8;k++) {
                        Vec3 vec3 = position();
                        Vec3 vec4 = Vec3.ZERO;
                        if (k == 0) {
                            vec3 = vec3.add(0, 0, 6);
                            vec4 = vec4.add(0, 0, 6);
                        } else if (k == 1) {
                            vec3 = vec3.add(6, 0, 0);
                            vec4 = vec4.add(6, 0, 0);
                        } else if (k == 2) {
                            vec3 = vec3.add(0, 0, -6);
                            vec4 = vec4.add(0, 0, -6);
                        } else if (k == 3) {
                            vec3 = vec3.add(-6, 0, 0);
                            vec4 = vec4.add(-6, 0, 0);
                        } else if (k == 4) {
                            vec3 = vec3.add(-6, 0, -6);
                            vec4 = vec4.add(-6, 0, -6);
                        } else if (k == 5) {
                            vec3 = vec3.add(6, 0, 6);
                            vec4 = vec4.add(6, 0, 6);
                        } else if (k == 6) {
                            vec3 = vec3.add(-6, 0, 6);
                            vec4 = vec4.add(-6, 0, 6);
                        } else {
                            vec3 = vec3.add(6, 0, -6);
                            vec4 = vec4.add(6, 0, -6);
                        }
                        ParticleUtil.sendParticles(getServerLevel(), ParticleTypes.EXPLOSION_EMITTER, vec3, 1,
                                0, 0, 0, 0);
                        ParticleUtil.sendParticles(getServerLevel(), ParticleTypes.LAVA, vec3, 12,
                                1, 1, 1, 1);
                        List<LivingEntity> list = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().move(vec4)
                                        .inflate(4),
                                entity -> MobUtils.canHurt(entity, this));
                        if (!list.isEmpty()) {
                            for (LivingEntity living : list) {
                                living.hurt(this.damageSources().explosion(this, this), 12);
                            }
                        }
                    }
                }
            }
            if (getAniTick() >= 80) {
                resetAniTick();
                resetFlag();
            }
        }
        if (this.getFlag() == 10) {
            increaseAniTick();
            if (getAniTick() == Maths.toTick(0.75f)) {
                if (!level().isClientSide) {
                    playSound(SoundEvents.FIRE_EXTINGUISH, 2f, 0.5f);
                    EvokerFangs fangs = EntityType.EVOKER_FANGS.create(level());
                    if (fangs != null) {
                        LivingEntity optionalTarget = Optional.ofNullable(target).orElse(this);
                        fangs.moveTo(optionalTarget.position());
                        fangs.setOwner(this);
                        level().addFreshEntity(fangs);
                    }
                }
            }
            if (getAniTick() == Maths.toTick(1.25)) {
                playExplodeSound();
                for (int i = 0;i < 5;i++) {
                    if (!level().isClientSide) {
                        Vec3 vec3 = position();
                        Vec3 vec4 = Vec3.ZERO;
                        if (i == 1) {
                            vec3 = vec3.add(6, 0, 0);
                            vec4 = vec4.add(6, 0, 0);
                        } else if (i == 2) {
                            vec3 = vec3.add(0, 0, 6);
                            vec4 = vec4.add(0, 0, 6);
                        } else if (i == 3) {
                            vec3 = vec3.add(0, 0, -6);
                            vec4 = vec4.add(0, 0, -6);
                        } else if (i == 4) {
                            vec3 = vec3.add(-6, 0, 0);
                            vec4 = vec4.add(-6, 0, 0);
                        }
                        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox()
                                .move(vec4).inflate(4), entity -> MobUtils.canHurt(entity, this));
                        ParticleUtil.explode(getServerLevel(), vec3);
                        ParticleUtil.sendParticles(getServerLevel(), ParticleTypes.LAVA, vec3, 12,
                                1, 1, 1, 1);
                        if (!entities.isEmpty()) {
                            for (LivingEntity living : entities) {
                                setHealth(getHealth() + 3);
                                living.setHealth(living.getHealth() - (living.getMaxHealth() / 5));
                                float headHunter$tick$10$BaseDamage = living.getMaxHealth() / 4f;
                                float headHunter$tick$10$DamageAmount = Math.max(headHunter$tick$10$BaseDamage, 10f);
                                living.hurt(this.damageSources().indirectMagic(this, this),
                                        headHunter$tick$10$DamageAmount);
                            }
                        }
                    }
                }
            }
            if (getAniTick() == Maths.toTick(1.75)) {
                playExplodeSound();
                for (int i = 0;i < 7;i++) {
                    if (!level().isClientSide) {
                        Vec3 vec3 = position();
                        Vec3 vec4 = Vec3.ZERO;
                        if (i == 0) {
                            vec3 = vec3.add(12, 0, 0);
                            vec4 = vec4.add(12, 0, 0);
                        } else if (i == 1) {
                            vec3 = vec3.add(0, 0, 12);
                            vec4 = vec4.add(0, 0, 12);
                        } else if (i == 2) {
                            vec3 = vec3.add(0, 0, -12);
                            vec4 = vec4.add(0, 0, -12);
                        } else if (i == 3) {
                            vec3 = vec3.add(-12, 0, 0);
                            vec4 = vec4.add(-12, 0, 0);
                        } else if (i == 4) {
                            vec3 = vec3.add(-12, 0, -12);
                            vec4 = vec4.add(-12, 0, -12);
                        } else if (i == 5) {
                            vec3 = vec3.add(12, 0, 12);
                            vec4 = vec4.add(12, 0, 12);
                        } else {
                            vec3 = vec3.add(-12, 0, 12);
                            vec4 = vec4.add(-12, 0, 12);
                        }
                        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox()
                                .move(vec4).inflate(4), entity -> MobUtils.canHurt(entity, this));
                        ParticleUtil.explode(getServerLevel(), vec3);
                        ParticleUtil.sendParticles(getServerLevel(), ParticleTypes.LAVA, vec3, 12,
                                1, 1, 1, 1);
                        if (!entities.isEmpty()) {
                            for (LivingEntity living : entities) {
                                setHealth(getHealth() + 3);
                                living.setHealth(living.getHealth() - (living.getMaxHealth() / 5f));
                                float headHunter$tick$10$BaseDamage = living.getMaxHealth() / 4f;
                                float headHunter$tick$10$DamageAmount = Math.max(headHunter$tick$10$BaseDamage, 10f);
                                living.hurt(this.damageSources().indirectMagic(this, this),
                                        headHunter$tick$10$DamageAmount);
                            }
                        }
                    }
                }
            }
            if (getAniTick() >= Maths.toTick(2.25f)) {
                resetAniTick();
                resetFlag();
            } else {
                for (int i = 0;i < 13;i++) {
                    if (level().isClientSide) {
                        Vec3 vec3 = position();
                        if (i == 0) {
                            vec3 = position();
                        } else if (i == 1) {
                            vec3 = vec3.add(12, 0, 0);
                        } else if (i == 2) {
                            vec3 = vec3.add(0, 0, 12);
                        } else if (i == 3) {
                            vec3 = vec3.add(0, 0, -12);
                        } else if (i == 4) {
                            vec3 = vec3.add(-12, 0, 0);
                        } else if (i == 5) {
                            vec3 = vec3.add(-12, 0, -12);
                        } else if (i == 6) {
                            vec3 = vec3.add(12, 0, 12);
                        } else if (i == 7) {
                            vec3 = vec3.add(-12, 0, 12);
                        } else if (i == 8) {
                            vec3 = vec3.add(12, 0, -12);
                        } else if (i == 9) {
                            vec3 = vec3.add(6, 0, 0);
                        } else if (i == 10) {
                            vec3 = vec3.add(0, 0, 6);
                        } else if (i == 11) {
                            vec3 = vec3.add(0, 0, -6);
                        } else {
                            vec3 = vec3.add(-6, 0, 0);
                        }
                        flameParticle(vec3);
                    }
                }
            }
        }
        if (this.getFlag() == 11) {
            increaseAniTick();
            if (getAniTick() == 10) {
                playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE);
                attack();
            }
            if (getAniTick() == 20) {
                MobUtils.rangeHurt(3, 0.3, 3, this, damageSources().mobAttack(this),
                        9f);
            }
            if (getAniTick() == 35) {
                playExplodeSound();
                for (int i = 0;i < 5;i++) {
                    if (!level().isClientSide) {
                        Vec3 vec3 = position();
                        Vec3 vec4 = Vec3.ZERO;
                        if (i == 1) {
                            vec3 = vec3.add(6, 0, 0);
                            vec4 = vec4.add(6, 0, 0);
                        } else if (i == 2) {
                            vec3 = vec3.add(0, 0, 6);
                            vec4 = vec4.add(0, 0, 6);
                        } else if (i == 3) {
                            vec3 = vec3.add(0, 0, -6);
                            vec4 = vec4.add(0, 0, -6);
                        } else if (i == 4) {
                            vec3 = vec3.add(-6, 0, 0);
                            vec4 = vec4.add(-6, 0, 0);
                        }
                        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox()
                                .move(vec4).inflate(4), entity -> MobUtils.canHurt(entity, this));
                        ParticleUtil.explode(getServerLevel(), vec3);
                        ParticleUtil.sendParticles(getServerLevel(), ParticleTypes.LAVA, vec3, 12,
                                1, 1, 1, 1);
                        if (!entities.isEmpty()) {
                            for (LivingEntity living : entities) {
                                setHealth(getHealth() + 3);
                                living.setHealth(living.getHealth() - (living.getMaxHealth() / 5));
                                float headHunter$tick$10$BaseDamage = living.getMaxHealth() / 4f;
                                float headHunter$tick$10$DamageAmount = Math.max(headHunter$tick$10$BaseDamage, 10f);
                                living.hurt(this.damageSources().indirectMagic(this, this),
                                        headHunter$tick$10$DamageAmount);
                            }
                        }
                    }
                }
            }
            if (getAniTick() >= 55) {
                resetAniTick();
                resetFlag();
            }
        }
    }

    public ServerLevel getServerLevel() throws ClassCastException {
        return (ServerLevel)this.level();
    }

    public boolean isHalfHealth() {
        return MobUtils.isHalfHealth(this);
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        int duration = p_147208_.getDuration();
        if (duration != -1) {
            duration /= 3;
        }
        return super.addEffect(new MobEffectInstance(p_147208_.getEffect(), duration, p_147208_
                .getAmplifier()), p_147209_);
    }

    protected float getStandingEyeHeight(Pose p_21131_, EntityDimensions p_21132_) {
        return 2.5f;
    }

    public void aiStep() {
        super.aiStep();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.expositionAttackTick > 0) {
            this.expositionAttackTick--;
        }
        if (this.tpTick > 0) {
            --this.tpTick;
        }
        if (this.getAvoidCooldown() > 0) {
            this.setAvoidCooldown(getAvoidCooldown() - 1);
        }
        if (this.isOnCooldown()) {
            this.setCooldown(this.getCooldown() - 1);
        }
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    private void playAttackSound() {
        this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2, 1);
    }

    private void shootArrow() {
        LivingEntity $$0 = this.getTarget();
        if ($$0 != null) {
            if (MobUtils.isHard(this)) {
                if ($$0 instanceof Player player && player.getCooldowns().isOnCooldown(Items.SHIELD)) {
                    return;
                }
                EntityEventHandler.broadcastEntityEvent(this, 6);
            } else {
                Arrow arrow = new Arrow(this.level(), this);
                double $$4 = $$0.getX() - this.getX();
                double $$5 = $$0.getY(0.5) - this.getY(0.5);
                double $$6 = $$0.getZ() - this.getZ();
                arrow.setBaseDamage(8);
                arrow.shoot($$4, $$5, $$6, 2.5f, 0.8f);
                this.level().addFreshEntity(arrow);
            }
            this.playSound(SoundEvents.CROSSBOW_SHOOT);
        }
    }

    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(p_21016_, p_21017_);
        } else {
            float vFloat = Math.min(p_21017_, DAMAGE_CAPE);
            if (vFloat > 1F) {
                if (this.getFlag() == 5) {
                    return false;
                }
                Entity entity = p_21016_.getEntity();
                if (!MobUtils.canHurt(this, entity)) {
                    return false;
                }
                if (p_21016_.is(DamageTypeTags.IS_FALL)) {
                    return false;
                }
                return super.hurt(p_21016_, vFloat);
            } else return false;
        }
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (p_21240_.is(DamageTypes.GENERIC_KILL)) {
            super.actuallyHurt(p_21240_, p_21241_);
        } else {
            float vFloat = Math.min(p_21241_, DAMAGE_CAPE);
            if (this.hurtCount >= 6) {
                vFloat *= 0;
            } else if (this.hurtCount > 4) {
                vFloat *= 0.25F;
            } else if (this.hurtCount > 2) {
                vFloat *= 0.5F;
            }
            if (this.getFlag() == 5) {
                return;
            }
            if (vFloat > 1F) {
                ++this.hurtCount;
                super.actuallyHurt(p_21240_, vFloat);
            }
        }
    }

    protected void tickDeath() {
        ++this.deathTick;
        if (this.level().isClientSide) {
            for (int i = 0;i < 2;++i) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.8),
                        this.getRandomY(), this.getRandomZ(0.8), 0, 0, 0);
            }
        }
        if (this.deathTick >= 20 && !this.isRemoved()) {
            this.remove(RemovalReason.KILLED);
        }
    }

    private void attack() {
        Vec3 vec3 = getLookAngle();
        setDeltaMovement(vec3.x * 3, getDeltaMovement().y, vec3.z * 3);
    }

    private void avoid() {
        int chance = randomUtil.nextInt(3);
        Vec3 vec3;
        if (chance == 0) {
            vec3 = getLookAngle();
            setDeltaMovement(vec3.x() * -6, getDeltaMovement().y(), vec3.z() * -6);
        } else if (chance == 1) {
            vec3 = Vec3.directionFromRotation(0, getYRot() - 90)
                    .scale(1.5).multiply(3.2, 1, 3.2);
            setDeltaMovement(vec3);
        } else {
            vec3 = Vec3.directionFromRotation(0, getYRot() + 90)
                    .scale(1.5).multiply(3.2, 1, 3.2);
            setDeltaMovement(vec3);
        }
        setAvoidCooldown(30);
    }

    public void setFlag(int flag) {
        this.entityData.set(DATA_BOSS_FLAG, flag);
        if (flag == 99) {
            this.setCooldown(40);
        }
    }

    public void resetFlag() {
        this.setFlag(99);
    }

    public void resetAniTick() {
        setAniTick(0);
    }
    
    public int getAniTick() {
        return this.entityData.get(DATA_ATTACK_TICK);
    }
    
    public void setAniTick(int tick) {
        this.entityData.set(DATA_ATTACK_TICK, tick);
    }

    public void playExplodeSound() {
        playSound(SoundEvents.GENERIC_EXPLODE, 2f, 1f);
    }

    public void die(DamageSource p_21014_) {
        this.setFlag(4);
        super.die(p_21014_);
    }

    public void setPose(Pose pPose) {
        if (pPose == Pose.DYING)
            return;
        super.setPose(pPose);
    }

    private void flameParticle(Vec3 pos) {
        double d = Maths.randomBetween(-2, 2);
        double d1 = Maths.randomBetween(-2, 2);
        ParticleUtil.addParticle(level(), ParticleTypes.FLAME, pos.add(d, 0, d1), 0,
                0, 0);
    }

    public boolean immobile() {
        return this.getFlag() == 5 || this.getFlag() == 6 || this.getFlag() == 9
                || getFlag() == 11;
    }

    public int getFlag() {
        return this.entityData.get(DATA_BOSS_FLAG);
    }

    public ImmutableList<AnimationState> hhAnis() {
        return ImmutableList.of(
                swordAttacking, swordGroundAttack, changingPhase, shooting, charge, sword_ground_explode, attack_explode
        );
    }

    public void stopAllAnimations() {
        for (AnimationState state : hhAnis()) {
            state.stop();
        }
    }

    public boolean isSecondPhase() {
        return this.getBossPhase() == 2;
    }

    public byte getBossPhase() {
        return this.entityData.get(DATA_BOSS_PHASE);
    }

    public void setBossPhase(int phase) {
        this.entityData.set(DATA_BOSS_PHASE, (byte) phase);
    }

    private int getCooldown() {
        return this.entityData.get(DATA_COOLDOWN);
    }

    private void setCooldown(int cooldown) {
        this.entityData.set(DATA_COOLDOWN, cooldown);
    }

    private boolean isOnCooldown() {
        return this.getCooldown() > 0;
    }

    void setAvoidCooldown(int cooldown) {
        this.entityData.set(DATA_AVOID_COOLDOWN, cooldown);
    }

    int getAvoidCooldown() {
        return entityData.get(DATA_AVOID_COOLDOWN);
    }

    public void spawnAnim() {
        if (this.level().isClientSide) {
            for (int i = 0;i < 20;++i) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                double d3 = 10.0;
                this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(1.0)
                                - d0 * d3,
                        this.getRandomY() - d1 * d3, this.getRandomZ(1.0)
                                - d2 * d3, d0, d1, d2);
            }
        } else {
            this.level().broadcastEntityEvent(this, (byte) 20);
        }
    }

    public void addAdditionalSaveData(CompoundTag p_21484_) {
        p_21484_.putInt("BossFlag", this.getFlag());
        p_21484_.putByte("BossPhase", this.getBossPhase());
        super.addAdditionalSaveData(p_21484_);
    }

    public void readAdditionalSaveData(CompoundTag p_21450_) {
        if (p_21450_.contains("BossPhase")) {
            this.setBossPhase(p_21450_.getByte("BossPhase"));
        }
        if (p_21450_.contains("BossFlag")) {
            this.setFlag(p_21450_.getInt("BossFlag"));
        }
        super.readAdditionalSaveData(p_21450_);
    }

    public void startSeenByPlayer(ServerPlayer p_20119_) {
        this.bossEvent.addPlayer(p_20119_);
    }

    public void stopSeenByPlayer(ServerPlayer p_20174_) {
        this.bossEvent.removePlayer(p_20174_);
    }

    @Override
    public void setCustomName(@Nullable Component p_20053_) {
        super.setCustomName(p_20053_);
        if (p_20053_ != null) {
            this.bossEvent.setName(p_20053_);
        }
    }

    @Override
    public boolean canDisableShield() {
        return super.canDisableShield() || this.randomUtil.nextFloat() > 0.9;
    }

    @Override
    public float getSpeed() {
        if (this.immobile()) {
            return 0.0f;
        }
        return super.getSpeed();
    }

    @Override
    public final boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    @Override
    public boolean isSteppingCarefully() {
        return true;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.GENERIC_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        this.heal(9f);
        return super.killedEntity(p_216988_, p_216989_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return HeadHunter.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 90)
                .add(Attributes.MAX_HEALTH, NoixmodAPIAttributesConfig.headhunterHealth.get())
                .add(Attributes.ARMOR, NoixmodAPIAttributesConfig.headhunterArmor.get())
                .add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75);
    }

    static {
        DATA_ATTACK_TICK = SynchedEntityData.defineId(HeadHunter.class, EntityDataSerializers.INT);
        DATA_AVOID_COOLDOWN = SynchedEntityData.defineId(HeadHunter.class, EntityDataSerializers.INT);
        DATA_BOSS_FLAG = SynchedEntityData.defineId(HeadHunter.class, EntityDataSerializers.INT);
        DATA_BOSS_PHASE = SynchedEntityData.defineId(HeadHunter.class, EntityDataSerializers.BYTE);
        DATA_COOLDOWN = SynchedEntityData.defineId(HeadHunter.class, EntityDataSerializers.INT);
    }

    private static class HunterMeleeAttackGoal extends ApiMeleeAttackGoal {
        final HeadHunter hunter;

        public HunterMeleeAttackGoal(HeadHunter finder) {
            super(finder, 1, 9);
            this.hunter = finder;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity p_9957_, double p_25558_) {
        }

        @Override
        public void stop() {
            this.hunter.getNavigation().stop();
            if (this.hunter.getTarget() == null) {
                this.hunter.setAggressive(false);
            }
        }

        @Override
        public boolean canUse() {
            if (this.hunter.getFlag() == 5) {
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }
    }

    /*private static class AttackGoal extends Goal {
        private final HeadHunter mob;

        public AttackGoal(HeadHunter hunter) {
            this.mob = hunter;
        }

        @Override
        public boolean canUse() {
            return this.mob.getTarget() != null && this.mob.getAttackTick() > 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.mob.getAttackTick() < Maths.toTick(1);
        }
    }*/

    private static class SetSecondGoal extends Goal {
        private final HeadHunter hunter;

        SetSecondGoal(HeadHunter headHunter) {
            this.hunter = headHunter;
        }

        public void start() {
            this.hunter.resetAniTick();
            this.hunter.setFlag(5);
        }

        public boolean canUse() {
            return this.hunter.isHalfHealth() && !this.hunter.isSecondPhase() && this.hunter.getFlag() != 5;
        }

        public boolean canContinueToUse() {
            return this.canUse();
        }
    }
}
