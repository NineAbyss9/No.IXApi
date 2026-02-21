
package com.bilibili.player_ix.noixmod_api.compat.goety;

import com.bilibili.player_ix.noixmod_api.compat.Compatable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

public class RevelationCompat implements Compatable {
    public RevelationCompat() {
    }

    public static boolean revelationLoaded() {
        return ModList.get() != null && ModList.get().getModContainerById("goety_revelation").isPresent();
    }

    @Nonnull
    public static Item getItem(String name) {
        return Compatable.getItem("goety_revelation:", name);
    }

    @Nonnull
    public static ItemStack getItemStack(String name) {
        return Compatable.getItemStack("goety_revelation:", name);
    }

    public void setup(FMLCommonSetupEvent event) {

    }
}
