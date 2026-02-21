
package com.bilibili.player_ix.noixmod_api.entities.servant.sculk.warden;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class Sniffing<E extends WardenServant> extends Behavior<E> {
    public Sniffing(int pDuration) {
        super(ImmutableMap.of(MemoryModuleType.IS_SNIFFING, MemoryStatus.VALUE_PRESENT, MemoryModuleType
                .ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus
                .VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType
                .NEAREST_ATTACKABLE, MemoryStatus.REGISTERED, MemoryModuleType.DISTURBANCE_LOCATION,
                MemoryStatus.REGISTERED, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.REGISTERED),
                pDuration);
    }

    protected boolean canStillUse(ServerLevel pLevel, E pEntity, long pGameTime) {
        return true;
    }

    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        pEntity.playSound(SoundEvents.WARDEN_SNIFF, 5.0F, 1.0F);
    }

    protected void stop(ServerLevel pLevel, E pEntity, long pGameTime) {
        if (pEntity.hasPose(Pose.SNIFFING)) {
            pEntity.setPose(Pose.STANDING);
        }
        pEntity.getBrain().eraseMemory(MemoryModuleType.IS_SNIFFING);
        pEntity.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).filter(pEntity::canTargetEntity)
                .ifPresent((p_289391_) -> {
            if (pEntity.closerThan(p_289391_, 6.0D, 20.0D)) {
                pEntity.increaseAngerAt(p_289391_);
            }
            if (!pEntity.getBrain().hasMemoryValue(MemoryModuleType.DISTURBANCE_LOCATION)) {
                WardenServantAi.setDisturbanceLocation(pEntity, p_289391_.blockPosition());
            }
        });
    }
}
