
package com.bilibili.player_ix.noixmod_api.entities.servant.sculk.warden;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class WardenEntitySensor<E extends WardenServant> extends NearestLivingEntitySensor<E> {
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(),
                List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    protected void doTick(ServerLevel pLevel, E pEntity) {
        super.doTick(pLevel, pEntity);
        getClosest(pEntity, (p_289409_) -> p_289409_.getType() == EntityType.PLAYER).or(() ->
                getClosest(pEntity, (p_289408_) ->
                        p_289408_.getType() != EntityType.PLAYER)).ifPresentOrElse((p_217841_) -> {
            pEntity.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, p_217841_);
        }, () -> {
            pEntity.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE);
        });
    }

    private static Optional<LivingEntity> getClosest(WardenServant pWarden, Predicate<LivingEntity> pPredicate) {
        return pWarden.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(
                Collection::stream).filter(pWarden::canTargetEntity).filter(pPredicate).findFirst();
    }

    protected int radiusXZ() {
        return 24;
    }

    protected int radiusY() {
        return 24;
    }
}
