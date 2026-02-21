
package com.github.NineAbyss9.ix_api.api;

import net.minecraftforge.common.IExtensibleEnum;

public enum ApiPose implements IExtensibleEnum
{
    ATTACKING,
    BOW_AND_ARROW,
    CROSSBOW_CHARGE,
    CROSSBOW_HOLD,
    CROSSED,
    NATURAL,
    SPELL_CASTING,
    ZOMBIE_ATTACKING;

    ApiPose() {
    }

    @SuppressWarnings("all")
    public static ApiPose create(String name) {
        throw new IllegalStateException("Enum not extended");
    }
}
