
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.entities.servant.ice.StrayServant;
import com.bilibili.player_ix.noixmod_api.entities.servant.ice.Yeti;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.github.NineAbyss9.ix_api.util.Colors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class FrostStaff
extends Staff
{
    public FrostStaff(Properties pProperties)
    {
        super(pProperties);
    }

    public FrostStaff()
    {
        this(new Properties().rarity(Rarity.RARE));
    }

    public boolean checkConditions(Level pLevel, LivingEntity pEntity)
    {
        return pEntity.isCrouching() ?
                pLevel.getEntitiesOfClass(Yeti.class, pEntity.getBoundingBox().inflate(64.0D),
                                yeti -> yeti.getOwner() == pEntity).size() < 2 :
                pLevel.getEntitiesOfClass(StrayServant.class, pEntity.getBoundingBox().inflate(64.0D),
                strayServant -> strayServant.getOwner() == pEntity).size() < 5;
    }

    public float[] getSpellColor()
    {
        return Colors.LIGHT_BLUE;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pUsedTime)
    {
        (pCaster.isCrouching() ? Spells.YETI.get() : Spells.STRAY.get()).castSpell(pLevel, pCaster);
    }
}
