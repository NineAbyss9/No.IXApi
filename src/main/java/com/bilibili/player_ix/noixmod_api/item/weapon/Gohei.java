
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.github.NineAbyss9.ix_api.util.ItemUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

//御币
public class Gohei
extends SwordItem {
    public Gohei() {
        super(ItemUtil.getTier(9999, 6F, 9F, 5, 17, Ingredient.EMPTY),
                3, -2.0F, new Properties().rarity(Rarity.EPIC));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
