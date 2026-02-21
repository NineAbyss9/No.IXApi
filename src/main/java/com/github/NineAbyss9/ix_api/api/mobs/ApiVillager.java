
package com.github.NineAbyss9.ix_api.api.mobs;

import com.google.common.collect.Sets;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.Set;

/**A class that copy {@link AbstractVillager}*/
public interface ApiVillager {
    default void rewardTradeXp(MerchantOffer merchantOffer) {}

    void updateTrades();

    default SoundEvent getTradeUpdatedSound(boolean p_35323_) {
        return p_35323_ ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
    }

    default void addOffersFromItemListings(MerchantOffers merchantOffers, VillagerTrades.ItemListing[] listings) {
        RandomSource random = RandomSource.create();
        Set<Integer> set = Sets.newHashSet();
        if (listings.length > 4) {
            while(set.size() < 4) {
                set.add(random.nextInt(listings.length));
            }
        } else {
            for(int i = 0; i < listings.length; ++i) {
                set.add(i);
            }
        }
        for (int integer : set) {
            VillagerTrades.ItemListing villagertrades$itemlisting = listings[integer];
            MerchantOffer merchantoffer = villagertrades$itemlisting.getOffer((Entity)this, random);
            if (merchantoffer != null) {
                merchantOffers.add(merchantoffer);
            }
        }
    }
}
