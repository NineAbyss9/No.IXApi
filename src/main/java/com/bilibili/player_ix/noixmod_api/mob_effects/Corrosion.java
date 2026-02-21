
package com.bilibili.player_ix.noixmod_api.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class Corrosion
extends MobEffect {
    public Corrosion(int p_19452_) {
        super(MobEffectCategory.HARMFUL, p_19452_);
    }

    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        p_19467_.hurt(p_19467_.damageSources().wither(), 0.5f);
    }

    public double getAttributeModifierValue(int level, AttributeModifier p_19458_) {
        return -1 * (level + 1);
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
