
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.api.mobs.SpellCasterMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.AbstractUseSpellGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.entities.servant.worm.Worm;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**This mob was requested to be created by GreenSinger.Do not ask @Player_IX.
@author Player_IX*/
public class GreenSinger extends SpellcasterNihilist implements ApiPoseMob, SpellCasterMob {
    public GreenSinger(EntityType<? extends GreenSinger> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new SummonSpellGoal(this));
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1, true, false));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.7f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        pAmount = Math.min(15, pAmount);
        if (pSource.is(DamageTypeTags.IS_FIRE)) {
            return false;
        }
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        this.heal(3f);
        if (p_21372_ instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER));
        }
        return super.doHurtTarget(p_21372_);
    }

    @Override
    public double getAttributeValue(Attribute p_21134_) {
        if (p_21134_.equals(Attributes.ATTACK_DAMAGE)) {
            return super.getAttributeValue(p_21134_) * Mth.randomBetween(this.random, 1f, 1.25f);
        }
        return super.getAttributeValue(p_21134_);
    }

    @Override
    public ApiPose getPoses() {
        if (this.isCastingSpell()) {
            return ApiPose.SPELL_CASTING;
        }
        if (this.isAggressive()) {
            return ApiPose.ATTACKING;
        }
        return ApiPose.NATURAL;
    }

    public void summonWorm() {
        if (this.level() instanceof ServerLevel level) {
            Worm worm = new Worm(NoixmodAPIEntities.WORM.get(), level);
            worm.moveTo(Vec9.of(this.blockPosition().offset(Maths.randomInt(3), 0, Maths.randomInt(3))));
            worm.setOwner(this);
            worm.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 3));
            worm.finalizeSpawn(level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED);
            level.addFreshEntity(worm);
        }
    }

    private void spawnFreakyWorm() {
        if (this.level() instanceof ServerLevel level) {
            var worm = NoixmodAPIEntities.FREAKY_WORM.get().create(level);
            if (worm != null) {
                worm.moveTo(Vec9.of(this.blockPosition().offset(Maths.randomInt(2), 0, Maths.randomInt(2))));
                worm.setOwner(this);
                worm.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 3));
                worm.finalizeSpawn(level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED);
                level.addFreshEntity(worm);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.ATTACK_DAMAGE, 12).add(Attributes.MAX_HEALTH, 120)
                .add(Attributes.FOLLOW_RANGE, 129).add(Attributes.ARMOR, 2);
    }

    @Nullable
    @Override
    public SoundEvent getCastingSoundEvent() {
        return this.getCastSound();
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    private class SummonSpellGoal
            extends AbstractUseSpellGoal {
        public SummonSpellGoal(SpellCasterMob finder) {
            super(finder);
        }

        @Override
        protected void castSpell() {
            for (int i = 0; i < 3; ++i) {
                GreenSinger.this.summonWorm();
            }
            spawnFreakyWorm();
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
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.FIRE;
        }
    }
}
