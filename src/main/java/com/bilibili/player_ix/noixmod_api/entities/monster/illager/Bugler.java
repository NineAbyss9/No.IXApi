
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.ix_api.util.IXList;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Iterator;
import java.util.List;

public class Bugler
extends AbstractIllager {
    private int cheerCooldown;
    public Bugler(EntityType<? extends AbstractIllager> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(NoixmodAPIItems.OMINOUS_HORN.get()));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.85));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public int getExperienceReward() {
        return 2;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    public void addAdditionalSaveData(CompoundTag p_37870_) {
        p_37870_.putInt("CheerCooldown", this.getCheerCooldown());
        super.addAdditionalSaveData(p_37870_);
    }

    public void readAdditionalSaveData(CompoundTag p_37862_) {
        if (p_37862_.contains("CheerCooldown")) {
            this.setCheerCooldown(p_37862_.getInt("CheerCooldown"));
        }
        super.readAdditionalSaveData(p_37862_);
    }

    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if (this.getCheerCooldown() <= 0) {
            Iterator<AbstractIllager> iterator = cheerTargets().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                ++i;
                AbstractIllager illager = iterator.next();
                this.cheer(illager);
                if (i < 6 && !illager.level().isClientSide) {
                    WorldUtil.sendParticles(ParticleTypes.ANGRY_VILLAGER, illager, 3, 1, 0, 1, 0.1);
                }
            }
            this.setCheerCooldown(NoixmodAPIMainConfig.TrumpeterCheerCooldown.get());
            this.playSound(SoundEvents.VILLAGER_NO);
        }
        if (target != null) {
            if (this.distanceToSqr(target) <= Maths.square(6)) {
                if (!this.moveControl.hasWanted()) {
                    for (AbstractIllager illager : this.cheerTargets()) {
                        if (illager != this) {
                            this.getNavigation().moveTo(illager, 1);
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getCheerCooldown() > 0) {
            --this.cheerCooldown;
        }
    }

    private List<AbstractIllager> cheerTargets() {
        return this.level().getEntitiesOfClass(AbstractIllager.class, this.getBoundingBox().inflate(16, 16,16),
                illager -> MobUtils.areAllies(this, illager) && !(illager instanceof Bugler) &&
                        !(illager instanceof Armorer));
    }

    public void cheer(AbstractIllager illager) {
        illager.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Maths.toTick(10), 1));
        illager.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Maths.toTick(10), 1));
        illager.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Maths.toTick(10), 0));
    }

    public static void init() {
        if (NoixmodAPIMainConfig.TrumpeterJoinRaids.get()) {
            Raid.RaiderType.create("bugler", NoixmodAPIEntities.BUGLER.get(),
                    IXList.raidCount(NoixmodAPIMainConfig.BuglerRaidCount.get()));
        }
        MobUtils.registerSpawn(NoixmodAPIEntities.BUGLER.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MobUtils::illagerSpawnPredicate);
    }

    public void applyRaidBuffs(int i, boolean b) {}

    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }

    public IllagerArmPose getArmPose() {
        return IllagerArmPose.CROSSBOW_HOLD;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Bugler.createMonsterAttributes().add(Attributes.MAX_HEALTH, 24).add(Attributes.FOLLOW_RANGE,
                        100).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    public int getCheerCooldown() {
        return this.cheerCooldown;
    }

    public void setCheerCooldown(int i) {
        this.cheerCooldown = i;
    }
}
