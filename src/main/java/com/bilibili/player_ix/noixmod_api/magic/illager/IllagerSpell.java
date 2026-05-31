
package com.bilibili.player_ix.noixmod_api.magic.illager;

import com.bilibili.player_ix.noixmod_api.magic.Spell;

public abstract class IllagerSpell extends Spell {
    public Type getSpellType() {
        return Type.ILLAGER;
    }
}
