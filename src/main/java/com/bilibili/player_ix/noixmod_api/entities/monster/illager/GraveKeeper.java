
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiIllagerBoss;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.ix_api.util.IXList;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.GraveGhost;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.ApiSpellcaster;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GraveKeeper
extends ApiSpellcaster
implements ApiIllagerBoss {
    public GraveKeeper(EntityType<? extends GraveKeeper> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 50;
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NoixmodAPIItems.GRAVE_AXE.get()));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new HealSpellGoal());
        this.goalSelector.addGoal(2, new BlindnessSpellGoal());
        this.goalSelector.addGoal(2, new BuffSpellGoal());
        this.goalSelector.addGoal(2, new SummonSpellGoal());
        this.goalSelector.addGoal(3, new ApiMeleeAttackGoal(this, 0.75, false, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void summonGhost() {
        if (this.level() instanceof ServerLevel level) {
            BlockPos pos = this.blockPosition().offset(Maths.randomInteger(3), 0, Maths.randomInteger(3));
            GraveGhost ghost = new GraveGhost(NoixmodAPIEntities.GRAVE_GHOST.get(), level);
            ghost.moveTo(pos, 0, 0);
            ghost.setOwner(this);
            ghost.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED,
                    null, null);
            level.addFreshEntity(ghost);
            level.sendParticles(ParticleTypes.SOUL, ghost.getRandomX(0.6), ghost.getRandomY(),
                    ghost.getRandomZ(0.6),
            19, 0, 0, 0, this.random.nextGaussian() * 0.3);
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 2));
        }
    }

    public IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return IllagerArmPose.SPELLCASTING;
        }
        if (this.isAggressive()) {
            return IllagerArmPose.ATTACKING;
        }
        return IllagerArmPose.CROSSED;
    }

    public float getVoicePitch() {
        return 0.9F;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.VINDICATOR_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.FOLLOW_RANGE, 120).add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.ATTACK_DAMAGE, 7).add(Attributes.ARMOR_TOUGHNESS, 2);
    }

    public static void initialize() {
        if (NoixmodAPIMainConfig.GraveKeeperJoinRaids.get())
            Raid.RaiderType.create("ApiGraveKeeper", NoixmodAPIEntities.GRAVE_KEEPER.get(),
                    IXList.raidCount(NoixmodAPIMainConfig.GraveKeeperRaidCount.get()));
    }

    private class SummonSpellGoal
    extends UseSpellGoal {

        protected void castSpell() {
            for (int i = 0; i < 3;++i) {
                GraveKeeper.this.summonGhost();
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
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.ZOMBIE;
        }
    }

    private class HealSpellGoal
    extends UseSpellGoal {

        @Override
        protected void castSpell() {
            GraveKeeper.this.heal(5f);
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 500;
        }

        @Nullable
        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EMPTY;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.REGEN;
        }
    }

    private class BuffSpellGoal
    extends UseSpellGoal {

        @Override
        protected void castSpell() {
            GraveKeeper.this.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_BOOST, Maths.toTick(20), 1));
            GraveKeeper.this.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED, Maths.toTick(20), 1));
            GraveKeeper.this.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE, Maths.toTick(20), 1));
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 600;
        }

        @Nullable
        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EMPTY;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.POTION;
        }
    }

    private class BlindnessSpellGoal
    extends UseSpellGoal {

        @Override
        protected void castSpell() {
            LivingEntity entity = GraveKeeper.this.getTarget();
            if (entity != null) {
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, Maths.toTick(10), 0));
            }
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 600;
        }

        @Nullable
        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EMPTY;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.ATTACK;
        }
    }

    private class BreatheFireGoal extends UseSpellGoal {

        @Override
        protected void castSpell() {

        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 500;
        }

        @Nullable
        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.FIRE;
        }
    }
}
