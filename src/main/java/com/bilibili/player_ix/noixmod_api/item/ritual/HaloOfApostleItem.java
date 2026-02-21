
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class HaloOfApostleItem
extends RitualSupplies {
    public HaloOfApostleItem() {
        super(new Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1));
    }

    public boolean hurtEnemy(ItemStack p_41395_, LivingEntity enemy, LivingEntity p_41397_) {
        if (!(enemy instanceof Nihilistic) && enemy.isAlive()) {
            enemy.setHealth(enemy.getHealth() - 9);
        }
        return true;
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!(entity instanceof Nihilistic)) {
            if (entity instanceof LivingEntity living && living.isAlive()) {
                living.setHealth(living.getHealth() - 9);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
