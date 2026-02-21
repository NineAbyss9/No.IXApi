
package com.bilibili.player_ix.noixmod_api.client.gui.button;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

@OnlyInClient
public class ChatButton extends Button {
    public ChatButton(int p_259075_, int p_259271_, int p_260232_, int p_260028_, Component p_259351_, OnPress p_260152_, CreateNarration p_259552_) {
        super(p_259075_, p_259271_, p_260232_, p_260028_, p_259351_, p_260152_, p_259552_);
    }
    /*

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.blit(isForward ? book.rightButton() : book.leftButton(), this.getX(), this.getY(), 0, 0, book.buttonWidth(), book.buttonHeight(), book.buttonWidth(), book.buttonHeight());
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (this.playTurnSound) {
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }*/
}
