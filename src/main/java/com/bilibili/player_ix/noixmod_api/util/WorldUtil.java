
package com.bilibili.player_ix.noixmod_api.util;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.PAMAreNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;

@PAMAreNonnullByDefault
public record WorldUtil(Entity entity) {
    public boolean isRainingAtSelf() {
        return isRainingAt(this.entity());
    }

    public static boolean isRainingAt(Entity in) {
        return in.level().isRainingAt(in.blockPosition());
    }

    public double low() {
        return low(entity);
    }

    public static double low(Entity mob) {
        HitResult result = rayTrace(mob);
        if (result instanceof BlockHitResult hitResult) {
            if (hitResult.getDirection() == Direction.UP) {
                BlockState hitBlock = mob.level().getBlockState(hitResult.getBlockPos());
                if (hitBlock.getBlock() instanceof SlabBlock) {
                    return hitResult.getBlockPos().getY() + 0.5625F;
                } else {
                    return hitResult.getBlockPos().getY() + 1.0625F;
                }
            }
        }
        return mob.getY();
    }

    public static HitResult rayTrace(Entity entity) {
        Vec3 startPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        Vec3 endPos = new Vec3(entity.getX(), 0, entity.getZ());
        return entity.level().clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, entity));
    }

    public static void createSpellEntities(double var, double var1, double var2, double var3, float var4, int t, LivingEntity
            lie, Level level) {
        BlockPos pos = BlockPos.containing(var, var3, var1);
        boolean b = false;
        double d = 0.0d;
        do {
            VoxelShape $$12;
            BlockPos $$9 = pos.below();
            BlockState $$10 = lie.level().getBlockState($$9);
            if (!$$10.isFaceSturdy(lie.level(), $$9, Direction.UP)) continue;
            if (!lie.level().isEmptyBlock(pos) && !($$12 = (lie.level().getBlockState(pos)).getCollisionShape(lie.level(), pos))
                    .isEmpty()) {
                d = $$12.max(Direction.Axis.Y);
            }
            b = true;
            break;
        } while ((pos = pos.below()).getY() >= Mth.floor(var2) - 1);
        if (b) {
            level.addFreshEntity(new EvokerFangs(level, var, pos.getY() + d, var1, var4, t, lie));
        }
    }

    public static List<LivingEntity> makeList(Class<LivingEntity> classes, Entity mob, double x, double y, double z) {
        return mob.level().getEntitiesOfClass(classes, mob.getBoundingBox().inflate(x, y, z));
    }

    public static List<Entity> entityList(Class<Entity> classes, Entity entity, double x, double y, double z) {
        return entity.level().getEntitiesOfClass(classes, entity.getBoundingBox().inflate(x, y, z));
    }

    public ServerLevel getServerLevel() {
        return getServerLevel(this.entity());
    }

    public static ServerLevel getServerLevel(Entity entity) {
        return (ServerLevel)entity.level();
    }

    public static void finalizeSpawn(Mob mob, ServerLevel level, DifficultyInstance instance, MobSpawnType type,
                                     @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        ForgeEventFactory.onFinalizeSpawn(mob, level, instance, type, data, tag);
    }

    public static void nullableFinalizeSpawn(Mob mob, ServerLevel level, DifficultyInstance instance, MobSpawnType type) {
        WorldUtil.finalizeSpawn(mob, level, instance, type, null, null);
    }

    public static void nullableFinalizeSpawn(Mob mob, Level level, BlockPos pos, MobSpawnType type) {
        WorldUtil.finalizeSpawn(mob, (ServerLevel)level, level.getCurrentDifficultyAt(pos), type, null, null);
    }

    public static void sendParticles(SimpleParticleType type, Entity entity, int count, double speed) {
        WorldUtil.sendParticles(type, entity, count, 0, 0, 0, speed);
    }

    public static void sendParticles(SimpleParticleType type, Entity entity, int count, double x, double y, double z, double speed) {
        WorldUtil.sendParticles(type, entity, count, x, y, z, speed, WorldUtil.getServerLevel(entity));
    }

    public static void sendParticles(SimpleParticleType type, Entity entity, int count, double x, double y, double z, double
            speed, ServerLevel level) {
        level.sendParticles(type, entity.getX(), entity.getY(), entity.getZ(), count, x, y, z, speed);
    }

    public static void send(ServerLevel level, Holder<SoundEvent> event, SoundSource source, Entity entity, float var,
                            float var1, long time) {
        level.sendPacketToServer(new ClientboundSoundPacket(event, source, entity.getX(), entity.getY(), entity.getZ(),
                var, var1, time));
    }

    public static void send(ServerLevel level, Holder<SoundEvent> event, SoundSource source, Entity entity) {
        WorldUtil.send(level, event, source, entity, 64f, 1f, level.random.nextLong());
    }

    public static void send(ClientLevel level, Holder<SoundEvent> event, SoundSource source, Entity entity, float var,
                            float var1, long time) {
        level.sendPacketToServer(new ClientboundSoundPacket(event, source, entity.getX(), entity.getY(), entity.getZ(),
                var, var1, time));
    }

    public static void send(ClientLevel level, Holder<SoundEvent> event, SoundSource source, Entity entity) {
        WorldUtil.send(level, event, source, entity, 64f, 1f, level.random.nextLong());
    }
}
