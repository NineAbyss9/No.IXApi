
package com.bilibili.player_ix.noixmod_api.item.ritual;

import net.minecraft.world.item.Rarity;

public class Evil extends RitualSupplies {
    public Evil() {
        super(new Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1));
    }
}
