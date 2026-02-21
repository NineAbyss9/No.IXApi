
package com.github.NineAbyss9.ix_api.ix_api.api.mobs;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public interface IWormMob {
    Predicate<LivingEntity> NO_WORM_PREDICATE = livingEntity -> !(livingEntity instanceof IWormMob);
    Predicate<Entity> WORM_PREDICATE = entity -> entity instanceof IWormMob;
}
