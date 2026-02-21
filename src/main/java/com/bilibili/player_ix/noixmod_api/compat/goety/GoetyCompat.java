
package com.bilibili.player_ix.noixmod_api.compat.goety;

import com.bilibili.player_ix.noixmod_api.compat.Compatable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

public class GoetyCompat
implements Compatable {
    public GoetyCompat() {
    }

    public void setup(@Nonnull FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    @Nonnull
    public static ItemStack getItemStack(String name) {
        return Compatable.getItemStack("goety:", name);
    }

    @Nonnull
    public static Item getItem(String name) {
        return Compatable.getItem("goety:", name);
    }

    public static void p() {

    }

    public boolean isLoaded() {
        return ModList.get() != null && ModList.get().getModContainerById("goety").isPresent();
    }

    public static boolean goetyLoaded() {
        return ModList.get() != null && ModList.get().getModContainerById("goety").isPresent();
    }
}
