
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;

public interface IProjectile {
    void onHit(HitResult result);

    void onHitEntity(EntityHitResult pResult);

    void onHitBlock(BlockHitResult pResult);

    default boolean canHitEntity(@Nonnull Entity pEntity) {
        return true;
    }
}
