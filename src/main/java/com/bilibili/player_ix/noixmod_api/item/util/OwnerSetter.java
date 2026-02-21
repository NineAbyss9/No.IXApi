
package com.bilibili.player_ix.noixmod_api.item.util;

import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class OwnerSetter
extends Item {
    public OwnerSetter() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof Ownable own) {
            if (own.getOwner() == null && own.wouldHaveOwner()) {
                own.setOwner(player);
                if (player.level().isClientSide) {
                    Minecraft.getInstance().gui.setOverlayMessage(Component.translatable(
                            "info.noixmodapi.owner_setter.set_owner", entity.getDisplayName(),
                            player.getDisplayName()),
                            false);
                }
                return true;
            } else {
                return own.getOwner() != player && super.onLeftClickEntity(stack, player, entity);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
