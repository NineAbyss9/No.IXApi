
package com.bilibili.player_ix.noixmod_api.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.UUID;

/**Increase the player's speed and damage*/
public class Pioneer
extends Enchantment
{
    public static final float INCREASEMENT_EACH_LEVEL = 0.1F;
    public static final UUID MODIFIER_UUID = UUID.fromString("2D778372-679B-421B-A35B-BF92DE782B0B");
    public Pioneer()
    {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON,
                new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return false;
    }

    public int getMaxLevel()
    {
        return 3;
    }
}
