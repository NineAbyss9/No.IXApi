
package com.bilibili.player_ix.noixmod_api.entities.boss.priest;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiNihilisticBoss;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IShieldUser;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.github.NineAbyss9.ix_api.ix_api.util.UnmodifiableList;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.NoAttackMeleeGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.bilibili.player_ix.noixmod_api.util.EntitiesFinder;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public class Priest
extends SpellcasterNihilist
implements ApiNihilisticBoss, IFlagMob {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Integer> DATA_HURT_COOLDOWN;
    private static final EntityDataAccessor<Optional<UUID>> DATA_TARGET_UNIQUE_ID;
    private static final EntityDataAccessor<Integer> DATA_TARGET_ID;
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK;
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private final ServerBossEvent bossInfo;
    private final PriestData data;
    public AnimationState attack = new AnimationState();
    public AnimationState sweep = new AnimationState();
    public AnimationState attack1 = new AnimationState();
    public AnimationState attack2 = new AnimationState();
    public AnimationState thrust = new AnimationState();
    public AnimationState attack3 = new AnimationState();
    public AnimationState circle = new AnimationState();
    public AnimationState fate = new AnimationState();
    public AnimationState ranged = new AnimationState();
    private int priestDeathTime;
    public Priest(EntityType<Priest> type, Level world) {
        super(type, world);
        data = new PriestData(this);
        bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE,
                BossEvent.BossBarOverlay.PROGRESS);
        bossInfo.setDarkenScreen(true);
        bossInfo.setCreateWorldFog(true);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HURT_COOLDOWN, 0);
        this.entityData.define(DATA_TARGET_UNIQUE_ID, Optional.empty());
        this.entityData.define(DATA_TARGET_ID, -1);
        this.entityData.define(DATA_ATTACK_TICK, 0);
        this.entityData.define(DATA_FLAGS, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new NoAttackMeleeGoal(this, 1.0, Math.PI));
        OwnableMob.addBehaviorGoals(this, 6, 0.8, 10F, true, true);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Nihilist.class));
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void tick() {
        super.tick();
        if (!this.isLostTarget()) {
            this.getNavigation().moveTo(Objects.requireNonNull(this.getTarget()), 1.0);
            if (this.getFlag() == 0) {
                this.selectFlag();
            }
        }
        tickCooldown();
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide && data.isHalfHealth()) {
            this.clientLevel().addParticle(NoixmodAPIParticleTypes.DARK_SPELL.get(), this.getRandomX(
                    0.8), this.getRandomY(), this.getRandomZ(0.8), 0, 0, 0);
        }
        if (this.isFlag(0)) {
            return;
        }
        if (this.isFlag(1)) {
            plusAttackTick();
            if (this.getAttackTick() == 15) {
                sweepSound();
                AABB aabb = MobUtils.getRange(this, 2, 2, 0, 2, 2, 4, 2);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        living.hurt(this.damageSources().mobAttack(this), this.getAttackDamage());
                    }
                }
            }
            if (this.getAttackTick() > 30) {
                this.resetFlag();
                this.resetAttackTick();
                return;
            }
        }
        if (this.isFlag(2)) {
            plusAttackTick();
            if (this.getAttackTick() == 15) {
                sweepSound();
                AABB aabb = MobUtils.getRange(this, 3, 3, 0, 3, 3, 4, 3);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this), this.getAttackDamage()
                                + living.getMaxHealth() / 10, 3F);
                    }
                }
            }
            if (this.getAttackTick() > 35) {
                this.resetFlag();
                this.resetAttackTick();
                return;
            }
        }
        if (this.getFlag() == 3) {
            plusAttackTick();
            if (this.getAttackTick() == 10) {
                sweepSound();
                AABB aabb = MobUtils.getRange(this, 2, 2, 0, 2, 2, 4, 2);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this), this.getAttackDamage(),
                                0.25F);
                    }
                }
            }
            if (this.getAttackTick() == 30) {
                explodeSound();
                AABB aabb = MobUtils.getRange(this, 3, 3, 0, 3, 3, 4, 3);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this), 23
                                + living.getMaxHealth() / 8, this.getHealAmount(3.5F));
                    }
                }
                if (!this.level().isClientSide) {
                    ParticleUtil.darkCircle(this, 6, 0.3F);
                }
                /*for (int k=0;k !=2;k++) {
                    for (int j = 1;j != 3;j++) {
                        for (int i = 1;i != 3;i++) {
                            boolean flag = k == 1;
                            int l = flag ? -1 : 1;
                            BlockPos pos = this.blockPosition().offset(j * l, 1, i * l);
                            FallingBlockEntity fallingBlock =
                                    FallingBlockEntity.fall(this.level(), pos, this.getBlockStateOn());
                            fallingBlock.setDeltaMovement(0, 0.3, 0);
                            fallingBlock.dropItem = false;
                            fallingBlock.disableDrop();
                            fallingBlock.setHurtsEntities(5, 10);
                        }
                    }
                }*/
            }
            if (this.getAttackTick() > 45) {
                this.resetFlag();
                resetAttackTick();
                return;
            }
        }
        if (this.getFlag() == 4) {
            plusAttackTick();
            if (this.getAttackTick() == 10) {
                sweepSound();
                AABB aabb = MobUtils.getRange(this, 3, 0, 3, 3, 3, 4, 3);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.getAttackDamage());
                    }
                }
            }
            if (this.getAttackTick() > 30) {
                this.resetFlag();
                this.resetAttackTick();
                return;
            }
        }
        if (this.getFlag() == 5) {
            plusAttackTick();
            if (this.getAttackTick() == 10) {
                attack();
                sweepSound();
                AABB aabb = MobUtils.getRange(this, 3, 3, 0, 3, 3, 4, 3);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this),
                                this.getAttackDamage(), this.getHealAmount(1F));
                    }
                }
            }
            if (this.getAttackTick() > 30) {
                this.resetFlag();
                this.resetAttackTick();
                return;
            }
        }
        if (this.getFlag() == 6) {
            plusAttackTick();
            if (this.getAttackTick() == 10) {
                this.teleport();
                this.setDeltaMovement(this.getDeltaMovement().add(0, -0.3, 0));
            }
            if (this.getAttackTick() == 15) {
                this.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR);
                AABB aabb = this.getBoundingBox().inflate(2, 0.5, 2);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().magic(), this.getAttackDamage()
                                + living.getMaxHealth() / 10, 2F);
                    }
                }
                if (!this.level().isClientSide) {
                    ParticleUtil.sendParticles(this.serverLevel(), NoixmodAPIParticleTypes.BLACK_CLOUD.get(),
                            position(), 40, 2, 0.3, 2, 0);
                }
            }
            if (this.getAttackTick() > 35) {
                this.resetFlag();
                this.resetAttackTick();
                return;
            }
        }
        if (this.getFlag() == 7) {
            plusAttackTick();
            if (this.getAttackTick() == 10) {
                sweepSound();
                AABB aabb = this.getBoundingBox().inflate(4, 4, 4);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this), this.getAttackDamage()
                                + living.getMaxHealth() / 10, 1F);
                    }
                }
            }
            if (this.getAttackTick() == 15) {
                sweepSound();
                AABB aabb = this.getBoundingBox().inflate(4, 4, 4);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this), this.getAttackDamage()
                                + living.getMaxHealth() / 10, this.getHealAmount(1F));
                    }
                }
            }
            if (this.getAttackTick() == 20) {
                sweepSound();
                AABB aabb = this.getBoundingBox().inflate(4, 4, 4);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this), this.getAttackDamage()
                                + living.getMaxHealth() / 10, this.getHealAmount(1.2F));
                    }
                }
            }
            if (this.getAttackTick() == 25) {
                sweepSound();
                AABB aabb = this.getBoundingBox().inflate(4, 4, 4);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this), this.getAttackDamage()
                                + living.getMaxHealth() / 10, 1F);
                    }
                }
            }
            if (this.getAttackTick() == 30) {
                sweepSound();
                AABB aabb = this.getBoundingBox().inflate(4, 4, 4);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this), this.getAttackDamage()
                                + living.getMaxHealth() / 10, 1F);
                    }
                }
            }
            if (this.getAttackTick() > 45) {
                this.resetFlag();
                this.resetAttackTick();
                return;
            }
        }
        if (this.getFlag() == 8) {
            plusAttackTick();
            if (this.level().isClientSide) {
                double x = this.getX() + Maths.randomInt(5);
                double y = this.getY();
                double z = this.getZ() + Maths.randomInt(5);
                this.clientLevel().addParticle(NoixmodAPIParticleTypes.DARK_SPELL.get(), x, y, z,
                        0, 0, 0);
            } else if (this.tickCount % 10 == 0) {
                ParticleUtil.darkCircle(this, 1.0F);
            }
            List<LivingEntity> list = this.makeList(this.getBoundingBox().inflate(6));
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size();i++) {
                    this.doHeal(0.5F);
                }
            }
            if (this.getAttackTick() == 30) {
                totemSound();
                AABB aabb = this.getBoundingBox().inflate(6);
                List<LivingEntity> entities = this.makeList(aabb);
                if (!entities.isEmpty()) {
                    for (LivingEntity living : entities) {
                        this.doAttack(living, this.damageSources().mobAttack(this), this.getAttackDamage()
                                + living.getMaxHealth() / 2, 20F);
                    }
                }
                if (!this.level().isClientSide) {
                    ParticleUtil.darkCircle(this);
                    ParticleUtil.sendParticles(this.serverLevel(), NoixmodAPIParticleTypes.DARK_SPELL.get(), position(),
                            15, 1, 0.2, 1, 0);
                }
            }
            if (this.getAttackTick() > 45) {
                resetFlag();
                resetAttackTick();
            }
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isLostTarget() && this.tickCount % 15 == 0) {
            this.doHeal(1.0F);
        }
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                switch (this.getFlag()) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        stopAllAnimations();
                        this.attack.startIfStopped(tickCount);
                        break;
                    }
                    case 2: {
                        stopAllAnimations();
                        this.sweep.startIfStopped(tickCount);
                        break;
                    }
                    case 3: {
                        stopAllAnimations();
                        this.attack1.startIfStopped(tickCount);
                        break;
                    }
                    case 4: {
                        stopAllAnimations();
                        this.attack2.startIfStopped(tickCount);
                        break;
                    }
                    case 5: {
                        stopAllAnimations();
                        this.thrust.startIfStopped(tickCount);
                        break;
                    }
                    case 6: {
                        stopAllAnimations();
                        this.attack3.startIfStopped(tickCount);
                        break;
                    }
                    case 7: {
                        stopAllAnimations();
                        this.circle.startIfStopped(tickCount);
                        break;
                    }
                    case 8: {
                        stopAllAnimations();
                        this.fate.startIfStopped(tickCount);
                        break;
                    }
                    case 9: {
                        stopAllAnimations();
                        this.ranged.startIfStopped(tickCount);
                        break;
                    }
                    default: {
                        LOGGER.warn("Can't handle synched event in Priest.class");
                        break;
                    }
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.getHurtCooldown() <= 0)
            return super.hurt(pSource, pAmount);
        else
            return false;
    }

    protected void actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
        if (this.getHurtCooldown() <= 0) {
            float trueAmount = Math.min(15F, pDamageAmount);
            this.setHurtCooldown(15);
            super.actuallyHurt(pDamageSource, trueAmount);
        }
    }

    public boolean isInvulnerableTo(DamageSource pSource) {
        if (pSource.is(DamageTypeTags.IS_FALL))
            return true;
        if (pSource.is(DamageTypeTags.IS_FIRE))
            return true;
        if (pSource.is(DamageTypes.IN_WALL))
            return true;
        if (pSource.is(DamageTypes.FALLING_ANVIL) || pSource.is(DamageTypes.FALLING_BLOCK))
            return true;
        return super.isInvulnerableTo(pSource);
    }

    private void selectFlag() {
        Random randomUtil = this.getRandomUtil();
        int i = randomUtil.nextInt(26);
        if (i < 10 && this.data.canAttack()) {
            if (i < 2) {
                this.setFlag(1);
            } else if (i < 4) {
                this.setFlag(2);
            } else if (i < 8) {
                this.setFlag(3);
            } else {
                this.setFlag(4);
            }
        } else if (i < 12) {
            this.setFlag(5);
        } else if (i < 16) {
            this.setFlag(6);
        } else if (i < 20) {
            this.setFlag(7);
        } else {
            if (data.isHalfHealth())
                this.setFlag(8);
            else {
                if (randomUtil.nextBoolean())
                    this.setFlag(7);
                else
                    this.setFlag(6);
            }
        }
        /* else {
            this.setFlag(9);
        }*/
    }

    private void doHeal(float healAmount) {
        float trueAmount = Mth.clamp(healAmount, 0.0F, Float.MAX_VALUE);
        this.setHealth(this.getHealth() + trueAmount);
    }

    private void doAttack(LivingEntity entity, float damage) {
        this.doAttack(entity, this.damageSources().mobAttack(this), damage, 0.0F, false);
    }

    private void doAttack(LivingEntity entity, DamageSource source, float damage, float healAmount) {
        this.doAttack(entity, source, damage, healAmount, true);
    }

    private void doAttack(LivingEntity entity, DamageSource source, float damage, float healAmount, boolean bypassShield) {
        if (!bypassShield && entity.isBlocking()) {
            IShieldUser.hurtShield(entity, 12);
            return;
        }
        if (entity instanceof Player)
            entity.hurt(source, damage);
        else {
            if (!MobUtils.actuallyHurt(entity, source, damage)) {
                entity.hurt(source, damage);
            }
        }
        this.doHeal(healAmount);
    }

    private List<LivingEntity> makeList(AABB aabb) {
        return this.level().getEntitiesOfClass(LivingEntity.class, aabb, living -> MobUtils.canHurt(living,
                this));
    }

    public int getAttackTick() {
        return this.entityData.get(DATA_ATTACK_TICK);
    }

    public void setAttackTick(int tick) {
        this.entityData.set(DATA_ATTACK_TICK, tick);
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int flag) {
        this.entityData.set(DATA_FLAGS, flag);
    }

    @Nullable
    public LivingEntity getTarget() {
        if (this.level().isClientSide) {
            return EntitiesFinder.getLivingEntity(this.level(), this.getTargetId());
        }
        return EntitiesFinder.getLivingEntity(this.level(), this.getTargetUuid());
    }

    public void setTarget(@Nullable LivingEntity pTarget) {
        if (pTarget != null) {
            this.setTargetUuid(pTarget.getUUID());
            this.setTargetId(pTarget.getId());
        } else {
            this.setTargetUuid(null);
            this.setTargetId(-1);
        }
    }

    @Nullable
    public UUID getTargetUuid() {
        return this.entityData.get(DATA_TARGET_UNIQUE_ID).orElse(null);
    }

    public void setTargetUuid(@Nullable UUID uuid) {
        this.entityData.set(DATA_TARGET_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    public int getTargetId() {
        return this.entityData.get(DATA_TARGET_ID);
    }

    public void setTargetId(int id) {
        this.entityData.set(DATA_TARGET_ID, id);
    }

    private UnmodifiableList<AnimationState> allAnimations() {
        return UnmodifiableList.of(attack, sweep, attack1, attack2, thrust, attack3, circle, fate, ranged);
    }

    private void stopAllAnimations() {
        for (AnimationState state : allAnimations()) {
            state.stop();
        }
    }

    @Nullable
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return 2.55F;
    }

    public void startSeenByPlayer(ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);
        this.bossInfo.addPlayer(pServerPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);
        this.bossInfo.removePlayer(pServerPlayer);
    }

    protected void tickDeath() {
        ++this.priestDeathTime;
        if (this.priestDeathTime > 20) {
            this.remove(RemovalReason.KILLED);
        }
    }

    private int getHurtCooldown() {
        return this.entityData.get(DATA_HURT_COOLDOWN);
    }

    private void setHurtCooldown(int hurtCooldown) {
        this.entityData.set(DATA_HURT_COOLDOWN, hurtCooldown);
    }

    private void tickCooldown() {
        if (this.getHurtCooldown() > 0) {
            this.setHurtCooldown(this.getHurtCooldown() - 1);
        }
    }

    private float getAttackDamage() {
        return 20.0F;
    }

    private void sweepSound() {
        this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1f, 1f);
    }

    private void explodeSound() {
        this.playSound(SoundEvents.GENERIC_EXPLODE, 2f, 1.0F);
    }

    private void totemSound() {
        this.playSound(SoundEvents.TOTEM_USE);
    }

    private void attack() {
        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 vec3 = this.getLookAngle();
        this.setDeltaMovement(deltaMovement.add(vec3.x * 3, deltaMovement.y, vec3.z * 3));
    }

    private void teleport() {
        LivingEntity entity = this.getTarget();
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)46);
        }
        if (entity != null)
            this.teleportTo(entity.getX(), entity.getY() + 3, entity.getZ());
    }

    private boolean isLostTarget() {
        return this.getTarget() == null;
    }

    private float getHealAmount(float base) {
        return this.data.isHalfHealth() ? base * 2.0F : base;
    }

    public boolean isPersistenceRequired() {
        return true;
    }

    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    public void checkDespawn() {
    }

    public void push(double pX, double pY, double pZ) {
        super.push(pX / 3, pY / 3, pZ / 3);
    }

    public boolean killedEntity(ServerLevel pLevel, LivingEntity pEntity) {
        this.doHeal(5F);
        return super.killedEntity(pLevel, pEntity);
    }

    public boolean canBeAffected(MobEffectInstance pEffectInstance) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return NoixmodAPISounds.APOSTLE_IDLE.get();
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SCULK_CATALYST_BREAK;
    }

    public float getSpeed() {
        if (this.getFlag() == 8) {
            return 0.0f;
        }
        return super.getSpeed();
    }

    public static AttributeSupplier createAttributes() {
        return createPathAttributes().add(Attributes.ATTACK_DAMAGE, 22.0)
                .add(Attributes.FOLLOW_RANGE, 72).add(Attributes.ARMOR, 10)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75).add(Attributes.MAX_HEALTH, 420)
                .add(Attributes.MOVEMENT_SPEED, 0.3).build();
    }

    static {
        DATA_HURT_COOLDOWN = SynchedEntityData.defineId(Priest.class, EntityDataSerializers.INT);
        DATA_TARGET_UNIQUE_ID = SynchedEntityData.defineId(Priest.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_TARGET_ID = SynchedEntityData.defineId(Priest.class, EntityDataSerializers.INT);
        DATA_ATTACK_TICK = SynchedEntityData.defineId(Priest.class, EntityDataSerializers.INT);
        DATA_FLAGS = SynchedEntityData.defineId(Priest.class, EntityDataSerializers.INT);
    }
}
