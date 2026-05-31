
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.entities.servant.aquatic.DrownedServant;
import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class DrownedSpell extends Spell {
    public DrownedSpell() {
        super();
    }

    public Type getSpellType() {
        return Type.WATER;
    }

    public float spellPower() {
        return 25;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i = 0;i < ThreadLocalRandom.current().nextInt(3) + 2;++i) {
            DrownedServant servant = new DrownedServant(NoixmodAPIEntities.DROWNED_SERVANT.get(), pLevel);
            OwnerSummon ownerSummon = new OwnerSummon(pCaster);
            ownerSummon.integerSummon(servant, 2);
            servant.spawnAnim();
        }
    }
}
