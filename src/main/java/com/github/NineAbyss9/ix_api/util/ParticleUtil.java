
package com.github.NineAbyss9.ix_api.util;

import com.github.NineAbyss9.ix_api.api.annotation.ClientOnly;
import com.org.NineAbyss9.annotation.PAMAreNonnullByDefault;
import com.github.NineAbyss9.ix_api.api.annotation.ServerOnly;
import com.bilibili.player_ix.noixmod_api.client.particle.CircleParticleOption;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

@PAMAreNonnullByDefault
public class ParticleUtil {
    private static final RandomSource random = RandomSource.create();
    public ParticleUtil() {
    }

    @ServerOnly
    public static void addParticleAroundSelf(Entity entity, ParticleOptions options, int count) {
        if (!entity.level().isClientSide()) {
            ServerLevel level = WorldUtil.getServerLevel(entity);
            double x = level.getRandom().nextGaussian() * 0.05;
            level.sendParticles(options, entity.getX(), entity.getRandomY(), entity.getZ(), count,
                    1, 1, 1, x);
        }
    }

    @ClientOnly
    public static void addFlatParticle(ParticleOptions options, Entity entity, double xScale, double zScale) {
        if (entity.level().isClientSide()) {
            double rx = entity.getRandomX(xScale);
            double y = entity.getRandomY();
            double rz = entity.getRandomZ(zScale);
            entity.level().addParticle(options, rx, y, rz, 0, 0, 0);
        }
    }

    @ClientOnly
    public static void addRedStoneParticle(Entity entity, double x, double y, double z, double xS, double yS, double zS) {
        if (entity.level().isClientSide) {
            entity.level().addParticle(DustParticleOptions.REDSTONE, x, y, z, xS, yS, zS);
        }
    }

    @ServerOnly
    public static void darkCircle(Entity entity) {
        darkCircle(entity, 0.05F);
    }

    @ServerOnly
    public static void darkCircle(Entity entity, float speed) {
        darkCircle(entity, 10, speed);
    }

    @ServerOnly
    public static void darkCircle(Entity entity, int size, float speed) {
        circle(entity, 0, 0, 0, size, speed);
    }

    @ServerOnly
    public static void circle(Entity entity, float r, float g, float b, int size, float speed) {
        sendParticles((ServerLevel)entity.level(), new CircleParticleOption(r, g, b, size, speed),
                entity.position().add(0, 0.15, 0), 1, 0, 0, 0, 0);
    }

    @ServerOnly
    public static void sendParticles(ServerLevel level, ParticleOptions particle, Vec3 position, int count, double dx,
                                     double dy, double dz, double speed) {
        level.sendParticles(particle, position.x(), position.y(), position.z(), count, dx, dy, dz, speed);
    }

    @ServerOnly
    public static void explode(ServerLevel level, Vec3 position) {
        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, position.x(), position.y(), position.z(), 1,
                0, 0, 0, 0);
    }

    @ClientOnly
    public static void addParticle(Level level, ParticleOptions options, Vec3 pos, double dx, double dy, double dz) {
        if (level.isClientSide) {
            level.addParticle(options, pos.x(), pos.y(), pos.z(), dx, dy, dz);
        }
    }

    @ClientOnly
    public static void addParticle(Level level, ParticleOptions options, Vec3 position, double[] doubles) {
        if (level.isClientSide) {
            level.addParticle(options, position.x, position.y, position.z, doubles[0], doubles[1], doubles[2]);
        }
    }

    @ClientOnly
    public static void addParticle(Level level, ParticleOptions options, Vec3 position, float[] floats) {
        if (level.isClientSide) {
            addParticle(level, options, position, new double[]{
                    floats[0], floats[1], floats[2]
            });
        }
    }

    @ServerOnly
    public static void spawnAnim(Entity entity, ParticleOptions options) {
        AABB aabb = entity.getBoundingBox();
        sendParticles((ServerLevel)entity.level(), options, entity.position(), 20,
                aabb.getXsize() - 0.2, aabb.getYsize(), aabb.getZsize() - 0.2, random.nextGaussian() * 0.02);
    }

    @ServerOnly
    public static void spawnAnim(Entity entity, Supplier<ParticleOptions> supplier) {
        spawnAnim(entity, getFromSupplier(supplier));
    }

    @ClientOnly
    public static void spawnAnim(ParticleOptions options, Level level, Entity pos) {
        for (int i = 0; i<20;i++) {
            level.addParticle(options, pos.getRandomX(1), pos.getRandomY(), pos.getRandomZ(1),
                    0, 0, 0);
        }
    }

    @ClientOnly
    public static void spawnAnim(Supplier<ParticleOptions> supplier, Level level, Entity pos) {
        spawnAnim(getFromSupplier(supplier), level, pos);
    }

    @ServerOnly
    public static void spawnAnim(Entity entity) {
        spawnAnim(entity, ParticleTypes.POOF);
    }

    public static ParticleOptions getItemParticleOption(ItemStack stackIn) {
        return new ItemParticleOption(ParticleTypes.ITEM, stackIn);
    }

    private static ParticleOptions getFromSupplier(Supplier<ParticleOptions> supplier) {
        return supplier.get();
    }
}
