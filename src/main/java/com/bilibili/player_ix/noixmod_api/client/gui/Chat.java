
package com.bilibili.player_ix.noixmod_api.client.gui;

import com.bilibili.player_ix.noixmod_api.client.gui.button.ChatButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class Chat extends Screen {
    private final ChatButton[] chatButtons;
    public Chat(String name, int count) {
        super(Component.translatableWithFallback(name, "Chat"));
        this.chatButtons = new ChatButton[count];
    }

    public void onClose() {
        super.onClose();
    }
}
