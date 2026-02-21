
package com.bilibili.player_ix.noixmod_api.compat;

import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

public interface Compatable {
    void setup(FMLCommonSetupEvent event);

    @Nonnull
    static Item getItem(String path, String name) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(path + name));
        if (item == null) {
            throw new NoSuchElementException("Entered name not present.");
        }
        return item;
    }

    @Nonnull
    static MobEffect getMobEffect(String path, String name) {
        MobEffect item = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(path + name));
        if (item == null) {
            throw new NoSuchElementException("Entered name not present.");
        }
        return item;
    }

    @Nonnull
    static ItemStack getItemStack(String path, String name) {
        return ItemStacks.of(getItem(path, name));
    }
}
