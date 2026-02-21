
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.ix_api.api.annotation.Prototype;
import com.bilibili.player_ix.noixmod_api.api.entity.IX;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.entities.projectile.WaterTrap;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.*;
import com.bilibili.player_ix.noixmod_api.entities.villager.VillagerFighter;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.*;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

@Prototype(prototype = "bilibili@Player_IX")
public class NihilisticLord
extends SpellcasterNihilist
implements RangedAttackMob, ApiNihilisticBoss, IX {
    public float spin = 0;
    private static final Predicate<Entity> NO_RAVAGER_AND_ALIVE = $$0 -> {
        if ($$0 == null) {
            return false;
        }
        return $$0.isAlive() && !($$0 instanceof AbstractVillager) && !($$0 instanceof Nihilistic);
    };
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getName(), ServerBossEvent.BossBarColor.PURPLE, ServerBossEvent.BossBarOverlay.NOTCHED_10);
    private static final EntityDataAccessor<Boolean> SecondPhase = SynchedEntityData.defineId(NihilisticLord.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor <Integer> CurseCoolDown = SynchedEntityData.defineId(NihilisticLord.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor <Integer> HitCounts = SynchedEntityData.defineId(NihilisticLord.class, EntityDataSerializers.INT);
    private static  final EntityDataAccessor<Byte> BossPhase = SynchedEntityData.defineId(NihilisticLord.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor <Integer> SummonCoolDown = SynchedEntityData.defineId(NihilisticLord.class, EntityDataSerializers.INT);
    @Nullable
    public AbstractVillager MissionaryTarget;
    protected boolean isSettingSecondPhase = false;
    protected int hurtCooldown = 0;
    public NihilisticLord(EntityType<? extends NihilisticLord> e, Level l) {
        super(e, l);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        this.setMaxUpStep(3f);
    }

    @Nullable
    public AbstractVillager getMissionaryTarget() {
        return this.MissionaryTarget;
    }

    public void forceAddEffect(MobEffectInstance p_147216_, @Nullable Entity p_147217_) {
        this.removeAllEffects();
    }

    public Entity ixSelf() {
        return this;
    }

    protected ParticleOptions getSpellParticle() {
        return NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get();
    }

    public int getExperienceReward() {
        return 10000;
    }

    private void setMissionaryTarget(@Nullable AbstractVillager villager) {
        this.MissionaryTarget = villager;
    }

    public boolean fireImmune() {
        return true;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SecondPhase, false);
        this.entityData.define(CurseCoolDown, 0);
        this.entityData.define(HitCounts, 0);
        this.entityData.define(BossPhase, (byte)1);
        this.entityData.define(SummonCoolDown, 0);
    }

    public boolean canChangeDimensions() {
        return false;
    }

    public boolean canFreeze() {
        return false;
    }

    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SetSecondPhaseGoal<>(this));
        this.goalSelector.addGoal(2, new LordRangedBowAttackGoal<>(this, 30f));
        this.goalSelector.addGoal(2, new LordCastingSpellGoal());
        this.goalSelector.addGoal(2, new DestroySpellGoal());
        this.goalSelector.addGoal(2, new TrapSpellGoal());
        this.goalSelector.addGoal(3, new LurkerSpellGoal());
        this.goalSelector.addGoal(3, new HealSpellGoal());
        this.goalSelector.addGoal(3, new RangedSummonSpellGoal());
        this.goalSelector.addGoal(3, new SummonSpellGoal());
        this.goalSelector.addGoal(3, new ZombieSpellGoal());
        this.goalSelector.addGoal(3, new GhastSpellGoal());
        this.goalSelector.addGoal(3, new FireballSpellGoal());
        this.goalSelector.addGoal(3, new MissionarySpellGoal());
        this.goalSelector.addGoal(3, new KnockSpellGoal());
        this.goalSelector.addGoal(3, new ExplodeSpellGoal());
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.targetSelector.addGoal(1, new LordHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new LordNearestAttackGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(2, new LordNearestAttackGoal<>(this, AbstractGolem.class, false));
        this.targetSelector.addGoal(2, new LordNearestAttackGoal<>(this, VillagerFighter.class, false));
    }

    public final boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public void hitTeleport() {
        if (this.entityData.get(HitCounts) >= 4) {
            this.teleport();
            this.entityData.set(HitCounts, 0);
        }
    }

    protected float getDamageAfterArmorAbsorb(DamageSource $$0, float $$1) {
        if (this.hurtDuration > 9) {
            this.entityData.set(HitCounts, this.entityData.get(HitCounts) + 1);
            this.hitTeleport();
        }
        $$1 = super.getDamageAfterArmorAbsorb($$0, $$1);
        if ($$0.getEntity() == this) {
            $$1 = 0.0f;
        }
        if ($$0.getEntity() instanceof NihilityMobs || $$0.getEntity() instanceof Nihilist) {
            $$1 = 0.0f;
        }
        if (this.getHealth() <= this.getMaxHealth() / 2) {
            $$1 *= 0.5f;
        }
        if ($$0.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            $$1 *= 0.15f;
        }
        if ($$1 > 25.0f) {
            $$1 = 25.0f;
        }
        return $$1;
    }

    protected float getDamageAfterMagicAbsorb(DamageSource ds, float fl) {
        fl = super.getDamageAfterMagicAbsorb(ds, fl);
        if (ds.getEntity() == this) {
            fl = 0;
        }
        if (ds.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            fl *= 0.15f;
        }
        return fl;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.getApostles().isEmpty()) {
            return false;
        }
        if (this.hurtCooldown > 0) {
            return false;
        } else {
            this.hurtCooldown = 20;
        }
        if (pSource.is(DamageTypeTags.WITHER_IMMUNE_TO) && this.getHealth() <= (this.getMaxHealth() / 2)) {
            return false;
        }
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        if (pSource.is(DamageTypeTags.IS_FIRE)) {
            return false;
        }
        if (pSource.is(DamageTypes.DROWN)) {
            return false;
        }
        if (pSource.is(DamageTypes.BAD_RESPAWN_POINT)) {
            return false;
        }
        if (pSource.is(DamageTypes.EXPLOSION) || pSource.is(DamageTypes.PLAYER_EXPLOSION)) {
            return false;
        }
        if (pSource.is(DamageTypes.IN_WALL)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public void setHealth(float p_21154_) {
        super.setHealth(p_21154_);
    }

    /*public NihilisticLord(PlayMessages.@NotNull SpawnEntity spawn, Level world) {
        this(NoixmodAPIEntities.NIHILISTIC_LORD.get(), world);
        spawn.getEntity();
    }*/

    public void remove(RemovalReason p_276115_) {
        if (this.isDeadOrDying() || !this.isAlive()) {
            super.remove(p_276115_);
        }
    }

    public List<Apostle> getApostles() {
        return this.level().getEntitiesOfClass(Apostle.class, this.getBoundingBox().inflate(64), apostle ->
                apostle.getType() == NoixmodAPIEntities.APOSTLE.get());
    }

    public MobType getMobType() {
        return super.getMobType();
    }

    public int getCurseCoolDown() {
        return this.entityData.get(CurseCoolDown);
    }

    public void setCurseCoolDown(int $$0) {
        this.entityData.set(CurseCoolDown, this.getCurseCoolDown() + $$0);
    }

    public int getSummonCoolDown() {
        return this.entityData.get(SummonCoolDown);
    }

    public void setSummonCoolDown(int $$0) {
        this.entityData.set(SummonCoolDown, $$0);
    }

    public void aiStep() {
        super.aiStep();
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public void tick() {
        super.tick();
        if (!NoixmodAPIMainConfig.HorrorMode.get()) {
            this.setInvulnerable(!this.getApostles().isEmpty());
        }
        if (this.hurtCooldown > 0) {
            --this.hurtCooldown;
        }
        if (this.isAlive()) {
            if (this.getCurseCoolDown() > 0) {
                this.setCurseCoolDown(this.getCurseCoolDown() - 1);
            }
            if (this.getSummonCoolDown() > 0) {
                this.setSummonCoolDown(this.getSummonCoolDown() - 1);
            }
        }
        if (this.isSettingSecondPhase) {
            this.setHealth(this.getHealth() + 5);
            if (this.getHealth() == this.getMaxHealth()) {
                this.entityData.set(BossPhase, (byte)2);
                this.entityData.set(SecondPhase, true);
                this.isSettingSecondPhase = false;
            }
        }
        if (this.spin < 3.14f) {
            this.spin += 0.01f;
            if (this.spin >= 3.14f) {
                this.spin = 0.f;
            }
        }
    }

    public static InteractionHand getWeaponHoldingHand(LivingEntity p_37298_, Item p_37299_) {
        return p_37298_.getMainHandItem().is(p_37299_) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public void performRangedAttack(LivingEntity $$0, float $$1) {
        ItemStack $$2 = this.getProjectile(this.getItemInHand(NihilisticLord.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow $$3 = ProjectileUtil.getMobArrow(this, $$2, $$1);
        double $$4 = $$0.getX() - this.getX();
        double $$5 = $$0.getY(0.5) - $$3.getY(0.5);
        double $$6 = $$0.getZ() - this.getZ();
        float speed = this.getPhase() == 2 ? 3.6f : 2.8f;
        int level = this.getPhase() == 2 ? 1:0;
        float f = this.getPhase() == 2 ? 1f : 0.8f;
        $$3.setCritArrow(true);
        $$3.setBaseDamage(40);
        if ($$3 instanceof Arrow arrow) {
            arrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, level));
            arrow.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, level));
        }
        $$3.shoot($$4, $$5, $$6, speed, f);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity($$3);
        this.heal(1f);
    }

    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        ServerLevel level = (ServerLevel)this.level();
        BlockPos $$1 = this.blockPosition();
        Lurker $$0 = (NoixmodAPIEntities.LURKER.get()).create(this.level());
        if ($$0 != null) {
            $$0.moveTo(this.getX(), this.getY(), this.getZ());
            WorldUtil.finalizeSpawn($$0, level, level.getCurrentDifficultyAt($$1), MobSpawnType.MOB_SUMMONED, null, null);
            $$0.setOwner(this);
            level.addFreshEntity($$0);
        }
        return super.killedEntity(p_216988_, p_216989_);
    }

    protected SoundEvent getAmbientSound() {
        if (this.getPhase() == 1) {
            return SoundEvents.EVOKER_AMBIENT;
        }
        return SoundEvents.WITHER_AMBIENT;
    }

    protected SpellCastType getSpellCastType() {
        return SpellCastType.NIHILISTIC;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        if (this.getPhase() == 1) {
            return SoundEvents.EVOKER_HURT;
        }
        return SoundEvents.WITHER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.RESPAWN_ANCHOR_SET_SPAWN;
    }

    public boolean isAlliedTo(@Nullable Entity $$0) {
        if ($$0 == null) {
            return false;
        }
        if ($$0 == this) {
            return true;
        }
        if ($$0 instanceof Ownable ownable) {
            return this.isAlliedTo(ownable.getOwner());
        }
        return false;
    }

    public NihilistArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return NihilistArmPose.SPELL_AND_WEAPON;
        }
        if (this.isAggressive()) {
            return NihilistArmPose.BOW_AND_ARROW;
        }
        if (!this.isAlive()) {
            return NihilistArmPose.DIE;
        }
        return NihilistArmPose.CROSSED;
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }

    public byte getPhase() {
        return this.entityData.get(BossPhase);
    }

    public boolean isPowered() {
        return this.getHealth() <= 320 && this.getPhase() != 1;
    }

    private class MissionarySpellGoal
    extends UseSpellGoal {
        private final TargetingConditions wololoTargeting = TargetingConditions.forCombat().range(32).selector(LivingEntity::isAlive);
        NihilisticLord lord = NihilisticLord.this;

        MissionarySpellGoal() {
        }

        public boolean canUse() {
            if (lord.getTarget() != null) {
                return false;
            } else if (lord.tickCount < this.nextAttackTickCount){
                return false;
            } else if (lord.isCastingSpell()) {
                return false;
            } else {
                List<AbstractVillager> $$0 = lord.level().getNearbyEntities(AbstractVillager.class, this.wololoTargeting, lord, lord.getBoundingBox().inflate(16.0, 4.0, 16.0));
                if ($$0.isEmpty()) {
                    return false;
                } else {
                    lord.setMissionaryTarget($$0.get(lord.random.nextInt($$0.size())));
                    return true;
                }
            }
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 200;
        }

        public void stop() {
            super.stop();
            lord.setMissionaryTarget(null);
        }

        public boolean canContinueToUse() {
            return lord.getMissionaryTarget() != null && this.attackWarmupDelay > 0 && lord.getTarget() == null;
        }

        @Override
        protected void castSpell() {
            assert lord.getMissionaryTarget() != null;
            ServerLevel $$0 = (ServerLevel)lord.getMissionaryTarget().level();
            CursedNihilisticEvoker $$2 = (NoixmodAPIEntities.CURSED_NIHILISTIC_EVOKER.get()).create($$0);
            if (lord.getMissionaryTarget() != null) {
                double x = lord.getMissionaryTarget().getX();
                double y = lord.getMissionaryTarget().getY();
                double z = lord.getMissionaryTarget().getZ();
                if ($$2 != null) {
                    $$2.moveTo(x, y, z);
                    $$2.finalizeSpawn($$0, $$0.getCurrentDifficultyAt($$2.blockPosition()), MobSpawnType.CONVERSION, null, null);
                    $$2.setPersistenceRequired();
                    $$2.setOwner(lord);
                    $$0.addFreshEntityWithPassengers($$2);
                    lord.getMissionaryTarget().discard();
                }
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_WOLOLO;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }
    }

    private class FireballSpellGoal
    extends SpellcasterNihilist.UseSpellGoal {
        NihilisticLord lord = NihilisticLord.this;
        FireballSpellGoal() {
        }

        @Override
        protected void castSpell() {
            LivingEntity $$0 = lord.getTarget();
            assert $$0 != null;
            Level $$2 = lord.level();
            Vec3 $$4 = lord.getViewVector(1.0f);
            double $$5 = $$0.getX() - (lord.getX() + $$4.x * 4.0);
            double $$6 = $$0.getY(0.5) - (0.5 + lord.getY(0.5));
            double $$7 = $$0.getZ() - (lord.getZ() + $$4.z * 4.0);
            LargeFireball $$8 = new LargeFireball($$2, lord, $$5, $$6, $$7, 2);
            $$8.setPos(lord.getX() + $$4.x * 2.0, lord.getY(0.5) + 0.5, $$8.getZ() + $$4.z * 2.0);
            $$2.addFreshEntity($$8);
            lord.level().playSound(null, lord.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 0.25f, 1f);
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        public boolean canUse() {
            if (!(super.canUse())) {
                return false;
            }
            if (lord.getPhase() == 2) {
                return false;
            }
            return lord.getTarget() != null;
        }

        @Override
        protected int getCastingInterval() {
            return 300;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.FIRE;
        }

    }

    class SummonSpellGoal extends SpellcasterNihilist.UseSpellGoal {
        SummonSpellGoal(){
        }

        public boolean canUse() {
            if (!(super.canUse())) {
                return false;
            }
            if (NihilisticLord.this.getCurseCoolDown() != 0) {
                return false;
            }
            return NihilisticLord.this.getTarget() != null;
        }

        @Override
        protected int getCastingInterval() {
            return 1200;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.NIHILISTIC;
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected void castSpell() {
            ServerLevel $$0 = (ServerLevel) NihilisticLord.this.level();
            NihilisticLord lord = NihilisticLord.this;
            for (int $$1 = 0; $$1 < 1; ++$$1) {
                BlockPos $$2 = lord.blockPosition().offset(-2 + lord.random.nextInt(10), 1, -2 + lord.random.nextInt(10));
                CursedNihilisticEvoker $$3 = (CursedNihilisticEvoker) ((EntityType<?>)NoixmodAPIEntities.CURSED_NIHILISTIC_EVOKER.get()).create(lord.level());
                if ($$3 == null) continue;
                $$3.moveTo($$2, 0.0f, 0.0f);
                $$3.finalizeSpawn($$0, lord.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED, null, null);
                $$3.setPersistenceRequired();
                $$3.setOwner(lord);
                $$3.setTarget(lord.getTarget());
                double d = 0.25;
                ((ServerLevel)lord.level()).sendParticles(ParticleTypes.LARGE_SMOKE, $$3.getX(), $$3.getY(), $$3.getZ(), 25, 1.0, 1.0, 1.0, d);
                $$0.addFreshEntityWithPassengers($$3);
                lord.heal(3f);
                lord.setCurseCoolDown(lord.getCurseCoolDown() + 400);
            }
        }
    }

    class GhastSpellGoal extends SpellcasterNihilist.UseSpellGoal {
        private final TargetingConditions ghastCountTargeting;
        GhastSpellGoal(){
            this.ghastCountTargeting = TargetingConditions.forNonCombat().range(128.0).ignoreLineOfSight().ignoreInvisibilityTesting();
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Override
        public boolean canUse() {
            if (!(super.canUse())) {
                return false;
            }
            int $$0 = NihilisticLord.this.level().getNearbyEntities(NihilisticZombie.class, this.ghastCountTargeting, NihilisticLord.this, NihilisticLord.this.getBoundingBox().inflate(128.0)).size();
            if ($$0 >= 1) {
                return false;
            }
            return NihilisticLord.this.getTarget() != null;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.NIHILISTIC;
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected void castSpell() {
            ServerLevel $$0 = (ServerLevel) NihilisticLord.this.level();
            NihilisticLord lord = NihilisticLord.this;
            for (int $$1 = 0; $$1 < 1; ++$$1) {
                BlockPos $$2 = lord.blockPosition().offset(-2 + lord.random.nextInt(10), 1, -2 + lord.random.nextInt(10));
                NihilisticGhast $$3 = (NihilisticGhast) ((EntityType<?>)NoixmodAPIEntities.NIHILISTIC_GHAST.get()).create(lord.level());
                if ($$3 == null) continue;
                $$3.moveTo($$2, 0.0f, 0.0f);
                WorldUtil.nullableFinalizeSpawn($$3, $$0, lord.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED);
                $$3.setPersistenceRequired();
                $$3.setOwner(lord);
                $$3.setTarget(lord.getTarget());
                double d = 0.25;
                ((ServerLevel)lord.level()).sendParticles(ParticleTypes.LARGE_SMOKE, $$3.getX(), $$3.getY(), $$3.getZ(), 25, 1.0, 1.0, 1.0, d);
                $$0.addFreshEntityWithPassengers($$3);
                lord.heal(1f);
            }
        }
    }

    class ZombieSpellGoal extends SpellcasterNihilist.UseSpellGoal {
        private final TargetingConditions zombieCountTargeting;
        ZombieSpellGoal(){
            this.zombieCountTargeting = TargetingConditions.forNonCombat().range(128.0).ignoreLineOfSight().ignoreInvisibilityTesting();
        }

        @Override
        protected int getCastingInterval() {
            if (NihilisticLord.this.getPhase() != 1) {
                return 400;
            }
            return 600;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.NIHILISTIC;
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        public boolean canUse() {
            if (!(super.canUse())) {
                return false;
            }
            int $$0 = NihilisticLord.this.level().getNearbyEntities(NihilisticZombie.class, this.zombieCountTargeting, NihilisticLord.this, NihilisticLord.this.getBoundingBox().inflate(128.0)).size();
            if ($$0 > 3) {
                return false;
            }
            return 3 > $$0;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected void castSpell() {
            ServerLevel $$0 = (ServerLevel) NihilisticLord.this.level();
            NihilisticLord lord = NihilisticLord.this;
            if (lord.getPhase() == 1) {
                for (int $$1 = 0; $$1 < 6; ++$$1) {
                    BlockPos $$2 = lord.blockPosition().offset(-2 + lord.random.nextInt(15), 1, -2 + lord.random.nextInt(15));
                    NihilisticZombie $$3 = (NihilisticZombie) ((EntityType<?>) NoixmodAPIEntities.NIHILISTIC_ZOMBIE.get()).create(lord.level());
                    if ($$3 == null) continue;
                    $$3.moveTo($$2, 0.0f, 0.0f);
                    $$3.finalizeSpawn($$0, lord.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED, null, null);
                    $$3.setPersistenceRequired();
                    $$3.setOwner(lord);
                    $$3.setTarget(lord.getTarget());
                    double d = 0.25;
                    ((ServerLevel) lord.level()).sendParticles(ParticleTypes.LARGE_SMOKE, $$3.getX(), $$3.getY(), $$3.getZ(), 20, 1.0, 1.0, 1.0, d);
                    $$0.addFreshEntityWithPassengers($$3);
                    lord.heal(0.25f);
                }
            } else {
                for (int $$1 = 0; $$1 < 1; ++$$1) {
                    BlockPos $$2 = lord.blockPosition();
                    NihilisticZombie $$3 = (NihilisticZombie) ((EntityType<?>) NoixmodAPIEntities.NIHILISTIC_ZOMBIE.get()).create(lord.level());
                    if ($$3 == null) continue;
                    $$3.moveTo($$2, 0.0f, 0.0f);
                    $$3.finalizeSpawn($$0, lord.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED, null, null);
                    $$3.setPersistenceRequired();
                    $$3.setOwner(lord);
                    $$3.setTarget(lord.getTarget());
                    $$3.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
                    $$3.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
                    $$3.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
                    $$3.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
                    $$3.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_AXE));
                    double d = 0.25;
                    ((ServerLevel) lord.level()).sendParticles(ParticleTypes.LARGE_SMOKE, $$3.getX(), $$3.getY(), $$3.getZ(), 50, 1.0, 1.0, 1.0, d);
                    $$0.addFreshEntityWithPassengers($$3);
                    lord.heal(0.25f);
                }
            }
        }
    }

    class HealSpellGoal extends UseSpellGoal {
        NihilisticLord lord = NihilisticLord.this;
        HealSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!(super.canUse())) {
                return false;
            }
            if (lord.getPhase() == 1) {
                return false;
            }
            return lord.getPhase() == 2 && lord.getHealth() <= 450;
        }

        @Override
        protected void castSpell() {
            if (lord.isAlive() && lord.getHealth() != 0) {
                lord.setHealth(lord.getHealth() + 30);
            }
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
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.RESPAWN_ANCHOR_CHARGE;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.REGEN;
        }
    }

    class TrapSpellGoal
    extends UseSpellGoal {
        NihilisticLord lord = NihilisticLord.this;

        @Override
        protected void castSpell() {
            LivingEntity $$0 = lord.getTarget();
            assert $$0 != null;
            double $$1 = Math.min($$0.getY(), lord.getY());
            double $$2 = Math.max($$0.getY(), lord.getY()) + 2.0;
            float $$3 = (float) Mth.atan2($$0.getZ() - lord.getZ(), $$0.getX() - lord.getX());
            for (int $$4 = 0; $$4 < 5; ++$$4) {
                float $$5 = $$3 + (float) $$4 * (float) Math.PI * 0.4f;
                this.createSpellEntity(lord.getX() + (double) Mth.cos($$5) * 1.5, lord.getZ() + (double) Mth.sin($$5) * 1.5, $$1, $$2, $$5, 0);
            }
            for (int $$6 = 0; $$6 < 8; ++$$6) {
                float $$7 = $$3 + (float) $$6 * (float) Math.PI * 2.0f / 8.0f + 1.2566371f;
                this.createSpellEntity(lord.getX() + (double) Mth.cos($$7) * 2.5, lord.getZ() + (double) Mth.sin($$7) * 2.5, $$1, $$2, $$7, 3);
            }
        }

        private void createSpellEntity(double $$0, double $$1, double $$2, double $$3, float $$4, int $$5) {
            BlockPos $$6 = BlockPos.containing($$0, $$3, $$1);
            boolean $$7 = false;
            double $$8 = 0.0;
            do {
                VoxelShape $$12;
                BlockPos $$9 = $$6.below();
                BlockState $$10 = lord.level().getBlockState($$9);
                if (!$$10.isFaceSturdy(lord.level(), $$9, Direction.UP)) continue;
                if (!lord.level().isEmptyBlock($$6) && !($$12 = lord.level().getBlockState($$6).getCollisionShape(lord.level(), $$6)).isEmpty()) {
                    $$8 = $$12.max(Direction.Axis.Y);
                }
                $$7 = true;
                break;
            } while (($$6 = $$6.below()).getY() >= Mth.floor($$2) - 1);
            if ($$7) {
                lord.level().addFreshEntity(new WaterTrap(lord.level(), $$0, (double) $$6.getY() + $$8, $$1, $$4, $$5, lord));
            }
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        public boolean canUse() {
            if (!(super.canUse())) {
                return false;
            }
            if (lord.distanceToSqr(Objects.requireNonNull(lord.getTarget())) > 4.0) {
                return false;
            }
            return lord.getPhase() == 1;
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.FISHING_BOBBER_SPLASH;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.WATER;
        }
    }

    class LurkerSpellGoal extends UseSpellGoal {
        NihilisticLord lord = NihilisticLord.this;
        LurkerSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!(super.canUse())) {
                return false;
            }
            return lord.getPhase() == 2;
        }

        @Override
        protected void castSpell() {
            ServerLevel $$0 = (ServerLevel) lord.level();
            for (int nt = 0;nt < 1; ++ nt) {
                BlockPos $$2 = lord.blockPosition().offset(-2 + lord.random.nextInt(1), 1, -2 + lord.random.nextInt(1));
                Lurker $$3 = (Lurker) ((EntityType<?>) NoixmodAPIEntities.LURKER.get()).create(lord.level());
                if ($$3 == null) continue;
                $$3.moveTo($$2, 0.0f, 0.0f);
                WorldUtil.nullableFinalizeSpawn($$3, $$0, lord.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED);
                $$3.setOwner(lord);
                double d = 0.25;
                ((ServerLevel) lord.level()).sendParticles(ParticleTypes.LARGE_SMOKE, $$3.getX(), $$3.getY(), $$3.getZ(), 50, 1.0, 1.0, 1.0, d);
                $$0.addFreshEntityWithPassengers($$3);
                lord.heal(1f);
            }
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected int getCastingInterval() {
            return 3200;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }
    }

    class KnockSpellGoal extends UseSpellGoal {
        KnockSpellGoal(){
        }

        @Override
        protected int getCastingInterval() {
            return 200;
        }

        @Override
        public boolean canUse() {
            assert NihilisticLord.this.getTarget() != null;
            if (!(super.canUse())) {
                return false;
            }
            LivingEntity lie = NihilisticLord.this.getTarget();
            if (NihilisticLord.this.distanceToSqr(lie) > 8.0) {
                return false;
            }
            return NihilisticLord.this.getTarget() != null;
        }

        private void strongKnockback(LivingEntity $$0) {
            double x = NihilisticLord.this.getX();
            double y = NihilisticLord.this.getY();
            double z = NihilisticLord.this.getZ();
            $$0.push(x / z * 0, 3, y / z * 0);
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected void castSpell() {
            NihilisticLord lord = NihilisticLord.this;
            if (lord.getPhase() == 1) {
                List<LivingEntity> $$0 = lord.level().getEntitiesOfClass(LivingEntity.class, lord.getBoundingBox().inflate(8.0), NO_RAVAGER_AND_ALIVE);
                for (LivingEntity livingEntity : $$0) {
                    if (!(livingEntity instanceof NihilityMobs)) {
                        livingEntity.hurt(lord.damageSources().magic(), 8.0f);
                    }
                    this.strongKnockback(livingEntity);
                }
                ((ServerLevel) lord.level()).sendParticles(ParticleTypes.LARGE_SMOKE, lord.getX(), lord.getY() + 1.0, lord.getZ(), 100, 0, 1.0, 0, 0.25);
                lord.teleport();
            } else {
                ServerLevel $$0 = (ServerLevel) lord.level();
                WaterTrap $$1 = (WaterTrap) ((EntityType<?>) NoixmodAPIEntities.WATER_TRAP.get()).create(lord.level());
                if ($$1 != null) {
                    $$1.moveTo(lord.getX(), lord.getY(), lord.getZ());
                    $$1.setOwner(lord);
                    $$1.warmupDelayTicks = 40;
                    $$1.setLifeTick(60);
                    $$0.addFreshEntityWithPassengers($$1);
                    lord.teleport();
                }
            }
        }
    }

    class LordCastingSpellGoal
    extends CastingSpellGoal {
        NihilisticLord lord = NihilisticLord.this;
        LordCastingSpellGoal() {
        }

        @Override
        public void tick() {
            if (lord.getTarget() != null) {
                lord.getLookControl().setLookAt(lord.getTarget(), lord.getMaxHeadYRot(), lord.getMaxHeadXRot());
                lord.navigation.setSpeedModifier(0);
            } else if (lord.getMissionaryTarget() != null) {
                lord.getLookControl().setLookAt(lord.getMissionaryTarget().getX(), lord.getMissionaryTarget().getY(), lord.getMissionaryTarget().getZ());
            }
        }

        @Override
        public boolean canUse() {
            if (lord.getMissionaryTarget() == null && lord.getTarget() == null) {
                return false;
            }
            return super.canUse();
        }
    }

    class ExplodeSpellGoal
    extends UseSpellGoal {
        NihilisticLord lord = NihilisticLord.this;
        ExplodeSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!(super.canUse())) {
                return false;
            }
            return lord.getPhase() == 1;
        }

        @Override
        protected void castSpell() {
            LivingEntity l = lord.getTarget();
            if (l != null) {
                lord.level().explode(lord, lord.getX(), lord.getY(), lord.getZ(), 2f, Level.ExplosionInteraction.MOB);
            }
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
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.CREEPER_PRIMED;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.RANGE;
        }
    }

    class DestroySpellGoal  extends UseSpellGoal {
        NihilisticLord lord = NihilisticLord.this;

        DestroySpellGoal(){
        }

        @Override
        public boolean canUse() {
            if (!(super.canUse())) {
                return false;
            }
            if (lord.getPhase() == 1) {
                return false;
            }
            if (lord.getTarget() != null && lord.distanceToSqr(lord.getTarget()) > 12) {
                return false;
            }
            return lord.getTarget() != null;
        }

        @Override
        protected void castSpell() {
            List<LivingEntity> $$0 = lord.level().getEntitiesOfClass(LivingEntity.class, lord.getBoundingBox().inflate(36), NO_RAVAGER_AND_ALIVE);
            for (LivingEntity livingEntity : $$0) {
                if (!(livingEntity instanceof NihilityMobs || livingEntity instanceof Player player && player.isCreative())) {
                    livingEntity.hurt(lord.damageSources().fellOutOfWorld(), 100f);
                    lord.Knockback(livingEntity);
                }
                if (livingEntity instanceof NihilisticZombie || livingEntity instanceof NihilisticGhast || livingEntity instanceof CursedNihilisticEvoker || livingEntity instanceof Lurker || livingEntity instanceof NihilisticLord) {
                    livingEntity.heal(100f);
                }
            }
            ((ServerLevel)lord.level()).sendParticles(ParticleTypes.LARGE_SMOKE, lord.getX(), lord.getY() + 1.0, lord.getZ(), 100, 0, 1.0, 0, 0.25);
            lord.removeAllEffects();
            lord.heal(100f);
            lord.teleport();
        }

        @Override
        protected int getCastingTime() {
            return 80;
        }

        @Override
        protected int getCastingInterval() {
            return 6000;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.RESPAWN_ANCHOR_CHARGE;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.NIHILISTIC;
        }
    }

    protected void markHurt() {
        this.hurtMarked = false;
    }

    public void Knockback (LivingEntity lie) {
        double x = NihilisticLord.this.getX();
        double y = NihilisticLord.this.getY();
        double z = NihilisticLord.this.getZ();
        lie.push(x / z * 0, 1, y / z * 0);
    }

    public void teleport() {
        if (this.getTarget() != null) {
            ServerLevel $$0 = (ServerLevel)this.level();
            double d = Mth.randomBetween(RandomSource.create(), 10, -10);
            double x = this.getTarget().getX();
            double y = this.getY();
            double z = this.getTarget().getZ();
            double x1 = this.getX();
            double d3 = x + d;
            double d4 = z + d;
            double d5 = this.getZ();
            double d6 = this.random.nextDouble() + 2;
            if (this.getPhase() == 2) {
                WaterTrap $$1 = (NoixmodAPIEntities.WATER_TRAP.get()).create(this.level());
                if ($$1 != null) {
                    $$1.moveTo(x1, y, d5);
                    $$1.setOwner(this);
                    $$0.addFreshEntity($$1);
                }
            }
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 0.5f, 1f);
            for (int $$t=0;$$t<10;++$$t) {
                this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get(), x1, y, d5, 0, 0.1, 0);
            }
            this.randomTeleport(d3, y + d6, d4, false);
        }
    }

    class RangedSummonSpellGoal extends UseSpellGoal {
        NihilisticLord lord = NihilisticLord.this;

        public RangedSummonSpellGoal() {}

        @Override
        protected void castSpell() {
            ServerLevel $$0 = (ServerLevel) lord.level();
            for (int nt = 0; nt < 1; ++nt) {
                NihilisticBlaze $$1 = (NoixmodAPIEntities.NIHILISTIC_BLAZE.get()).create(lord.level());
                if ($$1 != null) {
                    $$1.moveTo(lord.getX(), lord.getY(), lord.getZ());
                    $$1.setOwner(lord);
                    $$0.addFreshEntityWithPassengers($$1);
                }
            }
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
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.BLAZE_AMBIENT;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.FIRE;
        }
    }

    static class SetSecondPhaseGoal <T extends NihilisticLord>
    extends Goal {
        public final T mob;

        public SetSecondPhaseGoal(T entity) {
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.mob = entity;
        }

        @Override
        public void tick() {
            super.tick();
            this.mob.isSettingSecondPhase = true;
        }

        @Override
        public boolean canUse() {
            return this.mob.getPhase() == 1 && this.mob.getHealth() <= this.mob.getMaxHealth() / 2;
        }
    }

    protected static class LordNearestAttackGoal<T extends LivingEntity>
    extends NearestAttackableTargetGoal<T> {
        public LordNearestAttackGoal(Mob p_26060_, Class<T> p_26061_, boolean p_26062_) {
            super(p_26060_, p_26061_, p_26062_);
        }

        @Override
        public boolean canUse() {
            if (NoixmodAPIMainConfig.HorrorMode.get()) {
                return this.target instanceof Enemy;
            }
            return super.canUse();
        }
    }

    protected static class LordHurtByTargetGoal
    extends HurtByTargetGoal {
        public LordHurtByTargetGoal(PathfinderMob p_26039_, Class<?>... p_26040_) {
            super(p_26039_, p_26040_);
        }

        @Override
        public boolean canUse() {
            if (NoixmodAPIMainConfig.HorrorMode.get()) {
                return this.targetMob instanceof Enemy;
            }
            return super.canUse();
        }
    }

    static class LordRangedBowAttackGoal<T extends NihilisticLord>
    extends Goal {
        public final T mob;
        public int attackIntervalMin;
        public final float attackRadiusSqr;
        public int attackTime = -1;
        public int seeTime;
        public boolean strafingClockwise;
        public boolean strafingBackwards;
        public int strafingTime = -1;

        public LordRangedBowAttackGoal(T p_25792_, float p_25795_) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.mob = p_25792_;
            this.attackRadiusSqr = p_25795_ * p_25795_;
        }

        @Override
        public boolean canUse() {
            return this.mob.getTarget() != null && this.isHoldingBow();
        }

        protected boolean isHoldingBow() {
            return this.mob.isHolding((is) -> is.getItem() instanceof BowItem);
        }

        @Override
        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.navigation.isDone()) && this.isHoldingBow() && !this.mob.isCastingSpell();
        }

        @Override
        public void start() {
            super.start();
            this.mob.setAggressive(true);
        }

        @Override
        public void stop() {
            super.stop();
            this.mob.setAggressive(false);
            this.seeTime = 0;
            this.attackTime = -1;
            if (this.mob.getTarget() == null) {
                this.mob.stopUsingItem();
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1) {
                    this.seeTime = 0;
                }
                if (flag) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }
                if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.mob.navigation.stop();
                    ++this.strafingTime;
                } else {
                    this.mob.navigation.moveTo(livingentity, this.mob.isCastingSpell() ? 0.05:0.5);
                    this.strafingTime = -1;
                }
                if (this.strafingTime >= 20) {
                    if ((double) this.mob.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }
                    if ((double) this.mob.getRandom().nextFloat() < 0.3) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }
                    this.strafingTime = 0;
                }
                if (this.strafingTime > -1) {
                    if (d0 > (double) (this.attackRadiusSqr * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if (d0 < (double) (this.attackRadiusSqr * 0.25F)) {
                        this.strafingBackwards = true;
                    }
                    this.mob.moveControl.strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    Entity entity = this.mob.getControlledVehicle();
                    if (entity instanceof Mob) {
                        NihilisticLord mob = (NihilisticLord) entity;
                        mob.lookAt(livingentity, 30.0F, 30.0F);
                    }
                    this.mob.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }
                if (!this.mob.isSettingSecondPhase) {
                    if (this.mob.isUsingItem()) {
                        if (!flag && this.seeTime < -60) {
                            this.mob.stopUsingItem();
                        } else if (flag) {
                            int i = (this.mob.getTicksUsingItem());
                            if (i >= 20 && !this.mob.isCastingSpell()) {
                                this.mob.stopUsingItem();
                                this.mob.performRangedAttack(livingentity, (BowItem.getPowerForTime(i) / 1.5f));
                                if (this.mob.getPhase() == 2) {
                                    this.mob.ShootWitherSkull();
                                    List<LivingEntity> $$0 = this.mob.level().getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate(5.0));
                                    for (LivingEntity livingEntity : $$0) {
                                        if (livingEntity instanceof NihilisticZombie || livingEntity instanceof NihilityMobs || livingEntity instanceof NihilisticLord || livingEntity instanceof NihilisticBlaze) {
                                            livingEntity.heal(1);
                                        } else if (!(livingEntity instanceof Player player && player.isCreative())) {
                                            livingEntity.hurt(this.mob.damageSources().fellOutOfWorld(), 10);
                                            this.mob.Knockback(livingEntity);
                                        }
                                    }
                                }
                            }
                            this.attackTime = attackIntervalMin;
                        }
                    } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                        this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, (item) -> item instanceof BowItem));
                    }
                }
            }
        }
    }

    public void ShootWitherSkull() {
        LivingEntity lie = this.getTarget();
        double d0 = this.getX(1);
        double d1 = this.getY(1);
        double d2 = this.getZ(1);
        double d3;
        if (lie != null) {
            d3 = lie.getX() - d0;
            double d5 = lie.getZ() - d2;
            double d4 = lie.getY() - d1;
            WitherSkull witherskull = new WitherSkull(this.level(), this, d3, d4 + 0.5, d5);
            witherskull.setOwner(this);
            witherskull.setDangerous(false);
            witherskull.setPosRaw(d0, d1, d2);
            this.level().addFreshEntity(witherskull);
        }
        this.heal(5f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Nihilist.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.MAX_HEALTH, 640)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.ARMOR_TOUGHNESS, 4)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85)
                .add(Attributes.ATTACK_KNOCKBACK, 1);
    }
}
