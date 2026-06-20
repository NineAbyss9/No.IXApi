
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class SonicBoomSpell
extends Spell
{
    public Type getSpellType()
    {
        return Type.OVERWORLD;
    }

    public float spellPower()
    {
        return 15.0F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster)
    {
        if (pCaster instanceof Mob mob) {
            EntityEventHandler.sonicBoom(mob, mob.getTarget(), 20.0D, ParticleTypes.SONIC_BOOM,
                    entity -> {
                        entity.hurt(pLevel.damageSources().sonicBoom(mob), 10.0F);
                    });
        } else {//Based on Polarice3's codes.
            double range = 15.0D;
            Vec3 srcVec = new Vec3(pCaster.getX(), pCaster.getEyeY(), pCaster.getZ());
            Vec3 lookVec = pCaster.getViewVector(1.0F);
            Vec3 destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
            for (int i = 1; i < Math.floor(destVec.length()) + 7; ++i) {
                Vec3 vector3d2 = srcVec.add(lookVec.scale(i));
                pLevel.sendParticles(ParticleTypes.SONIC_BOOM, vector3d2.x, vector3d2.y, vector3d2.z, 1,
                        0.0D, 0.0D, 0.0D, 0.0D);
            }
            if (MobUtils.getSingleTarget(pLevel, pCaster, range, 3.0D) instanceof LivingEntity target1){
                target1.hurt(pCaster.damageSources().sonicBoom(pCaster), 10.0F);
                double d0 = target1.getX() - pCaster.getX();
                double d1 = target1.getZ() - pCaster.getZ();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                MobUtils.push(target1, d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
            }
        }
    }
}
