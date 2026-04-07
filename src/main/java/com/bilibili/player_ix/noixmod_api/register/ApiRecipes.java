
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.api.craft.RitualRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ApiRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES,
            "noixmodapi");
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS,
            "noixmodapi");

    public static final RegistryObject<RecipeType<RitualRecipe>> RITUAL_RECIPE = RECIPES.register("ritual", () ->
            new RecipeType<RitualRecipe>() {
                public String toString() {
                    return "noixmodapi:ritual";
                }
            });
    public static final RegistryObject<RecipeSerializer<RitualRecipe>> RITUAL_SER = RECIPE_SERS
            .register("ritual", RitualRecipe.Serializer::new);

    public static void register(IEventBus bus)
    {
        RECIPES.register(bus);
        RECIPE_SERS.register(bus);
    }
}
