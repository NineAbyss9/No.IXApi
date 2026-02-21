
package com.bilibili.player_ix.noixmod_api.item.ritual;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

//Smite 13. 斑驳锈迹上，暗浮的银光声明着专戮之责
public class Cross extends RitualSupplies {
    public Cross() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public boolean hurtEnemy(ItemStack p_41395_, LivingEntity p_41396_, LivingEntity p_41397_) {
        return super.hurtEnemy(p_41395_, p_41396_, p_41397_);
    }
}
