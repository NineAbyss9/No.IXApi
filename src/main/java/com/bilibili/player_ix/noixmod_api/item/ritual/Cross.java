
package com.bilibili.player_ix.noixmod_api.item.ritual;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;

//Smite 13. 斑驳锈迹上，暗浮的银光声明着专戮之责
public class Cross extends RitualSupplies {
    public Cross() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(Map.of(Enchantments.SMITE, 13),stack);
        return stack;
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
