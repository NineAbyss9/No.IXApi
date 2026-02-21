
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.util.Mth;

public interface IAllay {
    float getHoldingItemAnimationTick();

    float getHoldingItemAnimationTicks();

    default float getHoldingItemAnimationProgress(float p_218395_) {
        return Mth.lerp(p_218395_, this.getHoldingItemAnimationTicks(), this.getHoldingItemAnimationTick()) / 5.0F;
    }
}
