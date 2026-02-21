
package com.bilibili.player_ix.noixmod_api.entities.npc.DanDing;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.BasicItemListing;

public class Buy extends BasicItemListing {
    public Buy(Item sell, Item currency, int sellCount, int currencyCount, int maxUses) {
        this(new ItemStack(sell, sellCount), new ItemStack(currency, currencyCount), maxUses);
    }

    public Buy(ItemStack sell, ItemStack currency, int maxUses) {
        super(sell, currency, maxUses, 0, 0);
    }

    public Buy(Item sell, Item currency, Item currency1, int sellCount, int currencyCount, int currency1Count, int maxUses) {
        this(new ItemStack(sell, sellCount), new ItemStack(currency, currencyCount), new ItemStack(currency1, currency1Count),
                maxUses);
    }

    public Buy(ItemStack sell, ItemStack currency, ItemStack currency1, int maxUses) {
        super(currency, currency1, sell, maxUses, 0, 0);
    }
}
