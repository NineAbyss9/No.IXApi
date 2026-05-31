
package com.bilibili.player_ix.noixmod_api.magic.nether;

import com.bilibili.player_ix.noixmod_api.entities.servant.nether.LavaZombieServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class LavaZombieSpell
extends NetherSpell {
    public LavaZombieSpell() {
        super();
    }

    public float spellPower() {
        return 20F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i = 0;i < ThreadLocalRandom.current().nextInt(3) + 2;++i) {
            LavaZombieServant servant = new LavaZombieServant(NoixmodAPIEntities.LAVA_ZOMBIE_SERVANT.get(), pLevel);
            OwnerSummon ownerSummon = new OwnerSummon(pCaster);
            ownerSummon.integerSummon(servant, 2);
            servant.spawnAnim();
        }
    }
}
