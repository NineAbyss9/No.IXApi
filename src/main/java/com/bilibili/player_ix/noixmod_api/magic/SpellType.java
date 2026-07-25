
package com.bilibili.player_ix.noixmod_api.magic;

import org.NineAbyss9.util.function.Holder;

import java.util.function.Supplier;

public class SpellType implements Holder<ISpell> {
    private final String name;
    private final Supplier<ISpell> type;

    public SpellType(String name, Supplier<ISpell> spell) {
        this.name = name;
        this.type = spell;
    }

    public String toString()
    {
        return name;
    }

    public ISpell get() {
        return this.type.get();
    }
}
