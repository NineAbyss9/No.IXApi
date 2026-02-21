
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiIllagerBoss;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.compat.bo.BlueOceansCompat;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.ApiSpellcaster;
import com.bilibili.player_ix.noixmod_api.entities.servant.FreakySpider;
import com.bilibili.player_ix.noixmod_api.entities.servant.RainbowphobiaPatients;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticZombie;
import com.bilibili.player_ix.noixmod_api.entities.servant.worm.FreakyWorm;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.ProjectileUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.BlockPos;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Biologist
extends ApiSpellcaster
implements RangedAttackMob,
        ApiIllagerBoss {
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(),
            BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_6);
    private static final Predicate<Entity> NO_RAVAGER_AND_ALIVE = (p_33346_) -> p_33346_.isAlive()
            && !(p_33346_ instanceof FreakyWorm) && !(p_33346_ instanceof Biologist);
    private static final EntityDataAccessor<Boolean> IS_SUMMONING
            = SynchedEntityData.defineId(Biologist.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAN_START
            = SynchedEntityData.defineId(Biologist.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_HEALING
            = SynchedEntityData.defineId(Biologist.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> HEAL_COOL_DOWN
            = SynchedEntityData.defineId(Biologist.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ROAR_COOL_DOWN
            = SynchedEntityData.defineId(Biologist.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_ROARING
            = SynchedEntityData.defineId(Biologist.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ANGRY
            = SynchedEntityData.defineId(Biologist.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> AnimationStates;
    public int healTicks;
    public int RoarTicks;
    private int ThrownTNTCoolDown;
    private static final EntityDataAccessor<Boolean> IsSecondPhase;
    public AnimationState ambient;
    public AnimationState walking;
    public AnimationState attacking;
    public AnimationState summon;
    public String ATTACK = "attack";
    public String AMBIENT = "ambient";
    public String WALK = "walk";
    public String SUMMON = "ownerSummon";
    public String SUMMON_1 = "summon_1";
    public Biologist(EntityType<? extends Biologist> e, Level l) {
        super(e, l);
        this.setMaxUpStep(2);
        this.xpReward = 50;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IsSecondPhase, false);
        this.entityData.define(IS_ANGRY, false);
        this.entityData.define(IS_SUMMONING, false);
        this.entityData.define(AnimationStates, 0);
        this.entityData.define(IS_ROARING, false);
        this.entityData.define(CAN_START, true);
        this.entityData.define(HEAL_COOL_DOWN, 0);
        this.entityData.define(ROAR_COOL_DOWN, 0);
        this.entityData.define(IS_HEALING, false);
    }

    public Integer getAnimationState() {
        return this.entityData.get(AnimationStates);
    }

    public void setRoarTicks(int t) {
        this.RoarTicks = t;
    }

    public boolean isAngry() {
        return this.entityData.get(IS_ANGRY);
    }

    public boolean isHalfHealth() {
        return MobUtils.isHalfHealth(this);
    }

    public void makeExplode(double x, double y, double z, int num, float power) {
        for (int t = 0; t < 4; ++t) {
            double pos = Mth.nextDouble(RandomSource.create(), num, -num);
            this.level().explode(this, x + pos, y, z + pos, power,
                    Level.ExplosionInteraction.MOB);
        }
    }

    public void freakySummon() {
        ServerLevel level = (ServerLevel) this.level();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        for (int $ = 0; $ < 2; ++$) {
            if (this.getTarget() == null) continue;
            float summonNum = this.distanceTo(this.getTarget());
            BlockPos pos = this.blockPosition();
            if (summonNum > 5 && summonNum <= 8) {
                FreakySpider spider = NoixmodAPIEntities.FREAKY_SPIDER.get().create(this.level());
                if (spider != null) {
                    spider.moveTo(x, y, z);
                    spider.setOwner(this);
                    spider.finalizeSpawn(level, this.level().getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED,
                            null, null);
                    level.addFreshEntityWithPassengers(spider);
                }
            } else if (summonNum > 8 && summonNum < 11) {
                NihilisticZombie zombie = NoixmodAPIEntities.NIHILISTIC_ZOMBIE.get().create(this.level());
                if (zombie != null) {
                    zombie.moveTo(x, y, z);
                    zombie.setOwner(this);
                    zombie.finalizeSpawn(level, this.level().getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED,
                            null, null);
                    level.addFreshEntityWithPassengers(zombie);
                }
            } else if (summonNum > 11) {
                this.makeExplode(this.getX(), this.getY(), this.getZ(), 0, 2f);
            } else if (summonNum <= 5) {
                this.roar();
            } else if (summonNum >= 9) {
                RainbowphobiaPatients rainbow = NoixmodAPIEntities.RAINBOWPHOBIA_PATIENTS.get().create(this.level());
                if (rainbow != null) {
                    rainbow.moveTo(x, y, z);
                    rainbow.setOwner(this);
                    rainbow.finalizeSpawn(level, this.level().getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED,
                            null, null);
                    level.addFreshEntityWithPassengers(rainbow);
                }
                FreakyWorm zombie = NoixmodAPIEntities.FREAKY_WORM.get().create(this.level());
                if (zombie != null) {
                    zombie.moveTo(x, y, z);
                    zombie.setOwner(this);
                    zombie.finalizeSpawn(level, this.level().getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED,
                            null, null);
                    level.addFreshEntityWithPassengers(zombie);
                }
            } else if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 4) {
                this.heal(5f);
                this.roar();
            }
        }
    }

    public int getRoarCoolDown() {
        return this.entityData.get(ROAR_COOL_DOWN);
    }

    public void setRoarCoolDown(int nt) {
        this.entityData.set(ROAR_COOL_DOWN, nt);
    }

    public boolean isSummoning() {
        return this.entityData.get(IS_SUMMONING);
    }

    public boolean isHealing() {
        return this.entityData.get(IS_HEALING);
    }

    public int getHealCoolDown() {
        return this.entityData.get(HEAL_COOL_DOWN);
    }

    @SuppressWarnings("unused")
    public Biologist(PlayMessages.SpawnEntity packet, Level world) {
        this(NoixmodAPIEntities.BIOLOGIST.get(), world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FreakySummonGoal());
        this.goalSelector.addGoal(0, new StartGoal());
        this.goalSelector.addGoal(1, new HealGoal<>(this));
        this.goalSelector.addGoal(1, new ThrownTNTGoal());
        this.goalSelector.addGoal(1, new BlindnessSpellGoal());
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new SummonSpellGoal());
        this.goalSelector.addGoal(2, new MeleeGoal(this, 0.75, true, false));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 20));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, false));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this, Raider.class));
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_37856_, DifficultyInstance p_37857_, MobSpawnType p_37858_, @Nullable SpawnGroupData p_37859_, @Nullable CompoundTag p_37860_) {
        if (p_37858_ != MobSpawnType.COMMAND && p_37858_ != MobSpawnType.SPAWN_EGG && p_37856_.getRandom().nextFloat() >= 0.29) {
            this.discard();
        }
        return super.finalizeSpawn(p_37856_, p_37857_, p_37858_, p_37859_, p_37860_);
    }

    public boolean isCastingSpell() {
        return super.isCastingSpell() || this.healTicks > 0;
    }

    public IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return IllagerArmPose.SPELLCASTING;
        }
        if (this.isAggressive()) {
            return IllagerArmPose.ATTACKING;
        }
        return IllagerArmPose.CROSSED;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.ILLUSIONER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

    @Override
    public int getAmbientSoundInterval() {
        return this.random.nextInt(400);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    public boolean isSecondPhase() {
        return this.entityData.get(IsSecondPhase);
    }

    public void setSecondPhase(boolean b) {
        this.entityData.set(IsSecondPhase, b);
    }

    protected float getDamageAfterArmorAbsorb(DamageSource ds, float a) {
        if (ds.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            a *= 0.5f;
        }
        if (ds.getEntity() == this) {
            a = 0f;
        }
        if (ds.is(DamageTypes.THROWN)) {
            a *= 0.5f;
        }
        return a;
    }

    public boolean hurt(DamageSource ds, float f) {
        if (ds.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        if (ds.is(DamageTypes.LIGHTNING_BOLT) || ds.is(DamageTypes.ON_FIRE)) {
            return false;
        }
        if (!ds.is(DamageTypes.GENERIC_KILL) && f > 15) {
            f = 15f;
        }
        if (this.getHealth() - f <= 0 && !this.isSecondPhase()) {
            this.setHealth(this.getMaxHealth());
            this.roar();
            if (!this.level().isClientSide) {
                WorldUtil.sendParticles(NoixmodAPIParticleTypes.BLOOD.get(), this, 25, 0, 0.5, 0, 0);
            }
            this.setSecondPhase(true);
            return false;
        }
        return super.hurt(ds, f);
    }

    public static void init() {
        if (NoixmodAPIMainConfig.BiologistRaid.get())
            Raid.RaiderType.create("APIBiologist", NoixmodAPIEntities.BIOLOGIST.get(), new int[]{
                    0, 0, 0, 0, 0, 1, 0, 0
            });
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> p_21104_) {
        if (AnimationStates.equals(p_21104_) && this.level().isClientSide()) {
            switch (this.getAnimationState()) {
                case 0: {
                    this.stopAllAnimations();
                    this.ambient.stop();
                    break;
                }
                case 1: {
                    this.stopAllAnimations();
                    this.ambient.start(this.tickCount);
                    break;
                }
                case 2: {
                    this.stopAllAnimations();
                    this.walking.start(this.tickCount);
                    break;
                }
                case 3: {
                    this.stopAllAnimations();
                    this.attacking.start(this.tickCount);
                    break;
                }
                case 4: {
                    this.stopAllAnimations();
                    this.summon.start(this.tickCount);
                    break;
                }
            }
        }
        super.onSyncedDataUpdated(p_21104_);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
        if (BlueOceansCompat.isLoaded()) {
            this.spawnAtLocation(BlueOceansCompat.getItemStack("gravy_bottle"), 3);
            this.spawnAtLocation(BlueOceansCompat.getItemStack("test_tube"), 2);
        }
        int j = Maths.toInteger();
        for (int i = 0;i < j;i++) {
            this.spawnAtLocation(NoixmodAPIItems.WORM_REAGENT.get());
        }
        super.dropCustomDeathLoot(p_21385_, p_21386_, p_21387_);
    }

    @Override
    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        if (this.isSecondPhase()){
            this.heal(2f);
        }
        return super.killedEntity(p_216988_, p_216989_);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        this.heal(1f);
        this.setAnimationState(this.ATTACK);
        return super.doHurtTarget(p_21372_);
    }

    @Override
    public void tick() {
        super.tick();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        if (!this.entityData.get(IS_ANGRY) && this.isHalfHealth()) {
            this.entityData.set(IS_ANGRY, true);
        }
        if (this.isHealing()) {
            if (this.level().isClientSide) {
                this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get(), x, y + 2.5, z, 0.1, 0.1, 0.0);
            }
        }
        if (this.RoarTicks > 0) {
            this.setRoarTicks(this.RoarTicks - 1);
        }
        if (this.RoarTicks == 10) {
            this.roar();
            this.entityData.set(IS_ROARING, false);
        }
        if (this.getHealCoolDown() > 0) {
            this.entityData.set(HEAL_COOL_DOWN, this.getHealCoolDown() - 1);
        }
        if (this.getRoarCoolDown() > 0) {
            this.setRoarCoolDown(this.getRoarCoolDown() - 1);
        } else if (this.getTarget() != null && this.getRoarCoolDown() == 0 && this.distanceToSqr(this.getTarget()) < 10) {
            this.RoarTicks = 60;
            this.setRoarCoolDown(600);
        }
    }

    public List<AnimationState> getAnimations() {
        ArrayList<AnimationState> animationStates = new ArrayList<>();
        animationStates.add(this.ambient);
        animationStates.add(this.walking);
        animationStates.add(this.attacking);
        animationStates.add(this.summon);
        return animationStates;
    }

    public void stopAllAnimations() {
        for (AnimationState animationState : this.getAnimations()) {
            animationState.stop();
        }
    }

    public Integer getAni(String string) {
        if (Objects.equals(this.AMBIENT, string)) {
            return 1;
        }
        if (Objects.equals(this.WALK, string)) {
            return 2;
        }
        if (Objects.equals(this.ATTACK, string)) {
            return 3;
        }
        if (Objects.equals(this.SUMMON, string)) {
            return 4;
        }
        if (Objects.equals(this.SUMMON_1, string)) {
            return 5;
        }
        return 0;
    }

    public void setAnimationState(Integer i) {
        this.entityData.set(AnimationStates, i);
    }

    public void setAnimationState(String string) {
        this.setAnimationState(this.getAni(string));
    }

    @NotNull
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 160).add(Attributes.FOLLOW_RANGE, 100.0).add(Attributes.ARMOR, 4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3).add(Attributes.ATTACK_DAMAGE, 12).add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    @Override
    public boolean addEffect(@NotNull MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return !this.isCastingSpell();
    }

    public void Knockback(@NotNull Entity p_33340_) {
        double d0 = p_33340_.getX() - this.getX();
        double d1 = p_33340_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        p_33340_.push(d0 / d2 * 4.0, 0.2, d1 / d2 * 4.0);
    }

    public void roar() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        if (!this.level().isClientSide) {
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.LARGE_SMOKE, x, y + 1.0, z, 100, 0.5, 0.5, 0.5, 0.25);
        }
        if (this.isAlive()) {
            List<LivingEntity> var1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0), NO_RAVAGER_AND_ALIVE);
            for (LivingEntity livingentity : var1) {
                if ((!(livingentity instanceof AbstractIllager))) {
                    livingentity.hurt(this.damageSources().indirectMagic(this, this), 8.0F);
                    this.Knockback(livingentity);
                    this.entityData.set(IS_ROARING, true);
                }
            }
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.RAVAGER_ROAR, SoundSource.HOSTILE, 1f, 0.25f);
    }

    @Override
    public boolean removeWhenFarAway(double p_37894_) {
        return false;
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity $$0, float $$1) {
        ItemStack $$2 = this.getProjectile(this.getItemInHand(ProjectileUtils.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow $$3 = ProjectileUtil.getMobArrow(this, $$2, $$1);
        double $$4 = $$0.getX() - this.getX();
        double $$5 = $$0.getY(0.3333333333333333) - $$3.getY();
        double $$6 = $$0.getZ() - this.getZ();
        double $$7 = Math.sqrt($$4 * $$4 + $$6 * $$6);
        if ($$3 instanceof Arrow arrow) {
            arrow.addEffect(new MobEffectInstance(MobEffects.WITHER, 600, 1));
            arrow.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 0));
        }
        $$3.shoot($$4, $$5 + $$7 * (double) 0.2f, $$6, 1.6f, 14 - this.level().getDifficulty().getId() * 4);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity($$3);
    }

    protected abstract class BiologistSpellGoal
    extends UseSpellGoal {
        protected boolean flag = Biologist.this.isSecondPhase();

        @Override
        public void start() {
            super.start();
            Biologist.this.setAnimationState(Biologist.this.SUMMON);
        }

        @Override
        public boolean canUse() {
            if (this.isFlag()) {
                return false;
            }
            return super.canUse();
        }

        protected boolean isFlag() {
            return this.flag;
        }
    }

    private static class MeleeGoal
    extends ApiMeleeAttackGoal {
        private final Biologist biologist;
        public MeleeGoal(Biologist p_25552_, double p_25553_, boolean p_25554_, boolean p_25555_) {
            super(p_25552_, p_25553_, p_25554_, p_25555_);
            this.biologist = p_25552_;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(p_25557_);
                this.biologist.setAnimationState(this.biologist.ATTACK);
            }
        }

        @Override
        public boolean canUse() {
            if (this.biologist.isSecondPhase()) {
                return super.canUse();
            } else {
                return false;
            }
        }

        @Override
        protected double getAttackReachSqr(@NotNull LivingEntity p_25556_) {
            return Maths.square(1.75);
        }
    }

    private class StartGoal extends BiologistSpellGoal {
        Biologist biologist = Biologist.this;

        @Override
        protected void castSpell() {
            ServerLevel serverLevel = (ServerLevel) biologist.level();
            BlockPos pos = biologist.blockPosition();
            FreakyWorm worm = (FreakyWorm) ((EntityType<?>) NoixmodAPIEntities.FREAKY_WORM.get()).create(biologist.level());
            if (worm != null) {
                worm.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
                worm.moveTo(pos, 0, 0);
                worm.setOwner(biologist);
                serverLevel.addFreshEntityWithPassengers(worm);
            }
            biologist.entityData.set(CAN_START, false);
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            if (Biologist.this.RoarTicks != 0) {
                return false;
            }
            return biologist.getTarget() != null;
        }

        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.VILLAGER_NO;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.DARK;
        }
    }

    private class BlindnessSpellGoal extends BiologistSpellGoal {
        Biologist biologist = Biologist.this;

        @Override
        protected void castSpell() {
            if (biologist.getTarget() != null) {
                (biologist.getTarget()).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300));
                if (!(biologist.getTarget() instanceof Player)) {
                    biologist.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, 600, 1));
                }
            }
            biologist.heal(5f);
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
        protected SoundEvent getPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.DARK;
        }
    }

    private class SummonSpellGoal extends BiologistSpellGoal {
        Biologist biologist = Biologist.this;

        private SummonSpellGoal() {
        }

        @Override
        protected void castSpell() {
            ServerLevel serverLevel = (ServerLevel) biologist.level();
            FreakySpider spider = (FreakySpider) ((EntityType<?>) NoixmodAPIEntities.FREAKY_SPIDER.get()).create(biologist.level());
            BlockPos pos = biologist.blockPosition();
            if (spider != null) {
                spider.moveTo(biologist.getX(), biologist.getY(), biologist.getZ(), 0, 0);
                spider.finalizeSpawn(serverLevel, biologist.level().getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
                spider.setOwner(biologist);
                serverLevel.addFreshEntityWithPassengers(spider);
            }
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            if (Biologist.this.RoarTicks != 0) {
                return false;
            }
            return super.canUse();
        }

        @Override
        protected boolean isFlag() {
            return true;
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 200;
        }

        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.ATTACK;
        }
    }

    private class FreakySummonGoal extends UseSpellGoal {
        Biologist biologist = Biologist.this;

        @Override
        protected void castSpell() {
            biologist.freakySummon();
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 300;
        }

        @Override
        public boolean canUse() {
            if (!biologist.isAngry()) {
                return false;
            }
            if (!super.canUse()) {
                return false;
            }
            return super.canUse();
        }

        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.VILLAGER_NO;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.UNKNOWN;
        }
    }

    private class ThrownTNTGoal extends BiologistSpellGoal {
        public Biologist biologist = Biologist.this;

        private ThrownTNTGoal() {
            super();
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        protected void castSpell() {
            biologist.makeExplode(biologist.getX(), biologist.getY(), biologist.getZ(), 7, 1.5f);
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 500;
        }

        @Override
        protected @Nullable SoundEvent getPrepareSound() {
            return SoundEvents.TNT_PRIMED;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.ATTACK;
        }

        @Override
        public void stop() {
            super.stop();
            biologist.ThrownTNTCoolDown = 600;
        }

        @Override
        public boolean canUse() {
            if (biologist.ThrownTNTCoolDown != 0) {
                return false;
            }
            return biologist.getTarget() != null;
        }
    }

    private static class HealGoal<T extends Biologist> extends Goal {
        public final T mob;

        private HealGoal(T entity) {
            super();
            this.mob = entity;
        }

        @Override
        public void start() {
            super.start();
            this.mob.setAnimationState(this.mob.SUMMON);
            this.mob.healTicks = 44;
        }

        @Override
        public boolean canContinueToUse() {
            return this.mob.getHealCoolDown() == 0 && this.mob.getHealth() < this.mob.getMaxHealth() && this.canUse();
        }

        @Override
        public void tick() {
            super.tick();
            this.mob.entityData.set(IS_HEALING, true);
            --this.mob.healTicks;
            if (--this.mob.healTicks == 4) {
                this.stop();
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.mob.heal(10f);
            this.mob.entityData.set(IS_HEALING, false);
            this.mob.entityData.set(HEAL_COOL_DOWN, 600);
            this.mob.healTicks = 0;
        }

        @Override
        public boolean canUse() {
            if (this.mob.getHealCoolDown() != 0) {
                return false;
            }
            return !this.mob.isSummoning() && this.mob.getHealth() < this.mob.getMaxHealth();
        }
    }

    static {
        IsSecondPhase = SynchedEntityData.defineId(Biologist.class, EntityDataSerializers.BOOLEAN);
        AnimationStates = SynchedEntityData.defineId(Biologist.class, EntityDataSerializers.INT);
    }

    {
        this.ambient = new AnimationState();
        this.attacking = new AnimationState();
        this.walking = new AnimationState();
        this.summon = new AnimationState();
    }
}
