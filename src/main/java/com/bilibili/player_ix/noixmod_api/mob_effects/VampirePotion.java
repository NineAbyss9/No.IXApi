
package com.bilibili.player_ix.noixmod_api.mob_effects;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiMobType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class VampirePotion
extends MobEffect {
    protected final double multiplier;
    public VampirePotion() {
        super(MobEffectCategory.HARMFUL, -12189696);
        this.multiplier = -3;
    }

    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if (!ApiMobType.isUndead(p_19467_.getMobType())) {
            p_19467_.hurt(p_19467_.level().damageSources().wither(), 1f);
            if (p_19467_ instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 10, 1));
            } else {
                p_19467_.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
            }
        }
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    public double getAttributeModifierValue(int p_19430_, AttributeModifier p_19431_) {
        return this.multiplier * (double)(p_19430_ + 1);
    }
}
