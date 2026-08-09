
package com.bilibili.player_ix.noixmod_api.compat.jei;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.api.craft.RitualRecipe;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.library.recipes.ExtendableRecipeCategoryHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public class ApiRitualCategory
implements IRecipeCategory<RitualRecipe>
{
    private final IDrawable background;
    //private final IDrawable arrow;
    private final Component localizedName;
    private final ItemStack altar = new ItemStack(NoixmodAPIBlocks.ALTAR.get());
    //private final ItemStack pedestals = new ItemStack(ModItems.PEDESTAL_DUMMY.get());
    private final int iconWidth = 16;
    private final int ritualCenterX;
    private final int ritualCenterY;
    private int recipeOutputOffsetX = 50;
    private final IGuiHelper guiHelper;
    private final ICraftingGridHelper craftingGridHelper;
    private final ExtendableRecipeCategoryHelper<Recipe<?>, ICraftingCategoryExtension> extendableHelper;

    public ApiRitualCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(NoixmodAPI.gui("altar/background"), 0,
                0, 256, 256);
        this.ritualCenterX = this.background.getWidth() / 2 - this.iconWidth / 2 - 24;
        this.ritualCenterY = this.background.getHeight() / 2 - this.iconWidth / 2 + 10;
        this.localizedName = Component.translatable("noixmodapi.jei.ritual");
        this.altar.getOrCreateTag().putBoolean("RenderFull", true);
        //this.arrow = guiHelper.createDrawable(
        //        NoixmodAPI.location("textures/gui/jei/arrow.png"), 0, 0, 64, 46);\
        this.guiHelper = guiHelper;
        this.craftingGridHelper = guiHelper.createCraftingGridHelper();
        this.extendableHelper =
                new ExtendableRecipeCategoryHelper<Recipe<?>, ICraftingCategoryExtension>(RitualRecipe.class);
    }

    public int getWidth() {
        return 256;
    }

    public int getHeight() {
        return 256;
    }

    public RecipeType<RitualRecipe> getRecipeType()
    {
        return ApiJeiRecipes.RITUAL;
    }

    public Component getTitle()
    {
        return this.localizedName;
    }

    @Nullable
    public IDrawable getIcon()
    {
        return this.guiHelper.createDrawableItemLike(NoixmodAPIBlocks.ALTAR.get());
    }

    public void setRecipe(IRecipeLayoutBuilder layout, RitualRecipe recipe, IFocusGroup focuses)
    {
        layout.addOutputSlot(184, 79).setOutputSlotBackground().addItemStack(recipe.getResultItem());
        layout.addInputSlot(123, 29).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(0));
        layout.addInputSlot(87, 51).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(1));
        layout.addInputSlot(119, 84).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(2));
        layout.addInputSlot(158, 53).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(3));
        layout.addInputSlot(158, 104).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(4));
        layout.addInputSlot(120, 134).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(5));
        layout.addInputSlot(83, 104).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(6));
        layout.moveRecipeTransferButton(166, 130);
    }

    public void draw(RitualRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        this.background.draw(guiGraphics, (this.getWidth() - 256) / 2, (this.getHeight() - 256) / 2);
    }
}

/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

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