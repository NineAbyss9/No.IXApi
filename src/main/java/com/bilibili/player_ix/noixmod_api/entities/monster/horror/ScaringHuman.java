
package com.bilibili.player_ix.noixmod_api.entities.monster.horror;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ScaringHuman
extends AbstractHorrorMob
implements ApiPoseMob {
    public ScaringHuman(EntityType<? extends ScaringHuman> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public ApiPose getPoses() {
        if (isAggressive())
            return ApiPose.ZOMBIE_ATTACKING;
        return ApiPose.NATURAL;
    }
}
