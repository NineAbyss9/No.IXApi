
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IAgeableMob;
import com.bilibili.player_ix.noixmod_api.entities.servant.animal.AbstractSpiderServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PlayMessages;

public class FreakySpider extends AbstractSpiderServant {
    public FreakySpider(EntityType<? extends FreakySpider> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.setMaxUpStep(3f);
        this.xpReward = 15;
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel p_27564_, IAgeableMob p_27565_) {
    }

    @Override
    public boolean isInLove() {
        return false;
    }

    static class SpiderAttackGoal extends MeleeAttackGoal {
        public SpiderAttackGoal(FreakySpider p_33822_) {
            super(p_33822_, 1.0, true);
        }

        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        protected double getAttackReachSqr(LivingEntity p_33825_) {
            return 4.0F + p_33825_.getBbWidth();
        }
    }

    protected void playStepSound(BlockPos p_33804_, BlockState p_33805_) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.1F, 0.75F);
    }

    public FreakySpider(PlayMessages.SpawnEntity packet, Level world) {
        this(NoixmodAPIEntities.FREAKY_SPIDER.get(), world);
        packet.getEntity();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.7f));
        this.goalSelector.addGoal(2, new SpiderAttackGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, net.minecraft.world.entity.animal.Animal.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, net.minecraft.world.entity.animal.AbstractGolem.class, false));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.FOLLOW_RANGE, 30);
    }

    @Override
    public boolean isHostile() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() == this.getOwner()) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }
}
