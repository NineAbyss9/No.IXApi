
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.item.enchantment.NihilisticKillerEnchantment;
import com.bilibili.player_ix.noixmod_api.item.enchantment.PotentEnchantment;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ApiEnchantments {
    public static final EnchantmentCategory ANY = EnchantmentCategory.create("API_ANY",
            Predicates.alwaysTrue());
    public static final DeferredRegister<Enchantment> REGISTER = DeferredRegister.create(
            ForgeRegistries.ENCHANTMENTS, NoixmodAPI.MOD_ID);
    public static final RegistryObject<Enchantment> NIHILISTIC_KILLER;
    public static final RegistryObject<Enchantment> POTENT;
    private static RegistryObject<Enchantment> register(String name, Supplier<Enchantment> enchantment) {
        return REGISTER.register(name, enchantment);
    }

    static {
        NIHILISTIC_KILLER = register("nihilistic_killer", NihilisticKillerEnchantment::new);
        POTENT = register("potent", PotentEnchantment::new);
    }
}
