
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

    /**
     * Recipes with equal group are combined into one button in the recipe book
     */
    public String getGroup() {
        return this.group;
    }

    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.result;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    public boolean showNotification() {
        return this.showNotification;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        /*for (int i = 0; i <= pInv.getWidth() - this.width; ++i) {
            for (int j = 0; j <= pInv.getHeight() - this.height; ++j) {
                if (this.matches(pInv, i, j, true)) {
                    return true;
                }
                if (this.matches(pInv, i, j, false)) {
                    return true;
                }
            }
        }*/
        boolean flag = true;
        for (int i = 0;i < RECIPE_COUNT;i++) {
            var list = pInv.getItems();
            //try {
                if (list.get(i).isEmpty() && !recipeItems.get(i).isEmpty()) {
                    flag = false;
                } else if (!recipeItems.get(i).test(list.get(i))) {
                    flag = false;
                }
            //}
        }
        return flag;
    }

    /*private boolean matches(CraftingContainer pCraftingInventory, int pWidth, int pHeight, boolean pMirrored) {
        for (int i = 0; i < pCraftingInventory.getWidth(); ++i) {
            for (int j = 0; j < pCraftingInventory.getHeight(); ++j) {
                int k = i - pWidth;
                int l = j - pHeight;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (pMirrored) {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }
                if (!ingredient.test(pCraftingInventory.getItem(i + j * pCraftingInventory.getWidth()))) {
                    return false;
                }
            }
        }
        return true;
    }*/

    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return this.getResultItem(pRegistryAccess).copy();
    }

    /*public int getWidth() {
        return this.width;
    }

    public int getRecipeWidth() {
        return getWidth();
    }

    public int getHeight() {
        return this.height;
    }

    public int getRecipeHeight() {
        return getHeight();
    }

    static NonNullList<Ingredient> dissolvePattern(String[] pPattern, Map<String, Ingredient> pKeys, int pPatternWidth, int pPatternHeight) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(9, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(pKeys.keySet());
        set.remove(" ");
        for(int i = 0; i < pPattern.length; ++i) {
            for(int j = 0; j < pPattern[i].length(); ++j) {
                String s = pPattern[i].substring(j, j + 1);
                Ingredient ingredient = pKeys.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }
                set.remove(s);
                nonnulllist.set(j + pPatternWidth * i, ingredient);
            }
        }
        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... pToShrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;
        for (int i1 = 0; i1 < pToShrink.length; ++i1) {
            String s = pToShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }
                ++l;
            } else {
                l = 0;
            }
        }
        if (pToShrink.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[pToShrink.length - l - k];
            for (int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = pToShrink[k1 + k].substring(i, j + 1);
            }
            return astring;
        }
    }*/

    public RecipeType<?> getType() {
        return ApiRecipes.RITUAL_RECIPE.get();
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter((p_151277_) -> !p_151277_.isEmpty())
                .anyMatch(ForgeHooks::hasNoElements);
    }

    /*private static int firstNonSpace(String pEntry) {
        int i = 0;
        while (i < pEntry.length() && pEntry.charAt(i) == ' ')
            ++i;
        return i;
    }

    private static int lastNonSpace(String pEntry) {
        int i = pEntry.length() - 1;
        while (i >= 0 && pEntry.charAt(i) == ' ')
            --i;
        return i;
    }

    static String[] patternFromJson(JsonArray pPatternArray) {
        String[] astring = new String[pPatternArray.size()];
        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + 3 + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int i = 0; i < astring.length; ++i) {
                String s = GsonHelper.convertToString(pPatternArray.get(i), "pattern[" + i + "]");
                if (s.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + 3 + " is maximum");
                }
                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }
                astring[i] = s;
            }
            return astring;
        }
    }

    static Map<String, Ingredient> keyFromJson(JsonObject pKeyEntry) {
        Map<String, Ingredient> map = Maps.newHashMap();
        for(Map.Entry<String, JsonElement> entry : pKeyEntry.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue(), false));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }*/

    public static ItemStack itemStackFromJson(JsonObject pStackObject) {
        return CraftingHelper.getItemStack(pStackObject, true, true);
    }

    /*public static Item itemFromJson(JsonObject pItemObject) {
        String s = GsonHelper.getAsString(pItemObject, "item");
        Item item = BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(s)).orElseThrow(() ->
                new JsonSyntaxException("Unknown item '" + s + "'"));
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Empty ingredient not allowed here");
        } else {
            return item;
        }
    }*/

    public static class Serializer implements RecipeSerializer<RitualRecipe> {
        //private static final ResourceLocation NAME = NoixmodAPI.location("ritual");
        public RitualRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            String s = GsonHelper.getAsString(pJson, "group", "");
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(RECIPE_COUNT, Ingredient.EMPTY);
            nonnulllist.replaceAll(ingredient -> Ingredient.fromJson(pJson));
            ItemStack itemstack = RitualRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
            boolean flag = GsonHelper.getAsBoolean(pJson, "show_notification", true);
            return new RitualRecipe(pRecipeId, s, nonnulllist, itemstack, flag);
        }

        public RitualRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            String s = pBuffer.readUtf();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(RECIPE_COUNT, Ingredient.EMPTY);
            nonnulllist.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));
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
