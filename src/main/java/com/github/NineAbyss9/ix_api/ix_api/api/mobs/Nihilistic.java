
package com.github.NineAbyss9.ix_api.ix_api.api.mobs;

import com.bilibili.player_ix.noixmod_api.api.entity.IX;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface Nihilistic {
    int truth = 9;

    default List<Entity> getLord() {
        return this.myself().level().getEntitiesOfClass(Entity.class, this.myself().getBoundingBox().inflate(99),
                entity -> entity instanceof IX);
    }

    private Entity myself() {
        return (Entity)this;
    }
}
