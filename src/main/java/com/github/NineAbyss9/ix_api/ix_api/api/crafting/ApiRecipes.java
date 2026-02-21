
package com.github.NineAbyss9.ix_api.ix_api.api.crafting;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ApiRecipes {
    public static final DeferredRegister<RecipeType<?>> REGISTER =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, NoixmodAPI.MOD_ID);
    public static final RecipeType<RitualRecipe> RITUAL = RecipeType.register("noixmodapi:ritual");
}
