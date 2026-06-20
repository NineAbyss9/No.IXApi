
package com.bilibili.player_ix.noixmod_api.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class VampirePotion
extends MobEffect {
    protected final double multiplier;
    public VampirePotion() {
        super(MobEffectCategory.HARMFUL, -12189696);
        this.multiplier = -3;
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
    {
        if (pLivingEntity.getMobType() == MobType.UNDEAD) {
            pLivingEntity.removeEffect(this);
            return;
        }
        pLivingEntity.hurt(pLivingEntity.level().damageSources().wither(), 1f);
        if (pLivingEntity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 10, 1));
        } else {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 3 == 0;
    }

    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        return this.multiplier * (double)(pAmplifier + 1);
    }
}
