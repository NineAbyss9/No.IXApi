
package com.bilibili.player_ix.noixmod_api.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class Tetanus extends MobEffect {
    public Tetanus() {
        super(MobEffectCategory.HARMFUL, -7864320);
    }

    public void applyEffectTick(LivingEntity p_19467_, int pAmplifier)
    {
        if (p_19467_.hasEffect(MobEffects.REGENERATION)) {
            p_19467_.removeEffect(MobEffects.REGENERATION);
        }
        p_19467_.hurt(p_19467_.damageSources().magic(), pAmplifier + 1);
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
