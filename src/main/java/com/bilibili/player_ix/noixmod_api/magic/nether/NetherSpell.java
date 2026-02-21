
package com.bilibili.player_ix.noixmod_api.magic.nether;

import com.bilibili.player_ix.noixmod_api.magic.Spell;

public abstract class NetherSpell extends Spell {
    public NetherSpell() {
        super();
    }

    @Override
    public Type getSpellType() {
        return Type.NETHER;
    }
}
