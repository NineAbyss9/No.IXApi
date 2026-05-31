
package com.bilibili.player_ix.noixmod_api.magic.ice;

import com.bilibili.player_ix.noixmod_api.entities.servant.ice.StrayServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class StraySpell
extends IceSpell
{
    public float spellPower()
    {
        return 10.0F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster)
    {
        pCaster.playSound(SoundEvents.EVOKER_CAST_SPELL);
        for (int i = 0;i < ThreadLocalRandom.current().nextInt(2) + 2;++i) {
            OwnerSummon ownerSummon = new OwnerSummon(pCaster);
            StrayServant servant = NoixmodAPIEntities.STRAY_SERVANT.get().create(pLevel);
            if (servant == null) return;
            ownerSummon.integerSummon(servant, 2);
            servant.spawnAnim();
        }
    }
}
