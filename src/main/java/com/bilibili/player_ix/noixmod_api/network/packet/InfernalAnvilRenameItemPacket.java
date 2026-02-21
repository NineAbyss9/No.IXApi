
package com.bilibili.player_ix.noixmod_api.network.packet;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.gui.menu.InfernalIronAnvilMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record InfernalAnvilRenameItemPacket(String name) {
    public static void encode(InfernalAnvilRenameItemPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.name);
    }

    public static InfernalAnvilRenameItemPacket decode(FriendlyByteBuf buffer) {
        return new InfernalAnvilRenameItemPacket(buffer.readUtf());
    }

    public static void handle(InfernalAnvilRenameItemPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player;
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                player = NoixmodAPI.agent.getPlayerInstance();
            } else {
                player = ctx.get().getSender();
            }
            if (player != null) {
                AbstractContainerMenu abstractcontainermenu = player.containerMenu;
                if (abstractcontainermenu instanceof InfernalIronAnvilMenu menu) {
                    if (!menu.stillValid(player)) {
                        NoixmodAPI.LOGGER.debug("Player {} interacted with invalid menu {}", player, menu);
                        return;
                    }
                    menu.setItemName(packet.name());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
