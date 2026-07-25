
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.item.core.IWindItem;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class WindSword
extends ApiSword
implements IWindItem
{
    public WindSword() {
        super(1024, 5.5F, 3.0F, 3, 11, Ingredient.of(
                NoixmodAPIItems.WIND_ESSENCE.get()), 4, -2.4F,
                new Properties().stacksTo(1));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        this.fly(pLevel, pPlayer, pUsedHand);
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
