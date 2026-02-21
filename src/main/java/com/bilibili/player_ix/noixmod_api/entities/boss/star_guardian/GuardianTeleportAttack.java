
package com.bilibili.player_ix.noixmod_api.entities.boss.star_guardian;

import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class GuardianTeleportAttack {
    GuardianTeleportAttack() {
    }

    static void trigger(StarGuardian pGuardian, LivingEntity target) {
        if (pGuardian.attackTickEquals(15)) {
            sound(pGuardian);
            sendParticles(pGuardian);
            teleport(pGuardian, target);
        }
        if (pGuardian.attackTickEquals(20)) {
            doAttack(pGuardian);
        }
        if (pGuardian.attackTickEquals(25)) {
            sound(pGuardian);
            sendParticles(pGuardian);
            teleport(pGuardian, target);
        }
        if (pGuardian.attackTickEquals(30)) {
            doAttack(pGuardian);
        }
        if (pGuardian.attackTickEquals(35)) {
            sound(pGuardian);
            sendParticles(pGuardian);
            teleport(pGuardian, target);
        }
        if (pGuardian.attackTickEquals(40)) {
            doAttack(pGuardian);
        }
        if (pGuardian.attackTickEquals(45)) {
            sound(pGuardian);
            sendParticles(pGuardian);
            teleport(pGuardian, target);
        }
        if (pGuardian.attackTickEquals(50)) {
            pGuardian.finalVec = target.position();
            doAttack(pGuardian);
        }
        if (pGuardian.attackTickEquals(55)) {
            sound(pGuardian);
            teleport(pGuardian, target);
        }
        if (pGuardian.attackTickEquals(60)) {
            pGuardian.groundSound();
            cloud(pGuardian);
            List<LivingEntity> entities = pGuardian.level().getEntitiesOfClass(LivingEntity.class,
                    pGuardian.getBoundingBox().inflate(4, 0.2, 4), entity ->
                            MobUtils.canHurt(entity, pGuardian));
            if (!entities.isEmpty()) {
                for (LivingEntity entity : entities) {
                    entity.setHealth(entity.getHealth() - entity.getMaxHealth() / 7);
                    pGuardian.doHurtTarget(entity);
                }
            }
        }
    }

    private static int range() {
        return 3;
    }

    private static void sound(StarGuardian pGuardian) {
        pGuardian.playSound(SoundEvents.ENDERMAN_TELEPORT, 1F, 0.1F);
    }

    private static void doAttack(StarGuardian pGuardian) {
        MobUtils.areaAttack(pGuardian, 3.5F, 3.5F, 90, pGuardian.getAttackDamage(9.0F),
                0.1F, 0, pGuardian.damageSources().mobAttack(pGuardian),
                false, pEntity -> {
                    pGuardian.setPowerPlus();
                    pGuardian.doHeal();
                }, true);
    }

    private static void sendParticles(StarGuardian pGuardian) {
        if (pGuardian.isServerSide()) {
            ServerLevel serverLevel = pGuardian.serverLevel();
            ParticleUtil.sendParticles(serverLevel, NoixmodAPIParticleTypes.COLORED_ASH.get(),
                    pGuardian.position(), 20, 0.5, 1, 0.5, 0);
        }
    }

    private static void cloud(StarGuardian pGuardian) {
        if (pGuardian.isServerSide()) {
            ServerLevel serverLevel = pGuardian.serverLevel();
            ParticleUtil.sendParticles(serverLevel, NoixmodAPIParticleTypes.CLOUD.get(),
                    pGuardian.position(), 20, 1, 0.2, 1, 0);
        }
    }

    private static void teleport(StarGuardian pG, LivingEntity pTarget) {
        MobUtils.forceLook(pG, pTarget);
        int range = range();
        RandomSource source = pG.level().getRandom();
        if (pG.level().noCollision(pG.getBoundingBox().move(pTarget.position()))) {
            pG.teleportTo(pTarget.getX() + source.nextIntBetweenInclusive(-range, range),
                    pG.getY(), pTarget.getZ() + source.nextIntBetweenInclusive(-range, range));
        }
    }
}
