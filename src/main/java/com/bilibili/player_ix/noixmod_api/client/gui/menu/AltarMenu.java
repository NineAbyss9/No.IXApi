
package com.bilibili.player_ix.noixmod_api.client.gui.menu;

import com.bilibili.player_ix.noixmod_api.api.craft.RitualRecipe;
import com.bilibili.player_ix.noixmod_api.client.gui.ApiGuis;
import com.bilibili.player_ix.noixmod_api.register.ApiRecipes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

@SuppressWarnings("unused")
public class AltarMenu extends AbstractContainerMenu {
    public static final int resultSlotIndex = 0;
    private final CraftingContainer craftSlots;
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;
    public AltarMenu(int pContainerId, Inventory inventory, //AltarBlockEntity entity,
                     ContainerLevelAccess accessIn) {
        super(ApiGuis.ALTAR.get(), pContainerId);
        this.craftSlots = new AltarContainer(this);
        this.access = accessIn;
        this.player = inventory.player;
        this.addSlot(new ResultSlot(player, craftSlots, resultSlots, resultSlotIndex, 183, 79));
        this.addSlot(new Slot(craftSlots, 1, 123, 29));
        this.addSlot(new Slot(craftSlots, 2, 87, 51));
        this.addSlot(new Slot(craftSlots, 3, 119, 84));
        this.addSlot(new Slot(craftSlots, 4, 158, 53));
        this.addSlot(new Slot(craftSlots, 5, 158, 104));
        this.addSlot(new Slot(craftSlots, 6, 120, 134));
        this.addSlot(new Slot(craftSlots, 7, 83, 104));
        for (int i = 0;i < 9;++i) {
            this.addSlot(new Slot(inventory, i + 9, 50 + 18 * i, 158));
        }
        for (int i = 0;i < 9;++i) {
            this.addSlot(new Slot(inventory, i + 18, 50 + 18 * i, 177));
        }
        for (int i = 0;i < 9;++i) {
            this.addSlot(new Slot(inventory, i + 27, 50 + 18 * i, 195));
        }
        for (int k = 0;k < 9;++k) {
            this.addSlot(new Slot(inventory, k, 50 + k * 18, 216));
        }
    }

    public static AltarMenu create(int id, Inventory inventory, FriendlyByteBuf buf) {
        return new AltarMenu(id, inventory, //null,
                ContainerLevelAccess.NULL);
    }

    public ItemStack quickMoveStack(Player player, int pIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            stack = itemStack.copy();
            if (pIndex < 11) {
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

    public void createResult() {
    }

    public void slotsChanged(Container pInventory) {
        this.access.execute((p_39386_, p_39387_) ->
                slotChangedCraftingGrid(p_39386_, this.player, this.craftSlots, this.resultSlots));
    }

    protected void slotChangedCraftingGrid(Level pLevel,
                                                  Player pPlayer, CraftingContainer pContainer, ResultContainer pResult) {
        if (!pLevel.isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer)pPlayer;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<RitualRecipe> optional = pLevel.getServer().getRecipeManager().getRecipeFor(ApiRecipes.RITUAL_RECIPE
                    .get(), pContainer, pLevel);
            if (optional.isPresent()) {
                RitualRecipe recipe = optional.get();
                if (pResult.setRecipeUsed(pLevel, serverplayer, recipe)) {
                    ItemStack itemstack1 = recipe.assemble(pContainer, pLevel.registryAccess());
                    if (itemstack1.isItemEnabled(pLevel.enabledFeatures())) {
                        itemstack = itemstack1;
                    }
                }
            }
            pResult.setItem(resultSlotIndex, itemstack);
            this.setRemoteSlot(resultSlotIndex, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId()
                    , resultSlotIndex, itemstack));
        }
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.access.execute((p_39371_, p_39372_) -> this.clearContainer(pPlayer, this.craftSlots));
    }

    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultSlots && super.canTakeItemForPickAll(pStack, pSlot);
    }

    public boolean stillValid(Player player) {
        return player.isAlive() && access.evaluate((level, blockPos) ->
                level.getBlockState(blockPos).is(NoixmodAPIBlocks.ALTAR.get()), true);
    }
}
