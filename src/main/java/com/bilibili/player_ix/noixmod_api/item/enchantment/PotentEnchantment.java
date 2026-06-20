
package com.bilibili.player_ix.noixmod_api.item.enchantment;

import com.bilibili.player_ix.noixmod_api.register.ApiEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class PotentEnchantment
extends Enchantment {
    public PotentEnchantment() {
        super(Rarity.RARE, ApiEnchantments.ANY, EquipmentSlot.values());
    }

    public boolean isAllowedOnBooks()
    {
        return false;
    }

    public boolean isTradeable()
    {
        return false;
    }

    public boolean isTreasureOnly()
    {
        return true;
    }

    public int getMinCost(int p_44572_) {
        return 1 + (p_44572_ - 1) * 10;
    }

    public int getMaxCost(int p_44574_) {
        return this.getMinCost(p_44574_) + 15;
    }

    public int getMaxLevel() {
        return 5;
    }
}
