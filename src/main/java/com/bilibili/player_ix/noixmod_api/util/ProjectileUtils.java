
package com.bilibili.player_ix.noixmod_api.util;

import com.org.NineAbyss9.annotation.PAMAreNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

@PAMAreNonnullByDefault
public class ProjectileUtils {
    public ProjectileUtils() {
    }

    public static InteractionHand getWeaponHoldingHand(LivingEntity living, Item item) {
        return living.getMainHandItem().is(item) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}
