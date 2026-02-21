
package com.github.NineAbyss9.ix_api.api.mobs.ai;

import com.github.NineAbyss9.ix_api.api.mobs.SpellCasterMob;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class CastSpell {
    public CastSpell() {}

    public static OneShot<Mob> create(int time, int cooldown, SoundEvent castSound) {
        return BehaviorBuilder.create(instance -> instance.group(instance.registered(MemoryModuleType.LOOK_TARGET),
                instance.present(MemoryModuleType.ATTACK_TARGET), instance.absent(MemoryModuleType
                        .ATTACK_COOLING_DOWN),
                instance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(instance, (p, p1, p2, p3) ->
                (p4, p5, p6) -> {
                    LivingEntity target = instance.get(p1);
                    if ((instance.get(p3).contains(target))) {
                        p.set(new EntityTracker(target, true));
                        if (p5 instanceof SpellCasterMob mob) {
                            if (mob.getCastSound() != null) {
                                p5.playSound(mob.getCastSound());
                            }
                            mob.stopSpell();
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
        ));
    }
}
