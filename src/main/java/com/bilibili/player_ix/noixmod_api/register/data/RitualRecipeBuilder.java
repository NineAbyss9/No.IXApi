
package com.bilibili.player_ix.noixmod_api.register.data;

import com.bilibili.player_ix.noixmod_api.register.ApiRecipes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class RitualRecipeBuilder
implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    //private final List<String> rows = Lists.newArrayList();
    private NonNullList<Ingredient> materials = NonNullList.withSize(7, Ingredient.EMPTY);
    //private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    @Nullable
    private String group;
    private boolean showNotification = true;

    public RitualRecipeBuilder(RecipeCategory pCategory, ItemLike pResult, int pCount) {
        this.category = pCategory;
        this.result = pResult.asItem();
        this.count = pCount;
    }

    public static RitualRecipeBuilder ritual(RecipeCategory pCategory, ItemLike pResult, int pCount) {
        return new RitualRecipeBuilder(pCategory, pResult, pCount);
    }

    public RitualRecipeBuilder define(TagKey<Item> pTag) {
        return this.define(Ingredient.of(pTag));
    }

    /**
     * Adds a key to the recipe pattern.
     */
    public RitualRecipeBuilder define(Ingredient pIngredient) {
        return this.define(NonNullList.of(pIngredient, pIngredient));
    }

    /**
     * Adds a key to the recipe pattern.
     */
    public RitualRecipeBuilder define(NonNullList<Ingredient> pIngredients) {
        this.materials = pIngredients;
        return this;
    }

    /*
    public RitualRecipeBuilder pattern(String pPattern) {
        if (!this.rows.isEmpty() && pPattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pPattern);
            return this;
        }
    }*/

    public RitualRecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    public RitualRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    public RitualRecipeBuilder showNotification(boolean pShowNotification) {
        this.showNotification = pShowNotification;
        return this;
    }

    public Item getResult() {
        return this.result;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        //this.ensureValid(pRecipeId);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe",
                RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                .requirements(RequirementsStrategy.OR);
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, this.result, this.count, this.group == null ? "" :
                this.group, this.category, this.materials, this.advancement, pRecipeId
                .withPrefix("recipes/" + this.category.getFolderName() + "/"), this.showNotification));
    }

    /*
    private void ensureValid(ResourceLocation pId) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + pId + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + pId);
            } else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + pId + " only takes in a single item - should it be a shapeless recipe instead?");
            } else if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + pId);
            }
        }
    }*/

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final NonNullList<Ingredient> materials;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final boolean showNotification;

        public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, RecipeCategory pCategory,
                      NonNullList<Ingredient> ingredients, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId,
                      boolean pShowNotification) {
            this.id = pId;
            this.result = pResult;
            this.count = pCount;
            this.group = pGroup;
            this.materials = ingredients;
            this.advancement = pAdvancement;
            this.advancementId = pAdvancementId;
            this.showNotification = pShowNotification;
        }

        public void serializeRecipeData(JsonObject pJson) {
            if (!this.group.isEmpty()) {
                pJson.addProperty("group", this.group);
            }
            JsonArray jsonarray = new JsonArray();
            for (Ingredient s : this.materials) {
                jsonarray.add(s.toJson());
            }
            pJson.add("materials", jsonarray);
            JsonObject jo = new JsonObject();
            jo.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jo.addProperty("count", this.count);
            }
            pJson.add("result", jo);
            pJson.addProperty("show_notification", this.showNotification);
        }

        public RecipeSerializer<?> getType() {
            return ApiRecipes.RITUAL_SER.get();
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getId() {
            return this.id;
        }

        /**
         * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
         */
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
