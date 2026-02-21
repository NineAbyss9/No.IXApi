
package com.bilibili.player_ix.noixmod_api.magic.nihilistic;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIAttributes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class NihilisticRoarSpell extends NihilisticSpell {
    public NihilisticRoarSpell() {
        super();
    }

    public float spellPower() {
        return 50;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        MobUtils.rangeHurt(6, 0.2, 6, pCaster, pCaster.damageSources().starve(), 9);
        List<LivingEntity> list = pLevel.getEntitiesOfClass(LivingEntity.class, pCaster.getBoundingBox()
                        .inflate(6, 0.3, 6), living -> MobUtils.canHurt(living, pCaster));
        if (!list.isEmpty()) {
            for (LivingEntity living : list) {
                double d = NoixmodAPIAttributes.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE, living);
                double d0 = living.getX() - pCaster.getX();
                double d1 = living.getZ() - pCaster.getZ();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
                living.push(d0 / d2 * 4.0, 1.3 - d, d0 / d2 * 4.0);
            }
        }
        pLevel.sendParticles(NoixmodAPIParticleTypes.PURPLE_FLAME.get(), pCaster.getX(), pCaster.getY(), pCaster.getZ(),
                30, 1.5, 2, 1.5, this.random.nextGaussian() * 0.2);
        pLevel.sendParticles(NoixmodAPIParticleTypes.DARK_SPELL.get(), pCaster.getX(), pCaster.getY(), pCaster.getZ(),
                25, 1, 2, 1, 0);
        pCaster.playSound(SoundEvents.FIRE_EXTINGUISH, 2, 0.75F);
    }
}
