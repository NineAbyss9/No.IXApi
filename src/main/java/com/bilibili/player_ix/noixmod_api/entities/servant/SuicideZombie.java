
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.boss.EvokerIllager;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;

public class SuicideZombie
extends OwnableMob
implements Enemy {
    public AnimationState walking;
    public AnimationState aggressive;
    public int trueDeathTime;
    @Nullable
    public LivingEntity lastEntity;
    public SuicideZombie(EntityType<? extends SuicideZombie> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TNT));
    }

    public SuicideZombie(PlayMessages.SpawnEntity entity, Level p_21684_) {
        this(NoixmodAPIEntities.SUICIDE_ZOMBIE.get(), p_21684_);
    }

    public void tick() {
        super.tick();
        this.aggressive.animateWhen(this.isAggressive(), this.tickCount);
    }

    public boolean canAttack(LivingEntity p_21171_) {
        if (p_21171_ instanceof EvokerIllager) {
            return false;
        }
        return super.canAttack(p_21171_);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof EvokerIllager) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean fireImmune() {
        return FALSE;
    }

    @Override
    public boolean isOnFire() {
        return this.isAggressive() || this.getTarget() != null;
    }

    @Override
    protected void tickDeath() {
        ++this.trueDeathTime;
        if (this.trueDeathTime >= Maths.toTick(2)) {
            if (this.lastEntity != null && this.distanceToSqr(this.lastEntity) <= Maths.square(2))
                this.lastEntity.hurt(this.damageSources().explosion(this, this.getOwner()), 50);
            this.playSound(SoundEvents.GENERIC_EXPLODE, 1f, 1f);
            if (!this.level().isClientSide) {
                ((ServerLevel)this.level()).sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(),
                        this.getZ(), 2, 0.5, 0, 0.5, 0);
            }
            MobUtils.rangeHurtAndFire(4, 4, 4, this, this.damageSources().explosion(this,
                    this.getOwner()), 39f, 60);
            this.discard();
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SuicideZombieMeleeAttackGoal(this, 1,
                false, true, 3f, 1D));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                this, LivingEntity.class, false, lie -> MobUtils.canHurt(lie, this)));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return 0.75f;
    }

    @Override
    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
    }

    private static class SuicideZombieMeleeAttackGoal
    extends ApiMeleeAttackGoal {
        private final SuicideZombie zombie;
        public SuicideZombieMeleeAttackGoal(SuicideZombie p_25552_, double p_25553_, boolean p_25554_, boolean p_25555_, float exp, double range) {
            super(p_25552_, p_25553_, p_25554_, p_25555_, exp, range);
            this.zombie = p_25552_;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            this.zombie.lastEntity = p_25557_;
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return SuicideZombie.createPathAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 1).add(Attributes.ARMOR, 6)
                .add(Attributes.FOLLOW_RANGE, 120).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 20);
    }

    {
        this.trueDeathTime = 0;
        this.walking = new AnimationState();
        this.aggressive = new AnimationState();
    }
}
