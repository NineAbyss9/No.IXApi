
package com.bilibili.player_ix.noixmod_api.item.food;

import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class GoldenRabbitFoot extends Item {
    public GoldenRabbitFoot() {
        super(new Properties().food(new FoodProperties.Builder().
                nutrition(5).saturationMod(7).effect(
                        () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Maths.toTick(120), 1), 1
                ).effect(
                        () -> new MobEffectInstance(MobEffects.LUCK, Maths.toTick(120), 1), 1
                ).build()).stacksTo(64).rarity(Rarity.RARE));
    }
}
