
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class ApiSpellcaster
extends AbstractIllager {
    protected static final EntityDataAccessor<Byte> SPELL =
            SynchedEntityData.defineId(ApiSpellcaster.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Integer> SPELL_TICKS =
            SynchedEntityData.defineId(ApiSpellcaster.class, EntityDataSerializers.INT);
    protected IllagerSpellType currentSpell = IllagerSpellType.NONE;
    protected ApiSpellcaster(EntityType<? extends ApiSpellcaster> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL, (byte)0);
        this.entityData.define(SPELL_TICKS, 0);
    }

    public void applyRaidBuffs(int i, boolean b) {}

    public void addAdditionalSaveData(CompoundTag p_37870_) {
        if (this.canCastSpell()) {
            p_37870_.putInt("SpellTicks", this.getSpellTicks());
        }
        super.addAdditionalSaveData(p_37870_);
    }

    public void readAdditionalSaveData(CompoundTag p_37862_) {
        if (this.canCastSpell()) {
            this.setSpellTicks(p_37862_.getInt("SpellTicks"));
        }
        super.readAdditionalSaveData(p_37862_);
    }

    protected void registerGoals() {
        super.registerGoals();
        if (this.getNavigation() instanceof GroundPathNavigation path) {
            path.setCanOpenDoors(true);
        }
    }

    public IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return IllagerArmPose.SPELLCASTING;
        }
        if (this.isAggressive()) {
            return IllagerArmPose.ATTACKING;
        }
        if (this.isCrossbowCharging()) {
            return IllagerArmPose.CROSSBOW_CHARGE;
        }
        if (this.isCrossbowHolding()) {
            return IllagerArmPose.CROSSBOW_HOLD;
        }
        if (this.isCelebrating()) {
            return IllagerArmPose.CELEBRATING;
        }
        return IllagerArmPose.CROSSED;
    }

    public boolean isCrossbowHolding() {
        return false;
    }

    public boolean isCrossbowCharging() {
        return false;
    }

    public boolean canCastSpell() {
        return true;
    }

    @Nullable
    public SoundEvent getCastSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getSpellTicks() > 0) {
            this.setSpellTicks(this.getSpellTicks() - 1);
        }
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isCastingSpell()) {
            IllagerSpellType spellId = this.getSpellId();
            double $$1 = spellId.spellColor[0];
            double $$2 = spellId.spellColor[1];
            double $$3 = spellId.spellColor[2];
            float $$4 = this.yBodyRot * ((float) Math.PI / 180) + Mth.cos((float) this.tickCount * 0.6662f) * 0.25f;
            float $$5 = Mth.cos($$4);
            float $$6 = Mth.sin($$4);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + $$5 * 0.6,
                    this.getY() + 1.8, this.getZ() + $$6 * 0.6, $$1, $$2, $$3);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - $$5 * 0.6,
                    this.getY() + 1.8, this.getZ() - $$6 * 0.6, $$1, $$2, $$3);
        }
    }

    protected IllagerSpellType getSpellId() {
        if (!this.level().isClientSide) {
            return this.currentSpell;
        }
        return IllagerSpellType.getById(this.getSpell());
    }

    protected byte getSpell() {
        return this.entityData.get(SPELL);
    }

    protected void setSpell(IllagerSpellType type) {
        this.currentSpell = type;
        this.entityData.set(SPELL, (byte)type.id);
    }

    public boolean isCastingSpell() {
        if (this.level().isClientSide) {
            return this.getSpell() > 0;
        } else {
            return this.getSpellTicks() > 0;
        }
    }

    public int getSpellTicks() {
        return this.entityData.get(SPELL_TICKS);
    }

    public void setSpellTicks(int i) {
        this.entityData.set(SPELL_TICKS, Math.max(i, 0));
    }

    protected void stopSpell() {
        this.currentSpell = IllagerSpellType.NONE;
        this.setSpell(IllagerSpellType.NONE);
    }

    protected enum IllagerSpellType {
        NONE(0, 0.0, 0.0, 0.0),
        RANGE(1, 0.7, 0.7, 0.8),
        ATTACK(2, 0.4, 0.3, 0.35),
        NIHILITY(3, 0.9, 0.3, 0.9),
        FIRE(4, 1.0, 0.6, 0.0),
        WATER(5, 0.3, 0.3, 0.8),
        DARK(6, 0.1, 0.1, 0.1),
        UNKNOWN(7, 0.3, 0.3, 0.3),
        POTION(8, 0.3, 0.9, 0.3),
        REGEN(9, 0.7, 0.7, 0.7),
        WATERS(10, 0.1, 0.1, 0.79),
        ZOMBIE(11, 0.3, 0.85, 0.3),
        ICE(12, 0, 0, 0.9);
        private final int id;
        private final double[] spellColor;

        IllagerSpellType(int $$0, double x, double y, double z) {
            this.id = $$0;
            this.spellColor = new double[]{x, y, z};
        }

        public static IllagerSpellType getById(int i) {
            for (IllagerSpellType type : IllagerSpellType.values()) {
                if (i != type.id) continue;
                return type;
            }
            return NONE;
        }
    }

    protected abstract class UseSpellGoal
    extends Goal {
        protected int attackWarmupDelay;
        protected int nextAttackTickCount;
        protected UseSpellGoal() {
        }

        public boolean canUse() {
            LivingEntity lie = ApiSpellcaster.this.getTarget();
            if (this.needTarget() && lie == null) {
                return false;
            }
            return ApiSpellcaster.this.tickCount > this.nextAttackTickCount;
        }

        public boolean canContinueToUse() {
            if (this.needTarget() && ApiSpellcaster.this.getTarget() == null) {
                return false;
            }
            return this.attackWarmupDelay > 0;
        }

        public void start() {
            this.attackWarmupDelay = this.getCastWarmupTime();
            ApiSpellcaster.this.setSpell(this.getSpellType());
            ApiSpellcaster.this.setSpellTicks(this.getCastingTime());
            SoundEvent event = this.getPrepareSound();
            if (event != null) {
                ApiSpellcaster.this.playSound(event);
            }
        }

        public void tick() {
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 0) {
                this.nextAttackTickCount = ApiSpellcaster.this.tickCount + this.getCastingInterval();
                this.castSpell();
                ApiSpellcaster.this.stopSpell();
                if (ApiSpellcaster.this.getCastSound() != null) {
                    ApiSpellcaster.this.playSound(ApiSpellcaster.this.getCastSound());
                }
            }
        }

        protected boolean needTarget() {
            return true;
        }

        protected abstract void castSpell();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getPrepareSound();

        protected abstract IllagerSpellType getSpellType();
    }

    protected class CastingSpellGoal
    extends Goal {
        public CastingSpellGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        public boolean canUse() {
            return ApiSpellcaster.this.getSpellTicks() > 0;
        }

        public void start() {
            ApiSpellcaster.this.getNavigation().stop();
        }

        public void stop() {
            ApiSpellcaster.this.stopSpell();
        }

        public void tick() {
            LivingEntity target = ApiSpellcaster.this.getTarget();
            if (target != null) {
                ApiSpellcaster.this.lookControl.setLookAt(target, ApiSpellcaster.this.getMaxHeadYRot(),
                        ApiSpellcaster.this.getMaxHeadXRot());
            }
        }
    }
}
