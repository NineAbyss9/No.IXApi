
package com.bilibili.player_ix.noixmod_api.entities.monster.undead;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class WitherBoneSpellcaster
extends BoneSpellcaster
{
    public WitherBoneSpellcaster(EntityType<? extends WitherBoneSpellcaster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected OwnableMob getSummoned()
    {
        return NoixmodAPIEntities.WITHER_SKELETON_SERVANT.get().create(this.level());
    }

    public boolean fireImmune()
    {
        return true;
    }
}
