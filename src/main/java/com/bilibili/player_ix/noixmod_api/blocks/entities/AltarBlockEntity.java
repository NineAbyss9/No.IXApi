
package com.bilibili.player_ix.noixmod_api.blocks.entities;

import com.github.NineAbyss9.ix_api.util.Maths;
//import com.bilibili.player_ix.noixmod_api.client.gui.menu.AltarContainer;
import com.bilibili.player_ix.noixmod_api.register.ApiBlockEntities;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class AltarBlockEntity
extends BaseContainerBlockEntity
implements GameEventListener, RecipeHolder, WorldlyContainer {
    public final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private long recipeTime;
    //private final RecipeManager.CachedCheck<CraftingContainer, ? extends RitualRecipe> quickCheck;
    private NonNullList<ItemStack> items;
    public AltarBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ApiBlockEntities.ALTAR.get(), p_155229_, p_155230_);
        this.items = NonNullList.withSize(8, ItemStack.EMPTY);
        //quickCheck = RecipeManager.createCheck(ApiRecipes.RITUAL);
    }

    public void tick() {
    }

    public int getDisplayItemIndex() {
        return 3;
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        ContainerHelper.saveAllItems(pTag, items);
        pTag.putLong("RecipeTime", this.recipeTime);
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((location, integer) ->
                compoundtag.putInt(location.toString(), integer));
        pTag.put("RecipesUsed", compoundtag);
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, items);
        this.recipeTime = pTag.getLong("RecipeTime");
        CompoundTag compoundtag = pTag.getCompound("RecipesUsed");
        for (String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }
    }

    public void dropItems(Level pLevel, BlockPos pPos) {
        for (ItemStack stack : this.items) {
            ItemEntity itemEntity = createItemEntity(pLevel, pPos, stack);
            itemEntity.setNoPickUpDelay();
            pLevel.addFreshEntity(itemEntity);
        }
        this.clearContent();
    }

    private static ItemEntity createItemEntity(Level pLevel, BlockPos blockPos, ItemStack stack) {
        return new ItemEntity(pLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack,
                Maths.trueOrFalse(0.35), 0.1, Maths.trueOrFalse(0.35));
    }

    protected Component getDefaultName() {
        return Component.translatable("block.noixmodapi.altar");
    }

    @Nullable
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory inventory) {
        return //level == null ?
                null //:
                //new AltarContainer(pContainerId, inventory, ContainerLevelAccess.create(this.level, this.worldPosition))
        ;
    }

    public PositionSource getListenerSource() {
        return new BlockPositionSource(this.worldPosition);
    }

    public int getListenerRadius() {
        return 5;
    }

    public boolean handleGameEvent(ServerLevel serverLevel, GameEvent gameEvent, GameEvent.Context context, Vec3 vec3) {
        return false;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public int getContainerSize() {
        return items.size();
    }

    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    public ItemStack getItem(int i) {
        return this.getItems().get(i);
    }

    public ItemStack getResult() {
        return this.items.get(7);
    }

    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack $$2 = ContainerHelper.removeItem(this.getItems(), pSlot, pAmount);
        if (!$$2.isEmpty()) {
            this.setChanged();
        }
        return $$2;
    }

    public void preformRitual(Player player) {

    }

    public void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.getItems(), i);
    }

    public void setItem(int pSlot, ItemStack pStack) {
        this.items.set(pSlot, pStack);
    }

    public boolean stillValid(Player player) {
        return player.isAlive();
    }

    public void clearContent() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable Recipe<?> pRecipe) {

    }

    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public int[] getSlotsForFace(Direction pSide) {
        return new int[0];
    }

    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return false;
    }

    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return false;
    }
}
