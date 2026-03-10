
package com.bilibili.player_ix.noixmod_api.item.util;

import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.crafting.Ingredient;

public class IcePickaxe
extends PickaxeItem {
    public IcePickaxe() {
        super(ItemUtil.getTier(0, 5f, 15f, 3, 15, Ingredient.of(ItemStacks.of(
                Items.BLUE_ICE))), 1, -2.8f, new Properties());
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
