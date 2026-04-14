
package com.bilibili.player_ix.noixmod_api.magic;

import org.NineAbyss9.util.function.Holder;

import java.util.function.Supplier;

public class SpellType implements Holder<ISpell> {
    protected final String name;
    private final Supplier<ISpell> type;

    public SpellType(String name, Supplier<ISpell> spell) {
        this.name = name;
        this.type = spell;
    }

    public ISpell get() {
        return this.type.get();
    }
}
