
package com.bilibili.player_ix.noixmod_api.magic.illager;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;

public class EscapeSpell
extends IllagerSpell {
    public EscapeSpell() {
        super();
    }

    public float spellPower() {
        return 30;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        ParticleUtil.sendParticles(pLevel, ParticleTypes.FLASH, pCaster.position().add(0, 0.5, 0),
                1, 0, 0, 0, 0);
        RandomSource source = pLevel.getRandom();
        AreaEffectCloud cloud = new AreaEffectCloud(pLevel, pCaster.getX(), pCaster.getY(), pCaster.getZ());
        cloud.setOwner(pCaster);
        cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Maths.toTick(10), 0));
        cloud.addEffect(new MobEffectInstance(MobEffects.DARKNESS, Maths.toTick(3), 0));
        cloud.setDuration(Maths.toTick(10));
        pLevel.addFreshEntity(cloud);
        double x = pCaster.getX() + Mth.randomBetween(source, -6, 6);
        double z = pCaster.getZ() + Mth.randomBetween(source, -6, 6);
        for (int i = 0; i < 3;i++) {
            if (pCaster.randomTeleport(x, pCaster.getY(), z, true)) {
                break;
            }
        }
    }
}
