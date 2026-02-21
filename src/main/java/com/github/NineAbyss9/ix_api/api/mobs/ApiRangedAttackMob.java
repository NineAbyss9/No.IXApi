
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;

public interface ApiRangedAttackMob
extends RangedAttackMob{
    AbstractArrow getArrow(ItemStack stack, float pDistanceFactor);
}
