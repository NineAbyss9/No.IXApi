
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.nihilistic.NihilisticServantSpell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ShadowWalker
extends SpellcasterNihilist
implements Enemy {
    private static final EntityDataAccessor<Integer> DATA_INVISIBLE_TIME;
    private static final EntityDataAccessor<Integer> DATA_TP_COOLDOWN;
    public ShadowWalker(EntityType<? extends ShadowWalker> type, Level world) {
        super(type, world);
        setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NoixmodAPIItems.INFERNAL_IRON_SWORD.get()));
        this.xpReward = 9;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_INVISIBLE_TIME, 0);
        this.entityData.define(DATA_TP_COOLDOWN, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new CastingSpellGoal());
        goalSelector.addGoal(1, new SummonSpellGoal());
        goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1.25, Maths.square(2)));
        OwnableMob.addBehaviorGoals(this, 5, 0.6, 12f, true, false);
        targetSelector.addGoal(1, new HurtByTargetGoal(this, Nihilistic.class));
        targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void tick() {
        super.tick();
        if (this.getTeleportCooldown() > 0) {
            this.setTeleportCooldown(this.getTeleportCooldown() - 1);
        }
        if (this.getInvisibleTime() > 0) {
            this.setInvisibleTime(this.getInvisibleTime() - 1);
            if (this.level().isClientSide) {
                this.level().addParticle(NoixmodAPIParticleTypes.DARK_SPELL.get(), this.getX(), this.getY(),
                        this.getZ(), 0, 0, 0);
            }
        }
    }

    public void aiStep() {
        super.aiStep();
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (!this.isOnCooldown()) {
                this.teleport();
                handleInvisible();
            }
        }
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (p_21240_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            super.actuallyHurt(p_21240_, p_21241_);
        } else {
            float f = Math.min(p_21241_, this.getMaxHealth() - 1);
            super.actuallyHurt(p_21240_, f);
        }
    }

    private void teleport() {
        if (!this.level().isClientSide) {
            ParticleUtil.sendParticles(this.serverLevel(), ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.position(),
                    30, 1.25, 2, 1.25, 0);
            double d = this.getX() + Maths.randomBetween(-8, 8);
            double d1 = this.getY();
            double d2 = this.getZ() + Maths.randomBetween(-8, 8);
            for (int i = 0;i < 12;i++) {
                if (this.randomTeleport(d, d1, d2, false)) {
                    break;
                }
            }
            setTeleportCooldown(150);
        }
    }

    @Override
    public void spawnAnim() {
        if (this.level().isClientSide) {
            ParticleUtil.spawnAnim(ParticleTypes.LARGE_SMOKE, level(), this);
        } else {
            this.level().broadcastEntityEvent(this, (byte) 20);
        }
    }

    @Override
    protected void dropAllDeathLoot(DamageSource p_21192_) {
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        super.dropAllDeathLoot(p_21192_);
    }

    private boolean isOnCooldown() {
        return this.getTeleportCooldown() > 0;
    }

    private int getTeleportCooldown() {
        return this.entityData.get(DATA_TP_COOLDOWN);
    }

    private void setTeleportCooldown(int teleportCooldown) {
        this.entityData.set(DATA_TP_COOLDOWN, teleportCooldown);
    }

    private int getInvisibleTime() {
        return this.entityData.get(DATA_INVISIBLE_TIME);
    }

    private void setInvisibleTime(int time) {
        this.entityData.set(DATA_INVISIBLE_TIME, time);
    }

    private void handleInvisible() {
        this.setInvisibleTime(Maths.toTick(6));
    }

    public boolean isInvisible() {
        return super.isInvisible() || this.getInvisibleTime() > 0;
    }

    public boolean isAffectedByPotions() {
        return false;
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }

    public boolean doHurtTarget(Entity p_21372_) {
        boolean flag = super.doHurtTarget(p_21372_);
        if (flag) {
            this.heal(1f);
            return true;
        }
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return NoixmodAPISounds.CULTIST_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return NoixmodAPISounds.CULTIST_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return NoixmodAPISounds.CULTIST_DEATH.get();
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.ARMOR, 4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25).add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 60).add(Attributes.FOLLOW_RANGE, 64);
    }

    static {
        DATA_INVISIBLE_TIME = SynchedEntityData.defineId(ShadowWalker.class, EntityDataSerializers.INT);
        DATA_TP_COOLDOWN = SynchedEntityData.defineId(ShadowWalker.class, EntityDataSerializers.INT);
    }

    private class SummonSpellGoal extends UseSpellGoalA {

        @Override
        protected void castSpell() {
            if (!level().isClientSide) {
                ISpell spell = new NihilisticServantSpell(1);
                spell.castSpell((ServerLevel)level(), ShadowWalker.this);
            }
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return null;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.NIHILISTIC;
        }
    }
}
