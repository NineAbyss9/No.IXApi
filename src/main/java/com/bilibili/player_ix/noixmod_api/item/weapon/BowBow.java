
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.ArrowArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;

public class BowBow extends BowItem {
    public BowBow() {
        super(new Properties().durability(384).rarity(Rarity.UNCOMMON));
    }

    public AbstractArrow customArrow(AbstractArrow arrow) {
        if (!(arrow.getOwner() instanceof LivingEntity living)) return arrow;
        return new ArrowArrowEntity(arrow.level(), living);
    }
}
