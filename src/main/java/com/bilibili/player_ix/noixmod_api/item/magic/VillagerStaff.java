
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.github.NineAbyss9.ix_api.util.Colors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class VillagerStaff
extends Staff
{
    public VillagerStaff(Properties pProperties)
    {
        super(pProperties);
    }

    public float[] getSpellColor()
    {
        return Colors.GREEN;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pUsedTime)
    {
        (pCaster.isCrouching() ? Spells.VILLAGER_GOLEM.get() : Spells.VILLAGER_FANGS.get()).castSpell(pLevel, pCaster);
    }
}
