
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiPathfinderMob;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.mod.APIMonster;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PlateauBeast
extends APIMonster {
    public AnimationState attacking;
    public AnimationState walking;
    public int attackTicks;
    private static final EntityDataAccessor<Integer> DATA_FLAG_ID
            = SynchedEntityData.defineId(PlateauBeast.class, EntityDataSerializers.INT);
    public PlateauBeast(EntityType<? extends PlateauBeast> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = XP_REWARD_MEDIUM;
    }

    public PlateauBeast(PlayMessages.SpawnEntity entity, Level world) {
        this(NoixmodAPIEntities.PLATEAU_BEAST.get(), world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAG_ID, 0);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (DATA_FLAG_ID.equals(p_21104_)) {
           if (this.level().isClientSide) {
               if (this.entityData.get(DATA_FLAG_ID) == 1) {
                   this.attacking.start(this.tickCount);
               } else {
                   this.attacking.stop();
               }
            }
        }
        super.onSyncedDataUpdated(p_21104_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new BeastAttackGoal(this));
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.75));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                false, lie -> lie instanceof Animal));
    }

    public boolean canFreeze() {
        return false;
    }

    public boolean doHurtTarget(Entity p_21372_) {
        boolean flag = super.doHurtTarget(p_21372_);
        if (p_21372_ instanceof LivingEntity living && flag) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200,
                    this.random.nextFloat() <= 0.1f ? 1 : 0));
        }
        this.attackTicks = 10;
        this.entityData.set(DATA_FLAG_ID, 1);
        return flag;
    }

    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        MobUtils.healLiving(this, 1);
        return super.killedEntity(p_216988_, p_216989_);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_GROWL;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.WOLF_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    public float getVoicePitch() {
        return 0.75f;
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypeTags.IS_FREEZING)) {
            amount *= 0.25F;
        }
        if (damageSource.is(DamageTypeTags.IS_FALL)) {
            amount *= 0.5f;
        }
        return super.hurt(damageSource, amount);
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return p_147208_.getEffect() != MobEffects.GLOWING && p_147208_.getEffect()
                != MobEffects.MOVEMENT_SLOWDOWN;
    }

    public void aiStep() {
        super.aiStep();
    }

    public void tick() {
        super.tick();
        if (this.attackTicks > 0) {
            --this.attackTicks;
        }
        if (this.attackTicks == 1) {
            this.attacking.stop();
            this.entityData.set(DATA_FLAG_ID, 0);
        }
        if (this.isInSnowBiomes() || this.onSnowBlocks()) {
            if (!this.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 0));
            }
        } else {
            if (this.hasEffect(MobEffects.MOVEMENT_SPEED) && Objects.requireNonNull(this.getEffect(MobEffects
                    .MOVEMENT_SPEED)).getAmplifier() == 0) {
                this.removeEffect(MobEffects.MOVEMENT_SPEED);
            }
        }
    }

    public static void init() {
            MobUtils.registerSpawn(NoixmodAPIEntities.PLATEAU_BEAST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap
                            .Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Mob
                            .checkMobSpawnRules(entityType, world, reason, pos, random) && NoixmodAPIMainConfig
                            .PlateauBeastCanSummon.get()));
    }

    public boolean onSnowBlocks() {
        return this.getFeetBlockState().is(Blocks.POWDER_SNOW) || this.getFeetBlockState().is(BlockTags.SNOW)
                || this.getFeetBlockState().is(BlockTags.ICE);
    }

    public boolean isInSnowBiomes() {
        return this.level().getBiome(this.blockPosition()).is(BiomeTags.HAS_VILLAGE_SNOWY);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.FOLLOW_RANGE, 60)
                .add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ARMOR, 2)
                .add(Attributes.ATTACK_DAMAGE, 6);
    }

    {
        this.attackTicks = 0;
        this.attacking = new AnimationState();
        this.walking = new AnimationState();
    }

    private static class BeastAttackGoal
    extends ApiMeleeAttackGoal {
        protected final PlateauBeast beast;
        public BeastAttackGoal(PlateauBeast mob) {
            super(mob, 1, false, false);
            this.beast = mob;
        }

        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            super.checkAndPerformAttack(p_25557_, p_25558_);
        }

        public void stop() {
            super.stop();
            this.beast.entityData.set(DATA_FLAG_ID, 0);
        }

        protected int getAttackCooldown() {
            return 20;
        }
    }
}
