
package com.bilibili.player_ix.noixmod_api.magic.nihilistic;

import com.bilibili.player_ix.noixmod_api.magic.Spell;

public abstract class NihilisticSpell extends Spell {
    public NihilisticSpell() {
        super();
    }

    public Type getSpellType() {
        return Type.NIHILISTIC;
    }
}
