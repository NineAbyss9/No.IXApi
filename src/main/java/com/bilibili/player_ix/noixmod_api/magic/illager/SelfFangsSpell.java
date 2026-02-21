
package com.bilibili.player_ix.noixmod_api.magic.illager;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;

public class SelfFangsSpell extends IllagerSpell {
    @Override
    public float spellPower() {
        return 0;
    }

    @Override
    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int j = 0; j < 5; ++j) {
            for (int i = 0; i < 5; ++i) {
                EvokerFangs fangs = EntityType.EVOKER_FANGS.create(pLevel);
                if (fangs == null) continue;
                if (j < 3) {
                    fangs.moveTo(pCaster.blockPosition().offset(
                            Mth.floor(-i * Maths.trueOrFalse() * 0.5), 0,
                            Mth.floor(-i * Maths.trueOrFalse() * 0.5)), 0, 0);
                } else {
                    fangs.moveTo(pCaster.blockPosition().offset(
                            Mth.floor(i * Maths.trueOrFalse() * 0.5), 0,
                            Mth.floor(i * Maths.trueOrFalse() * 0.5)), 0, 0);
                }
                fangs.setOwner(pCaster);
                pLevel.addFreshEntity(fangs);
            }
        }
    }
}
