
package com.bilibili.player_ix.noixmod_api.entities.boss.star_guardian;

import com.github.NineAbyss9.ix_api.api.annotation.ServerOnly;
import com.github.NineAbyss9.ix_api.api.mobs.ApiNihilisticBoss;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.github.NineAbyss9.ix_api.util.UnmodifiableList;
import com.bilibili.player_ix.noixmod_api.api.entity.IX;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticFireball;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticServant;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.bilibili.player_ix.noixmod_api.register.*;
import com.bilibili.player_ix.noixmod_api.util.EntitiesFinder;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.NineAbyss9.math.AbyssMath;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**转（星之守护者）*/
public class StarGuardian
extends SpellcasterNihilist
implements ApiNihilisticBoss, IX, IFlagMob {
    private final ServerBossEvent bossEvent;
    public final OwnerSummon ownerSummonUtil = new OwnerSummon(this);
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TICK;
    private static final EntityDataAccessor<Integer> GUARDIAN_FLAG;
    private static final EntityDataAccessor<Integer> PHASE;
    private static final EntityDataAccessor<Integer> DATA_TARGET_ID;
    private static final EntityDataAccessor<Optional<UUID>> DATA_TARGET_UUID;
    private static final EntityDataAccessor<Integer> DATA_SHIELD_TICK;
    private int deathTick = 0;
    private int summonCooldown = Maths.toTick(30);
    private int cooldown = 0;
    private int hurtCooldown = 0;
    private int hurtCount = 0;
    private float power = 1F;
    public int setHurtCooldown;
    Vec3 finalVec = position();
    public AnimationState attack = new AnimationState();
    public AnimationState avoid = new AnimationState();
    public AnimationState die = new AnimationState();
    public AnimationState sweep = new AnimationState();
    public AnimationState summon = new AnimationState();
    public AnimationState teleportAttack = new AnimationState();
    public AnimationState trust = new AnimationState();
    public AnimationState sweep1 = new AnimationState();
    /**@deprecated */
    @Deprecated
    public AnimationState sweep2 = new AnimationState();
    public AnimationState attack1 = new AnimationState();
    public AnimationState ground = new AnimationState();
    public AnimationState trust1 = new AnimationState();
    public StarGuardian(EntityType<StarGuardian> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.setLeftHanded(false);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NoixmodAPIItems.STAR_SWORD.get()));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GUARDIAN_FLAG, 0);
        this.entityData.define(PHASE, 1);
        this.entityData.define(DATA_ATTACK_TICK, 0);
        this.entityData.define(DATA_TARGET_ID, -1);
        this.entityData.define(DATA_TARGET_UUID, Optional.empty());
        this.entityData.define(DATA_SHIELD_TICK, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new com.github.NineAbyss9.ix_api.api.mobs.ai.goal.MeleeGoal(this, 0.7D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 12F));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, Nihilist.class).setAlertOthers());
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Flag", this.getFlag());
        tag.putInt("Phase", this.getPhase());
        tag.putFloat("Power", this.getPower());
        tag.putInt("ShieldTick", this.getShieldTick());
        tag.putInt("AttackTick", this.getAniTick());
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Phase"))
            this.setPhase(tag.getInt("Phase"));
        if (tag.contains("Power"))
            this.setPower(tag.getFloat("Power"));
        super.readAdditionalSaveData(tag);
    }

    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        List<Mob> targets = this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(39),
                mob -> mob.getTarget() == this && MobUtils.canHurt(mob, this));
        if (!targets.isEmpty()) {
            for (Mob mob : targets) {
                if (this.getTarget() == null) {
                    this.setTarget(mob);
                    break;
                }
            }
        }
        if (this.needChangePhase()) {
            this.setPhase(2);
            this.setHealth(this.getMaxHealth());
            this.setPowerPlus();
        }
        if (this.level().isClientSide) {
            if (this.getRandomUtil().nextBoolean()) {
                this.level().addParticle(ParticleTypes.END_ROD, this.getRandomX(0.8), this.getRandomY(),
                        this.getRandomZ(0.8), 0, 0, 0);
                if (this.getShieldTick() > 0) {
                    this.level().addParticle(ParticleTypes.GLOW_SQUID_INK, this.getRandomX(0.8), this.getRandomY(),
                            this.getRandomZ(0.8), 0, 0, 0);
                }
            }
        }
        if (this.summonCooldown <= 0) {
            NihilisticServant servant = new NihilisticServant(NoixmodAPIEntities.NIHILISTIC_SERVANT.get(), this.level());
            this.ownerSummonUtil.integerSummon(servant, 4);
            this.summonCooldown = Maths.toTick(30);
            if (!this.level().isClientSide) {
                ParticleUtil.sendParticles((ServerLevel)this.level(), ParticleTypes.LARGE_SMOKE, servant.position(),
                        15, 1, 0, 1, 0);
            }
        }
        int chance = this.getRandomUtil().nextInt(31);
        if (target != null) {
            if (this.cooldown <= 0 && this.isFlag(0)) {
                if (chance < 5 && this.canAttack()) {
                    this.setFlag(1);
                } else if (chance < 6 && this.closerThan(target, 3)) {
                    this.setFlag(3);
                } else if (chance < 9 && this.canAttack()) {
                    this.setFlag(5);
                } else if (chance < 12) {
                    this.setFlag(6);
                } else if (chance < 15 && this.canAttack()) {
                    this.setFlag(9);
                } else if (chance < 18) {
                    this.setFlag(8);
                } else if (chance < 22) {
                    setFlag(12);
                } else if (chance < 25 && canAttack()) {
                    this.setFlag(10);
                } else if (chance < 28 && canAttack()) {
                    this.setFlag(11);
                } else {
                    this.setFlag(7);
                }
            }
        }
        if (this.isFlag(0)) return;
        if (this.isFlag(1)) {
            increaseAniTick();
            if (this.aniTickEquals(10)) {
                sweepSound();
                MobUtils.areaAttack(this, 3F, 3F, 90F, this.damageSources()
                                .mobAttack(this), this.getAttackDamage(0.5F));
                this.doHeal();
            }
            if (this.aniTick(20)) {
                this.resetState();
            }
        } else if (this.isFlag(3)) {
            increaseAniTick();
            if (this.aniTickEquals(1)) {
                this.avoid();
            }
            if (this.aniTick(25)) {
                this.resetState();
            }
        } else if (this.isFlag(5)) {
            increaseAniTick();
            if (this.aniTickEquals(10)) {
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(
                        2, 0.2, 2
                ), living -> MobUtils.canHurt(living, this));
                this.playSound(SoundEvents.GENERIC_EXPLODE, 2, 1);
                if (this.isServerSide()) {
                    ParticleUtil.sendParticles((ServerLevel)this.level(), new BlockParticleOption(ParticleTypes.BLOCK,
                                    this.getBlockStateOn()), this.position(), 30, 1.5, 0, 1.5, 0);
                }
                if (!list.isEmpty()) {
                    for (LivingEntity living : list) {
                        MobUtils.disableShield(1, 1, 1, living);
                        living.hurt(this.damageSources().mobAttack(this), this.getAttackDamage(5.0F));
                    }
                }
            }
            if (this.aniTickEquals(15)) {
                List<LivingEntity> list = this.makeAttackList(4);
                if (!list.isEmpty()) {
                    for (LivingEntity living : list) {
                        MobUtils.disableShield(1, 1, 1, living);
                        if (!this.level().isClientSide) {
                            ParticleUtil.sendParticles((ServerLevel)this.level(), new BlockParticleOption(ParticleTypes.BLOCK,
                                            living.getFeetBlockState()), living.position(), 30, 1.5, 0, 1.5, 0);
                        }
                        living.hurt(this.damageSources().starve(), this.getAttackDamage(25.0F));
                    }
                }
            }
            if (this.aniTick(25)) {
                this.resetState();
            }
        } else if (this.getFlag() == 6) {
            increaseAniTick();
            if (this.aniTickEquals(10)) {
                for (int i = 0;i<4;++i) {
                    int n = Maths.randomInt(2);
                    int t = Maths.randomInt(2);
                    if (target != null) {
                        double d1 = ownerSummonUtil.projectileDouble(target)[0];
                        double d2 = ownerSummonUtil.projectileDouble(target)[1];
                        double d3 = ownerSummonUtil.projectileDouble(target)[2];
                        NihilisticFireball fireBall = new NihilisticFireball(this.level(), this, d1, d2, d3);
                        BlockPos pos = this.blockPosition().offset(n, 4, t).mutable();
                        fireBall.setDamage(12f + this.randomUtil.nextFloat() * 3);
                        fireBall.moveTo(pos.getX(), pos.getY() + i, pos.getZ());
                        fireBall.setPosRaw(fireBall.getX(), this.getY(0.5) + 0.5, fireBall.getZ());
                        fireBall.setOwner(this);
                        fireBall.setRadius(4D);
                        this.level().addFreshEntity(fireBall);
                    }
                }
            }
            if (this.aniTick(45)) {
                this.resetState();
            }
        } else if (this.isFlag(7)) {
            increaseAniTick();
            if (target != null) {
                GuardianTeleportAttack.trigger(this, target);
            }
            if (this.aniTick(80)) {
                this.resetState();
            }
        } else if (this.isFlag(8)) {
            increaseAniTick();
            if (this.aniTickEquals(15)) {
                trust();
                this.doTrust();
            }
            if (this.aniTickEquals(20) || this.aniTickEquals(25)) {
                this.doTrust();
            }
            if (this.aniTick(25)) {
                this.resetState();
            }
        } else if (this.isFlag(9)) {
            increaseAniTick();
            if (this.aniTickEquals(10)) {
                sweepSound();
                MobUtils.areaAttack(this, 3F, 3F, 90F, 20F + this.getPower() * 2F,
                        0, 0, this.damageSources().mobAttack(this), false,
                        pEntity -> {
                    this.doHeal();
                    this.setPowerPlus();
                }, false);
            }
            if (this.aniTick(25)) {
                this.resetState();
            }
        } else if (isAttack1()) {
            increaseAniTick();
            if (this.aniTickEquals(10)) {
                sweepSound();
                MobUtils.areaAttack(this, 3F, 3F, 90F, this.damageSources()
                        .mobAttack(this), this.getAttackDamage(0.5F));
                this.doHeal();
            }
            if (this.aniTick(35)) {
                this.resetState();
            }
        } else if (isGround()) {
            increaseAniTick();
            if (this.aniTickEquals(15)) {
                groundSound();
                if (!level().isClientSide) {
                    List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(6, 0.5,
                                    6), e -> MobUtils.canHurt(e, this));
                    if (!entities.isEmpty())
                        entities.forEach(e -> {
                            e.hurt(damageSources().magic(), 12F);
                            setPowerPlus();
                        });
                    ISpell spell = Spells.CRACK.get();
                    spell.castSpell(serverLevel(), this);
                }
            }
            if (this.aniTick(40)) {
                resetState();
            }
        } else if (isTrust1()) {
            increaseAniTick();
            if (aniTickEquals(15)) {
                sweepSound();
                trust();
            }
            if (aniTickEquals(15) || aniTickEquals(20)) {
                MobUtils.areaAttack(this, 3F, 3F, 90F, this.getAttackDamage(4F),
                        0, 0, this.damageSources().mobAttack(this), false,
                        pEntity -> {
                            this.doHeal();
                            this.setPowerPlus();
                        }, false);
            }
            if (aniTick(45)) {
                resetState();
            }
        }
    }

    public void aiStep() {
        super.aiStep();
        if (!this.followers().isEmpty()) {
            if (this.tickCount % 50 == 0 && this.isServerSide()) {
                this.heal(this.getMaxHealth() / 150);
            }
        }
        if (this.tickCount % 100 == 0 && this.getPower() > 1F) {
            --this.power;
        }
        if (this.isLeftHanded()) {
            this.setLeftHanded(false);
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        if (this.summonCooldown > 0) {
            --this.summonCooldown;
        }
        if (this.hurtCooldown > 0) {
            --this.hurtCooldown;
        }
        if (this.getShieldTick() > 0) {
            this.setShieldTick(this.getShieldTick() - 1);
        }
        if (this.cooldown > 0) {
            --this.cooldown;
        }
        if (this.setHurtCooldown > 0) {
            this.setHurtCooldown--;
        }
    }

    public float getSpeed() {
        if (this.immobile()) {
            return 0.0F;
        }
        return super.getSpeed();
    }

    private boolean immobile() {
        return this.isFlag(7) || this.isFlag(9) || isGround();
    }

    public boolean isInvulnerable() {
        return super.isInvulnerable() || this.isInvisible() && this.isFlag(7);
    }

    protected void tickDeath() {
        ++this.deathTick;
        if (this.deathTick == 1) {
            this.setFlag(4);
        }
        if (this.deathTick > 20) {
            if (this.level() instanceof ServerLevel level) {
                level.sendParticles(NoixmodAPIParticleTypes.DARK_SPELL.get(), this.getX(), this.getY(), this.getZ(),
                        30, 0.5, 1, 0.5, 0);
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    public boolean killedEntity(ServerLevel pLevel, LivingEntity pEntity) {
        this.heal(1f + this.getPower() + pEntity.getMaxHealth() / 40);
        return super.killedEntity(pLevel, pEntity);
    }

    public void die(DamageSource pDamageSource) {
        if (!ForgeHooks.onLivingDeath(this, pDamageSource)) {
            if (!this.dead && !this.isRemoved()) {
                Entity entity = pDamageSource.getEntity();
                LivingEntity livingentity = this.getKillCredit();
                if (this.deathScore >= 0 && livingentity != null) {
                    livingentity.awardKillScore(this, this.deathScore, pDamageSource);
                }
                this.getCombatTracker().recheckStatus();
                Level level = this.level();
                if (!this.level().isClientSide) {
                    ServerLevel serverlevel = (ServerLevel) this.level();
                    if (entity == null || entity.killedEntity(serverlevel, this)) {
                        ExperienceOrb.award(serverlevel, this.position(), this.getExperienceReward());
                        this.spawnAtLocation(NoixmodAPIItems.STAR_SWORD.get());
                    }
                }
                level.broadcastEntityEvent(this, AbyssMath.toByte(3));
            }
        }
    }

    public boolean isInvisible() {
        if (this.isFlag(7)) {
            return this.getAniTick() >= 10 && this.getAniTick() < 15
                    || this.getAniTick() >= 20 && this.getAniTick() < 25
                    || this.getAniTick() >= 30 && this.getAniTick() < 35
                    || this.getAniTick() >= 40 && this.getAniTick() < 45;
        }
        return super.isInvisible();
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PLAYER_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    public boolean isInvulnerableTo(DamageSource pSource) {
        if (pSource.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return true;
        }
        if (pSource.is(DamageTypeTags.IS_FREEZING)) {
            return true;
        }
        if (this.isFlag(3)) {
            return true;
        }
        return super.isInvulnerableTo(pSource);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        float var0 = Math.min(pAmount, 11f);
        if (this.hurtCooldown > 0 || this.getShieldTick() > 0) {
            return false;
        } else {
            return super.hurt(pSource, var0);
        }
    }

    protected float getDamageAfterMagicAbsorb(DamageSource source, float v) {
        if (source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            v *= 0.25F;
        }
        return super.getDamageAfterMagicAbsorb(source, v);
    }

    protected void actuallyHurt(DamageSource source, float pDamageAmount) {
        float var = Math.min(pDamageAmount, 11f) - (this.getPower() / 3);
        if (this.getShieldTick() > 0) {
            return;
        }
        if (this.hurtCount >= 4) {
            this.hurtCount = 0;
        }
        if (this.hurtCooldown <= 0) {
            this.hurtCooldown = 10;
        }
        if (pDamageAmount > 3F && this.getPower() > 1F) {
            this.setPower(this.getPower() - 0.25F);
        }
        ++this.hurtCount;
        super.actuallyHurt(source, var);
    }

    public void setHealth(float pHealth) {
        float delta = pHealth - this.getHealth() ;
        if (delta < 0) {
            if (this.setHurtCooldown > 0) {
                return;
            }
            this.setHurtCooldown = 10;
            if (delta < -12.5f) {
                pHealth = this.getHealth() - 12.5f;
            }
        }
        super.setHealth(pHealth);
    }

    public boolean isFullyFrozen() {
        return false;
    }

    public void setTicksFrozen(int pTicksFrozen) {}

    public void push(Entity pEntity) {
        if (this.hurtCooldown <= 0) {
            super.push(pEntity);
        }
    }

    public void push(double pX, double pY, double pZ) {
        if (this.hurtCooldown <= 0) {
            super.push(pX, pY, pZ);
        }
    }

    public void startSeenByPlayer(ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);
        this.bossEvent.addPlayer(pServerPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);
        this.bossEvent.removePlayer(pServerPlayer);
    }

    public void heal(float var1) {
        MobUtils.healLiving(this, var1);
    }

    public boolean addEffect(MobEffectInstance instance, @Nullable Entity pEntity) {
        return NoixmodAPITags.CAN_EFFECT_APOSTLE.test(instance.getEffect()) &&
                super.addEffect(instance, pEntity);
    }

    public void forceAddEffect(MobEffectInstance pInstance, @Nullable Entity pEntity) {
        if (NoixmodAPITags.CAN_EFFECT_APOSTLE.test(pInstance.getEffect())) {
            super.forceAddEffect(pInstance, pEntity);
        }
    }

    protected void tickEffects() {
        this.getActiveEffects().removeIf(NoixmodAPITags.CAN_NOT_EFFECT_APOSTLE);super.tickEffects();}

    protected void onEffectAdded(MobEffectInstance instance, @Nullable Entity pEntity) {
        if (NoixmodAPITags.CAN_EFFECT_APOSTLE.test(instance.getEffect())) {
            super.onEffectAdded(instance, pEntity);
        }
    }

    public boolean startRiding(Entity p_21396_, boolean p_21397_) {return false;}

    public boolean startRiding(Entity p_20330_) {return false;}

    protected boolean canRide(Entity p_20339_) {return false;}

    public final boolean removeWhenFarAway(double pDistanceToClosestPlayer) {return false;}

    public void checkDespawn() {
    }

    public void cooldownPlayerShield(Player player) {
        player.disableShield(this.getRandomUtil().nextFloat()
                >= 0.7F);
    }

    public void avoid() {
        this.setDeltaMovement(this.getLookAngle().x * -5, this.getDeltaMovement().y,
                this.getLookAngle().z * -5);
    }

    public int getShieldTick() {
        return this.entityData.get(DATA_SHIELD_TICK);
    }

    public void setShieldTick(int tick) {
        this.entityData.set(DATA_SHIELD_TICK, tick);
    }

    public float getPower() {
        return this.power;
    }

    public void setPower(float pPower) {
        this.power = Math.max(pPower, power);
    }

    public void setPowerPlus() {
        if (this.getPower() < 1F) {
            this.setPower(1F);
        }
        this.setPower(this.getPower() + 1);
    }

    public Integer getPhase() {
        return this.entityData.get(PHASE);
    }

    public void setPhase(Integer phase) {
        this.entityData.set(PHASE, Mth.clamp(phase, 1, 2));
    }

    public boolean isSecondPhase() {
        return this.getPhase() == 2;
    }

    public int getFlag() {
        return this.entityData.get(GUARDIAN_FLAG);
    }

    public void setFlag(int flag) {
        if (flag == 0 && this.getFlag() != 3) {
            this.cooldown = 20;
        }
        this.entityData.set(GUARDIAN_FLAG, flag);
    }

    public int getAniTick() {
        return this.entityData.get(DATA_ATTACK_TICK);
    }

    public void setAniTick(int attackTick) {
        this.entityData.set(DATA_ATTACK_TICK, attackTick);
    }

    public boolean needChangePhase() {
        if (this.isSecondPhase()) {
            return false;
        }
        return this.getHealth() <= this.getMaxHealth() / 3;
    }

    @ServerOnly
    public void spreadStarParticles(Entity pEntity) {
        ((ServerLevel)this.level()).sendParticles(ParticleTypes.END_ROD, pEntity.getX(), pEntity.getY(),
                pEntity.getZ(), 20, 1.5, 2, 1.5, 0);
    }

    public Entity ixSelf() {
        return this;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (GUARDIAN_FLAG.equals(pKey)) {
            if (this.level().isClientSide()) {
                switch (this.getFlag()) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        this.stopAllAnimations();
                        this.attack.startIfStopped(tickCount);
                        break;
                    }
                    case 3: {
                        this.stopAllAnimations();
                        this.avoid.startIfStopped(tickCount);
                        break;
                    }
                    case 4: {
                        this.stopAllAnimations();
                        this.die.start(tickCount);
                        break;
                    }
                    case 5: {
                        this.stopAllAnimations();
                        this.sweep.startIfStopped(tickCount);
                        break;
                    }
                    case 6: {
                        this.stopAllAnimations();
                        this.summon.startIfStopped(tickCount);
                        break;
                    }
                    case 7: {
                        this.stopAllAnimations();
                        this.teleportAttack.startIfStopped(tickCount);
                        break;
                    }
                    case 8: {
                        this.stopAllAnimations();
                        this.trust.startIfStopped(tickCount);
                        break;
                    }
                    case 9: {
                        this.stopAllAnimations();
                        this.sweep1.startIfStopped(tickCount);
                        break;
                    }
                    case 10: {
                        stopAllAnimations();
                        attack1.startIfStopped(tickCount);
                        break;
                    }
                    case 11: {
                        stopAllAnimations();
                        ground.startIfStopped(tickCount);
                        break;
                    }
                    case 12: {
                        stopAllAnimations();
                        trust1.startIfStopped(tickCount);
                        break;
                    }
                    default: {
                        LOGGER.warn("Can't handle SynchedEvent in {}.", this.getClass());
                        this.setFlag(0);
                        break;
                    }
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    private List<AnimationState> allAnis() {
        return UnmodifiableList.of(attack, avoid, die, summon, sweep, teleportAttack, trust, sweep1, attack1,
                ground, trust1);
    }

    public void stopAllAnimations() {
        for (AnimationState state : allAnis()) {
            state.stop();
        }
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    void doHeal() {
        float healAmount;
        if (this.isSecondPhase()) {
            healAmount = this.getMaxHealth() / 150 + this.getPower();
        } else {
            healAmount = this.getMaxHealth() / 150;
        }
        this.heal(healAmount);
    }

    public boolean doHurtTarget(Entity pEntity) {
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) + (this.getPower() * 2) +
                (this.getPhase() == 2 ? 3 : 0);
        float f1 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        this.setPowerPlus();
        float healAmount;
        if (this.isSecondPhase()) {
            healAmount = this.getMaxHealth() / 150 + this.getPower();
        } else {
            healAmount = this.getMaxHealth() / 150;
        }
        this.heal(healAmount);
        if (this.getFlag() != 5 && this.getFlag() != 6) {
            this.setFlag(1);
        }
        if (this.level() instanceof ServerLevel && this.isSecondPhase()) {
            this.spreadStarParticles(pEntity);
        }
        if (pEntity instanceof LivingEntity living) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), living.getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);
        }
        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            pEntity.setSecondsOnFire(i * 4);
        }
        boolean flag = pEntity.hurt(this.damageSources().mobAttack(this), f);
        if (flag) {
            if (f1 > 0.0F && pEntity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.NIHILISTIC.get(), 30,
                        0), living);
                living.knockback(f1 * 0.5F, Mth.sin(this.getYRot() * 0.017453292F),
                        -Mth.cos(this.getYRot() * 0.017453292F));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }
            this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP);
            if (pEntity instanceof Player player) {
                this.cooldownPlayerShield(player);
                this.maybeDisableShield(player, this.getMainHandItem(), player.isUsingItem() ?
                        player.getUseItem() : ItemStack.EMPTY);
            }
            this.doEnchantDamageEffects(this, pEntity);
            this.setLastHurtMob(pEntity);
        }
        return flag;
    }

    public void maybeDisableShield(Player player, ItemStack stack, ItemStack itemStack) {
        if (!stack.isEmpty() && !itemStack.isEmpty() && stack.getItem() instanceof AxeItem && itemStack
                .is(Items.SHIELD)) {
            float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.level().broadcastEntityEvent(player, (byte)30);
            }
        }
    }

    public boolean isAttack1() {
        return isFlag(10);
    }

    public boolean isGround() {
        return isFlag(11);
    }

    public boolean isTrust1() {
        return isFlag(12);
    }

    @Nullable
    public LivingEntity getTarget() {
        if (this.level().isClientSide)
            return EntitiesFinder.getLivingEntity(this.level(), this.getTargetId());
        else
            return EntitiesFinder.getLivingEntity(this.level(), this.getTargetUuid());
    }

    public void setTarget(@Nullable LivingEntity pTarget) {
        this.setTargetUuid(pTarget == null ? null : pTarget.getUUID());
        this.setTargetId(pTarget != null ? pTarget.getId() : -1);
    }

    private boolean canAttack() {
        return this.getTarget() != null && this.closerThan(this.getTarget(), 4);
    }

    public float getAttackDamage(float pBase) {
        float finalDamage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) + (this.getPower() * 2) +
                (this.getPhase() == 2 ? 3 : 0) + pBase;
        if (this.getMainHandItem().getItem() instanceof SwordItem item) {
            finalDamage += item.getDamage();
        }
        return finalDamage;
    }

    @Nullable
    public UUID getTargetUuid() {
        return this.entityData.get(DATA_TARGET_UUID).orElse(null);
    }

    public void setTargetUuid(@Nullable UUID uuid) {
        this.entityData.set(DATA_TARGET_UUID, Optional.ofNullable(uuid));
    }

    public int getTargetId() {
        return this.entityData.get(DATA_TARGET_ID);
    }

    public void setTargetId(int id) {
        this.entityData.set(DATA_TARGET_ID, id);
    }

    List<LivingEntity> makeAttackList(double range) {
        return this.level().getEntitiesOfClass(LivingEntity.class, MobUtils.getRange(this, range, range,
                range, range, range, range, range), entity -> MobUtils.canHurt(entity, this));
    }

    void sweepSound() {
        playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2F, 1F);
    }

    void groundSound() {
        this.playSound(SoundEvents.TOTEM_USE, 2F, 0.1F);
    }

    private void trust() {
        this.setDeltaMovement(this.getLookAngle().x * 4, this.getDeltaMovement().y, this.getLookAngle().z * 4);
    }

    private void doTrust() {
        List<LivingEntity> entities = this.makeAttackList(3);
        if (!entities.isEmpty()) {
            for (LivingEntity entity : entities) {
                this.doHurtTarget(entity);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NoixmodAPIAttributes.baseAttributes(7, 0.35, 0.75)
                .add(Attributes.ARMOR, 9).
                add(Attributes.MAX_HEALTH, 243).add(Attributes.FOLLOW_RANGE, 120);
    }

    {
        this.bossEvent = new ServerBossEvent(this.getDisplayName().copy().withStyle(ChatFormatting.BLUE),
                BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
    }

    static {
        GUARDIAN_FLAG = SynchedEntityData.defineId(StarGuardian.class, EntityDataSerializers.INT);
        PHASE = SynchedEntityData.defineId(StarGuardian.class, EntityDataSerializers.INT);
        DATA_ATTACK_TICK = SynchedEntityData.defineId(StarGuardian.class, EntityDataSerializers.INT);
        DATA_TARGET_ID = SynchedEntityData.defineId(StarGuardian.class, EntityDataSerializers.INT);
        DATA_TARGET_UUID = SynchedEntityData.defineId(StarGuardian.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_SHIELD_TICK = SynchedEntityData.defineId(StarGuardian.class, EntityDataSerializers.INT);
    }
}
