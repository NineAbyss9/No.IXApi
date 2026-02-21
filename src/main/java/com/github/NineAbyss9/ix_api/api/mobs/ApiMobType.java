
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.world.entity.MobType;

@SuppressWarnings("all")
public class ApiMobType {
    public static final MobType VILLAGER;
    public static final MobType NIHILISTIC;
    public static final MobType NIHILISTIC_UNDEAD;
    public ApiMobType() {
    }

    public static boolean isNihilistic(MobType mobType) {
        return mobType.equals(NIHILISTIC) || mobType.equals(NIHILISTIC_UNDEAD);
    }

    public static boolean isUndead(MobType mobType) {
        return mobType.equals(NIHILISTIC_UNDEAD) || mobType.equals(MobType.UNDEAD);
    }

    static {
        VILLAGER = new MobType();
        NIHILISTIC = new MobType();
        NIHILISTIC_UNDEAD = new MobType();
    }
}
