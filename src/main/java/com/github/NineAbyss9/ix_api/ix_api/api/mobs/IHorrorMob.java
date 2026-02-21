
package com.github.NineAbyss9.ix_api.ix_api.api.mobs;

import net.minecraft.world.entity.monster.Enemy;

public interface IHorrorMob extends Enemy {
    default boolean shouldSpawn() {
        return false;
    }
}
