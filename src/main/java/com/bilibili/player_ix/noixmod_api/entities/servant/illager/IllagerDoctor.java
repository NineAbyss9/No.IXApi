
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class IllagerDoctor
extends OwnableIllager {
    public IllagerDoctor(EntityType<? extends IllagerDoctor> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    private void healOther() {

    }
}
