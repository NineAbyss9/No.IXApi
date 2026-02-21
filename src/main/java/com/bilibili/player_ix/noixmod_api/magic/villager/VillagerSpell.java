
package com.bilibili.player_ix.noixmod_api.magic.villager;

import com.bilibili.player_ix.noixmod_api.magic.Spell;

public abstract class VillagerSpell extends Spell {
    public VillagerSpell() {
        super();
    }

    public Type getSpellType() {
        return Type.VILLAGER;
    }
}
