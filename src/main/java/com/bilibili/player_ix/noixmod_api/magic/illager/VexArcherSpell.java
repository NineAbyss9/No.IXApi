
package com.bilibili.player_ix.noixmod_api.magic.illager;

import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexArcher;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class VexArcherSpell extends IllagerSpell {
    public float spellPower() {
        return 100f;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i = 0;i < pLevel.getRandom().nextInt(3) + 2;i++) {
            VexArcher archer = NoixmodAPIEntities.VEX_ARCHER.get().create(pLevel);
            if (archer != null) {
                OwnerSummon ownerSummon = new OwnerSummon(pCaster);
                ownerSummon.integerSummon(archer, 3);
                archer.spawnAnim();
            }
        }
    }
}
