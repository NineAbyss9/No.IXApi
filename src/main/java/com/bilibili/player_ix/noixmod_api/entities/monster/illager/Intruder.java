
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import org.NineAbyss9.util.Option;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.util.BlockUtil;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class Intruder
extends APISpellcaster {
    protected int attackTicks = 0;
    protected int escapeTicks = 0;
    protected int hurtCount = 0;
    protected int hurtCooldown = 0;
    private final ServerBossEvent event;
    protected static final AttributeModifier JUMPING_DAMAGE = new AttributeModifier(
            "1Player_IX2-931-Intruder-Damage", 0.5,
            AttributeModifier.Operation.MULTIPLY_BASE);
    private static final EntityDataAccessor<Boolean> DATA_BOSS;
    public Intruder(EntityType<Intruder> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 15;
        event = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED,
                BossEvent.BossBarOverlay.NOTCHED_10);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NoixmodAPIItems.UNINVITED_SWORD.get()));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BOSS, false);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(4, new IntruderAttackGoal(this));
        OwnableMob.addBehaviorGoals(this, 5, 0.7, 10F, true, true);
        this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
    }

    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {}

    public void setAttackTicks(int ticks) {
        this.attackTicks = ticks;
    }

    public void setHurtCount(int count) {
        this.hurtCount = count;
    }

    public void resetAttackTicks() {
        this.setAttackTicks(0);
    }

    public void resetHurtCount() {
        this.setHurtCount(0);
    }

    public void setEscapeTicks(int ticks) {
        this.escapeTicks = ticks;
    }

    public void resetEscapeTicks() {
        this.setEscapeTicks(Maths.toTick(15));
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {return false;}

    public boolean randomTeleport(double p_20985_, double p_20986_, double p_20987_, boolean p_20988_) {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        double d3 = p_20986_;
        boolean flag = false;
        BlockPos blockpos = BlockPos.containing(p_20985_, p_20986_, p_20987_);
        Level level = this.level();
        if (ApiPathfinderMob.hasChunkAt(this, blockpos)) {
            boolean flag1 = false;
            while(!flag1 && blockpos.getY() > level.getMinBuildHeight()) {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = level.getBlockState(blockpos1);
                if (BlockUtil.isMotion(blockstate)) {
                    flag1 = true;
                } else {
                    --d3;
                    blockpos = blockpos1;
                }
            }
            if (flag1) {
                this.teleportTo(p_20985_, d3, p_20987_);
                if (level.noCollision(this) && !level.containsAnyLiquid(this.getBoundingBox())) {
                    flag = true;
                }
            }
        }
        if (!flag) {
            this.teleportTo(d0, d1, d2);
            return false;
        } else {
            if (p_20988_) {
                level.broadcastEntityEvent(this, (byte)46);
            }
            return true;
        }
    }

    protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
    }

    public void teleport() {
        LivingEntity target = this.getTarget();
        this.summonCloud();
        if (!this.level().isClientSide) {
            ParticleUtil.sendParticles((ServerLevel)this.level(), ParticleTypes.FLASH, this.position()
                            .add(0, 0.5, 0), 1, 0, 0, 0, 0);
            EntityEventHandler.broadcastEntityEvent(this, 0);
        }
        this.playSound(SoundEvents.AMBIENT_CAVE.get());
        double x = (target == null ? this.getX() : target.getX())
                + Mth.randomBetween(this.getRandom(), -6, 6);
        double z = (target == null ? this.getZ() : target.getZ())
                + Mth.randomBetween(this.getRandom(), -6, 6);
        for (int i = 0;i < 10;++i) {
            if (this.randomTeleport(x, this.getY(), z, false)) {
                break;
            }
        }
        if (target != null) {
            this.getNavigation().moveTo(target, 1);
        }
    }

    public void summonCloud() {
        AreaEffectCloud cloud = new AreaEffectCloud(EntityType.AREA_EFFECT_CLOUD, this.level());
        cloud.setOwner(this);
        cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Maths.toTick(10), 0));
        cloud.addEffect(new MobEffectInstance(MobEffects.DARKNESS, Maths.toTick(3), 0));
        cloud.moveTo(this.blockPosition(), 0, 0);
        cloud.setDuration(Maths.toTick(10));
        this.playSound(SoundEvents.SPLASH_POTION_BREAK);
        this.level().addFreshEntity(cloud);
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        if (p_20122_.is(DamageTypeTags.IS_FALL)) {
            return true;
        }
        if (p_20122_.is(DamageTypes.IN_WALL)) {
            return true;
        }
        return super.isInvulnerableTo(p_20122_);
    }

    public boolean hurt(DamageSource p_37849_, float p_37850_) {
        if (this.hurtCooldown > 0 || this.escapeTicks > Maths.toTick(12)) {
            return false;
        } else {
            if (p_37849_.is(DamageTypeTags.IS_FALL)) {
                return false;
            }
            if (p_37849_.is(DamageTypes.IN_WALL)) {
                return false;
            }
            ++this.hurtCount;
            this.hurtCooldown = 10;
            return super.hurt(p_37849_, p_37850_);
        }
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
       var var1 = Math.min(5F, p_21241_);
        super.actuallyHurt(p_21240_, var1);
    }

    protected void customServerAiStep() {
        if (this.attackTicks > 0) {
            --this.attackTicks;
        }
        if (this.escapeTicks > 0) {
            --this.escapeTicks;
        }
        if (this.hurtCooldown > 0) {
            --this.hurtCooldown;
        }
        super.customServerAiStep();
        if (this.isBoss()) {
            this.event.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    public void startSeenByPlayer(ServerPlayer p_20119_) {
        super.startSeenByPlayer(p_20119_);
        if (this.isBoss()) {
            this.event.addPlayer(p_20119_);
        }
    }

    public void stopSeenByPlayer(ServerPlayer p_20174_) {
        super.stopSeenByPlayer(p_20174_);
        if (this.isBoss()) {
            this.event.removePlayer(p_20174_);
        }
    }

    private boolean isBoss() {
        return this.entityData.get(DATA_BOSS);
    }

    public void setBoss(boolean flag) {
        this.entityData.set(DATA_BOSS, flag);
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.isBoss();
    }

    public void tick() {
        super.tick();

    }

    public void aiStep() {
        super.aiStep();
        if (this.isAggressive()) {
            if (!this.getMainHandItem().is(NoixmodAPIItems.UNINVITED_SWORD.get())) {
                this.setItemInHand(InteractionHand.MAIN_HAND,
                        ItemStacks.of(NoixmodAPIItems.UNINVITED_SWORD.get()));
                if (this.level().isClientSide) {
                    for (int i = 0; i < 15; i++) {
                        this.level().addParticle(ParticleTypes.ENCHANT, this.getRandomX(0.5), this.getRandomY(),
                                this.getRandomZ(0.5), 0, 0, 0);
                    }
                }
            }
        } else {
            if (this.tickCount % 5 == 0){
                this.heal(1F);
            }
        }
        if (this.level().isClientSide) return;
        var damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null) {
            if (this.onGround()) {
                if (damage.hasModifier(JUMPING_DAMAGE)) {
                    damage.removeModifier(JUMPING_DAMAGE);
                }
            } else {
                if (!damage.hasModifier(JUMPING_DAMAGE)) {
                    damage.addTransientModifier(JUMPING_DAMAGE);
                }
            }
        }
        var target = this.getTarget();
        if (this.hurtCount >= 4) {
            this.teleport();
            this.resetAttackTicks();
            this.resetHurtCount();
        }
        if (this.escapeTicks <= 0) {
            this.resetEscapeTicks();
            if (this.isAggressive() || this.getTarget() != null) {
                this.teleport();
            }
        }
        if (target != null) {
            if (this.distanceToSqr(target) < Maths.square(4) && target.getY() > this.getY() + 0.5
                    && this.onGround()) {
                this.jumpFromGround();
            }
            if (this.tickCount % 10 == 0)
                this.getNavigation().moveTo(target, 1.2);
        }
        if (this.escapeTicks == Maths.toTick(3)) {
            this.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.LINGERING_POTION));
        } else {
            if (this.escapeTicks == Maths.toTick(15)) {
                if (this.getOffhandItem().is(Items.LINGERING_POTION)) {
                    this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
            }
        }
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return Option.ofNullable(SoundEvents.EVOKER_AMBIENT).getIf(this.getTarget() == null);
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {return SoundEvents.VINDICATOR_HURT;}

    protected SoundEvent getDeathSound() {return SoundEvents.VINDICATOR_DEATH;}

    public SoundEvent getCelebrateSound() {return SoundEvents.VINDICATOR_CELEBRATE;}

    public boolean doHurtTarget(Entity p_21372_) {
        boolean flag = super.doHurtTarget(p_21372_);
        if (flag) {
            if (p_21372_ instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.WITHER, Maths.toTick(5), 0));
            }
        }
        return flag;
    }

    public IntruderArmPose getPoses() {
        if (this.isCastingSpell()) {
            return IntruderArmPose.SPELL_CASTING;
        } else if (this.isAggressive()) {
            return IntruderArmPose.ATTACKING;
        }
        return IntruderArmPose.CROSSED;
    }

    public boolean canJoinRaid() {return false;}
    public void setCanJoinRaid(boolean p_37898_) {}
    public boolean canBeLeader() {return false;}
    public boolean startRiding(Entity p_21396_, boolean p_21397_) {return false;}
    public boolean startRiding(Entity p_20330_) {return false;}
    protected boolean canRide(Entity p_20339_) {return false;}
    public boolean fireImmune() {return true;}

    public enum IntruderArmPose {
        ATTACKING,
        CROSSED,
        SPELL_CASTING,
        SWEEPING
    }

    static {
        DATA_BOSS = SynchedEntityData.defineId(Intruder.class, EntityDataSerializers.BOOLEAN);
    }

    private static class IntruderAttackGoal extends ApiMeleeAttackGoal {
        final Intruder intruder;
        public IntruderAttackGoal(Intruder finder) {
            super(finder, 1, Maths.square(1.75));
            this.intruder = finder;
        }

        public void start() {
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                this.mob.getLookControl().setLookAt(livingentity, 30.0F, this.mob.getMaxHeadXRot());
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(livingentity);
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if ((this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0
                        && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 ||
                        livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 ||
                        this.mob.getRandom().nextFloat() < 0.05F)) {
                    this.pathedTargetX = livingentity.getX();
                    this.pathedTargetY = livingentity.getY();
                    this.pathedTargetZ = livingentity.getZ();
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                    if (d0 > 1024.0) {
                        this.ticksUntilNextPathRecalculation += 4;
                    } else if (d0 > 256.0) {
                        this.ticksUntilNextPathRecalculation += 2;
                    }
                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingentity, d0);
            }
        }

        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            double d0 = this.getAttackReachSqr(p_25557_);
            if (p_25558_ <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                MobUtils.healLiving(this.intruder, 1F);
                if (!p_25557_.level().isClientSide()) {
                    this.particle(p_25557_);
                }
                this.intruder.swing(InteractionHand.MAIN_HAND);
                this.intruder.doHurtTarget(p_25557_);
                this.intruder.setAttackTicks(20);
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = reducedTickDelay(20);
        }

        public void particle(LivingEntity living) {
            EntityEventHandler.broadcastEntityEvent(living, 0);
            if (!this.mob.onGround()) {
                EntityEventHandler.broadcastEntityEvent(living, 1);
                living.playSound(SoundEvents.PLAYER_ATTACK_CRIT);
            }
        }
    }
}
