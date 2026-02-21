
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiNeutralMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IActive;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Fighter
extends Nihilist
implements ApiNeutralMob, IActive {
    @Nullable
    private UUID persistentAngerTarget;
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE;
    private static final EntityDataAccessor<Boolean> DATA_FAILED;
    public Fighter(EntityType<? extends Fighter> fighter, Level level) {
        super(fighter, level);
        this.xpReward = 15;
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        EnchantmentHelper.enchantItem(level.random, this.getMainHandItem(), 4, false);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ACTIVE, false);
        this.entityData.define(DATA_FAILED, false);
    }

    @Override
    public NihilistArmPose getArmPose() {
        if (this.isAggressive()) {
            return NihilistArmPose.ATTACKING;
        } else if (!this.getLord().isEmpty()) {
            return NihilistArmPose.SPELL_CASTING;
        }
        return NihilistArmPose.CROSSED;
    }

    public Fighter(PlayMessages.SpawnEntity entity, Level level) {
        this(NoixmodAPIEntities.FIGHTER.get(), level);
        entity.getEntity();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.VINDICATOR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return EntityType.VINDICATOR.getDefaultLootTable();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FighterMeleeAttackGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.5));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.isActive()) {
            this.setActive(true);
            Entity entity = pSource.getEntity();
            if (entity instanceof LivingEntity living && super.canAttack(living)) {
                this.setTarget(living);
            }
            return false;
        } else {
            pAmount = Math.min(10F, pAmount);
            float health = this.getHealth() - pAmount;
            if (health <= 0) {
                if (this.isAlive()) {
                    if (!this.isFailed()) {
                        this.setHealth(this.getMaxHealth());
                        this.setFailed(true);
                        this.setTarget(null);
                        this.setAggressive(false);
                        return false;
                    }
                }
            }
            return super.hurt(pSource, pAmount);
        }
    }

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    public void setActive(boolean value) {
        this.entityData.set(DATA_ACTIVE, value);
    }

    public boolean isFailed() {
        return this.entityData.get(DATA_FAILED);
    }

    public void setFailed(boolean flag) {
        this.entityData.set(DATA_FAILED, flag);
    }

    @Override
    protected InteractionResult mobInteract(Player p_21472_, InteractionHand p_21473_) {
        return super.mobInteract(p_21472_, p_21473_);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Nihilist.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ARMOR, 2)
                .add(Attributes.MAX_HEALTH, 90).add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75).add(Attributes.FOLLOW_RANGE, 64);
    }

    static {
        DATA_ACTIVE = SynchedEntityData.defineId(Fighter.class, EntityDataSerializers.BOOLEAN);
        DATA_FAILED = SynchedEntityData.defineId(Fighter.class, EntityDataSerializers.BOOLEAN);
    }

    private static class FighterMeleeAttackGoal extends ApiMeleeAttackGoal {
        final Fighter fighter;
        public FighterMeleeAttackGoal(Fighter finder) {
            super(finder, 1.2, 8.5);
            this.fighter = finder;
        }

        @Override
        public boolean canUse() {
            if (this.fighter.isFailed()) {
                return false;
            }
            return this.fighter.isActive() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if (!this.fighter.isActive()) {
                return false;
            }
            return !this.fighter.isFailed() && super.canContinueToUse();
        }
    }
}
