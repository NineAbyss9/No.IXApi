
package com.bilibili.player_ix.noixmod_api.register.data;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;

public class ApiRecipeProvider
extends RecipeProvider {
    public ApiRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        //Shaped
        shaped(RecipeCategory.MISC, NoixmodAPIItems.ALTAR.get(), Items.OBSIDIAN, NoixmodAPIItems.NIHILISTIC_ESSENCE.get(),
                getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN), "ixi", " i ", "iii", pWriter);

        shaped(RecipeCategory.TOOLS, NoixmodAPIItems.GRAVE_AXE.get(), Items.IRON_INGOT,
                getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT), "ii", "ii", " i", pWriter);
        shaped(RecipeCategory.COMBAT, NoixmodAPIItems.GRAVE_SWORD.get(), Items.IRON_INGOT,
                getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT), "i", "i", "i", pWriter);

        shaped(RecipeCategory.COMBAT, NoixmodAPIItems.CREEPER_EGG.get(), Items.GUNPOWDER,
                getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER), " i ", "i i", " i ",
                pWriter);
        shaped(RecipeCategory.COMBAT, NoixmodAPIItems.MAGICAL_SWORD.get(), NoixmodAPIItems.SPIRIT_STONE.get(),
                Items.IRON_INGOT, getHasName(NoixmodAPIItems.SPIRIT_STONE.get()), has(NoixmodAPIItems.SPIRIT_STONE.get()),
                "  x", " x ", "i  ", pWriter);
        //Shapeless
        shapeless(RecipeCategory.COMBAT, NoixmodAPIItems.BOW_BOW.get(), Items.BOW, 2, pWriter);

        //Ritual
        ritual(RecipeCategory.MISC, NoixmodAPIItems.BANNED_BOOK.get(), 1,
                List.of(ing(Items.END_CRYSTAL), ing(NoixmodAPIItems.NIHILISTIC_ESSENCE.get()),
                        ing(Items.NETHERITE_INGOT)), pWriter);
    }

    /**Single*/
    public static void shapeless(RecipeCategory pRC, ItemLike pResult, ItemLike pMaterial, Consumer<FinishedRecipe> pWriter)
    {
        ShapelessRecipeBuilder.shapeless(pRC, pResult).requires(pMaterial)
                .unlockedBy(getHasName(pResult), has(pMaterial)).save(pWriter,
                        NoixmodAPI.location(pResult + "_shapeless"));
    }

    /**Single with count*/
    public static void shapeless(RecipeCategory pRC, ItemLike pResult, ItemLike pMaterial, int matCount, Consumer<FinishedRecipe> pWriter)
    {
        ShapelessRecipeBuilder.shapeless(pRC, pResult).requires(pMaterial, matCount)
                .unlockedBy(getHasName(pResult), has(pMaterial)).save(pWriter);
    }

    public static void shapeless(RecipeCategory pRC, ItemLike pResult, int pCount, Ingredient pIngredient, Consumer<FinishedRecipe> pWriter)
    {
        ShapelessRecipeBuilder.shapeless(pRC, pResult, pCount).requires(pIngredient)
                .unlockedBy(getHasName(pResult), has(pIngredient.getItems()[0].getItem())).save(pWriter);
    }

    public static void shaped(RecipeCategory pC, ItemLike pResult, ItemLike pItem,
                              String cn, CriterionTriggerInstance instance, String pattern1, String pattern2,
                              String pattern3, Consumer<FinishedRecipe> pWriter)
    {
        ShapedRecipeBuilder.shaped(pC, pResult).define('i', pItem)
                .pattern(pattern1).pattern(pattern2).pattern(pattern3)
                .unlockedBy(cn, instance).save(pWriter);
    }

    public static void shaped(RecipeCategory pC, ItemLike pResult, ItemLike pItem, ItemLike pItem1,
                              String cn, CriterionTriggerInstance instance, String pattern1, String pattern2,
                              String pattern3, Consumer<FinishedRecipe> pWriter)
    {
        ShapedRecipeBuilder.shaped(pC, pResult).define('i', pItem)
                .define('x', pItem1).pattern(pattern1).pattern(pattern2).pattern(pattern3)
                .unlockedBy(cn, instance).save(pWriter);
    }

    public static void shaped(RecipeCategory pC, ItemLike pResult, ItemLike pItem, ItemLike pItem1, ItemLike pItem2,
                              String cn, CriterionTriggerInstance instance, String pattern1, String pattern2,
                              String pattern3, Consumer<FinishedRecipe> pWriter)
    {
        ShapedRecipeBuilder.shaped(pC, pResult).define('i', pItem)
                .define('x', pItem1).define('9', pItem2).pattern(pattern1).pattern(pattern2).pattern(pattern3)
                .unlockedBy(cn, instance).save(pWriter);
    }

    public static Ingredient ing(ItemLike like) {
        return Ingredient.of(like);
    }

    public static void ritual(RecipeCategory pCategory, ItemLike pResult, int pCount,
                              List<Ingredient> materials, Consumer<FinishedRecipe> pConsumer) {
        ItemStack stack = materials.get(0).getItems()[0];
        RitualRecipeBuilder.ritual(pCategory, pResult, pCount).define(NonNullList.of(Ingredient.EMPTY,
                        materials.toArray(new Ingredient[0])))
                .unlockedBy(getHasName(stack.getItem()), has(stack.getItem()))
                .save(pConsumer, new ResourceLocation(NoixmodAPI.MOD_ID, pResult.asItem() + "_ritual"));
    }
}
