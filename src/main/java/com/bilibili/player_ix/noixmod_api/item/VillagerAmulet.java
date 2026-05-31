
package com.bilibili.player_ix.noixmod_api.item;

import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.villager.VillagerGolemSpell;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VillagerAmulet
extends Item {
    public VillagerAmulet() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) {
            return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
        }
        ISpell spell = new VillagerGolemSpell(false);
        spell.castSpell((ServerLevel)pLevel, pPlayer);
        ItemUtil.shrink(pPlayer.getItemInHand(pUsedHand), pPlayer);
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
    {
        pTooltipComponents.add(Component.translatable("info.noixmodapi.villager_amulet"));
    }
}
