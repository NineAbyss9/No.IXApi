
package com.github.NineAbyss9.ix_api.ix_api.api.crafting;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.PAMAreNonnullByDefault;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ShapeRecipe implements Recipe<CraftingContainer> {
    protected final ResourceLocation id;
    protected final String group;
    protected final CraftingBookCategory category;
    protected final ItemStack result;
    protected final NonNullList<Ingredient> ingredients;
    protected final boolean isSimple;
    protected final ShapeSerializer serializer = new ShapeSerializer();

    public ShapeRecipe(ResourceLocation location, String s, CraftingBookCategory cr, ItemStack stack,
                       NonNullList<Ingredient> list) {
        this.id = location;
        this.group = s;
        this.category = cr;
        this.result = stack;
        this.ingredients = list;
        this.isSimple = list.stream().allMatch(Ingredient::isSimple);
    }

    public boolean matches(CraftingContainer p_44262_, Level p_44263_) {
        StackedContents stackedcontents = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        int i = 0;
        for(int j = 0; j < p_44262_.getContainerSize(); ++j) {
            ItemStack itemstack = p_44262_.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    stackedcontents.accountStack(itemstack, 1);
                else inputs.add(itemstack);
            }
        }
        return i == this.ingredients.size() && (isSimple ? stackedcontents.canCraft(this, null)
                : RecipeMatcher.findMatches(inputs,  this.ingredients) != null);
    }

    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    public boolean canCraftInDimensions(int i, int i1) {
        return i * i1 > this.ingredients.size();
    }

    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHAPELESS_RECIPE;
    }

    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @PAMAreNonnullByDefault
    public static class ShapeSerializer implements RecipeSerializer<ShapeRecipe> {
        static int MAX_WIDTH = 3;
        static int MAX_HEIGHT = 3;
        private static final ResourceLocation NAME = new ResourceLocation("minecraft", "crafting_shapeless");

        public ShapeRecipe fromJson(ResourceLocation p_44290_, JsonObject p_44291_) {
            String s = GsonHelper.getAsString(p_44291_, "group", "");
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(p_44291_, "category", (String) null), CraftingBookCategory.MISC);
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(p_44291_, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > MAX_WIDTH * MAX_HEIGHT) {
                throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is " + (MAX_WIDTH * MAX_HEIGHT));
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_44291_, "result"));
                return new ShapeRecipe(p_44290_, s, craftingbookcategory, itemstack, nonnulllist);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray p_44276_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();
            for (int i = 0; i < p_44276_.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(p_44276_.get(i), false);
                // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
                nonnulllist.add(ingredient);
            }
            return nonnulllist;
        }

        @Nullable
        public ShapeRecipe fromNetwork(ResourceLocation p_44293_, FriendlyByteBuf p_44294_) {
            String string = p_44294_.readUtf();
            CraftingBookCategory craftingbookcategory = p_44294_.readEnum(CraftingBookCategory.class);
            int i = p_44294_.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(ignored -> Ingredient.fromNetwork(p_44294_));
            ItemStack itemstack = p_44294_.readItem();
            return new ShapeRecipe(p_44293_, string, craftingbookcategory, itemstack, nonnulllist);
        }

        public void toNetwork(FriendlyByteBuf p_44281_, ShapeRecipe p_44282_) {
            p_44281_.writeUtf(p_44282_.group);
            p_44281_.writeEnum(p_44282_.category);
            p_44281_.writeVarInt(p_44282_.ingredients.size());
            for (Ingredient ingredient : p_44282_.ingredients) {
                ingredient.toNetwork(p_44281_);
            }
            p_44281_.writeItem(p_44282_.result);
        }
    }
}
