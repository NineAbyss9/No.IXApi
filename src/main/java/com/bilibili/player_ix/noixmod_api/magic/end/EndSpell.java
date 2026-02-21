
package com.bilibili.player_ix.noixmod_api.magic.end;

import com.bilibili.player_ix.noixmod_api.magic.Spell;

public abstract class EndSpell extends Spell {
    public EndSpell() {
        super();
    }

    @Override
    public Type getSpellType() {
        return Type.END;
    }
}
