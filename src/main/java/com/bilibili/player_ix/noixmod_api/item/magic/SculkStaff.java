
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.github.NineAbyss9.ix_api.util.Colors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class SculkStaff
extends Staff
{
    public SculkStaff(Properties pProperties)
    {
        super(pProperties);
    }

    public float[] getSpellColor()
    {
        return Colors.BLUE;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pUsedTime)
    {
        (pCaster.isCrouching() ? Spells.SCULK_ZOMBIE.get() : Spells.SONIC_BOOM.get())
                .castSpell(pLevel, pCaster);
    }
}
