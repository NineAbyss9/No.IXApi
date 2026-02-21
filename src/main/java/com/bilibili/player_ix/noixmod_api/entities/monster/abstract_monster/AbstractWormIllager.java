
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.github.NineAbyss9.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.IWormMob;
import com.github.NineAbyss9.ix_api.api.mobs.SpellCasterMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class AbstractWormIllager
extends ApiPathfinderMob
implements SpellCasterMob, IWormMob {
    protected static final EntityDataAccessor<Byte> WORM_SPELL;
    private static final EntityDataAccessor<Integer> DATA_SPELL_TICKS;
    protected ApiSpells.ApiSpell wormSpellType = ApiSpells.ApiSpell.NONE;
    public AbstractWormIllager(EntityType<? extends ApiPathfinderMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SPELL_TICKS, 0);
        this.entityData.define(WORM_SPELL, Maths.ZERO_BYTE);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getSpellTick() > 0) {
            this.setSpellTick(this.getSpellTick() - 1);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isCastingSpell()) {
            ApiSpells.ApiSpell spellType = this.getSpellType();
            double d = spellType.spellColor[0];
            double d1 = spellType.spellColor[1];
            double d2 = spellType.spellColor[2];
            float $$4 = this.yBodyRot * (Maths.CLOSER_PI / 180) + Mth.cos(this.tickCount * 0.6662f) * 0.25f;
            float $$5 = Mth.cos($$4);
            float $$6 = Mth.sin($$4);
            this.level().addParticle(this.getSpellParticle(), this.getX() + $$5 * 0.6, this.getY()
                    + 1.8, this.getZ() + $$6 * 0.6, d, d1, d2);
            this.level().addParticle(this.getSpellParticle(), this.getX() - $$5 * 0.6, this.getY()
                    + 1.8, this.getZ() - $$6 * 0.6, d, d1, d2);
        }
    }

    @Override
    public boolean canAttack(LivingEntity p_21171_) {
        if (!MobUtils.canHurt(p_21171_, this)) {
            return false;
        }
        return NO_WORM_PREDICATE.test(p_21171_) && super.canAttack(p_21171_);
    }

    public int getSpellTick() {
        return this.entityData.get(DATA_SPELL_TICKS);
    }

    @Override
    public void setSpellTick(int i) {
        this.entityData.set(DATA_SPELL_TICKS, i);
    }

    protected ParticleOptions getSpellParticle() {
        return ParticleTypes.ENTITY_EFFECT;
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    protected ApiSpells.ApiSpell getSpellType() {
        if (!this.level().isClientSide) {
            return this.wormSpellType;
        }
        return ApiSpells.ApiSpell.getById(this.entityData.get(WORM_SPELL));
    }

    @Override
    public void setSpellType(ApiSpells.ApiSpell spell) {
        this.wormSpellType = spell;
        this.entityData.set(WORM_SPELL, (byte)spell.id);
    }

    public boolean isCastingSpell() {
        return getSpellTick() > 0;
    }

    public enum WormIllagerArmPose {
        ATTACKING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        CROSSED,
        DIE,
        NONE,
        SPELL_CASTING,
        ZOMBIE_ATTACKING
    }

    public WormIllagerArmPose getArmPose() {
        return WormIllagerArmPose.CROSSED;
    }

    static {
        DATA_SPELL_TICKS = SynchedEntityData.defineId(AbstractWormIllager.class, EntityDataSerializers.INT);
        WORM_SPELL = SynchedEntityData.defineId(AbstractWormIllager.class, EntityDataSerializers.BYTE);
    }
}
