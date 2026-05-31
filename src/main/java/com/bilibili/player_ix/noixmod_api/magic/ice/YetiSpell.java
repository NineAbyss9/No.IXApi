
package com.bilibili.player_ix.noixmod_api.magic.ice;

import com.bilibili.player_ix.noixmod_api.entities.servant.ice.Yeti;
import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import org.NineAbyss9.math.AbyssMath;

public class YetiSpell
extends Spell
{
    public YetiSpell()
    {
        super();
    }

    public Type getSpellType()
    {
        return Type.ICE;
    }

    public float spellPower()
    {
        return 20.0F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster)
    {
        Yeti yeti = NoixmodAPIEntities.YETI.get().create(pLevel);
        if (yeti == null) return;
        yeti.moveTo(pCaster.position().add(AbyssMath.random(5.0D), 0.0D, AbyssMath.random(5.0D)));
        yeti.setOwner(pCaster);
        WorldUtil.nullableFinalizeSpawn(yeti, pLevel, pCaster.blockPosition(), MobSpawnType.EVENT);
        pLevel.addFreshEntity(yeti);
    }
}
