
package com.bilibili.player_ix.noixmod_api.entities.villager;

import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Team;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class VillagerGolem
extends OwnableMob
implements Ownable,
NeutralMob {
    private int limitedLifeTicks;
    private boolean hasLimitedLife;
    private int attackAnimationTick;
    private int ran;
    @Nullable
    private UUID targetUUID;

    public VillagerGolem(EntityType<? extends VillagerGolem> p_27508_, Level level) {
        super(p_27508_, level);
        this.xpReward = 3;
        this.setMaxUpStep(2f);
    }

    public boolean isHostile() {
        return false;
    }

    public void tick() {
        super.tick();
        if (this.hasLimitedLife) {
            --this.limitedLifeTicks;
            if (this.limitedLifeTicks < 0) {
                this.discard();
            }
        }
    }

    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        if (!MobUtils.canHurt(this, p_21016_.getEntity())) {
            return false;
        }
        return super.hurt(p_21016_, p_21017_);
    }

    public void setLimitedLife(int p_33988_) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = p_33988_;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_28872_) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    public void aiStep() {
        super.aiStep();
        if (this.attackAnimationTick > 0) {
            --this.attackAnimationTick;
        }
        if (this.getOwner() != null && this.tickCount % 40 == 0) {
            this.heal(1f);
        }
        this.setTargetByOwner();
    }

    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    public boolean doHurtTarget(Entity p_28837_) {
        this.attackAnimationTick = 10;
        this.level().broadcastEntityEvent(this, (byte)4);
        float $$1 = this.getAttackDamage();
        float $$2 = (int)$$1 > 0 ? $$1 / 2.0F + (float)this.random.nextInt((int)$$1) : $$1;
        boolean $$3 = p_28837_.hurt(this.damageSources().mobAttack(this), $$2);
        if ($$3) {
            double var10000;
            if (p_28837_ instanceof LivingEntity $$4) {
                var10000 = $$4.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            } else {
                var10000 = 0.0;
            }
            double $$5 = var10000;
            double $$6 = Math.max(0.0, 1.0 - $$5);
            p_28837_.setDeltaMovement(p_28837_.getDeltaMovement().add(0.0, 0.4000000059604645
                    * $$6, 0.0));
            this.doEnchantDamageEffects(this, p_28837_);
        }
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return $$3;
    }

    public void handleEntityEvent(byte p_28844_) {
        if (p_28844_ == 4) {
            this.attackAnimationTick = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.handleEntityEvent(p_28844_);
        }
    }

    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0f, 1.0f);
    }

    public int getRemainingPersistentAngerTime() {
        return this.ran;
    }

    public void setRemainingPersistentAngerTime(int i) {
        this.ran = i;
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.targetUUID;
    }

    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.targetUUID = uuid;
        if (uuid != null && this.getOwnerUUID() == uuid) {
            this.targetUUID = null;
        }
    }

    public void startPersistentAngerTimer() {
        this.ran = 10;
    }

    public boolean canAttack(LivingEntity p_21171_) {
        if (!MobUtils.canHurt(p_21171_, this)) {
            return false;
        }
        return super.canAttack(p_21171_);
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1.0f, false, false));
        this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6,
                false));
        this.goalSelector.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.6));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                5, false, false, p_26054_ -> p_26054_ instanceof Enemy));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this,
                false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return VillagerGolem.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(
                Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.KNOCKBACK_RESISTANCE,
                1.0).add(Attributes.ATTACK_DAMAGE, 10.0);
    }

    @Nullable
    public Team getTeam() {
        LivingEntity entity = this.getOwner();
        if (entity != null && !this.areBothOwner(entity)) {
            return entity.getTeam();
        }
        return super.getTeam();
    }
}
