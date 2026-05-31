
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.github.NineAbyss9.ix_api.util.Colors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class EndStaff
extends Staff
{
    public EndStaff(Properties pProperties)
    {
        super(pProperties);
    }

    public float[] getSpellColor()
    {
        return Colors.PURPLE;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pUsedTime)
    {
        Spells.ENDERMAN.get().castSpell(pLevel, pCaster);
    }
}
