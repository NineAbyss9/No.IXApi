
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiBoss;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.NoAttackMeleeGoal;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
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
import java.util.function.Consumer;

import static com.bilibili.player_ix.noixmod_api.entities.boss.HeadHunter.DAMAGE_CAPE;

public class NewHeadHunter
extends Monster
implements ApiBoss, IFlagMob {
    private int hurtCount;
    private int deathTick;
    protected final Random randomUtil;
    public AnimationState swordAttacking = new AnimationState();
    public AnimationState summoning = new AnimationState();
    public AnimationState exposition = new AnimationState();
    public AnimationState changingPhase = new AnimationState();
    public AnimationState shooting = new AnimationState();
    public AnimationState dying = new AnimationState();
    public AnimationState groundExplode = new AnimationState();
    public AnimationState swordGroundAttack = new AnimationState();
    public AnimationState charge = new AnimationState();
    public AnimationState circleAttack = new AnimationState();
    public AnimationState sword_ground_explode = new AnimationState();
    public AnimationState attack_explode = new AnimationState();
    public AnimationState attack1 = new AnimationState();
    public AnimationState avoid = new AnimationState();
    private final ServerBossEvent bossEvent;
    private static final EntityDataAccessor<Integer> DATA_AVOID_COOLDOWN;
    private static final EntityDataAccessor<Integer> DATA_COOLDOWN;
    private static final EntityDataAccessor<Integer> DATA_BOSS_FLAG;
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK;
    private static final EntityDataAccessor<Byte> DATA_BOSS_PHASE;
    public static final int IDLE = 0;
    public static final int SWORD_ATTACKING = 1;
    public static final int SUMMONING = 2;
    public static final int EXPOSITION = 3;
    public static final int DYING = 4;
    public static final int CHANGING_PHASE = 5;
    public static final int SHOOTING = 6;
    public static final int SWORD_GROUND_ATTACK = 7;
    public static final int CHARGING = 8;
    public static final int SWORD_GROUND_EXPLODE = 9;
    public static final int ATTACK_EXPLODE = 10;
    public static final int CIRCLE_ATTACK = 11;
    public static final int ATTACK1 = 12;
    public static final int AVOIDING = 13;
    public static final int GROUND_EXPLODE = 14;
    public NewHeadHunter(EntityType<? extends NewHeadHunter> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.randomUtil = new Random();
        this.xpReward = 120;
        this.setPersistenceRequired();
        bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED,
                BossEvent.BossBarOverlay.NOTCHED_6);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACK_TICK, 0);
        this.entityData.define(DATA_AVOID_COOLDOWN, 0);
        this.entityData.define(DATA_BOSS_FLAG, 0);
        this.entityData.define(DATA_BOSS_PHASE, Maths.ONE_BYTE);
        this.entityData.define(DATA_COOLDOWN, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new NoAttackMeleeGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 30F));
        this.goalSelector.addGoal(6, new FloatGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void tick() {
        super.tick();
        if (this.tickCount % 40 == 0 && this.hurtCount > 0) {
            this.hurtCount--;
        }
        if (getAvoidCooldown() > 0)
            setAvoidCooldown(getAvoidCooldown() - 1);
        if (isOnCooldown())
            setCooldown(getCooldown() - 1);
        if (this.getTarget() != null && !this.getTarget().isRemoved()) {
            this.selectFlags(this.getTarget());
        }
        if (isHalfHealth() && !isSecondPhase() && getFlag() != CHANGING_PHASE) {
            resetAttackTick();
            setFlag(CHANGING_PHASE);
        }
        if (isSwordAttacking()) {
            Skills.swordAttack(this);
        } else if (isSummoning()) {
            summon(getTarget());
        } else if (isChangingPhase()) {
            Skills.changePhase(this);
        } else if (isShooting())
            Skills.shootArrows(this);
        else if (isCharging())
            Skills.charge(this);
        else if (isSwordGroundExplode())
            swordExplode(getTarget());
        else if (isAttackExplode())
            Skills.attackExplode(this);
        else if (isCircleAttack())
            Skills.circleAttack(this);
        else if (isAttack1())
            Skills.attack1(this);
        else if (isAvoiding())
            Skills.avoid(this);
        else if (isGroundExplode())
            Skills.groundExplode(this);
    }

    public void aiStep() {
        super.aiStep();
        if (this.isSecondPhase() && this.level().isClientSide && randomUtil.nextBoolean()) {
            ParticleUtil.addRedStoneParticle(this, getRandomX(0.8), getRandomY(),
                    getRandomZ(0.8), 0, 0, 0);
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (pKey.equals(DATA_BOSS_FLAG)) {
            if (this.level().isClientSide) {
                if (!isIdle())
                    stopAllAnis();
                if (this.isSwordAttacking()) {
                    this.swordAttacking.startIfStopped(tickCount);
                } else if (this.isSummoning()) {
                    this.summoning.startIfStopped(tickCount);
                } else if (this.isExposition()) {
                    exposition.startIfStopped(tickCount);
                } else if (this.isDying()) {
                    this.dying.start(tickCount);
                } else if (this.isChangingPhase()) {
                    changingPhase.startIfStopped(tickCount);
                } else if (this.isShooting()) {
                    shooting.startIfStopped(tickCount);
                } else if (isSwordGroundAttack()) {
                    swordGroundAttack.startIfStopped(tickCount);
                } else if (isCharging()) {
                    charge.startIfStopped(tickCount);
                } else if (isGroundExplode())
                    groundExplode.startIfStopped(tickCount);
                else if (isSwordGroundExplode()) {
                    sword_ground_explode.startIfStopped(tickCount);
                } else if (isAttackExplode()) {
                    attack_explode.startIfStopped(tickCount);
                } else if (isCircleAttack()) {
                    circleAttack.startIfStopped(tickCount);
                } else if (isAttack1()) {
                    attack1.startIfStopped(tickCount);
                } else if (isAvoiding()) {
                    avoid.startIfStopped(tickCount);
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(p_21016_, p_21017_);
        } else {
            float vFloat = Math.min(p_21017_, DAMAGE_CAPE);
            if (vFloat > 1F) {
                if (this.isChangingPhase()) {
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
                vFloat *= 0.05F;
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

    public void die(DamageSource pDamageSource) {
        resetState();
        this.setFlag(DYING);
        super.die(pDamageSource);
    }

    protected void tickDeath() {
        ++this.deathTick;
        if (this.level().isClientSide) {
            for (int i = 0;i < 2;++i) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.8),
                        this.getRandomY(), this.getRandomZ(0.8), 0, 0, 0);
            }
        }
        if (this.deathTick >= 45 && !this.isRemoved()) {
            this.remove(RemovalReason.KILLED);
        }
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putByte("BossPhase", this.getPhase());
        pCompound.putInt("BossFlag", this.getFlag());
        super.addAdditionalSaveData(pCompound);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("BossPhase"))
            this.setPhase(pCompound.getByte("BossPhase"));
        if (pCompound.contains("BossFlag"))
            this.setFlag(pCompound.getInt("BossFlag"));
        super.readAdditionalSaveData(pCompound);
    }

    public void shootArrow() {
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

    public void spawnAnim() {
        if (this.level().isClientSide) {
            for (int i = 0;i < 20;++i) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                double d3 = 10.0;
                this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(1.0) - d0 * d3,
                        this.getRandomY() - d1 * d3, this.getRandomZ(1.0) - d2 * d3, d0, d1, d2);
            }
        } else {
            this.level().broadcastEntityEvent(this, (byte) 20);
        }
    }

    public int getFlag() {
        return this.entityData.get(DATA_BOSS_FLAG);
    }

    public void setFlag(int flag) {
        if (flag == IDLE && !isAvoiding())
            setCooldown(30);
        this.entityData.set(DATA_BOSS_FLAG, flag);
    }

    public int getAttackTick() {
        return this.entityData.get(DATA_ATTACK_TICK);
    }

    public void setAttackTick(int tick) {
        this.entityData.set(DATA_ATTACK_TICK, tick);
    }

    public byte getPhase() {
        return this.entityData.get(DATA_BOSS_PHASE);
    }

    public void setPhase(int phase) {
        this.entityData.set(DATA_BOSS_PHASE, (byte)phase);
    }

    private int getCooldown() {
        return this.entityData.get(DATA_COOLDOWN);
    }

    private void setCooldown(int cooldown) {
        this.entityData.set(DATA_COOLDOWN, cooldown);
    }

    public int getAvoidCooldown() {
        return this.entityData.get(DATA_AVOID_COOLDOWN);
    }

    public void setAvoidCooldown(int cooldown) {
        this.entityData.set(DATA_AVOID_COOLDOWN, cooldown);
    }

    private boolean isOnCooldown() {
        return this.getCooldown() > 0;
    }

    public boolean isIdle() {
        return isFlag(IDLE);
    }

    public boolean isSwordAttacking() {
        return isFlag(1);
    }

    public boolean isSummoning() {
        return isFlag(2);
    }

    public boolean isExposition() {
        return isFlag(EXPOSITION);
    }

    public boolean isDying() {
        return isFlag(DYING);
    }

    public boolean isChangingPhase() {
        return isFlag(CHANGING_PHASE);
    }

    public boolean isShooting() {
        return isFlag(6);
    }

    public boolean isSwordGroundAttack() {
        return isFlag(SWORD_GROUND_ATTACK);
    }

    public boolean isCharging() {
        return isFlag(8);
    }

    public boolean isSwordGroundExplode() {
        return isFlag(9);
    }

    public boolean isAttackExplode() {
        return isFlag(10);
    }

    public boolean isCircleAttack() {
        return isFlag(11);
    }

    public boolean isAttack1() {
        return isFlag(12);
    }

    public boolean isAvoiding() {
        return isFlag(13);
    }

    public boolean isGroundExplode() {
        return isFlag(GROUND_EXPLODE);
    }

    public void swordExplode(@Nullable LivingEntity target) {
        plusAttackTick();
        if (getAttackTick() == Maths.toTick(0.75f)) {
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
        if (getAttackTick() == Maths.toTick(1.25)) {
            Skills.normalExplode(this);
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
                    ParticleUtil.explode(serverLevel(), vec3);
                    ParticleUtil.sendParticles(serverLevel(), ParticleTypes.LAVA, vec3, 12,
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
        if (getAttackTick() == Maths.toTick(1.75)) {
            Skills.normalExplode(this);
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
                    ParticleUtil.explode(serverLevel(), vec3);
                    ParticleUtil.sendParticles(serverLevel(), ParticleTypes.LAVA, vec3, 12,
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
        if (getAttackTick() >= Maths.toTick(2.25f)) {
            resetState();
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

    public void summon(@Nullable LivingEntity target) {
        plusAttackTick();
        if (getAttackTick() == 10) {
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
                    Skills.normalExplode(this);
                    ParticleUtil.explode(serverLevel(), position());
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
        if (getAttackTick() >= 50) {
            resetState();
        }
    }

    public void selectFlags(LivingEntity target) {
        int chance = randomUtil.nextInt(19);
        if (isOnCooldown()) {
            if (this.closerThan(target, 6) && !isAvoiding()
                && this.getAvoidCooldown() <= 0) {
                setFlag(AVOIDING);
            }
        } else if (this.isIdle()) {
            if (isSecondPhase()) {
                if (chance < 3 && canAttackTarget(target))
                    setFlag(SWORD_ATTACKING);
                else if (chance < 5)
                    setFlag(SUMMONING);
                else if (chance < 6)
                    setFlag(GROUND_EXPLODE);
                else if (chance < 7)
                    setFlag(CHARGING);
                else if (chance < 9)
                    setFlag(SWORD_GROUND_EXPLODE);
                else if (chance < 11)
                    setFlag(ATTACK_EXPLODE);
                else if (chance < 12)
                    setFlag(CIRCLE_ATTACK);
                else if (canAttackTarget(target))
                    setFlag(ATTACK1);
                else
                    setFlag(CHARGING);
            } else {
                if (chance < 4)
                    setFlag(SUMMONING);
                else if (chance < 10 && canAttackTarget(target)) {
                    if (randomUtil.nextBoolean())
                        setFlag(SWORD_ATTACKING);
                    else
                        setFlag(ATTACK1);
                }
                else if (chance < 12)
                    setFlag(SHOOTING);
                else if (chance < 14)
                    setFlag(SUMMONING);
                else
                    setFlag(CHARGING);
            }
        }
    }

    private boolean canAttackTarget(LivingEntity target) {
        return closerThan(target, 4);
    }

    private void flameParticle(Vec3 pos) {
        double d = Maths.randomBetween(-2, 2);
        double d1 = Maths.randomBetween(-2, 2);
        ParticleUtil.addParticle(level(), ParticleTypes.FLAME, pos.add(d, 0, d1), 0,
                0, 0);
    }

    public boolean isSecondPhase() {
        return getPhase() == 2;
    }

    public boolean immobile() {
        return isChangingPhase() || isCharging() || isSwordGroundExplode() || isCircleAttack();
    }

    public List<AnimationState> allAnis() {
        return List.of(swordAttacking, summoning, exposition, changingPhase, shooting, swordGroundAttack, charge,
                groundExplode, sword_ground_explode, attack_explode, circleAttack, attack1, avoid);
    }

    public void stopAllAnis() {
        this.allAnis().forEach(AnimationState::stop);
    }

    public ServerLevel serverLevel() {
        return (ServerLevel)this.level();
    }

    public boolean isHalfHealth() {
        return MobUtils.isHalfHealth(this);
    }

    public boolean addEffect(MobEffectInstance pEffectInstance, @Nullable Entity pEntity) {
        int duration = pEffectInstance.getDuration();
        if (duration != -1) {
            duration /= 3;
        }
        return super.addEffect(new MobEffectInstance(pEffectInstance.getEffect(), duration, pEffectInstance
                .getAmplifier()), pEntity);
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_,
                                        MobSpawnType p_21436_) {
        return this.finalizeSpawn(p_21434_, p_21435_, p_21436_, null, null);
    }

    public void startSeenByPlayer(ServerPlayer pServerPlayer) {
        this.bossEvent.addPlayer(pServerPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer pServerPlayer) {
        this.bossEvent.removePlayer(pServerPlayer);
    }

    public void setCustomName(@Nullable Component pName) {
        super.setCustomName(pName);
        if (pName != null) {
            this.bossEvent.setName(pName);
        }
    }

    public boolean canDisableShield() {
        return super.canDisableShield() || randomUtil.nextFloat() > 0.9F;
    }

    public final boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    public boolean killedEntity(ServerLevel pLevel, LivingEntity pEntity) {
        this.heal(9.0F);
        return super.killedEntity(pLevel, pEntity);
    }

    public float getSpeed() {
        if (immobile())
            return .0F;
        return super.getSpeed();
    }

    static {
        DATA_ATTACK_TICK = SynchedEntityData.defineId(NewHeadHunter.class, EntityDataSerializers.INT);
        DATA_AVOID_COOLDOWN = SynchedEntityData.defineId(NewHeadHunter.class, EntityDataSerializers.INT);
        DATA_BOSS_FLAG = SynchedEntityData.defineId(NewHeadHunter.class, EntityDataSerializers.INT);
        DATA_BOSS_PHASE = SynchedEntityData.defineId(NewHeadHunter.class, EntityDataSerializers.BYTE);
        DATA_COOLDOWN = SynchedEntityData.defineId(NewHeadHunter.class, EntityDataSerializers.INT);
    }

    private static final class Skills {
        public static void swordAttack(NewHeadHunter pHunter) {
            pHunter.plusAttackTick();
            if (pHunter.attackTickEquals(15)) {
                attackSound(pHunter);
                MobUtils.areaAttack(pHunter, 2, 2, 90,
                        12F, 0, 6, pHunter.damageSources().mobAttack(pHunter),
                        false, entity -> pHunter.heal(1F), false);
            }
            if (pHunter.attackTickMoreThan(35)) {
                pHunter.resetState();
            }
        }

        public static void avoid(NewHeadHunter pHunter) {
            pHunter.plusAttackTick();
            if (pHunter.attackTickEquals(1)) {
                onAvoid(pHunter);
            }
            if (pHunter.attackTickMoreThan(15)) {
                pHunter.resetState();
                pHunter.setAvoidCooldown(10);
            }
        }

        public static void shootArrows(NewHeadHunter pHunter) {
            pHunter.plusAttackTick();
            if (pHunter.getTarget() != null) {
                pHunter.lookControl.setLookAt(pHunter.getTarget());
            }
            if (pHunter.attackTickEquals(10) || pHunter.attackTickEquals(15) ||
                    pHunter.attackTickEquals(20))
                pHunter.shootArrow();
            if (pHunter.attackTickMoreThan(30))
                pHunter.resetState();
        }

        public static void changePhase(NewHeadHunter pHunter) {
            pHunter.plusAttackTick();
            if (!pHunter.level().isClientSide && pHunter.attackTickEquals(15) ||
                    pHunter.attackTickEquals(20)) {
                LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, pHunter.level());
                bolt.setVisualOnly(true);
                bolt.moveTo(pHunter.position());
                pHunter.level().addFreshEntity(bolt);
            }
            if (pHunter.attackTickEquals(30)) {
                explodeSound(pHunter);
                areaAttack(pHunter, 10.0F, 2.0F, 10.0F, 0.1F,
                        4, pHunter.damageSources().mobAttack(pHunter), entity -> {
                            float f = entity.getMaxHealth() / 8;
                            if ((entity.getHealth() - f) >= 0)
                                entity.setHealth(entity.getHealth() - f);
                        });
            }
            if (pHunter.attackTickMoreThan(50)) {
                pHunter.setPhase(2);
                pHunter.resetState();
            }
        }

        public static void charge(NewHeadHunter pHunter) {
            pHunter.plusAttackTick();
            if (pHunter.attackTickEquals(45))
            {
                if (pHunter.isEffectiveAi()) {
                    explodeSound(pHunter);
                    ParticleUtil.explode(pHunter.serverLevel(), pHunter.position());
                    pHunter.serverLevel().sendParticles(ParticleTypes.LAVA, pHunter.getX(), pHunter.getY(),
                            pHunter.getZ(), 30, 1, 1, 1, 1);
                    var list = makeList(pHunter, 4);
                    if (!list.isEmpty()) {
                        list.forEach(e -> {
                            e.setHealth(e.getHealth() - e.getMaxHealth() / 8);
                            e.hurt(pHunter.damageSources().indirectMagic(pHunter, pHunter), 12);
                        });
                    }
                }
            }
            if (pHunter.attackTickMoreThan(65))
            {
                pHunter.resetState();
            }
        }

        public static void groundExplode(NewHeadHunter pHunter)
        {
            pHunter.plusAttackTick();
            if (pHunter.attackTickEquals(20) && pHunter.isEffectiveAi()) {
                explodeSound(pHunter);
                ParticleUtil.explode(pHunter.serverLevel(), pHunter.position());
                ParticleUtil.sendParticles(pHunter.serverLevel(), ParticleTypes.LARGE_SMOKE, pHunter.position(),
                        40, 2, 0.3, 2, 0);
                List<LivingEntity> entities = pHunter.level().getEntitiesOfClass(LivingEntity.class, pHunter.getBoundingBox()
                        .inflate(4));
                if (!entities.isEmpty())
                    entities.forEach(entity -> {
                        if (pHunter.doHurtTarget(entity))
                            pHunter.heal(0.5F);
                    });
            }
            if (pHunter.attackTickMoreThan(45))
            {
                pHunter.resetState();
            }
        }

        public static void attackExplode(NewHeadHunter pHunter) {
            pHunter.plusAttackTick();
            if (pHunter.attackTickEquals(10))
            {
                pHunter.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE);
                Vec3 vec3 = pHunter.getLookAngle();
                pHunter.setDeltaMovement(vec3.x * 3, pHunter.getDeltaMovement().y,
                        vec3.z * 3);
            }
            if (pHunter.attackTickEquals(20))
            {
                areaAttack(pHunter, 2, 2, 14.0F, 10, null);
            }
            if (pHunter.attackTickEquals(30) && pHunter.isEffectiveAi()) {
                normalExplode(pHunter);
                for (int i = 0;i < 5;i++) {
                    Vec3 vec3 = pHunter.position();
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
                    List<LivingEntity> entities = pHunter.level().getEntitiesOfClass(LivingEntity.class, pHunter.getBoundingBox()
                            .move(vec4).inflate(4), entity -> MobUtils.canHurt(entity, pHunter));
                    ParticleUtil.explode(pHunter.serverLevel(), vec3);
                    ParticleUtil.sendParticles(pHunter.serverLevel(), ParticleTypes.LAVA, vec3, 12,
                            1, 1, 1, 1);
                    if (!entities.isEmpty()) {
                        for (LivingEntity living : entities) {
                            pHunter.setHealth(pHunter.getHealth() + 3);
                            living.setHealth(living.getHealth() - (living.getMaxHealth() / 5));
                            float headHunter$tick$10$BaseDamage = living.getMaxHealth() / 4f;
                            float headHunter$tick$10$DamageAmount = Math.max(headHunter$tick$10$BaseDamage, 10f);
                            living.hurt(pHunter.damageSources().indirectMagic(pHunter, pHunter),
                                    headHunter$tick$10$DamageAmount);
                        }
                    }
                }
            }
            if (pHunter.attackTickMoreThan(45))
                pHunter.resetState();
        }

        public static void circleAttack(NewHeadHunter pHunter) {
            pHunter.plusAttackTick();
            if (pHunter.attackTickEquals(10) || pHunter.attackTickEquals(20)
                || pHunter.attackTickEquals(23) || pHunter.attackTickEquals(26)
                || pHunter.attackTickEquals(30)) {
                attackSound(pHunter);
                List<LivingEntity> entities = pHunter.level().getEntitiesOfClass(LivingEntity.class,
                        pHunter.getBoundingBox().inflate(4, 3, 4), e->
                        MobUtils.canHurt(e, pHunter));
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities)
                    {
                        living.setHealth(living.getHealth() - living.getMaxHealth() / 10);
                        living.hurt(pHunter.damageSources().mobAttack(pHunter), 6F);
                    }
                }
            }
            if (pHunter.attackTickEquals(40)) {
                explodeSound(pHunter, 3.0F);
                if (!pHunter.level().isClientSide) {
                    ParticleUtil.explode(pHunter.serverLevel(), pHunter.position());
                    ParticleUtil.sendParticles(pHunter.serverLevel(), ParticleTypes.LAVA, pHunter.position(), 20,
                            1, 1, 1, 1);
                }
                areaAttack(pHunter, 10.0F, 2.0F, 30.0F, 0.1F,
                        4, pHunter.damageSources().mobAttack(pHunter), entity -> {
                            float f = entity.getMaxHealth() / 5;
                            if ((entity.getHealth() - f) >= 0)
                                entity.setHealth(entity.getHealth() - f);
                        });
            }
            if (pHunter.attackTickMoreThan(60)) {
                pHunter.resetState();
            }
        }

        public static void attack1(NewHeadHunter pHunter) {
            pHunter.plusAttackTick();
            if (pHunter.attackTickEquals(15)) {
                attackSound(pHunter);
                MobUtils.areaAttack(pHunter, 2, 2, 90,
                        12F, 0, 6, pHunter.damageSources().mobAttack(pHunter),
                        false, entity -> pHunter.heal(1F), false);
            }
            if (pHunter.attackTickEquals(30))
            {
                explodeSound(pHunter);
                areaAttack(pHunter, 3.0F, 2.0F, 5.0F, 0.1F,
                        4, pHunter.damageSources().mobAttack(pHunter), entity -> {
                            float f = entity.getMaxHealth() / 12;
                            if ((entity.getHealth() - f) >= 0)
                                entity.setHealth(entity.getHealth() - f);
                        });
            }
            if (pHunter.attackTickMoreThan(45))
                pHunter.resetState();
        }

        public static void onAvoid(NewHeadHunter pHunter) {
            int chance = pHunter.randomUtil.nextInt(3);
            Vec3 vec3;
            if (chance == 0) {
                vec3 = pHunter.getLookAngle();
                pHunter.setDeltaMovement(vec3.x() * -6, pHunter.getDeltaMovement().y(), vec3.z() * -6);
            } else if (chance == 1) {
                vec3 = Vec3.directionFromRotation(0, pHunter.getYRot() - 90)
                        .scale(1.5).multiply(3.2, 1, 3.2);
                pHunter.setDeltaMovement(vec3);
            } else {
                vec3 = Vec3.directionFromRotation(0, pHunter.getYRot() + 90)
                        .scale(1.5).multiply(3.2, 1, 3.2);
                pHunter.setDeltaMovement(vec3);
            }
        }

        public static void areaAttack(NewHeadHunter pHunter, float range, float height, float damage,
                                      int shield, @Nullable Consumer<LivingEntity> effect) {
            areaAttack(pHunter, range, height, damage, 0, shield, pHunter.damageSources().mobAttack(pHunter),
                    effect);
        }

        public static void areaAttack(NewHeadHunter pHunter, float range, float height, float damage, float hp,
                                      int shield, DamageSource source, @Nullable Consumer<LivingEntity> effect) {
            MobUtils.areaAttack(pHunter, range, height, 90F, damage, hp, shield, source, false,
                    effect, false);
        }

        public static void attackSound(Entity pEntity) {
            pEntity.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2, 1);
        }

        public static void normalExplode(Entity pEntity) {
            pEntity.playSound(SoundEvents.GENERIC_EXPLODE);
        }

        public static void explodeSound(Entity pEntity) {
            explodeSound(pEntity, 2.0F);
        }

        public static void explodeSound(Entity pEntity, float f) {
            pEntity.playSound(SoundEvents.GENERIC_EXPLODE, f, 0.2F);
        }

        public static List<LivingEntity> makeList(Entity pEntity, double range) {
            return makeList(pEntity, range, range, range);
        }

        public static List<LivingEntity> makeList(Entity pEntity, double x, double y, double z)
        {
            return pEntity.level().getEntitiesOfClass(LivingEntity.class, pEntity.getBoundingBox().inflate(x, y, z),
                    e->MobUtils.canHurt(e, pEntity));
        }
    }
}
