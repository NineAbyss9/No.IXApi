
package com.bilibili.player_ix.noixmod_api.mob_effects;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIDamageSource;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class NihilisticEffect
extends MobEffect {
    public NihilisticEffect(int p_19452_) {
        super(MobEffectCategory.HARMFUL, p_19452_);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
    {
        pLivingEntity.hurt(NoixmodAPIDamageSource.nihility(pLivingEntity.level()), 4.9f + pAmplifier);
    }

    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier)
    {
        if (!(pLivingEntity instanceof Nihilistic) &&
                (!(pLivingEntity instanceof Player player) || !player.getInventory().hasAnyMatching(stack ->
                stack.is(NoixmodAPIItems.STAR_SWORD.get())))) {
            super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        } else {
            pLivingEntity.removeEffect(this);
        }
    }

    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return -2.9 * ( p_19457_ + 1);
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 10 == 0;
    }
}
