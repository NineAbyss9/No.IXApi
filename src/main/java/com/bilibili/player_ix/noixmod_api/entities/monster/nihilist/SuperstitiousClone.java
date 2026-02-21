
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.OwnableNihilist;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;

public class SuperstitiousClone extends OwnableNihilist {
    protected int hurtCooldown = 0;
    public SuperstitiousClone(EntityType<? extends SuperstitiousClone> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 39;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new OwnableMob.FollowOwnerGoal<>(this, 1, 6, 2, false, 24));
        this.goalSelector.addGoal(1, new MeleeGoal(this, 1));
        OwnableMob.addBehaviorGoals(this, 5, 0.6, 10F, true, true);
        this.targetSelector.addGoal(0, new ApiOwnerTargetGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Nihilist.class));
        this.targetSelector.addGoal(2, new OwnableMob.OwnableTargetGoal<>(this, false));
    }

    @Override
    public void tick() {
        if (this.hurtCooldown > 0) {
            --this.hurtCooldown;
        }
        super.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public boolean isHostile() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_FIRE)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        float var = Superstitious.DAMAGE_CAPE;
        if (this.hurtCooldown <= 0) {
            this.hurtCooldown = 40;
            if (p_21241_ > Superstitious.DAMAGE_CAPE) {
                super.actuallyHurt(p_21240_, var);
            } else {
                super.actuallyHurt(p_21240_, p_21241_);
            }
        }
    }

    @Override
    public boolean isAttackable() {
        return this.hurtCooldown <= 0;
    }

    @Override
    public boolean isInvulnerable() {
        return this.hurtCooldown > 0 || super.isInvulnerable();
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if (p_21372_ instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.STUN.get(), 8, 0));
            living.setYBodyRot(-living.yBodyRot);
            living.setXRot(-living.getXRot());
            living.setYRot(-living.getYRot());
            living.setYHeadRot(-living.getYHeadRot());
        }
        return super.doHurtTarget(p_21372_);
    }

    @Override
    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        this.sendSystemMessage(Component.translatable("message.noixmodapi.destiny"));
        return super.killedEntity(p_216988_, p_216989_);
    }

    private static class MeleeGoal extends ApiMeleeAttackGoal {
        public MeleeGoal(SuperstitiousClone finder, double speed) {
            super(finder, speed, Maths.square(2.5));
        }

        @Override
        protected int getAttackCooldown() {
            return 1;
        }
    }
}
