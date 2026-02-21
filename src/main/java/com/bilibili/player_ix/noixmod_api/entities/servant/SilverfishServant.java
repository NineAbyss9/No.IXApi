
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IntimacyData;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.ItemUtil;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.worm.Worm;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SilverfishServant
extends OwnableMob {
    private static final ItemStack STONE;
    private final IntimacyData intimacyData;
    private static final EntityDataAccessor<Integer> DATA_COOLDOWN;
    public SilverfishServant(EntityType<? extends SilverfishServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.intimacyData = new IntimacyData(this);
        this.intimacyData.reset();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COOLDOWN, 600);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1));
        this.addBehaviorGoal(4, 0.6, 10F, true, false);
        this.addTargetGoal();
    }

    public void tick() {
        this.yBodyRot = this.getYRot();
        if (this.getCooldown() > 0) {
            this.setCooldown(this.getCooldown() - this.getNext());
        } else {
            resetCooldown();
            this.playSound(SoundEvents.ITEM_PICKUP);
            this.spawnAtLocation(STONE);
        }
        super.tick();
    }

    public void setYBodyRot(float p_33553_) {
        this.setYRot(p_33553_);
        super.setYBodyRot(p_33553_);
    }

    protected MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    public float getWalkTargetValue(BlockPos p_33530_, LevelReader p_33531_) {
        return InfestedBlock.isCompatibleHostBlock(p_33531_.getBlockState(p_33530_.below())) ?
                10.0F : super.getWalkTargetValue(p_33530_, p_33531_);
    }

    protected float getStandingEyeHeight(Pose p_21131_, EntityDimensions p_21132_) {
        return 0.13f;
    }

    public double getMyRidingOffset() {
        return 0.1;
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(Items.STONE);
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (this.isFood(stack)) {
            ItemUtil.shrink(stack, pPlayer);
            if (this.isUnowned()) {
                if (!this.level().isClientSide) {
                    ParticleUtil.addParticleAroundSelf(this, ParticleTypes.HEART, 12);
                }
                this.setOwner(pPlayer);
            } else {
                this.intimacyData.increase();
            }
            return InteractionResult.CONSUME;
        } else if (stack.is(NoixmodAPIItems.WORM_REAGENT.get())) {
            if (!this.level().isClientSide) {
                ServerLevel serverLevel = this.serverLevel();
                Worm worm = NoixmodAPIEntities.WORM.get().create(serverLevel);
                if (worm != null) {
                    worm.moveTo(position());
                    this.spawnAnim();
                    serverLevel.addFreshEntity(worm);
                    this.discard();
                }
            }
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(pPlayer, pHand);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.intimacyData.addAdditionalData(tag);
        tag.putInt("Cooldown", this.getCooldown());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.intimacyData.readAdditionalData(tag);
        if (tag.contains("Cooldown")) {
            this.setCooldown(tag.getInt("Cooldown"));
        }
    }

    public int getNext() {
        LivingEntity owner = this.getOwner();
        int next = 1;
        if (owner != null) {
            if (owner.getDisplayName().getString().equals("wu1wu2")) {
                next *= 2;
            }
            if (this.intimacyData.getIntimacy() > 10) {
                next *= 2;
            }
        }
        return next;
    }

    public void resetCooldown() {
        this.setCooldown(600);
    }

    public int getCooldown() {
        return this.entityData.get(DATA_COOLDOWN);
    }

    public void setCooldown(int cooldown) {
        this.entityData.set(DATA_COOLDOWN, Mth.clamp(cooldown, 0, Integer.MAX_VALUE));
    }

    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    protected void playStepSound(BlockPos p_33543_, BlockState p_33544_) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33549_) {
        return SoundEvents.SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 42)
                .build();
    }

    static {
        STONE = ItemStacks.of(Items.STONE);
        DATA_COOLDOWN = SynchedEntityData.defineId(SilverfishServant.class, EntityDataSerializers.INT);
    }
}
