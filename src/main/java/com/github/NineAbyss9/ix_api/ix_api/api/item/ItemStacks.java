
package com.github.NineAbyss9.ix_api.ix_api.api.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ItemStacks {
    private ItemStacks() {
    }

    public static ItemStack of() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Contract("_ -> new")
    public static ItemStack of(Item item) {
        return new ItemStack(item);
    }

    @Nonnull
    public static ItemStack of(@Nonnull Supplier<Item> item) {
        return of(item.get());
    }

    @Nonnull
    public static ItemStack of(Item item, int count) {
        return new ItemStack(item, count);
    }

    @Nonnull
    public static ItemStack of(@Nonnull Supplier<Item> item, int count) {
        return of(item.get(), count);
    }
}
