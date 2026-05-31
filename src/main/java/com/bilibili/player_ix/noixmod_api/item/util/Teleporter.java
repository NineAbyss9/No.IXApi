
package com.bilibili.player_ix.noixmod_api.item.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

import java.util.List;

public class Teleporter
extends Item {
    public Teleporter() {
        super(new Properties().stacksTo(1));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        List<Entity> entities = pLevel.getEntitiesOfClass(Entity.class, pPlayer.getBoundingBox().inflate(999),
                EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        if (entities.isEmpty()) {
            if (pLevel.isClientSide) {
                Minecraft.getInstance().gui.setOverlayMessage(Component.literal("Entity not found"),
                        false);
            }
            return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        }
        for (Entity entity : entities) {
            entity.setPos(pPlayer.position());
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
