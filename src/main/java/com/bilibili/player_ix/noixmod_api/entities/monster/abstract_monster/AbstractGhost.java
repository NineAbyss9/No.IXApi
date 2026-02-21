
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class AbstractGhost
extends OwnableMob
implements ApiPoseMob {
    public AbstractGhost(EntityType<? extends AbstractGhost> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public ApiPose getPoses() {
        return ApiPose.NATURAL;
    }
}
