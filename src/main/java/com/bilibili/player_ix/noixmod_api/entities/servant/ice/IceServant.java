
package com.bilibili.player_ix.noixmod_api.entities.servant.ice;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class IceServant
extends OwnableMob {
    public IceServant(EntityType<? extends IceServant> entityType, Level level) {
        super(entityType, level);
    }

    public void freeze(LivingEntity pTarget) {
        pTarget.setTicksFrozen(40);
    }
}
