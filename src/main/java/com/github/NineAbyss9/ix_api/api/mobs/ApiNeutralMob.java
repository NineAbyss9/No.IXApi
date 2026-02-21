
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.world.entity.NeutralMob;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ApiNeutralMob extends NeutralMob {
    default int getRemainingPersistentAngerTime() {return 0;}

    default void setRemainingPersistentAngerTime(int i) {}

    @Nullable
    default UUID getPersistentAngerTarget() {return null;}

    default void setPersistentAngerTarget(@Nullable UUID uuid) {}

    default void startPersistentAngerTimer() {}
}
