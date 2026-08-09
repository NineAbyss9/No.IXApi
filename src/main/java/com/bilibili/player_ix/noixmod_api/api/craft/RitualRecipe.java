
package com.bilibili.player_ix.noixmod_api.api.craft;

import com.bilibili.player_ix.noixmod_api.register.ApiRecipes;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class RitualRecipe implements Recipe<CraftingContainer> {
    public static final int RECIPE_COUNT = 7;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack result;
    private final ResourceLocation id;
    private final String group;
    private final boolean showNotification;

    public RitualRecipe(ResourceLocation pId, String pGroup, NonNullList<Ingredient> pRecipeItems,
                        ItemStack pResult, boolean pShowNotification) {
        this.id = pId;
        this.group = pGroup;
        this.recipeItems = pRecipeItems;
        this.result = pResult;
        this.showNotification = pShowNotification;
    }

    public RitualRecipe(ResourceLocation pId, String pGroup, NonNullList<Ingredient> pRecipeItems, ItemStack pResult) {
        this(pId, pGroup, pRecipeItems, pResult, true);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return ApiRecipes.RITUAL_SER.get();
    }

    public String getGroup() {
        return this.group;
    }

    public ItemStack getResultItem(@Nullable RegistryAccess pRegistryAccess) {
        return this.result;
    }

    public ItemStack getResultItem() {
        return this.result;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    public boolean showNotification() {
        return this.showNotification;
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public boolean matches(CraftingContainer pInv, Level pLevel) {
        for (int i = 0;i < RECIPE_COUNT;i++) {
            ItemStack stackInSlot = pInv.getItem(i);
            Ingredient required = recipeItems.get(i);
            if (!required.isEmpty() && stackInSlot.isEmpty()) {
                return false;
            }
            if (!required.isEmpty() && !required.test(stackInSlot)) {
                return false;
            }
            if (required.isEmpty() && !stackInSlot.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public ItemStack assemble(@Nullable CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return this.getResultItem(pRegistryAccess).copy();
    }

    public RecipeType<?> getType() {
        return ApiRecipes.RITUAL_RECIPE.get();
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter((p_151277_) -> !p_151277_.isEmpty())
                .anyMatch(ForgeHooks::hasNoElements);
    }

    public static ItemStack itemStackFromJson(JsonObject pStackObject) {
        ItemStack stack = CraftingHelper.getItemStack(pStackObject, true, true);
        return stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    public static class Serializer implements RecipeSerializer<RitualRecipe> {
        public RitualRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            String s = GsonHelper.getAsString(pJson, "group", "");
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(RECIPE_COUNT, Ingredient.EMPTY);
            var list = GsonHelper.getAsJsonArray(pJson, "materials").asList();
            for (int i = 0; i < RECIPE_COUNT; i++) {
                nonnulllist.set(i, Ingredient.fromJson(list.get(i)));
            }
            ItemStack itemstack = RitualRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
            boolean flag = GsonHelper.getAsBoolean(pJson, "show_notification", true);
            return new RitualRecipe(pRecipeId, s, nonnulllist, itemstack, flag);
        }

        public RitualRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            String s = pBuffer.readUtf();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(RECIPE_COUNT, Ingredient.EMPTY);
            for (int i = 0; i < RECIPE_COUNT; i++) {
                nonnulllist.set(i, Ingredient.fromNetwork(pBuffer));
            }
            ItemStack itemstack = pBuffer.readItem();
            boolean flag = pBuffer.readBoolean();
            return new RitualRecipe(pRecipeId, s, nonnulllist, itemstack, flag);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, RitualRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.group);
            for (Ingredient ingredient : pRecipe.recipeItems) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeBoolean(pRecipe.showNotification);
        }
    }
}
