
package com.bilibili.player_ix.noixmod_api.register.data;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
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
        ritual(RecipeCategory.MISC, NoixmodAPIItems.BANNED_BOOK.get(), 1,
                List.of(ing(Items.END_CRYSTAL), ing(NoixmodAPIItems.NIHILISTIC_ESSENCE.get()),
                        ing(Items.NETHERITE_INGOT)), pWriter);
    }

    public static Ingredient ing(ItemLike like) {
        return Ingredient.of(like);
    }

    public static void ritual(RecipeCategory pCategory, ItemLike pResult, int pCount,
                              List<Ingredient> materials, Consumer<FinishedRecipe> pConsumer) {
        ItemStack stack = materials.get(0).getItems()[0];
        RitualRecipeBuilder.ritual(pCategory, pResult, pCount).define(NonNullList.of(Ingredient.EMPTY, materials.toArray(new Ingredient[0])))
                .unlockedBy(getHasName(stack.getItem()), has(stack.getItem()))
                .save(pConsumer, new ResourceLocation("noixmodapi", pResult.asItem() + "_ritual"));
    }
}
