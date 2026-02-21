
package com.bilibili.player_ix.noixmod_api.commands;

import com.google.common.collect.ImmutableMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class CommandEnchanter {
    public CommandEnchanter() {
    }

    static void enchant(CommandSourceStack stack, Holder<Enchantment> location, int level) {
        Entity entity = stack.getEntity();
        if (entity instanceof LivingEntity living) {
            ItemStack itemStack = living.getMainHandItem();
            EnchantmentHelper.setEnchantments(ImmutableMap.<Enchantment, Integer>builder()
                    .putAll(itemStack.getAllEnchantments()).put(location.get(), level).build(), itemStack);
        }
    }
}
