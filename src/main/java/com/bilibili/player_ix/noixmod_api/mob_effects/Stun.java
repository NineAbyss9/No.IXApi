
package com.bilibili.player_ix.noixmod_api.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class Stun extends MobEffect {
    public Stun() {
        super(MobEffectCategory.HARMFUL, 16762624);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Mob mob) {
            mob.setNoAi(true);
        }
        pLivingEntity.setJumping(false);
    }

    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return -1;
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
        if (p_19469_ instanceof Mob mob) {
            mob.setNoAi(false);
        }
        super.removeAttributeModifiers(p_19469_, p_19470_, p_19471_);
    }
}
