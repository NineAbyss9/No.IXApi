
package com.bilibili.player_ix.noixmod_api.entities.ai;

import com.bilibili.player_ix.noixmod_api.entities.boss.Player__7;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.schedule.Activity;

import javax.annotation.Nullable;

public class Player7AI {
    PiglinAi piglinAi;
    public Player7AI() {
    }

    public static void tick(Player__7 p7) {
        Brain<Player__7> brain = p7.getBrain();
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
        p7.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    public Brain<Player__7> makeBrain(Player__7 p7, Brain<Player__7> p7Brain) {
        initCoreActivity(p7Brain);
        initFightActivity(p7, p7Brain);
        return p7Brain;
    }

    private static void initCoreActivity(Brain<Player__7> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new LookAtTargetSink(45, 90), InteractWithDoor.create(),
                StopBeingAngryIfTargetDead.create()
        ));
    }

    private static void initFightActivity(Player__7 p7, Brain<Player__7> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
                StopAttackingIfTargetInvalid.create(living -> isCloseToTarget(p7, living)),
                BehaviorBuilder.triggerIf(living -> true, BackUpIfTooClose.create(1, 0.8f)),
                SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1f), MeleeAttack.create(15)
        ), MemoryModuleType.ATTACK_TARGET);
    }

    public static boolean isCloseToTarget(Player__7 p7, @Nullable LivingEntity living) {
        if (living == null) {
            return false;
        } else {
            return p7.closerThan(living, 3);
        }
    }

    public static boolean isTooClose(Player__7 p7, @Nullable LivingEntity living) {
        if (living == null) {
            return false;
        } else {
            return p7.closerThan(living, 1.5);
        }
    }
}
