
package com.bilibili.player_ix.noixmod_api.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class Wet
extends MobEffect {
    public Wet(int p_19452_) {
        super(MobEffectCategory.NEUTRAL, p_19452_);
    }

    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if (p_19467_.isOnFire()) {
            p_19467_.extinguishFire();
        }
    }

    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return -0.03D * ((double)p_19457_ + 1D);
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return p_19455_ % 10 == 0;
    }
}
