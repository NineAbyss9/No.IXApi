
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.entities.servant.SkeletonServant;
import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class SkeletonSpell
extends Spell
{
    public Type getSpellType()
    {
        return Type.OVERWORLD;
    }

    public float spellPower()
    {
        return 15.0F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster)
    {
        pCaster.playSound(SoundEvents.EVOKER_CAST_SPELL);
        for (int i = 0;i < ThreadLocalRandom.current().nextInt(3) + 2;++i) {
            OwnerSummon ownerSummon = new OwnerSummon(pCaster);
            SkeletonServant servant = new SkeletonServant(NoixmodAPIEntities.SKELETON_SERVANT.get(), pLevel);
            ownerSummon.integerSummon(servant, 2);
            servant.spawnAnim();
        }
    }
}
