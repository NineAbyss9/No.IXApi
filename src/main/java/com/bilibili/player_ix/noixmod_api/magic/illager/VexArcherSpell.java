
package com.bilibili.player_ix.noixmod_api.magic.illager;

import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexArcher;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class VexArcherSpell extends IllagerSpell {
    public float spellPower() {
        return 100f;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        OwnerSummon ownerSummon = new OwnerSummon(pCaster);
        for (int i = 0;i < ThreadLocalRandom.current().nextInt(3) + 2;i++) {
            VexArcher archer = NoixmodAPIEntities.VEX_ARCHER.get().create(pLevel);
            if (archer == null) {
                break;
            }
            ownerSummon.integerSummon(archer, 3);
            archer.spawnAnim();
        }
    }
}
