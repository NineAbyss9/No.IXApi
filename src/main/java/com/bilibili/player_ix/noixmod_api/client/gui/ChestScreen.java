
package com.bilibili.player_ix.noixmod_api.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ChestScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public ChestScreen(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }
}
