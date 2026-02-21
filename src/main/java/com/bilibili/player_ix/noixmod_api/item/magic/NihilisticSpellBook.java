
package com.bilibili.player_ix.noixmod_api.item.magic;

import net.minecraft.world.item.Rarity;

public class NihilisticSpellBook
extends SpellBook {
    public NihilisticSpellBook() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }
}
