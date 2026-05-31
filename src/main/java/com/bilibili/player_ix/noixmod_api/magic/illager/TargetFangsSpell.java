
package com.bilibili.player_ix.noixmod_api.magic.illager;

import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.EvokerFangs;

public class TargetFangsSpell extends IllagerSpell {
    public float spellPower() {
        return 10.0F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i = 0; i < 2;i++) {
            for (int j = 0; j < 5; j++) {
                EvokerFangs fangs = EntityType.EVOKER_FANGS.create(pLevel);
                if (fangs == null) {
                    continue;
                }
                fangs.setOwner(pCaster);
                if (pCaster instanceof Mob mob) {
                    if (mob.getTarget() == null) return;
                    BlockPos pos = mob.getTarget().blockPosition().offset(i == 1? 0 : j, 0, i == 1 ? j : 0);
                    fangs.moveTo(pos, 0, 0);
                    pLevel.addFreshEntity(fangs);
                } else {
                    var target = MobUtils.getSingleTarget(pLevel, pCaster, 20.0D, 20.0D,
                            entity -> entity instanceof LivingEntity livingEntity && MobUtils.canHurt(livingEntity, pCaster));
                    if (target == null) {
                        return;
                    }
                    fangs.moveTo(target.position().add(i == 1? 0 : j, 0, i == 1 ? j : 0));
                    pLevel.addFreshEntity(fangs);
                }
            }
        }
    }
}
