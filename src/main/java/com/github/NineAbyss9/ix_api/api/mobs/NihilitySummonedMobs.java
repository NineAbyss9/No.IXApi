
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class NihilitySummonedMobs
extends NihilityMobs {
    protected NihilitySummonedMobs(EntityType<? extends NihilitySummonedMobs> entityType, Level level) {
        super(entityType, level);
    }
}
