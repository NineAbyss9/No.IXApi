
package com.bilibili.player_ix.noixmod_api.item.copper;

import com.bilibili.player_ix.noixmod_api.api.item.ApiTier;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;

public class CopperShovel extends ShovelItem {
    public CopperShovel() {
        super(ApiTier.COPPER, 1, -2.8F, new Properties());
    }

    public boolean hurtEnemy(ItemStack p_40994_, LivingEntity p_40995_, LivingEntity p_40996_) {
        EntityEventHandler.broadcastEntityEvent(p_40995_, 3);
        return super.hurtEnemy(p_40994_, p_40995_, p_40996_);
    }
}
