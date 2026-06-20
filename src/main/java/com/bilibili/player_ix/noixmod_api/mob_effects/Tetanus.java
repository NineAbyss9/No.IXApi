
package com.bilibili.player_ix.noixmod_api.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class Tetanus extends MobEffect {
    public Tetanus() {
        super(MobEffectCategory.HARMFUL, -7864320);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
    {
        if (pLivingEntity.hasEffect(MobEffects.REGENERATION)) {
            pLivingEntity.removeEffect(MobEffects.REGENERATION);
        }
        pLivingEntity.hurt(pLivingEntity.damageSources().magic(), pAmplifier + 1);
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 2 == 0;
    }
}
