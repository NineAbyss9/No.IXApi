
package com.bilibili.player_ix.noixmod_api.register.event;

import com.bilibili.player_ix.noixmod_api.magic.Spell;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;

public class SpellCastEvent extends Event {
    private final ServerLevel level;
    @Nullable
    private ParticleOptions spellParticle;
    private final LivingEntity caster;
    private final Spell.Type spellType;
    private final Spell spell;
    public SpellCastEvent(ServerLevel pLevel, LivingEntity pCaster, Spell.Type pType, Spell pSpell) {
        level = pLevel;
        this.caster = pCaster;
        this.spellType = pType;
        this.spell = pSpell;
    }

    public SpellCastEvent(LivingEntity pCaster, Spell.Type pType, Spell pSpell) {
        this((ServerLevel)pCaster.level(), pCaster, pType, pSpell);
    }

    public ServerLevel level() {
        return level;
    }

    public Vec3 getCastPos() {
        return this.getCaster().position();
    }

    public LivingEntity getCaster() {
        return this.caster;
    }

    public float getSpellPower() {
        return this.spell.spellPower();
    }

    @Nullable
    public ParticleOptions getSpellParticle() {
        return spellParticle;
    }

    public void setSpellParticle(@Nullable ParticleOptions particle) {
        this.spellParticle = particle;
    }

    public Spell getSpell() {
        return this.spell;
    }

    public Spell.Type getSpellType() {
        return spellType;
    }
}
