
package com.bilibili.player_ix.noixmod_api.entities.servant.sculk.warden;

import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SonicBoom<E extends WardenServant> extends Behavior<E> {
    public static final int COOLDOWN = 40;
    private static final int TICKS_BEFORE_PLAYING_SOUND = Mth.ceil(34.0D);
    private static final int DURATION = Mth.ceil(60.0F);

    public SonicBoom() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryStatus.VALUE_ABSENT, MemoryModuleType
                        .SONIC_BOOM_SOUND_COOLDOWN, MemoryStatus.REGISTERED, MemoryModuleType
                        .SONIC_BOOM_SOUND_DELAY, MemoryStatus.REGISTERED), DURATION);
    }

    protected boolean checkExtraStartConditions(ServerLevel pLevel, E pOwner) {
        return pOwner.closerThan(pOwner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow(),
                15.0D, 20.0D);
    }

    protected boolean canStillUse(ServerLevel pLevel, E pEntity, long pGameTime) {
        return true;
    }

    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        pEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true,
                DURATION);
        pEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.SONIC_BOOM_SOUND_DELAY, Unit.INSTANCE,
                TICKS_BEFORE_PLAYING_SOUND);
        pLevel.broadcastEntityEvent(pEntity, (byte)62);
        pEntity.playSound(SoundEvents.WARDEN_SONIC_CHARGE, 3.0F, 1.0F);
    }

    protected void tick(ServerLevel pLevel, E pOwner, long pGameTime) {
        pOwner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((p_289393_) ->
                pOwner.getLookControl().setLookAt(p_289393_.position()));
        if (!pOwner.getBrain().hasMemoryValue(MemoryModuleType.SONIC_BOOM_SOUND_DELAY) &&
                !pOwner.getBrain().hasMemoryValue(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN)) {
            pOwner.getBrain().setMemoryWithExpiry(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN,
                    Unit.INSTANCE, DURATION - TICKS_BEFORE_PLAYING_SOUND);
            pOwner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(pOwner::canTargetEntity)
                    .filter((p_217707_) -> pOwner.closerThan(p_217707_, 15.0D,
                            20.0D)).ifPresent((living) -> {
                Vec3 vec3 = pOwner.position().add(0.0D, 1.6F, 0.0D);
                Vec3 vec31 = living.getEyePosition().subtract(vec3);
                Vec3 vec32 = vec31.normalize();
                for (int i = 1; i < Mth.floor(vec31.length()) + 7; ++i) {
                    Vec3 vec33 = vec3.add(vec32.scale(i));
                    pLevel.sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0D,
                            0.0D, 0.0D, 0.0D);
                }
                pOwner.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
                living.hurt(pLevel.damageSources().sonicBoom(pOwner), pOwner.getSonicBoomDamage());
                double d1 = 0.5D * (1.0D - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                double d0 = 2.5D * (1.0D - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                living.push(vec32.x() * d0, vec32.y() * d1, vec32.z() * d0);
                if (pOwner.isPowerful()) {
                    pOwner.heal(4F);
                    List<LivingEntity> targets = pLevel.getEntitiesOfClass(LivingEntity.class, living.getBoundingBox()
                            .inflate(4.5), entity -> MobUtils.canHurt(entity, pOwner) &&
                            entity != living);
                    if (!targets.isEmpty())
                        targets.forEach(target -> {
                            target.hurt(pLevel.damageSources().sonicBoom(pOwner), pOwner.getSonicBoomDamage());
                            double d2 = 0.5D * (1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                            double d3 = 2.5D * (1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                            target.push(vec32.x() * d3, vec32.y() * d2, vec32.z() * d3);
                        });
                    pLevel.sendParticles(ParticleTypes.SCULK_SOUL, living.getX(), living.getY() + 1, living.getZ(),
                            25, 0.75, 0.75, 0.75, 0.0);
                } else
                    pOwner.heal(2F);
            });
        }
    }

    protected void stop(ServerLevel pLevel, E pEntity, long pGameTime) {
        setCooldown(pEntity, COOLDOWN);
    }

    public static void setCooldown(LivingEntity pEntity, int pCooldown) {
        pEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.SONIC_BOOM_COOLDOWN, Unit.INSTANCE,
                pCooldown);
    }
}
