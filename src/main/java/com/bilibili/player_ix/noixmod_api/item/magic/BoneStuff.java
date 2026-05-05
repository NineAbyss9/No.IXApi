
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class BoneStuff
extends Staff
{
    public BoneStuff(Properties properties)
    {
        super(properties);
    }

    public BoneStuff()
    {
        this(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public void castSpell(ServerLevel pLevel, LivingEntity living)
    {
        ISpell spell = pLevel.dimension() == Level.NETHER ?
                Spells.WITHER_SKELETON.get() : Spells.SKELETON.get();
        spell.castSpell(pLevel, living);
    }
}
