
package com.bilibili.player_ix.noixmod_api.client.gui.menu;

import com.bilibili.player_ix.noixmod_api.client.gui.ApiGuis;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AltarMenu extends ItemCombinerMenu {
    public AltarMenu(int p_38852_, Inventory inventory, ContainerLevelAccess access) {
        super(ApiGuis.ALTAR.get(), p_38852_, inventory, access);
        /*this.addSlot(new SlotItemHandler(handler, 1, 123, 29));
        this.addSlot(new SlotItemHandler(handler, 2, 87, 51));
        this.addSlot(new SlotItemHandler(handler, 3, 119, 84));
        this.addSlot(new SlotItemHandler(handler, 4, 158, 53));
        this.addSlot(new SlotItemHandler(handler, 5, 158, 104));
        this.addSlot(new SlotItemHandler(handler, 6, 120, 134));
        this.addSlot(new SlotItemHandler(handler, 7, 83, 104));
        for (int i = 0; i < 9; ++i) {//49, 157
            this.addSlot(new Slot(inventory, i + 7, 50 + 18 * i, 166));
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i + 16, 50 + 18 * i, 184));
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i + 25, 50 + 18 * i, 202));
        }
        for (int k = 0; k < 9; ++k) {//49, 215
            this.addSlot(new Slot(inventory, k, 50 + k * 18, 216));
        }*/
    }

    public static AltarMenu create(int id, Inventory inventory, FriendlyByteBuf buf) {
        return new AltarMenu(id, inventory, ContainerLevelAccess.NULL);
    }

    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            stack = itemStack.copy();
            if (i < 11) {
                if (!this.moveItemStackTo(itemStack, 11, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack, 0, 11, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
        return true;
    }

    protected void onTake(Player pPlayer, ItemStack pStack) {
    }

    protected boolean isValidBlock(BlockState pState) {
        return pState.is(NoixmodAPIBlocks.ALTAR.get());
    }

    public void createResult() {

    }

    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().build();
    }

    public boolean stillValid(Player player) {
        return player.isAlive();
    }
}
