
package com.bilibili.player_ix.noixmod_api.item.magic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class Staff
extends Item {
    public Staff(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
        if (!pLevel.isClientSide) {
            pPlayer.getCooldowns().addCooldown(this, 40);
            this.castSpell((ServerLevel)pLevel, pPlayer);
        }
        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide);
    }

    public abstract void castSpell(ServerLevel pLevel, LivingEntity living);
}
