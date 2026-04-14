
package com.bilibili.player_ix.noixmod_api.item.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

@Deprecated
public class AltarHandler extends ItemStackHandler {
    private final ItemStack itemStack;
    private final int size;
    private int slot;

    public AltarHandler(ItemStack itemStack, int size) {
        super(size);
        this.size = size;
        this.itemStack = itemStack;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    public ItemStack getSlot() {
        return getStackInSlot(slot);
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putInt("slot", slot);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            if (nbt.contains("slot")) {
                slot = nbt.getInt("slot");
                stacks.set(slot, ItemStack.of(itemTags));
            }
        }
        this.onLoad();
    }

    protected void onContentsChanged(int slot) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.putBoolean("api.dirty", !nbt.getBoolean("api.dirty"));
    }

    public int getSlotLimit(int slot) {
        return this.size;
    }
}
