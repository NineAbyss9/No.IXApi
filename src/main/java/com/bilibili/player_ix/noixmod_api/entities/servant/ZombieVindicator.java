
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.api.mobs.ApiMobType;
import com.github.NineAbyss9.ix_api.api.mobs.IConversion;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.OwnableNihilist;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ZombieVindicator
extends OwnableNihilist
implements Ownable, IConversion {
    private boolean isArmored = false;
    private int startTicks = 20;
    private static final EntityDataAccessor<Integer> CONVERSION_TICK;
    protected static final AttributeModifier FAST_SPEED = new AttributeModifier(
            "1Player_IX2-931-ZV-FastSpeed",
            0.1, AttributeModifier.Operation.ADDITION);
    protected static final AttributeModifier DAMAGE_PLUS = new AttributeModifier(
            "1Player_IX2-931-ZV-DamagePlus",
            3, AttributeModifier.Operation.ADDITION);
    public ZombieVindicator(EntityType<? extends ZombieVindicator> type, Level level) {
        super(type, level);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        this.enchantSpawnedWeapon(level.random, 3);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CONVERSION_TICK, -1);
    }

    public void aiStep() {
        super.aiStep();
    }

    public void tick() {
        super.tick();
        if (this.startTicks > 0) {
            this.setStartTicks(this.startTicks - 1);
            if (this.fallDistance == 0 && !this.onGround()) {
                this.makeParticle();
            }
        }
        this.convertTick();
    }

    public void setStartTicks(int i) {
        this.startTicks = i;
    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.ZOMBIE_VILLAGER.getDefaultLootTable();
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.ZOMBIE_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_VILLAGER_DEATH;
    }

    public float getVoicePitch() {
        return 0.7f;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new ApiMeleeAttackGoal(this, 1, false, false));
        this.goalSelector.addGoal(4, new OwnableMob.FollowOwnerGoal<>(this, 0.75,
                30f, 7f, false));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.targetSelector.addGoal(0, new ApiOwnerTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnableMob.OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(2, new OwnableMob.OwnableTargetGoal<>(this, false));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public void setArmored(boolean is) {
        if (is) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        }
        this.isArmored = is;
    }

    public boolean isArmored() {
        return this.isArmored;
    }

    protected InteractionResult mobInteract(Player p_21472_, InteractionHand p_21473_) {
        ItemStack stack = p_21472_.getItemInHand(p_21473_);
        if (stack.is(Items.GOLDEN_APPLE) && this.hasEffect(MobEffects.WEAKNESS)) {
            this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE);
            this.setConversionTick(Maths.toTick(60));
            return InteractionResult.SUCCESS;
        } else if (stack.is(Items.GOLDEN_APPLE) && this.hasEffect(MobEffects.LUCK)) {
            this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE);
            this.setConversionTick(Maths.toTick(60));
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(p_21472_, p_21473_);
    }

    public boolean isConverting() {
        return this.getConversionTick() != -1;
    }

    public int getConversionTick() {
        return this.entityData.get(CONVERSION_TICK);
    }

    public void setConversionTick(int tick) {
        this.entityData.set(CONVERSION_TICK, tick);
    }

    public void performConvert() {
        if (!this.level().isClientSide) {
            Mob mob;
            ServerLevel serverLevel = this.serverLevel();
            if (this.hasEffect(MobEffects.LUCK))
                mob = EntityType.VILLAGER.create(serverLevel);
            else if (this.hasEffect(MobEffects.WEAKNESS))
                mob = NoixmodAPIEntities.PILLAGER_SERVANT.get().create(serverLevel);
            else
                mob = null;
            if (mob != null) {
                this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
                mob.moveTo(position());
                WorldUtil.nullableFinalizeSpawn(mob, serverLevel, serverLevel.getCurrentDifficultyAt(blockPosition()),
                        MobSpawnType.CONVERSION);
                serverLevel.addFreshEntity(mob);
                this.discard();
            }
        }
    }

    public void addEffect() {
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (speed != null) {
            if (!speed.hasModifier(FAST_SPEED)) {
                speed.addTransientModifier(FAST_SPEED);
            }
        }
        if (damage != null) {
            if (!damage.hasModifier(DAMAGE_PLUS)) {
                damage.addTransientModifier(DAMAGE_PLUS);
            }
        }
    }

    public NihilistArmPose getArmPose() {
        if (this.isAggressive()) {
            return NihilistArmPose.ZOMBIE_ATTACKING;
        } else if (!this.getLord().isEmpty()) {
            return NihilistArmPose.SPELL_CASTING;
        }
        return NihilistArmPose.NONE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ZombieVindicator.createPathAttributes()
                .add(Attributes.MAX_HEALTH, 30).add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.MOVEMENT_SPEED, 0.27)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25)
                .add(Attributes.ARMOR, 2).add(Attributes.ATTACK_DAMAGE, 4);
    }

    public boolean canAttack(LivingEntity lie) {
        return super.canAttack(lie);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.IN_WALL)) {
            return false;
        }
        if (this.startTicks > 0) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public MobType getMobType() {
        return ApiMobType.NIHILISTIC_UNDEAD;
    }

    public void makeParticle() {
        if (this.level().isClientSide()) {
            double x = this.getRandom().nextGaussian() * 0.2;
            double y = this.getRandom().nextGaussian() * 0.2;
            double z = this.getRandom().nextGaussian() * 0.2;
            this.level().addParticle(ParticleTypes.SOUL, this.getRandomX(0.5), this.getRandomY() + 0.5,
                    this.getRandomZ(0.5), x, y, z);
        }
    }

    static {
        CONVERSION_TICK = SynchedEntityData.defineId(ZombieVindicator.class, EntityDataSerializers.INT);
    }
}
