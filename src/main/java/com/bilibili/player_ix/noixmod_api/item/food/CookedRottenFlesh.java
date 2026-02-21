
package com.bilibili.player_ix.noixmod_api.item.food;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CookedRottenFlesh extends Item {
    public CookedRottenFlesh() {
        super(new Properties().stacksTo(64).food(
                new FoodProperties.Builder().meat().alwaysEat().effect(()-> new MobEffectInstance(
                        MobEffects.DAMAGE_BOOST, Maths.toTick(20), 1
                ), 1).nutrition(4).saturationMod(1f).build()
        ));
    }

    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_,
                                TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("Power!"));
    }
}
