
package com.bilibili.player_ix.noixmod_api.compat.jei;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.api.craft.RitualRecipe;
import mezz.jei.api.recipe.RecipeType;

public class ApiJeiRecipes
{
    public static final RecipeType<RitualRecipe> RITUAL;

    public static RecipeType<RitualRecipe> getRitual(String type) {
        return getRitual(NoixmodAPI.MOD_ID, type);
    }

    public static RecipeType<RitualRecipe> getRitual(String modID, String type) {
        return RecipeType.create(modID, "ritual_" + type, RitualRecipe.class);
    }

    static {
        RITUAL = RecipeType.create(NoixmodAPI.MOD_ID, "ritual", RitualRecipe.class);
    }
}
