
package com.bilibili.player_ix.noixmod_api.item.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class NihilisticFood extends Item {
    public NihilisticFood() {
        super(new Properties().food(new FoodProperties.Builder().saturationMod(3F).nutrition(9)
                .alwaysEat().effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 900, 1)
                        , 1).build()).stacksTo(64).fireResistant().rarity(Rarity.EPIC));
    }
}
