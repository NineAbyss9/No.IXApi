
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.Prototype;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.ix_api.util.UnmodifiableList;
import com.bilibili.player_ix.noixmod_api.entities.ai.Player7AI;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Prototype(prototype = "bilibili@Player__7")
public class Player__7 extends ApiPathfinderMob {
    public int attackTick;
    public AnimationState attack = new AnimationState();
    public AnimationState summon = new AnimationState();
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private static final ImmutableList<SensorType<? extends Sensor<? super Player__7>>> SENSORS;
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES;
    private final Player7AI ai = new Player7AI();
    public Player__7(EntityType<? extends Player__7> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 771;
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                if (this.getCurrentFlag() == 1) {
                    this.stopAllAnimations();
                    this.summon.startIfStopped(this.tickCount);
                }
                if (this.getCurrentFlag() == 2) {
                    this.stopAllAnimations();
                    this.summon.startIfStopped(this.tickCount);
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void tick() {
        super.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getCurrentFlag() == 1) {
            ++this.attackTick;
        } else if (this.getCurrentFlag() == 2) {
            ++this.attackTick;
        }
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("Player7Brain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        Player7AI.tick(this);
        super.customServerAiStep();
    }


    @SuppressWarnings("unchecked")
    public Brain<Player__7> getBrain() {
        return (Brain<Player__7>)super.getBrain();
    }

    protected Brain.Provider<Player__7> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSORS);
    }

    protected Brain<?> makeBrain(Dynamic<?> p_21069_) {
        return this.ai.makeBrain(this, this.brainProvider().makeBrain(p_21069_));
    }

    private void resetAttackTick() {
        this.attackTick = 0;
    }

    private int getCurrentFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    private void setFlag(int flag) {
        this.entityData.set(DATA_FLAGS, flag);
    }

    public List<AnimationState> states() {
        return UnmodifiableList.of(
                this.attack, this.summon
        );
    }

    private void stopAllAnimations() {
        for (AnimationState state : states()) {
            state.stop();
        }
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.PLAYER_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(Player__7.class, EntityDataSerializers.INT);
        SENSORS = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS,
                SensorType.HURT_BY);
        MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType
                        .NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType
                        .WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType
                        .ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType
                        .INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType
                        .UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType
                        .HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType
                        .ATE_RECENTLY, MemoryModuleType.NEAREST_REPELLENT);
    }
}
