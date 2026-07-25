
package com.bilibili.player_ix.noixmod_api.item.enchantment;

import com.bilibili.player_ix.noixmod_api.item.weapon.WindHammer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class HeavyStrike
extends Enchantment
{
    public HeavyStrike()
    {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON,
                new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    public boolean canEnchant(ItemStack pStack)
    {
        if (pStack.getItem() instanceof AxeItem) return true;
        if (pStack.getItem() instanceof WindHammer) return true;
        return super.canEnchant(pStack);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return false;
    }

    public int getMaxLevel()
    {
        return 2;
    }
}
