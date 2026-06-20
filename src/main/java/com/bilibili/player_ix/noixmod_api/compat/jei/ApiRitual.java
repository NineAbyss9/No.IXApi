
package com.bilibili.player_ix.noixmod_api.compat.jei;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.api.craft.RitualRecipe;
import com.bilibili.player_ix.noixmod_api.register.ApiRecipes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ApiRitual
implements IRecipeCategory<RitualRecipe>
{
    @Nullable
    private final String ritualType;
    private final IDrawable background;
    private final IDrawable arrow;
    private final Component localizedName;
    private final ItemStack darkAltar = new ItemStack(NoixmodAPIBlocks.ALTAR.get());
    //private final ItemStack pedestals = new ItemStack(ModItems.PEDESTAL_DUMMY.get());
    private final int iconWidth = 16;
    private final int ritualCenterX;
    private final int ritualCenterY;
    private int recipeOutputOffsetX = 50;

    public ApiRitual(IGuiHelper guiHelper, @Nullable String ritualType) {
        this.ritualType = ritualType;
        this.background = guiHelper.createBlankDrawable(176, 140);
        this.ritualCenterX = this.background.getWidth() / 2 - this.iconWidth / 2 - 24;
        this.ritualCenterY = this.background.getHeight() / 2 - this.iconWidth / 2 + 10;
        this.localizedName = this.ritualType == null || Objects.equals(this.ritualType, "") ?
                Component.translatable("noixmodapi.jei.ritual") :
                Component.translatable( "noixmodapi.jei.craftType." + ritualType).append(" ")
                        .append(Component.translatable("noixmodapi.jei.ritualType"));
        this.darkAltar.getOrCreateTag().putBoolean("RenderFull", true);
        //this.pedestals.getOrCreateTag().putBoolean("RenderFull", true);
        this.arrow = guiHelper.createDrawable(
                NoixmodAPI.location("textures/gui/jei/arrow.png"), 0, 0, 64, 46);
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
        return null;
    }

    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, RitualRecipe ritualRecipe, IFocusGroup iFocusGroup)
    {

    }

    public void draw(RitualRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
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
