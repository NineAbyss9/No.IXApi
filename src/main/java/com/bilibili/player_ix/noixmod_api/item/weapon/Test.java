
package com.bilibili.player_ix.noixmod_api.item.weapon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class Test extends SwordItem {
    public Test(Properties p_43272_) {
        super(Tiers.IRON, 4, -2.4F, p_43272_);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity living) {
            double d;
            AttributeInstance instance = living.getAttribute(Attributes.MOVEMENT_SPEED);
            if (instance != null) {
                d = instance.getValue();
            } else {
                d = 0.0;
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
