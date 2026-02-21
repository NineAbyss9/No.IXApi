
package com.bilibili.player_ix.noixmod_api.mob_effects;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class Tetanus extends MobEffect {
    public Tetanus() {
        super(MobEffectCategory.HARMFUL, -7864320);
    }

    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if (p_19467_.hasEffect(MobEffects.REGENERATION)) {
            p_19467_.removeEffect(MobEffects.REGENERATION);
        }
        MobEffectInstance instance = p_19467_.getEffect(NoixmodAPIMobEffects.TETANUS.get());
        if (instance != null) {
            p_19467_.hurt(p_19467_.damageSources().magic(), instance.getAmplifier() + 1);
        }
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
