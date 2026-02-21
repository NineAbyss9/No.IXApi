
package com.bilibili.player_ix.noixmod_api.magic.illager;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.EvokerFangs;

public class TargetFangsSpell extends IllagerSpell {
    @Override
    public float spellPower() {
        return 10;
    }

    @Override
    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i = 0; i < 2;i++) {
            for (int j = 0; j < 5; j++) {
                if (!(pCaster instanceof Mob)) return;
                EvokerFangs fangs = EntityType.EVOKER_FANGS.create(pLevel);
                if (fangs != null) {
                    fangs.setOwner(pCaster);
                    if (pCaster instanceof Mob mob) {
                        if (mob.getTarget() == null) return;
                        BlockPos pos = mob.getTarget().blockPosition().offset(i == 1? 0 : j, 0, i == 1 ? j : 0);
                        fangs.moveTo(pos, 0, 0);
                        pLevel.addFreshEntity(fangs);
                    }
                }
            }
        }
    }
}
