
package com.bilibili.player_ix.noixmod_api.client.gui;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.gui.menu.AltarContainer;
import com.bilibili.player_ix.noixmod_api.client.gui.menu.InfernalIronAnvilMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ApiGuis {
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            NoixmodAPI.MOD_ID);

    public static final RegistryObject<MenuType<AltarContainer>> ALTAR = REGISTER.register("altar",
            () -> IForgeMenuType.create(AltarContainer::create));
    public static final RegistryObject<MenuType<InfernalIronAnvilMenu>> INFERNAL_IRON_ANVIL
            = REGISTER.register("infernal_iron_anvil", () -> IForgeMenuType.create(InfernalIronAnvilMenu::new));
}
