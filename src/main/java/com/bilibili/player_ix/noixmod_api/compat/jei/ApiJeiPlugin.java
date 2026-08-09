
package com.bilibili.player_ix.noixmod_api.compat.jei;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.api.craft.RitualRecipe;
import com.bilibili.player_ix.noixmod_api.register.ApiRecipes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

//Code from Polarice3's GoetyJeiPlugin.class
@JeiPlugin
public class ApiJeiPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelper;

    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper = registration.getJeiHelpers();
        registration.addRecipeCategories(new ApiRitualCategory(registration.getJeiHelpers().getGuiHelper()));
        /*for (IRitualType ritualType : RitualType.getAllRitualType()) {
            registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), ritualType.getName()));
        }
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.ANIMATION));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.NECROTURGY));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.FORGE));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.GEOTURGY));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.MAGIC));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.SABBATH));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.ADEPT_NETHER));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.EXPERT_NETHER));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.END));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.FROST));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.SKY));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.STORM));
        registration.addRecipeCategories(new ModRitualCategory(registration.getJeiHelpers().getGuiHelper(), RitualTypes.DEEP));*/
    }

    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(NoixmodAPIBlocks.ALTAR.get()), ApiJeiRecipes.RITUAL);
        //registration.addRecipeCatalyst(new ItemStack(ModBlocks.PEDESTAL.get()), JeiRecipeTypes.RITUAL);
        /*for (IRitualType ritualType : RitualType.getAllRitualType()) {
            registration.addRecipeCatalyst(ritualType.getJeiIcon(), ApiJeiRecipes.getRitual(ritualType.getName()));
        }*/
        /*registration.addRecipeCatalyst(new ItemStack(ModItems.ANIMATION_CORE.get()), JeiRecipeTypes.getRitual(RitualTypes.ANIMATION));
        registration.addRecipeCatalyst(new ItemStack(Blocks.SCULK), JeiRecipeTypes.getRitual(RitualTypes.NECROTURGY));
        registration.addRecipeCatalyst(new ItemStack(Blocks.ANVIL), JeiRecipeTypes.getRitual(RitualTypes.FORGE));
        registration.addRecipeCatalyst(new ItemStack(Blocks.CHIPPED_ANVIL), JeiRecipeTypes.getRitual(RitualTypes.FORGE));
        registration.addRecipeCatalyst(new ItemStack(Blocks.DAMAGED_ANVIL), JeiRecipeTypes.getRitual(RitualTypes.FORGE));
        registration.addRecipeCatalyst(new ItemStack(Blocks.AMETHYST_BLOCK), JeiRecipeTypes.getRitual(RitualTypes.GEOTURGY));
        registration.addRecipeCatalyst(new ItemStack(Blocks.ENCHANTING_TABLE), JeiRecipeTypes.getRitual(RitualTypes.MAGIC));
        registration.addRecipeCatalyst(new ItemStack(Blocks.CRYING_OBSIDIAN), JeiRecipeTypes.getRitual(RitualTypes.SABBATH));
        registration.addRecipeCatalyst(new ItemStack(Blocks.BLACKSTONE), JeiRecipeTypes.getRitual(RitualTypes.ADEPT_NETHER));
        registration.addRecipeCatalyst(new ItemStack(Blocks.NETHER_BRICKS), JeiRecipeTypes.getRitual(RitualTypes.EXPERT_NETHER));
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.VOID_BLOCK.get()), JeiRecipeTypes.getRitual(RitualTypes.END));
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FREEZING_LAMP.get()), JeiRecipeTypes.getRitual(RitualTypes.FROST));
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MARBLE_BLOCK.get()), JeiRecipeTypes.getRitual(RitualTypes.SKY));
        registration.addRecipeCatalyst(new ItemStack(Blocks.LIGHTNING_ROD), JeiRecipeTypes.getRitual(RitualTypes.STORM));
        registration.addRecipeCatalyst(new ItemStack(Blocks.PRISMARINE_BRICKS), JeiRecipeTypes.getRitual(RitualTypes.DEEP));*/
    }

    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
        RecipeManager recipeManager = world.getRecipeManager();
        //IIngredientManager ingredientManager = registration.getIngredientManager();
        //IVanillaRecipeFactory vanillaRecipeFactory = registration.getVanillaRecipeFactory();
        List<RitualRecipe> ritualRecipes = recipeManager.getAllRecipesFor(ApiRecipes.RITUAL_RECIPE.get());
        registration.addRecipes(ApiJeiRecipes.RITUAL, ritualRecipes);
        /*for (IRitualType ritualType : RitualType.getAllRitualType()) {
            this.registerRitualType(registration, recipeManager, ritualType.getName());
        }
        this.registerRitualType(registration, recipeManager, RitualTypes.ANIMATION);
        this.registerRitualType(registration, recipeManager, RitualTypes.NECROTURGY);
        this.registerRitualType(registration, recipeManager, RitualTypes.FORGE);
        this.registerRitualType(registration, recipeManager, RitualTypes.GEOTURGY);
        this.registerRitualType(registration, recipeManager, RitualTypes.MAGIC);
        this.registerRitualType(registration, recipeManager, RitualTypes.SABBATH);
        this.registerRitualType(registration, recipeManager, RitualTypes.ADEPT_NETHER);
        this.registerRitualType(registration, recipeManager, RitualTypes.EXPERT_NETHER);
        this.registerRitualType(registration, recipeManager, RitualTypes.END);
        this.registerRitualType(registration, recipeManager, RitualTypes.FROST);
        this.registerRitualType(registration, recipeManager, RitualTypes.SKY);
        this.registerRitualType(registration, recipeManager, RitualTypes.STORM);
        this.registerRitualType(registration, recipeManager, RitualTypes.DEEP);*/
    }

    public void registerRitualType(IRecipeRegistration registration, RecipeManager recipeManager, String type) {
        this.registerRitualType(registration, recipeManager, type, type);
    }

    public void registerRitualType(IRecipeRegistration registration, RecipeManager recipeManager,
                                   String type, String type2) {
        registration.addRecipes(ApiJeiRecipes.getRitual(type), this.ritualTypeRecipe(recipeManager, type2));
    }

    public List<RitualRecipe> ritualTypeRecipe(RecipeManager recipeManager, String type){
        return recipeManager.getAllRecipesFor(ApiRecipes.RITUAL_RECIPE.get()).stream().filter(
                ritualRecipe -> true/*ritualRecipe.getCraftType().contains(type)*/).toList();
    }

    public ResourceLocation getPluginUid() {
        return NoixmodAPI.location("jei_plugin");
    }
}

/*
MIT License

Copyright (c) 2023 Polarice3

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/