
package com.bilibili.player_ix.noixmod_api.entities.servant.sculk.warden;

import com.github.NineAbyss9.ix_api.api.Synchronizer;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.sculk.AbstractSculkServant;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.google.common.annotations.VisibleForTesting;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class WardenServant
extends AbstractSculkServant
implements VibrationSystem {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL;
    private static final EntityDataAccessor<Integer> DATA_POWER_LEVEL;
    private int tendrilAnimation;
    private int tendrilAnimationO;
    private int heartAnimation;
    private int heartAnimationO;
    public AnimationState roarAnimationState = new AnimationState();
    public AnimationState sniffAnimationState = new AnimationState();
    public AnimationState emergeAnimationState = new AnimationState();
    public AnimationState diggingAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState sonicBoomAnimationState = new AnimationState();
    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener
            = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
    private final VibrationSystem.User vibrationUser = new WardenServant.VibrationUser();
    private VibrationSystem.Data vibrationData = new VibrationSystem.Data();
    AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
    private static final AttributeModifier DAMAGE_PLUS = new AttributeModifier(
            "1Player_IX2-931-WS-DamagePlus", 2.0,
            AttributeModifier.Operation.MULTIPLY_TOTAL);
    public WardenServant(EntityType<? extends WardenServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(3, new FollowOwnerGoal<>(this, 0.8,
                25.0F, 7.0F, false));
        //this.targetSelector.addGoal(0, new WardenTargetGoal(this));
        this.targetSelector.addGoal(1, new WardenAttackTargetGoal<>(this));
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.hasPose(Pose.EMERGING) ? 1 : 0);
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_219420_) {
        super.recreateFromPacket(p_219420_);
        if (p_219420_.getData() == 1) {
            this.setPose(Pose.EMERGING);
        }
    }

    public boolean checkSpawnObstruction(LevelReader p_219398_) {
        return super.checkSpawnObstruction(p_219398_) && p_219398_.noCollision(this, this.getType()
                .getDimensions().makeBoundingBox(this.position()));
    }

    public boolean isInvulnerableTo(DamageSource p_219427_) {
        return this.isDiggingOrEmerging() && !p_219427_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || super.isInvulnerableTo(p_219427_);
    }

    public boolean isDiggingOrEmerging() {
        return this.hasPose(Pose.DIGGING) || this.hasPose(Pose.EMERGING);
    }

    protected boolean canRide(Entity p_20339_) {
        return false;
    }

    public boolean canDisableShield() {
        return true;
    }

    protected float nextStep() {
        return this.moveDist + 0.55F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 72)
                .add(Attributes.MAX_HEALTH, 500.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.ATTACK_DAMAGE, 30.0);
    }

    public boolean dampensVibrations() {
        return true;
    }

    protected float getSoundVolume() {
        return 4.0F;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return !this.hasPose(Pose.ROARING) && !this.isDiggingOrEmerging() ? this.getAngerLevel().getAmbientSound() : null;
    }

    protected SoundEvent getHurtSound(DamageSource p_219440_) {
        return SoundEvents.WARDEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    protected void playStepSound(BlockPos p_219431_, BlockState p_219432_) {
        this.playSound(SoundEvents.WARDEN_STEP, 2.0F, 1.0F);
    }

    public boolean doHurtTarget(Entity pEntity) {
        this.level().broadcastEntityEvent(this, (byte)4);
        this.playSound(SoundEvents.WARDEN_ATTACK_IMPACT, 10.0F, this.getVoicePitch());
        SonicBoom.setCooldown(this, 40);
        this.heal(this.isPowerful() ? 1.6F : 0.8F);
        return super.doHurtTarget(pEntity);
    }

    public float getSonicBoomDamage() {
        return (float)(this.getAttributeValue(Attributes.ATTACK_DAMAGE) / 3);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIENT_ANGER_LEVEL, 0);
        this.entityData.define(DATA_POWER_LEVEL, 0);
    }

    public int getClientAngerLevel() {
        return this.entityData.get(CLIENT_ANGER_LEVEL);
    }

    private void syncClientAngerLevel() {
        this.entityData.set(CLIENT_ANGER_LEVEL, this.getActiveAnger());
    }

    public void tick() {
        Level var2 = this.level();
        if (!var2.isClientSide && var2 instanceof ServerLevel $$0) {
            VibrationSystem.Ticker.tick($$0, this.vibrationData, this.vibrationUser);
            /*if (this.isPersistenceRequired() || this.requiresCustomPersistence()) {
                WardenServantAi.setDigCooldown(this);
            }*/
        }
        super.tick();
        if (this.level().isClientSide) {
            if (this.tickCount % this.getHeartBeatDelay() == 0) {
                this.heartAnimation = 10;
                if (!this.isSilent()) {
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.WARDEN_HEARTBEAT,
                            this.getSoundSource(), 5.0F, this.getVoicePitch(), false);
                }
            }
            if (this.isPowerful() && this.level().random.nextBoolean())
                this.level().addParticle(ParticleTypes.SCULK_SOUL, this.getRandomX(0.8), this.getRandomY(),
                        this.getRandomZ(0.8), 0, 0, 0);
            this.tendrilAnimationO = this.tendrilAnimation;
            if (this.tendrilAnimation > 0) {
                --this.tendrilAnimation;
            }
            this.heartAnimationO = this.heartAnimation;
            if (this.heartAnimation > 0) {
                --this.heartAnimation;
            }
            switch (this.getPose()) {
                case EMERGING -> this.clientDiggingParticles(this.emergeAnimationState);
                case DIGGING -> this.clientDiggingParticles(this.diggingAnimationState);
            }
        }
    }

    protected void customServerAiStep() {
        ServerLevel $$0 = (ServerLevel)this.level();
        $$0.getProfiler().push("wardenBrain");
        this.getBrain().tick($$0, this);
        this.level().getProfiler().pop();
        super.customServerAiStep();
        /* ((this.tickCount + this.getId()) % 120 == 0) {
            applyDarknessAround($$0, this.position(), this, 20);
        }*/
        if (this.tickCount % 20 == 0) {
            this.angerManagement.tick($$0, this::canTargetEntity);
            this.syncClientAngerLevel();
        }
        WardenServantAi.updateActivity(this);
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 4) {
            this.roarAnimationState.stop();
            this.attackAnimationState.start(this.tickCount);
        } else if (pId == 61) {
            this.tendrilAnimation = 10;
        } else if (pId == 62) {
            this.sonicBoomAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(pId);
        }
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(Items.SCULK) || stack.is(Items.SCULK_CATALYST) || stack.is(Items.SCULK_SENSOR)
                || stack.is(Items.SCULK_SHRIEKER);
    }

    protected float getHealAmount() {
        return isPowerful() ? 20.0F : 10.0F;
    }

    public void onSync(Synchronizer synchronizer) {
        if (!isPowerful() && synchronizer.getMessage().equals("FoodHeal"))
            setPowerPlus();
        super.onSync(synchronizer);
    }

    public boolean isPowerful() {
        return this.getPowerLevel() > 15;
    }

    public int getPowerLevel() {
        return this.entityData.get(DATA_POWER_LEVEL);
    }

    public void setPowerLevel(int level) {
        this.entityData.set(DATA_POWER_LEVEL, level);
    }

    public void setPowerPlus() {
        this.setPowerLevel(this.getPowerLevel() + 1);
        if (this.isPowerful()) {
            AttributeInstance instance = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if (instance != null && !instance.hasModifier(DAMAGE_PLUS)) {
                instance.addTransientModifier(DAMAGE_PLUS);
            }
        }
    }

    private int getHeartBeatDelay() {
        float $$0 = (float)this.getClientAngerLevel() / (float)AngerLevel.ANGRY.getMinimumAnger();
        return 40 - Mth.floor(Mth.clamp($$0, 0.0F, 1.0F) * 30.0F);
    }

    public float getTendrilAnimation(float p_219468_) {
        return Mth.lerp(p_219468_, (float)this.tendrilAnimationO, (float)this.tendrilAnimation) / 10.0F;
    }

    public float getHeartAnimation(float p_219470_) {
        return Mth.lerp(p_219470_, (float)this.heartAnimationO, (float)this.heartAnimation) / 10.0F;
    }

    private void clientDiggingParticles(AnimationState p_219384_) {
        if ((float)p_219384_.getAccumulatedTime() < 4500.0F) {
            RandomSource $$1 = this.getRandom();
            BlockState $$2 = this.getBlockStateOn();
            if ($$2.getRenderShape() != RenderShape.INVISIBLE) {
                for(int $$3 = 0; $$3 < 30; ++$$3) {
                    double $$4 = this.getX() + (double)Mth.randomBetween($$1, -0.7F, 0.7F);
                    double $$5 = this.getY();
                    double $$6 = this.getZ() + (double)Mth.randomBetween($$1, -0.7F, 0.7F);
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, $$2), $$4, $$5, $$6,
                            0.0, 0.0, 0.0);
                }
            }
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_POSE.equals(pKey)) {
            switch (this.getPose()) {
                case EMERGING -> this.emergeAnimationState.start(this.tickCount);
                case DIGGING -> this.diggingAnimationState.start(this.tickCount);
                case ROARING -> this.roarAnimationState.start(this.tickCount);
                case SNIFFING -> this.sniffAnimationState.start(this.tickCount);
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public boolean ignoreExplosion() {
        return this.isDiggingOrEmerging();
    }

    protected Brain<?> makeBrain(Dynamic<?> p_219406_) {
        return WardenServantAi.makeBrain(this, p_219406_);
    }

    @SuppressWarnings("unchecked")
    public Brain<WardenServant> getBrain() {
        return (Brain<WardenServant>) super.getBrain();
    }

    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> p_219413_) {
        Level var3 =  this.level();
        if (!var3.isClientSide)
            p_219413_.accept(this.dynamicGameEventListener, (ServerLevel)var3);
    }

    @Contract("null->false")
    public boolean canTargetEntity(@Nullable Entity pEntity) {
        if (pEntity instanceof LivingEntity entity) {
            if (!MobUtils.canHurt(entity, this))
                return false;
            return this.level() == pEntity.level() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(pEntity)
                    && !this.isAlliedTo(pEntity) && entity.getType() != EntityType.ARMOR_STAND &&
                    !(entity instanceof WardenServant) && !entity.isInvulnerable() && !entity.isDeadOrDying()
                    && this.level().getWorldBorder().isWithinBounds(entity.getBoundingBox());
        }
        return false;
    }

    public void addAdditionalSaveData(CompoundTag p_219434_) {
        super.addAdditionalSaveData(p_219434_);
        DataResult<?> var10000 = AngerManagement.codec(this::canTargetEntity).encodeStart(NbtOps.INSTANCE,
                this.angerManagement);
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((p_219437_) -> p_219434_.put("anger",
                (Tag)p_219437_));
        var10000 = VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData);
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((p_219418_) -> p_219434_.put("listener",
                (Tag)p_219418_));
    }

    public void readAdditionalSaveData(CompoundTag p_219415_) {
        super.readAdditionalSaveData(p_219415_);
        DataResult<?> var10000;
        Logger var10001;
        if (p_219415_.contains("anger")) {
            var10000 = AngerManagement.codec(this::canTargetEntity).parse(new Dynamic<>(NbtOps.INSTANCE, p_219415_
                    .get("anger")));
            var10001 = LOGGER;
            Objects.requireNonNull(var10001);
            var10000.resultOrPartial(var10001::error).ifPresent((p_219394_) -> this.angerManagement =
                    (AngerManagement) p_219394_);
            this.syncClientAngerLevel();
        }
        if (p_219415_.contains("listener", 10)) {
            var10000 = VibrationSystem.Data.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, p_219415_
                    .getCompound("listener")));
            var10001 = LOGGER;
            Objects.requireNonNull(var10001);
            var10000.resultOrPartial(var10001::error).ifPresent((p_281093_) -> this.vibrationData =
                    (Data)p_281093_);
        }
    }

    public boolean killedEntity(ServerLevel pLevel, LivingEntity pEntity) {
        if (!this.isPowerful())
            setPowerPlus();
        return super.killedEntity(pLevel, pEntity);
    }

    private void playListeningSound() {
        if (!this.hasPose(Pose.ROARING)) {
            this.playSound(this.getAngerLevel().getListeningSound(), 10.0F, this.getVoicePitch());
        }
    }

    public AngerLevel getAngerLevel() {
        return AngerLevel.byAnger(this.getActiveAnger());
    }

    private int getActiveAnger() {
        return this.angerManagement.getActiveAnger(this.getTarget());
    }

    public void clearAnger(Entity p_219429_) {
        this.angerManagement.clearAnger(p_219429_);
    }

    public void increaseAngerAt(@Nullable Entity p_219442_) {
        this.increaseAngerAt(p_219442_, 35, true);
    }

    @VisibleForTesting
    public void increaseAngerAt(@Nullable Entity p_219388_, int p_219389_, boolean p_219390_) {
        if (!this.isNoAi() && this.canTargetEntity(p_219388_)) {
            boolean $$3 = !(this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null)
                    instanceof Player);
            int $$4 = this.angerManagement.increaseAnger(p_219388_, p_219389_);
            if (p_219388_ instanceof Player && $$3 && AngerLevel.byAnger($$4).isAngry()) {
                this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
            }
            if (p_219390_) {
                this.playListeningSound();
            }
        }
    }

    public Optional<LivingEntity> getEntityAngryAt() {
        return this.getAngerLevel().isAngry() ? this.angerManagement.getActiveEntity() : Optional.empty();
    }

    @Nullable
    public LivingEntity getTarget() {
        return this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET)
                .orElse(null);
    }

    public boolean removeWhenFarAway(double p_219457_) {
        return false;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData p_219403_, @Nullable CompoundTag pDataTag) {
        this.getBrain().setMemoryWithExpiry(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200L);
        if (pReason != MobSpawnType.SPAWN_EGG) {
            this.setPose(Pose.EMERGING);
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.IS_EMERGING, Unit.INSTANCE,
                    WardenServantAi.EMERGE_DURATION);
            this.playSound(SoundEvents.WARDEN_AGITATED, 5.0F, 1.0F);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, p_219403_, pDataTag);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean $$2 = super.hurt(pSource, pAmount);
        if (!this.level().isClientSide && !this.isNoAi() && !this.isDiggingOrEmerging()) {
            Entity $$3 = pSource.getEntity();
            this.increaseAngerAt($$3, AngerLevel.ANGRY.getMinimumAnger() + 20, false);
            if (this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty() && $$3 instanceof LivingEntity $$4) {
                if (!pSource.isIndirect() || this.closerThan($$4, 5.0)) {
                    this.setAttackTarget($$4);
                }
            }
        }
        return $$2;
    }

    public void setAttackTarget(LivingEntity p_219460_) {
        this.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, p_219460_);
        this.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        SonicBoom.setCooldown(this, 40);
    }

    public void setTarget(@Nullable LivingEntity pTarget) {
        if (pTarget != null) {
            this.setAttackTarget(pTarget);
        } else {
            super.setTarget(null);
        }
    }

    public EntityDimensions getDimensions(Pose p_219392_) {
        EntityDimensions $$1 = super.getDimensions(p_219392_);
        return this.isDiggingOrEmerging() ? EntityDimensions.fixed($$1.width, 1.0F) : $$1;
    }

    public boolean isPushable() {
        return !this.isDiggingOrEmerging() && super.isPushable();
    }

    protected void doPush(Entity p_219353_) {
        if (!this.isNoAi() && !this.getBrain().hasMemoryValue(MemoryModuleType.TOUCH_COOLDOWN)) {
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.TOUCH_COOLDOWN, Unit.INSTANCE, 20L);
            this.increaseAngerAt(p_219353_);
            WardenServantAi.setDisturbanceLocation(this, p_219353_.blockPosition());
        }
        super.doPush(p_219353_);
    }

    @VisibleForTesting
    public AngerManagement getAngerManagement() {
        return this.angerManagement;
    }

    protected PathNavigation createNavigation(Level p_219396_) {
        return new GroundPathNavigation(this, p_219396_) {
            protected PathFinder createPathFinder(int p_219479_) {
                this.nodeEvaluator = new WalkNodeEvaluator();
                this.nodeEvaluator.setCanPassDoors(true);
                return new PathFinder(this.nodeEvaluator, p_219479_) {
                    protected float distance(Node p_219486_, Node p_219487_) {
                        return p_219486_.distanceToXZ(p_219487_);
                    }
                };
            }
        };
    }

    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    static {
        CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(WardenServant.class, EntityDataSerializers.INT);
        DATA_POWER_LEVEL = SynchedEntityData.defineId(WardenServant.class, EntityDataSerializers.INT);
    }

    class VibrationUser implements VibrationSystem.User {
        private final PositionSource positionSource = new EntityPositionSource(WardenServant.this, WardenServant.this
                .getEyeHeight());

        public VibrationUser() {
        }

        public int getListenerRadius() {
            return 16;
        }

        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.WARDEN_CAN_LISTEN;
        }

        public boolean canTriggerAvoidVibration() {
            return true;
        }

        public boolean canReceiveVibration(ServerLevel p_282574_, BlockPos p_282323_, GameEvent p_283003_,
                                           GameEvent.Context p_282515_) {
            if (!WardenServant.this.isNoAi() && !WardenServant.this.isDeadOrDying() && !WardenServant.this.getBrain()
                    .hasMemoryValue(MemoryModuleType.VIBRATION_COOLDOWN) && !WardenServant.this
                    .isDiggingOrEmerging() && p_282574_.getWorldBorder().isWithinBounds(p_282323_)) {
                Entity var6 = p_282515_.sourceEntity();
                if (var6 instanceof LivingEntity $$4) {
                    return WardenServant.this.canTargetEntity($$4);
                }
                return true;
            } else {
                return false;
            }
        }

        public void onReceiveVibration(ServerLevel pLevel, BlockPos pPos, GameEvent pEvent, @Nullable Entity pEntity,
                                       @Nullable Entity p_282582_, float p_283699_) {
            if (!WardenServant.this.isDeadOrDying()) {
                WardenServant.this.brain.setMemoryWithExpiry(MemoryModuleType.VIBRATION_COOLDOWN,
                        Unit.INSTANCE, 40L);
                pLevel.broadcastEntityEvent(WardenServant.this, (byte)61);
                WardenServant.this.playSound(SoundEvents.WARDEN_TENDRIL_CLICKS, 5.0F,
                        WardenServant.this.getVoicePitch());
                BlockPos $$6 = pPos;
                if (p_282582_ != null) {
                    if (WardenServant.this.closerThan(p_282582_, 30.0)) {
                        if (WardenServant.this.getBrain().hasMemoryValue(MemoryModuleType.RECENT_PROJECTILE)) {
                            if (WardenServant.this.canTargetEntity(p_282582_)) {
                                $$6 = p_282582_.blockPosition();
                            }
                            WardenServant.this.increaseAngerAt(p_282582_);
                        } else {
                            WardenServant.this.increaseAngerAt(p_282582_, 10, true);
                        }
                    }
                    WardenServant.this.getBrain().setMemoryWithExpiry(MemoryModuleType.RECENT_PROJECTILE,
                            Unit.INSTANCE, 100L);
                } else {
                    WardenServant.this.increaseAngerAt(pEntity);
                }
                if (!WardenServant.this.getAngerLevel().isAngry()) {
                    Optional<LivingEntity> $$7 = WardenServant.this.angerManagement.getActiveEntity();
                    if (p_282582_ != null || $$7.isEmpty() || $$7.get() == pEntity) {
                        WardenServantAi.setDisturbanceLocation(WardenServant.this, $$6);
                    }
                }
            }
        }
    }

    protected static class WardenTargetGoal extends ApiOwnerTargetGoal {
        public WardenTargetGoal(Mob ownable) {
            super(ownable);
        }

        public boolean canUse() {
            if (!checkTarget())
                return false;
            return super.canUse();
        }

        public boolean canContinueToUse() {
            return checkTarget() && super.canContinueToUse();
        }

        public boolean checkTarget() {
            return //this.mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()
            this.mob.getTarget() == null;
        }

        public void start() {
            super.start();
            LivingEntity entity = this.mob.getTarget();
            if (entity != null)
                ((WardenServant)this.mob).setAttackTarget(entity);
        }
    }

    protected static class WardenAttackTargetGoal<E extends WardenServant> extends OwnerHurtTargetGoal<E> {
        public WardenAttackTargetGoal(E ownable) {
            super(ownable);
        }

        public boolean canUse() {
            return super.canUse();
        }

        public void start() {
            super.start();
            //if (this.mob.getTarget() != null)
            //    this.mob.setAttackTarget(this.mob.getTarget());
        }
    }
}
