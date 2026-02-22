
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.mobs.ApiEntityDataSerializers;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.api.mobs.SpellCasterMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class
OwnableIllager
extends OwnableMob
implements ApiPoseMob, SpellCasterMob {
    protected static final EntityDataAccessor<Integer> SPELL_TICK;
    protected static final EntityDataAccessor<APISpells.APISpell> SPELL;
    protected OwnableIllager(EntityType<? extends OwnableIllager> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL_TICK, 0);
        this.entityData.define(SPELL, APISpells.APISpell.NONE);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isCastingSpell()) {
            APISpells.APISpell spellType = this.getSpellType();
            double d = spellType.spellColor[0];
            double d1 = spellType.spellColor[1];
            double d2 = spellType.spellColor[2];
            float $$4 = this.yBodyRot * (Maths.CLOSER_PI / 180) + Mth.cos(this.tickCount * 0.6662f) * 0.25f;
            float $$5 = Mth.cos($$4);
            float $$6 = Mth.sin($$4);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + $$5 * 0.6, this.getY()
                    + 1.8, this.getZ() + $$6 * 0.6, d, d1, d2);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - $$5 * 0.6, this.getY()
                    + 1.8, this.getZ() - $$6 * 0.6, d, d1, d2);
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getSpellTick() > 0) {
            this.setSpellTick(this.getSpellTick() - 1);
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        if (this.getNavigation() instanceof GroundPathNavigation gpn) {
            gpn.setCanOpenDoors(true);
        }
        addAttackGoal();
        addFollowOwnerGoal();
    }

    protected void addAttackGoal() {
    }

    protected void addTargetGoals() {
        super.addTargetGoals();
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
    }

    protected void addFollowOwnerGoal() {
        this.goalSelector.addGoal(5, new FollowOwnerGoal<>(this,
                1.0,  20F, 4F, false));
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity entity = pSource.getEntity();
        if (entity instanceof TraceableEntity traceable) {
            if (traceable.getOwner() == ownerOrThis(this, this)
            || (traceable.getOwner() instanceof OwnableIllager illager && !illager.isHostile())) {
                return false;
            } else if (traceable.getOwner() instanceof TraceableEntity traceable1 &&
                    traceable1.getOwner() == ownerOrThis(this, this)) {
                return false;
            }
        }
        return super.hurt(pSource, pAmount);
    }

    public APISpells.APISpell getSpellType() {
        return this.entityData.get(SPELL);
    }

    public void setSpellType(APISpells.APISpell spell) {
        this.entityData.set(SPELL, spell);
    }

    public boolean isCastingSpell() {
        return this.getSpellTick() > 0;
    }

    public int getSpellTick() {
        return this.entityData.get(SPELL_TICK);
    }

    public void setSpellTick(int tick) {
        this.entityData.set(SPELL_TICK, tick);
    }

    @Nullable
    public SoundEvent getCastSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public ApiPose getPoses() {
        return ApiPose.CROSSED;
    }

    public MobType getMobType() {
        return MobType.ILLAGER;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }

    public boolean canCastSpell() {
        return false;
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(Items.BREAD);
    }

    public boolean canAccept(ItemStack stack) {
        return !stack.isEmpty() && isFood(stack);
    }

    static {
        SPELL_TICK = SynchedEntityData.defineId(OwnableIllager.class, EntityDataSerializers.INT);
        SPELL = SynchedEntityData.defineId(OwnableIllager.class, ApiEntityDataSerializers.API_SPELL);
    }
}
