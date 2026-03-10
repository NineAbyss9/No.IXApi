
package com.bilibili.player_ix.noixmod_api.item.grave;

import com.bilibili.player_ix.noixmod_api.item.weapon.ApiAxe;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class GraveAxe
extends ApiAxe {
    public GraveAxe() {
        super(1561, 5, 4.29F, 11, 10, Ingredient.EMPTY,
                5, -3.1f, new Properties().rarity(Rarity.UNCOMMON)
                        .stacksTo(1).fireResistant());
    }

    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        if (p_41405_.random.nextFloat() <= 0.001f) {
            p_41406_.playSound(SoundEvents.SOUL_ESCAPE);
        }
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
    }
}
