
package com.bilibili.player_ix.noixmod_api.api.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public interface IIceItem {
    default void freeze(LivingEntity pEntity) {
        pEntity.setTicksFrozen(20);
        pEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN));
    }
}
