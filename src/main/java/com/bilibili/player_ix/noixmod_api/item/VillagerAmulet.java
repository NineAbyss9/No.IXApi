
package com.bilibili.player_ix.noixmod_api.item;

import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.villager.VillagerGolemSpell;
import com.github.NineAbyss9.ix_api.ix_api.util.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class VillagerAmulet
extends Item {
    public VillagerAmulet() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            ISpell spell = new VillagerGolemSpell(false);
            spell.castSpell((ServerLevel)pLevel, pPlayer);
            ItemUtil.shrink(pPlayer.getItemInHand(pUsedHand), pPlayer);
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
