
package com.bilibili.player_ix.noixmod_api.util;

import com.github.NineAbyss9.ix_api.api.annotation.ServerOnly;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class ApiParticleUtil
{
    @ServerOnly
    public static void betterExplode(ServerLevel level, Vec3 position)
    {
        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                position.x, position.y, position.z, 1, 0.0D, 0.0D,
                0.0D, 0.0D);
        level.sendParticles(ParticleTypes.POOF, position.x, position.y, position.z,
                30, 0.0D, 0.0D, 0.0D, 0.2D);
    }

    public static void spawnSmashAttackParticles(final LevelAccessor level, final BlockPos pos, final int count) {
        Vec3 center = Vec3.atCenterOf(pos).add(0.0D, 0.65D, 0.0D);
        var particle = new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(pos));
        var random = level.getRandom();
        for (int i = 0;(float)i < (float)count / 3.0F;++i) {
            double x = center.x + random.nextGaussian() / 2.0D;
            double y = center.y;
            double z = center.z + random.nextGaussian() / 2.0D;
            double xd = random.nextGaussian() * 0.2D;
            double yd = random.nextGaussian() * 0.2D;
            double zd = random.nextGaussian() * 0.2D;
            level.addParticle(particle, x, y, z, xd, yd, zd);
        }
        for (int i = 0;(float)i < (float)count / 1.5F; ++i) {
            double x = center.x + 3.5D * Math.cos((double)i) + random.nextGaussian() / 2.0D;
            double y = center.y;
            double z = center.z + 3.5D * Math.sin((double)i) + random.nextGaussian() / 2.0D;
            double xd = random.nextGaussian() * 0.05D;
            double yd = random.nextGaussian() * 0.05D;
            double zd = random.nextGaussian() * 0.05D;
            level.addParticle(particle, x, y, z, xd, yd, zd);
        }
    }
}
