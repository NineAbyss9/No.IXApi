
package com.bilibili.player_ix.noixmod_api.entities.friendly;

import com.github.NineAbyss9.ix_api.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.SpellCasterMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FriendlyCaster extends AbstractFriendlyMob implements SpellCasterMob {
    protected ApiSpells.ApiSpell spell;
    private int spellTick;
    public FriendlyCaster(EntityType<? extends FriendlyCaster> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isCastingSpell()) {
            ApiSpells.ApiSpell pSpell = this.spell;
            double d = pSpell.spellColor[0];
            double d1 = pSpell.spellColor[1];
            double d2 = pSpell.spellColor[2];
            float d3 = this.yBodyRot * Maths.PI_DIVIDING_180 + Maths.cos(this.tickCount * 0.6662f) * 0.25f;
            float d4 = Maths.cos(d3);
            float d5 = Maths.sin(d3);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + d4 * 0.6,
                    this.getY() + 1.8, this.getZ() + d5 * 0.6,
                    d, d1, d2);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - d4 * 0.6,
                    this.getY() + 1.8, this.getZ() - d5 * 0.6,
                    d, d1, d2);
        }
    }

    public int getSpellTick() {
        return spellTick;
    }

    public void setSpellType(ApiSpells.ApiSpell spell) {
        this.spell = spell;
    }

    public void setSpellTick(int tick) {
        this.spellTick = tick;
    }

    public boolean isCastingSpell() {
        return this.spellTick > 0;
    }

    @Nullable
    public SoundEvent getCastSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }
}
