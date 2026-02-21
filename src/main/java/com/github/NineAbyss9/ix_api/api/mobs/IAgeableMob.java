
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

public interface IAgeableMob {
    void spawnChildFromBreeding(ServerLevel p_27564_, IAgeableMob p_27565_);

    @Nullable
    IAgeableMob getBreedMob();

    boolean isInLove();

    default void resetLove() {
        this.setInLoveTime(0);
    }

    default int getInLoveTime() {
        return 0;
    }

    default void setInLoveTime(int i) {}
}
