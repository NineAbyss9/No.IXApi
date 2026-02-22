
package com.bilibili.player_ix.noixmod_api.entities.boss.abyss;

import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import com.github.NineAbyss9.ix_api.api.mobs.ApiNihilisticBoss;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.api.mobs.IShieldUser;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.api.mobs.effect.EffectInstance;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.github.NineAbyss9.ix_api.util.UnmodifiableList;
import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.api.entity.IX;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.boss.star_guardian.StarGuardian;
import com.bilibili.player_ix.noixmod_api.entities.monster.Golem;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.entities.projectile.Cage;
import com.bilibili.player_ix.noixmod_api.entities.projectile.DamageEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.bilibili.player_ix.noixmod_api.server.ApiBossEvent;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.TimeSelector;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Predicate;

/**重渊 Abyss
 *@see StarGuardian
 *@author Player_IX*/
public class Abyss
extends SpellcasterNihilist
implements ApiNihilisticBoss, IX, IFlagMob {
    private final AbyssData abyssData;
    private final ApiBossEvent bossInfo;
    private static final Logger LOGGER = LogUtils.getLogger();
    public float spin = 0.0f;
    int summonCooldown;
    private double targetBYO;
    public AnimationState attack = new AnimationState();
    public AnimationState attack2 = new AnimationState();
    public AnimationState attack3 = new AnimationState();
    public AnimationState clap = new AnimationState();
    public AnimationState ground = new AnimationState();
    public AnimationState summon = new AnimationState();
    public AnimationState throw_item = new AnimationState();
    public AnimationState idle = new AnimationState();
    public AnimationState clap_second = new AnimationState();
    public AnimationState attack4 = new AnimationState();
    public AnimationState attack5 = new AnimationState();
    private static final Component ABYSS;
    private static final int XP_REWARD = 1239;
    private static final float DAMAGE_CAPE;
    final UnmodifiableList<AnimationState> animations;
    final Predicate<LivingEntity> predicate;
    final DamageSource VOID;
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK;
    private static final EntityDataAccessor<Integer> DATA_BOSS_PHASE;
    private static final EntityDataAccessor<Integer> DATA_COOLDOWN;
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private static final EntityDataAccessor<Integer> DATA_HURT_COOLDOWN;
    private static final EntityDataAccessor<Integer> DATA_SUMMON_TICK;
    public Abyss(EntityType<Abyss> type, Level level) {
        super(type, level);
        this.abyssData = new AbyssData(this);
        this.bossInfo = new ApiBossEvent(this, ABYSS,
                BossEvent.BossBarColor.RED, true, true);
        animations = UnmodifiableList.of(attack, attack2, clap, ground, summon, attack3, throw_item,
                idle, clap_second, attack4, attack5);
        predicate = entity -> MobUtils.canHurt(entity, this) && !(entity instanceof Golem)
        && !(entity instanceof StarGuardian);
        VOID = level.damageSources().fellOutOfWorld();
        this.xpReward = XP_REWARD;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACK_TICK, 0);
        this.entityData.define(DATA_BOSS_PHASE, 1);
        this.entityData.define(DATA_COOLDOWN, 0);
        this.entityData.define(DATA_FLAGS, 0);
        this.entityData.define(DATA_HURT_COOLDOWN, 0);
        this.entityData.define(DATA_SUMMON_TICK, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AbyssAttackGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 30f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, Nihilistic.class));
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void tick() {
        super.tick();
        if (this.spin < Maths.CLOSER_PI) {
            this.spin = (this.spin + (Maths.CLOSER_PI / 180));
        } else {
            this.spin = (-Maths.CLOSER_PI);
        }
        this.abyssData.tickPhase();
        int chance = randomUtil.nextInt(19);
        if (this.canChangeFlag()) {
            if (this.isSecondPhase()) {
                if (chance < 7) {
                    this.setFlag(10);
                } else if (chance < 13) {
                    this.setFlag(8);
                } else {
                    this.setFlag(7);
                }
            } else {
                if (this.abyssData.canAttack()) {
                    if (chance < 4) {
                        this.setFlag(1);
                    } else if (chance < 8) {
                        this.setFlag(4);
                    } else if (chance < 12) {
                        this.setFlag(2);
                    } else if (chance < 16 && this.summonCooldown <= 0) {
                        this.setFlag(3);
                    } else {
                        this.setFlag(6);
                    }
                } else {
                    if (chance < 9 && this.summonCooldown <= 0) {
                        this.setFlag(3);
                    } else {
                        this.setFlag(2);
                    }
                }
            }
        }
        if (this.getFlag() == 1) {
            increaseAniTick();
            if (this.getAniTick() == 15) {
                AABB aabb = MobUtils.getRange(this, 3, 3, 3, 3, 3, 2, 3);
                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, aabb, predicate);
                attackSound();
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        if (living.isBlocking()) {
                            IShieldUser.hurtShield(living, 6);
                            MobUtils.disableShield(1, 1, 1, living);
                        } else {
                            this.doHurtTarget(living);
                            this.abyssHeal(0.25f);
                        }
                    }
                }
            }
            if (this.getAniTick() == 25) {
                resetAniTick();
                resetFlag();
            }
        }
        if (this.getFlag() == 2) {
            increaseAniTick();
            if (this.getAniTick() == 10) {
                this.clap();
            }
            if (this.getAniTick() == 20) {
                this.clap();
            }
            if (this.getAniTick() == 25) {
                this.resetAniTick();
                this.resetFlag();
            }
            return;
        }
        if (this.getFlag() == 3) {
            increaseAniTick();
            if (this.getAniTick() == TimeSelector.ONE_SEC) {
                List<LivingEntity> entities = this.abyssData.entities(2, 0.3, 2);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        living.hurt(VOID, 9f);
                        living.addEffect(EffectInstance.create(NoixmodAPIMobEffects.NIHILISTIC, 40, 0));
                    }
                }
                this.summon();
            }
            if (this.getAniTick() == 35) {
                resetAniTick();
                resetFlag();
            }
            return;
        }
        if (this.getFlag() == 4) {
            increaseAniTick();
            if (this.getAniTick() == 13) {
                AABB aabb = MobUtils.getRange(this, 3, 3, 3, 3, 3, 2, 3);
                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, aabb, predicate);
                attackSound();
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        if (living.isBlocking()) {
                            IShieldUser.hurtShield(living, 6);
                            MobUtils.disableShield(1, 1, 1, living);
                        } else {
                            this.doHurtTarget(living);
                            this.abyssHeal(0.25f);
                        }
                    }
                }
            }
            if (this.getAniTick() >= 25) {
                resetAniTick();
                resetFlag();
            }
            return;
        }
        if (this.getFlag() == 6) {
            increaseAniTick();
            if (this.getAniTick() == 25) {
                this.playSound(SoundEvents.TRIDENT_THUNDER, 2f, 1f);
                AABB aabb = MobUtils.getRange(this,  2.5, 5, 5, 5,5, 5, 5);
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb, predicate);
                if (!list.isEmpty()) {
                    for (LivingEntity living : list) {
                        abyssHeal(5f);
                        if (living.isBlocking()) {
                            IShieldUser.hurtShield(living, 1);
                        } else {
                            if (living.isAlive()) {
                                living.setHealth(living.getHealth() - 50f);
                            }
                            living.hurt(VOID, 19f);
                        }
                        if (!this.level().isClientSide) {
                            blood(living);
                        }
                    }
                }
            }
            if (this.getAniTick() >= 35) {
                resetAniTick();
                resetFlag();
            }
            return;
        }
        if (this.getFlag() == 7) {
            increaseAniTick();
            if (this.getAniTick() == 15) {
                LivingEntity target = this.getTarget();
                if (target != null) {
                    if (target.isBlocking()) {
                        IShieldUser.hurtShield(target, 10);
                    } else {
                        target.hurt(VOID, 12.0f);
                    }
                }
                this.setSummonTick(100);
            }
            if (this.getAniTick() >= 30) {
                resetAniTick();
                resetFlag();
            }
            return;
        }
        if (this.getFlag() == 8) {
            increaseAniTick();
            if (this.getAniTick() == 10) {
                this.clap();
            }
            if (this.getAniTick() == 20) {
                this.clap();
            }
            if (this.getAniTick() == 25) {
                this.resetAniTick();
                this.resetFlag();
            }
            return;
        }
        if (this.getFlag() == 10) {
            increaseAniTick();
            if (this.getAniTick() == 10) {
                LivingEntity target = this.getTarget();
                if (target != null) {
                    Cage cage = new Cage(NoixmodAPIEntities.CAGE.get(), this.level());
                    cage.moveTo(target.position());
                    cage.setOwner(this);
                    cage.setTarget(target);
                    this.level().addFreshEntity(cage);
                }
            }
            if (this.getAniTick() >= 25) {
                resetAniTick();
                resetFlag();
            }
        }
    }

    public void aiStep() {
        super.aiStep();
        if (this.tickCount % 20 == 0 && !this.level().isClientSide) {
            this.abyssHeal(1f);
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.tickCount % 5 == 0) {
            this.bossInfo.update();
        }
        if (this.isSecondPhase()) {
            LivingEntity target = this.getTarget();
            boolean flag;
            if (target != null) {
                flag = this.getY() > target.getY() + 6;
                this.targetBYO = target.getBoundingBox().getYsize();
            } else {
                flag = WorldUtil.low(this) > targetBYO + 5;
            }
            if (flag) {
                this.setDeltaMovement(Vec9.of(0, -0.15, 0));
            } else {
                if (target != null && this.getY() < target.getY() + 5) {
                    this.setDeltaMovement(Vec9.of(0, 0.25, 0));
                }
            }
        }
        if (this.isOnHurtCooldown()) {
            this.setHurtCooldown(this.getHurtCooldown() - 1);
        }
        if (this.getCooldown() > 0) {
            this.setCooldown(this.getCooldown() - 1);
        }
        if (this.isSummoning()) {
            if (this.getSummonTick() == 20 || this.getSummonTick() == 40
            || this.getSummonTick() == 60) {
                LivingEntity target = this.getTarget();
                if (target != null && !level().isClientSide) {
                    DamageEntity entity = new DamageEntity(NoixmodAPIEntities.DAMAGE_ENTITY.get(),
                            this.serverLevel());
                    entity.speed = 1.0f;
                    entity.options = ParticleTypes.SMOKE;
                    entity.dieParticles(NoixmodAPIParticleTypes.SUMMON_PARTICLE.get());
                    entity.damage = 10;
                    entity.lifeTicks = 20;
                    entity.source = VOID;
                    entity.setOwner(this);
                    entity.moveTo(target.position());
                    this.serverLevel().addFreshEntity(entity);
                }
            }
            this.setSummonTick(this.getSummonTick() - 1);
        }
        if (this.summonCooldown > 0) {
            --this.summonCooldown;
        }
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    protected void tickDeath() {
        ++this.deathTime;
        if (deathTime > 20) {
            if (!this.level().isClientSide) {
                ParticleUtil.spawnAnim(this, NoixmodAPIParticleTypes.BLOOD_SPELL.get());
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    //99 = Idle
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        this.stopAllAnimations();
                        this.attack.startIfStopped(tickCount);
                        break;
                    }
                    case 2: {
                        this.stopAllAnimations();
                        this.clap.startIfStopped(tickCount);
                        break;
                    }
                    case 3: {
                        this.stopAllAnimations();
                        this.ground.startIfStopped(tickCount);
                        break;
                    }
                    case 4: {
                        this.stopAllAnimations();
                        this.attack2.startIfStopped(tickCount);
                        break;
                    }
                    case 5: {
                        this.stopAllAnimations();
                        this.summon.startIfStopped(tickCount);
                        break;
                    }
                    case 6: {
                        this.stopAllAnimations();
                        this.attack3.startIfStopped(tickCount);
                        break;
                    }
                    case 7: {
                        this.stopAllAnimations();
                        this.throw_item.startIfStopped(tickCount);
                        break;
                    }
                    case 8: {
                        this.stopAllAnimations();
                        this.clap_second.startIfStopped(tickCount);
                        break;
                    }
                    case 10: {
                        this.stopAllAnimations();
                        this.attack5.startIfStopped(this.tickCount);
                        break;
                    }
                    case 99: {
                        this.idle.startIfStopped(tickCount);
                        break;
                    }
                    default: {
                        LOGGER.error("Cannot handle SyncedEvent {} in Abyss", this.getFlag());
                        this.setFlag(0);
                        break;
                    }
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public boolean isNoGravity() {
        return super.isNoGravity() || this.isSecondPhase();
    }

    private void clap() {
        LivingEntity entity = this.getTarget();
        if (!this.level().isClientSide) {
            if (entity != null) {
                List<LivingEntity> list = level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(
                    2, 2, 2), predicate);
                if (!list.isEmpty()) {
                    for (LivingEntity living : list) {
                        if (living.onGround()) {
                            entity.hurt(VOID, 15f);
                            blood(living);
                            this.abyssHeal(2.5f);
                        }
                    }
                }
                this.clapSound();
            }
        }
    }

    private void clapSound() {
        this.playSound(NoixmodAPISounds.CLAP.get(), 2f, 1f);
    }

    private void attackSound() {
        this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2f, 1f);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CurrentBossFlag", this.getFlag());
        tag.putInt("BossPhase", this.getBossPhase());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("CurrentBossFlag"))
            this.setFlag(tag.getInt("CurrentBossFlag"));
        if (tag.contains("BossPhase"))
            this.setBossPhase(tag.getInt("BossPhase"));
    }

    public void startSeenByPlayer(ServerPlayer p_20119_) {
        super.startSeenByPlayer(p_20119_);
        this.bossInfo.addPlayer(p_20119_);
    }

    public void stopSeenByPlayer(ServerPlayer p_20174_) {
        super.stopSeenByPlayer(p_20174_);
        this.bossInfo.removePlayer(p_20174_);
    }

    public float getSpeed() {
        if (this.immobile()) {
            return 0.0f;
        }
        return super.getSpeed();
    }

    public boolean immobile() {
        return this.getFlag() == 2 || this.getFlag() == 3 || this.getFlag() == 6;
    }

    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        this.abyssHeal(9f);
        return super.killedEntity(p_216988_, p_216989_);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isOnHurtCooldown()) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        if (p_20122_.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        if (p_20122_.is(DamageTypeTags.IS_FALL)) {
            return true;
        }
        if (p_20122_.is(DamageTypeTags.IS_LIGHTNING)) {
            return true;
        }
        return super.isInvulnerableTo(p_20122_);
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        p_21241_ = capeDamage(p_21241_);
        super.actuallyHurt(p_21240_, p_21241_);
    }

    protected float getDamageAfterArmorAbsorb(DamageSource p_21162_, float p_21163_) {
        p_21163_ = capeDamage(p_21163_);
        return super.getDamageAfterArmorAbsorb(p_21162_, p_21163_);
    }

    public void setHealth(float newHealth) {
        float health = this.getHealth();
        float delta = newHealth - health;
        if (delta < 0) {
            if (this.isOnHurtCooldown()) {
                return;
            }
            if (delta < -DAMAGE_CAPE) {
                newHealth = health - DAMAGE_CAPE;
            }
            this.setHurtCooldown(20);
        }
        super.setHealth(newHealth);
    }

    private float capeDamage(float damage) {
        damage = Math.min(DAMAGE_CAPE, damage);
        if (this.mobData.isInEnd()) {
            damage /= 1.5F;
        }
        return damage;
    }

    private void abyssHeal(float amount) {
        if (this.isAlive()) {
            this.setHealth(this.getHealth() + amount);
        }
    }

    private void blood(LivingEntity living) {
        ParticleUtil.sendParticles(this.serverLevel(), NoixmodAPIParticleTypes.BLOOD_SPELL.get(), living.position(),
                19, 1, 1, 1, 0);
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int flag) {
        this.entityData.set(DATA_FLAGS, flag);
    }

    public boolean isSecondPhase() {
        return this.getBossPhase() == 2;
    }

    boolean canChangeFlag() {
        return this.getTarget() != null && (this.getFlag() == 0 || this.getFlag() == 99)
                && this.getCooldown() <= 0;
    }

    int getBossPhase() {
        return this.entityData.get(DATA_BOSS_PHASE);
    }

    void setBossPhase(int phase) {
        this.entityData.set(DATA_BOSS_PHASE, Maths.clamp(phase, 1, 2));
    }

    public int getAniTick() {
        return this.entityData.get(DATA_ATTACK_TICK);
    }

    public void setAniTick(int tick) {
        this.entityData.set(DATA_ATTACK_TICK, tick);
    }

    public void resetAniTick() {
        this.setAniTick(0);
        this.setCooldown(20);
    }

    public void resetFlag() {
        if (this.isSecondPhase())
            this.setFlag(99);
        else
            this.setFlag(0);
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }

    protected void onEffectAdded(MobEffectInstance p_147190_, @Nullable Entity p_147191_) {
        removeAllEffects();
    }

    public void forceAddEffect(MobEffectInstance p_147216_, @Nullable Entity p_147217_) {
        removeAllEffects();
    }

    protected void tickEffects() {
        removeAllEffects();
    }

    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    public void checkDespawn() {
    }

    public int getHurtCooldown() {
        return this.entityData.get(DATA_HURT_COOLDOWN);
    }

    public void setHurtCooldown(int cooldown) {
        this.entityData.set(DATA_HURT_COOLDOWN, cooldown);
    }

    public int getCooldown() {
        return this.entityData.get(DATA_COOLDOWN);
    }

    public void setCooldown(int cooldown) {
        this.entityData.set(DATA_COOLDOWN, cooldown);
    }

    public boolean isOnHurtCooldown() {
        return this.getHurtCooldown() > 0;
    }

    boolean isSummoning() {
        return this.getSummonTick() > 0;
    }

    int getSummonTick() {
        return this.entityData.get(DATA_SUMMON_TICK);
    }

    void setSummonTick(int tick) {
        this.entityData.set(DATA_SUMMON_TICK, tick);
    }

    private void summon() {
        if (this.summonCooldown > 0) {
            return;
        }
        if (!this.level().isClientSide) {
            for (int i = 0;i < 5;i++) {
                ServerLevel serverLevel = this.serverLevel();
                Golem golem = new Golem(NoixmodAPIEntities.GOLEM.get(), serverLevel);
                this.getSummon().integerSummon(golem, 3);
                golem.setOwner(this);
                ParticleUtil.sendParticles(serverLevel, NoixmodAPIParticleTypes.DARK_SPELL.get(),
                        golem.position(), 12, 1, 1, 1, 0);
            }
            this.summonCooldown = 300;
        }
    }

    private void stopAllAnimations() {
        for (AnimationState state : animations) {
            state.stop();
        }
    }

    public Entity ixSelf() {
        return this;
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public static AttributeSupplier createAttributes() {
        return Abyss.createPathAttributes().add(Attributes.MAX_HEALTH, 297)
                .add(Attributes.ARMOR, 8).add(Attributes.FOLLOW_RANGE, 99)
                .add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 13)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.39).build();
    }

    static {
        ABYSS = Component.translatable("entity.noixmodapi.abyss")
                .withStyle(ChatFormatting.DARK_RED);
        DAMAGE_CAPE = 12.9f;
        DATA_ATTACK_TICK = SynchedEntityData.defineId(Abyss.class, EntityDataSerializers.INT);
        DATA_BOSS_PHASE = SynchedEntityData.defineId(Abyss.class, EntityDataSerializers.INT);
        DATA_COOLDOWN = SynchedEntityData.defineId(Abyss.class, EntityDataSerializers.INT);
        DATA_FLAGS = SynchedEntityData.defineId(Abyss.class, EntityDataSerializers.INT);
        DATA_HURT_COOLDOWN = SynchedEntityData.defineId(Abyss.class, EntityDataSerializers.INT);
        DATA_SUMMON_TICK = SynchedEntityData.defineId(Abyss.class, EntityDataSerializers.INT);
    }

    private static class AbyssAttackGoal extends ApiMeleeAttackGoal {
        public AbyssAttackGoal(PathfinderMob mob) {
            super(mob, 1, false, false);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {}
    }
}
