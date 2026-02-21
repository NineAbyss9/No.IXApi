
package com.bilibili.player_ix.noixmod_api.entities.npc.DanDing;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;

public class DanDingTrades {
    public static final VillagerTrades.ItemListing[] DAN_DING_TRADES;

    static {
        DAN_DING_TRADES = new VillagerTrades.ItemListing[] {
                new Buy(NoixmodAPIItems.SPIRIT_STONE.get(), Items.EMERALD, Items.DIAMOND, 1, 10, 1, 10),
                new Buy(NoixmodAPIItems.GOLDEN_RABBIT_FOOT.get(), Items.EMERALD, Items.DIAMOND, 1, 7, 1, 20),
                new Buy(NoixmodAPIItems.SPIRIT_STONE.get(), Items.ENCHANTED_GOLDEN_APPLE, 24, 1, 20),
                new Buy(NoixmodAPIItems.NIHILISTIC_LORD_S_NOTE.get(), NoixmodAPIItems.SPIRIT_STONE.get(), 1, 1, 2),
                new Buy(NoixmodAPIItems.SPIRIT_STONE.get(), NoixmodAPIItems.FRESH_SOUL.get(), 1, 12, 5)
        };
    }
}
