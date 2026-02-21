
package com.bilibili.player_ix.noixmod_api.item.util;

import com.bilibili.player_ix.noixmod_api.api.item.ApiTier;
import com.bilibili.player_ix.noixmod_api.item.weapon.ApiAxe;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.util.TimeSelector;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class InfernalIronAxe
extends ApiAxe {
    public InfernalIronAxe() {
        super(ApiTier.NETHERITE, 5, -3.0f, new Properties().fireResistant()
                .stacksTo(1));
    }

    public boolean hurtEnemy(ItemStack p_40994_, LivingEntity p_40995_, LivingEntity p_40996_) {
        p_40995_.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.STUN.get(),
                TimeSelector.SEC_Q, 0));
        return super.hurtEnemy(p_40994_, p_40995_, p_40996_);
    }
}
