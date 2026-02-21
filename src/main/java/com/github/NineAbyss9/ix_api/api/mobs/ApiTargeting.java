
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Targeting;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ApiTargeting extends Targeting {
    void setTarget(@Nullable LivingEntity living);

    @Nullable
    UUID getTargetUuid();
}
