
package com.bilibili.player_ix.noixmod_api.mob_effects;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIDamageSource;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class NihilisticEffect
extends MobEffect {
    public NihilisticEffect(int p_19452_) {
        super(MobEffectCategory.HARMFUL, p_19452_);
    }

    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if (p_19467_ instanceof Nihilistic) {
            p_19467_.removeEffect(this);
        } else {
            if (p_19467_ instanceof Player player && player.getMainHandItem().is(NoixmodAPIItems.STAR_SWORD.get())) {
                p_19467_.removeEffect(this);
                return;
            }
            p_19467_.hurt(NoixmodAPIDamageSource.nihility(p_19467_.level()), 4.9f + p_19468_);
        }
    }

    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return -2.9 * ( p_19457_ + 1);
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
