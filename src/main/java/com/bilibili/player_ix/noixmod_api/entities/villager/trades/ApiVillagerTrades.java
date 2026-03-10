
package com.bilibili.player_ix.noixmod_api.entities.villager.trades;

import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.BasicItemListing;

public class ApiVillagerTrades {
    public static final VillagerTrades.ItemListing[] SPELLCASTER_TRADES;
    public static final VillagerTrades.ItemListing[] MASTER_TRADES;
    public static final VillagerTrades.ItemListing[] EXORCIST_TRADES;
    public static final VillagerTrades.ItemListing[] EVOKER_TRADES;
    public static final VillagerTrades.ItemListing[] DOCTOR_TRADES;
    public static final VillagerTrades.ItemListing[] AMBUSHER_TRADES;

    static {
        var mending = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.MENDING,
                1));
        SPELLCASTER_TRADES = new VillagerTrades.ItemListing[] {
                new BasicItemListing(2, new ItemStack(Items.BOOK), 200, 1, 0),
                new BasicItemListing(4, new ItemStack(Items.BOOKSHELF), 100, 2, 0),
                new BasicItemListing(30, mending, 10, 10, 0.05F)
        };
        MASTER_TRADES = new VillagerTrades.ItemListing[] {
                new BasicItemListing(new ItemStack(Items.IRON_INGOT, 3), new ItemStack(Items.EMERALD),
                        10, 1, 0.01f),
                new BasicItemListing(new ItemStack(Items.BOW, 1), new ItemStack(Items.EMERALD),
                    10, 1, 0.01f),
                new BasicItemListing(new ItemStack(Items.ARROW, 5), new ItemStack(Items.EMERALD),
                        10, 1, 0.01f)
        };
        EXORCIST_TRADES = new VillagerTrades.ItemListing[] {
                new BasicItemListing(12, new ItemStack(NoixmodAPIItems.VILLAGER_AMULET.get()),
                        2, 3, 0.01f),
                new BasicItemListing(new ItemStack(NoixmodAPIItems.BLOOD_BOTTLE.get(), 1),
                        new ItemStack(Items.EMERALD, 9), 5, 1, 0.05f)
        };
        EVOKER_TRADES = new VillagerTrades.ItemListing[] {
                new BasicItemListing(33, new ItemStack(Items.TOTEM_OF_UNDYING), 2, 5, 0.1f),
                new BasicItemListing(4, new ItemStack(Items.BOOK), 20, 1, 0.01f),
                new BasicItemListing(15, ItemStacks.of(NoixmodAPIItems.WIND_ESSENCE), 5, 1,
                        0.01F)
        };
        DOCTOR_TRADES = new VillagerTrades.ItemListing[] {
                new BasicItemListing(12, new ItemStack(NoixmodAPIItems.HEALING_DOLL.get()), 10,
                        1, 0.0F),
                new BasicItemListing(50, ItemStacks.of(Items.TOTEM_OF_UNDYING), 5,
                        1, 0.05F)
        };
        ItemStack stack = EnchantedBookItem.createForEnchantment(
                new EnchantmentInstance(Enchantments.SHARPNESS, 6));
        AMBUSHER_TRADES = new VillagerTrades.ItemListing[] {
                new BasicItemListing(1, new ItemStack(Items.ARROW, 24), 10, 1),
                new BasicItemListing(1, new ItemStack(Items.POTATO, 6), 12, 1, 0.0f),
                new BasicItemListing(2, new ItemStack(Items.IRON_SWORD), 2, 1, 0.01f),
                new BasicItemListing(30, stack, 1, 10, 0.05F)
        };
    }
}
