
package com.bilibili.player_ix.noixmod_api.item.copper;

import com.bilibili.player_ix.noixmod_api.api.item.ApiTier;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class CopperSword extends SwordItem {
    public CopperSword() {
        super(ApiTier.COPPER, 3, -2.4f, new Properties());
    }

    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity p_43280_) {
        EntityEventHandler.broadcastEntityEvent(p_43279_, 3);
        return super.hurtEnemy(p_43278_, p_43279_, p_43280_);
    }
}
