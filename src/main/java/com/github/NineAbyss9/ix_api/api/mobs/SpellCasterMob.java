
package com.github.NineAbyss9.ix_api.api.mobs;

import com.github.NineAbyss9.ix_api.api.ApiSpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;

public interface SpellCasterMob {
    int getSpellTick();

    void setSpellType(ApiSpells.ApiSpell spell);

    void setSpellTick(int tick);

    boolean isCastingSpell();

    @Nullable
    SoundEvent getCastSound();

    default boolean canCastSpell() {
        return true;
    }

    default void stopSpell() {
        this.setSpellType(ApiSpells.ApiSpell.NONE);
        this.setSpellTick(0);
    }

    default void addSpellCasterAdditionalData(CompoundTag tag) {
        if (this.canCastSpell())
            tag.putInt("SpellTick", this.getSpellTick());
    }

    default void readSpellCasterAdditionalData(CompoundTag tag) {
        if (tag.contains("SpellTick"))
            this.setSpellTick(tag.getInt("SpellTick"));
    }
}
