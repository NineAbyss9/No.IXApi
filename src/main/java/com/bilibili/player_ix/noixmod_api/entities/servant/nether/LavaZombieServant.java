
package com.bilibili.player_ix.noixmod_api.entities.servant.nether;

import com.bilibili.player_ix.noixmod_api.entities.servant.AbstractZombieServant;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class LavaZombieServant
extends AbstractZombieServant {
    private final MobUtils mobUtils = new MobUtils(this);
    protected final OwnerSummon ownerSummon = new OwnerSummon(this);
    public LavaZombieServant(EntityType<? extends AbstractZombieServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.setMaxUpStep(1.2F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new AttackGoal(this, 1));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AbstractZombieServant.class)
                .setAlertOthers());
        this.targetSelector.addGoal(2, new OwnableTargetGoal<>(this, true));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal<>(this));
    }

    public void tick() {
        super.tick();
    }

    public void customServerAiStep() {
        super.customServerAiStep();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType
            pReason, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag pDataTag) {
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, p_21437_, pDataTag);
    }

    protected void populateDefaultItems() {
        Random rand = new Random();
        if (rand.nextFloat() > 0.6F) {
            int $$2 = rand.nextInt(16);
            if ($$2 < 10) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
        }
    }

    protected void populateDefaultEquipmentEnchantments(RandomSource p_217063_, DifficultyInstance p_217064_) {
        if (p_217063_.nextInt(8) == 0) {
            EnchantmentHelper.enchantItem(p_217063_, this.getMainHandItem(), 3, false);
        }
    }

    public boolean shouldBurn() {
        return false;
    }

    public boolean fireImmune() {
        return true;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.HUSK_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        this.playSound(SoundEvents.HUSK_STEP);
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        if (p_20122_.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        return super.isInvulnerableTo(p_20122_);
    }

    public ParticleOptions getAmbientParticle() {
        return ParticleTypes.FALLING_LAVA;
    }

    public int getAffectTime() {
        if (this.getMobUtils().isEasy()) {
            return 8;
        } else if (this.getMobUtils().isNormal()) {
            return 10;
        } else if (this.getMobUtils().isHard()) {
            return 12;
        }
        return 8;
    }

    public MobUtils getMobUtils() {
        return mobUtils;
    }

    public void affect(LivingEntity living) {
        this.breatheParticle(living);
        living.setSecondsOnFire(this.getAffectTime());
    }

    public void breatheParticle(LivingEntity target) {
        if (!target.level().isClientSide) {
            WorldUtil.sendParticles(ParticleTypes.FLAME, target, 8, 1, 1, 1, 0);
        }
    }

    public float getLightLevelDependentMagicValue() {
        return 3f;
    }
}
