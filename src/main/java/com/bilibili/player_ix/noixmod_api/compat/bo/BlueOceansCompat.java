
package com.bilibili.player_ix.noixmod_api.compat.bo;

import com.bilibili.player_ix.noixmod_api.compat.Compatable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class BlueOceansCompat
implements Compatable {
    public BlueOceansCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get() != null && ModList.get().getModContainerById("blue_oceans").isPresent();
    }

    public static MobEffect getEffect(String name) {
        return Compatable.getMobEffect("blue_oceans:", name);
    }

    public static ItemStack getItemStack(String name) {
        return Compatable.getItemStack("blue_oceans:", name);
    }

    public static Item getItem(String name) {
        return Compatable.getItem("blue_oceans:", name);
    }

    public void setup(FMLCommonSetupEvent event) {

    }
}
