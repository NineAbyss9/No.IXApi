
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.bilibili.player_ix.noixmod_api.entities.projectile.summon.SummonApostle;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

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

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide) {
            SummonApostle apostle = new SummonApostle(NoixmodAPIEntities.SUMMON_APOSTLE.get(), pLevel);
            apostle.setOwner(pPlayer);
            apostle.setBoss(false);
            apostle.moveTo(pPlayer.position());
            if (pLevel.addFreshEntity(apostle))
                ItemUtil.shrink(stack, pPlayer);
        }
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }
}
