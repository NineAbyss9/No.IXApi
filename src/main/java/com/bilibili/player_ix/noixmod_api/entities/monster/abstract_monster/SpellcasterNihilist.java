
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.github.NineAbyss9.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.api.mobs.APIEntityDataSerializers;
import com.github.NineAbyss9.ix_api.api.mobs.SpellCasterMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class SpellcasterNihilist
extends Nihilist
implements SpellCasterMob {
    protected static final EntityDataAccessor<Byte> SPELL = SynchedEntityData.defineId(SpellcasterNihilist.class,
            EntityDataSerializers.BYTE);
    protected int spellCastingTickCount;
    protected static final EntityDataAccessor<Integer> SPELL_TICKS = SynchedEntityData.defineId(
            SpellcasterNihilist.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<ApiSpells.ApiSpell> CURRENT_SPELL;
    protected ApiSpells.ApiSpell lastSpell = ApiSpells.ApiSpell.NONE;
    protected SpellcasterNihilist(EntityType<? extends SpellcasterNihilist> type, Level world) {
        super(type, world);
    }

    public int getSpellTick() {
        return this.entityData.get(SPELL_TICKS);
    }

    public void setSpellTick(int i) {
        this.entityData.set(SPELL_TICKS, i);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.spellCastingTickCount = tag.getInt("SpellTicks");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpellTicks", this.spellCastingTickCount);
        tag.putInt("TestSpellTicks", this.getSpellTick());
    }

    protected ApiSpells.ApiSpell getSpellId() {
        if (!this.level().isClientSide) {
            return this.entityData.get(CURRENT_SPELL);
        }
        return ApiSpells.ApiSpell.getById(this.entityData.get(SPELL));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellCastingTickCount > 0) {
            --this.spellCastingTickCount;
        }
        if (this.getSpellTick() > 0) {
            this.setSpellTick(this.getSpellTick() - 1);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isCastingSpell()) {
            this.spellTick();
        }
    }

    protected void spellTick() {
        ApiSpells.ApiSpell spellId = this.getSpellId() == ApiSpells.ApiSpell.NONE ? this.lastSpell : this.getSpellId();
        double $$1 = spellId.spellColor[0];
        double $$2 = spellId.spellColor[1];
        double $$3 = spellId.spellColor[2];
        float $$4 = this.yBodyRot * (Maths.CLOSER_PI / 180) + Mth.cos(this.tickCount * 0.6662f) * 0.25f;
        float $$5 = Mth.cos($$4);
        float $$6 = Mth.sin($$4);
        if (this.getSpellCastType() == SpellCastType.NORMAL) {
            this.level().addParticle(this.getSpellParticle(), this.getX() + $$5 * 0.6, this.getY()
                    + 1.8, this.getZ() + $$6 * 0.6, $$1, $$2, $$3);
            this.level().addParticle(this.getSpellParticle(), this.getX() - $$5 * 0.6, this.getY()
                    + 1.8, this.getZ() - $$6 * 0.6, $$1, $$2, $$3);
        }
        if (this.getSpellCastType() == SpellCastType.CULTIST) {
            this.level().addParticle(this.getSpellParticle(), this.getX(), this.getY() + 2.2, this.getZ(), $$1, $$2, $$3);
        }
        if (this.getSpellCastType() == SpellCastType.NIHILISTIC) {
            if (this.getMainArm() == HumanoidArm.RIGHT) {
                this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get(), this.getX()
                        + $$5 * 0.6, this.getY() + 1.8, this.getZ() + $$6 * 0.6, $$1, $$2, $$3);
            } else {
                this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get(), this.getX()
                        - $$5 * 0.6, this.getY() + 1.8, this.getZ() - $$6 * 0.6, $$1, $$2, $$3);
            }
        }
        if (this.getSpellCastType() == SpellCastType.NIHILISTIC_AND_NORMAL) {
            this.level().addParticle(this.getSpellParticle(),
                    this.getX() + $$5 * 0.6, this.getY() + 1.8, this.getZ() + $$6 * 0.6,
                    $$1, $$2, $$3);
            this.level().addParticle(this.getSpellParticle(),
                    this.getX() - $$5 * 0.6, this.getY() + 1.8, this.getZ() - $$6 * 0.6,
                    $$1, $$2, $$3);
        }
    }

    protected ParticleOptions getSpellParticle() {
        return ParticleTypes.ENTITY_EFFECT;
    }

    protected int getSpellCastingTime() {
        return this.spellCastingTickCount;
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return this.getCastingSoundEvent();
    }

    @Nullable
    protected abstract SoundEvent getCastingSoundEvent();

    protected abstract class UseSpellGoalA
    extends Goal {
        protected int attackWarmupDelay;
        protected int nextAttackTickCount;

        protected UseSpellGoalA() {
        }

        @Override
        public boolean canUse() {
            LivingEntity $$0 = SpellcasterNihilist.this.getTarget();
            if (($$0 == null || !$$0.isAlive()) && this.needTarget()) {
                return false;
            }
            if (SpellcasterNihilist.this.isCastingSpell()) {
                return false;
            }
            return SpellcasterNihilist.this.tickCount >= this.nextAttackTickCount;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.needTarget() && (SpellcasterNihilist.this.getTarget() == null ||
                    !SpellcasterNihilist.this.getTarget().isAlive())) {
                return false;
            }
            return this.attackWarmupDelay > 0;
        }

        @Override
        public void start() {
            this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
            SpellcasterNihilist.this.setSpellTick(this.getCastingTime());
            this.nextAttackTickCount = SpellcasterNihilist.this.tickCount + this.getCastingInterval();
            SoundEvent $$0 = this.getSpellPrepareSound();
            if ($$0 != null) {
                SpellcasterNihilist.this.playSound($$0, 1f, 1f);
            }
            SpellcasterNihilist.this.setSpellType(this.getSpell());
        }

        @Override
        public void tick() {
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 0) {
                this.castSpell();
                if (SpellcasterNihilist.this.getCastingSoundEvent() != null) {
                    SpellcasterNihilist.this.playSound(SpellcasterNihilist.this.getCastingSoundEvent(), 1f, 1f);
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
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract ApiSpells.ApiSpell getSpell();
    }

    protected abstract class UseSpellGoal
    extends Goal {
        protected int attackWarmupDelay;
        protected int nextAttackTickCount;

        protected UseSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!this.checkTarget()) {
                return false;
            }
            if (SpellcasterNihilist.this.isCastingSpell()) {
                return false;
            }
            return SpellcasterNihilist.this.tickCount >= this.nextAttackTickCount;
        }

        @Override
        public boolean canContinueToUse() {
            return this.attackWarmupDelay > 0 && this.checkTarget();
        }

        protected boolean checkTarget() {
            LivingEntity living = SpellcasterNihilist.this.getTarget();
            if (this.needTarget()) {
                return living != null && living.isAlive();
            } else {
                return true;
            }
        }

        public void start() {
            this.attackWarmupDelay = this.getCastWarmupTime();
            SpellcasterNihilist.this.setSpellTick(this.getCastingTime());
            SoundEvent $$0 = this.getSpellPrepareSound();
            if ($$0 != null) {
                SpellcasterNihilist.this.playSound($$0, 1.0f, 1.0f);
            }
            SpellcasterNihilist.this.lastSpell = this.getSpell();
            SpellcasterNihilist.this.setSpellType(this.getSpell());
        }

        public void tick() {
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 0) {
                this.nextAttackTickCount = SpellcasterNihilist.this.tickCount + this.getCastingInterval();
                SpellcasterNihilist.this.stopSpell();
                this.castSpell();
                if (this.customCastSound()) {
                    if (this.getCustomCastSound() != null) {
                        SpellcasterNihilist.this.playSound(this.getCustomCastSound(), this.getCastVolume(), this.getCastPitch());
                    }
                } else {
                    if (SpellcasterNihilist.this.getCastingSoundEvent() != null) {
                        SpellcasterNihilist.this.playSound(SpellcasterNihilist.this.getCastingSoundEvent(),
                                this.getCastVolume(), this.getCastPitch());
                    }
                }
            }
        }

        protected float getCastPitch() {
            return 1f;
        }

        protected boolean needTarget() {
            return true;
        }

        protected float getCastVolume() {
            return 1f;
        }

        protected abstract void castSpell();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract ApiSpells.ApiSpell getSpell();

        protected boolean customCastSound() {
            return false;
        }

        @Nullable
        protected SoundEvent getCustomCastSound() {
            return null;
        }
    }

    protected class CastingSpellGoal
    extends Goal {
        public CastingSpellGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return SpellcasterNihilist.this.getSpellCastingTime() > 0;
        }

        public void start() {
            SpellcasterNihilist.this.navigation.stop();
        }

        public void stop() {
            SpellcasterNihilist.this.stopSpell();
        }

        public void tick() {
            if (SpellcasterNihilist.this.getTarget() != null) {
                SpellcasterNihilist.this.getLookControl().setLookAt(SpellcasterNihilist.this.getTarget(),
                        SpellcasterNihilist.this.getMaxHeadYRot(), SpellcasterNihilist.this.getMaxHeadXRot());
            }
        }
    }

    public boolean isCastingSpell() {
        return this.getSpellTick() > 0;
    }

    public void setSpellType(ApiSpells.ApiSpell spell) {
        this.entityData.set(CURRENT_SPELL, spell);
        this.entityData.set(SPELL, (byte)spell.id);
    }

    public NihilistArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return NihilistArmPose.SPELL_CASTING;
        } else if (this.isAggressive()) {
            return NihilistArmPose.ATTACKING;
        } else if (!this.getLord().isEmpty()) {
            return NihilistArmPose.SPELL_CASTING;
        }
        return NihilistArmPose.CROSSED;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CURRENT_SPELL, ApiSpells.ApiSpell.NONE);
        this.entityData.define(SPELL, Maths.ZERO_BYTE);
        this.entityData.define(SPELL_TICKS, 0);
    }

    protected enum SpellCastType {
        NORMAL,
        NIHILISTIC,
        CULTIST,
        NIHILISTIC_AND_NORMAL
    }

    protected SpellCastType getSpellCastType() {
        return SpellCastType.NORMAL;
    }

    static {
        CURRENT_SPELL = SynchedEntityData.defineId(SpellcasterNihilist.class, APIEntityDataSerializers.API_SPELL);
    }
}
