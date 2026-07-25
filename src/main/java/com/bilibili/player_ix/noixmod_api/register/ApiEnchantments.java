
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.item.enchantment.HeavyStrike;
import com.bilibili.player_ix.noixmod_api.item.enchantment.NihilisticKillerEnchantment;
import com.bilibili.player_ix.noixmod_api.item.enchantment.Pioneer;
import com.bilibili.player_ix.noixmod_api.item.enchantment.PotentEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.NineAbyss9.util.function.FunctionCollector;

import java.util.function.Supplier;

public class ApiEnchantments {
    public static final EnchantmentCategory ANY = EnchantmentCategory.create("API_ANY",
            FunctionCollector.alwaysTrue());
    public static final DeferredRegister<Enchantment> REGISTER = DeferredRegister.create(
            ForgeRegistries.ENCHANTMENTS, NoixmodAPI.MOD_ID);
    public static final Supplier<Enchantment> HEAVY_STRIKE;
    public static final Supplier<Enchantment> PIONEER;
    public static final Supplier<Enchantment> NIHILISTIC_KILLER;
    public static final Supplier<Enchantment> POTENT;
    private static Supplier<Enchantment> register(String name, Supplier<Enchantment> enchantment) {
        return REGISTER.register(name, enchantment);
    }

    static {
        HEAVY_STRIKE = register("heavy_strike", HeavyStrike::new);
        PIONEER = register("pioneer", Pioneer::new);
        NIHILISTIC_KILLER = register("nihilistic_killer", NihilisticKillerEnchantment::new);
        POTENT = register("potent", PotentEnchantment::new);
    }
}
