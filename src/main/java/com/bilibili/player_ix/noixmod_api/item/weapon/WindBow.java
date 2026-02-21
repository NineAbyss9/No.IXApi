
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.WindArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Rarity;

public class WindBow
extends BowItem {
    public WindBow() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public AbstractArrow customArrow(AbstractArrow arrow) {
        return new WindArrow(arrow.level(), arrow.getOwner() instanceof LivingEntity living ? living : null);
    }
}
