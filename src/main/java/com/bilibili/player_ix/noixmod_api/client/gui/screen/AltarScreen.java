
package com.bilibili.player_ix.noixmod_api.client.gui.screen;

import com.bilibili.player_ix.noixmod_api.client.gui.menu.AltarMenu;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AltarScreen extends AbstractContainerScreen<AltarMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            "noixmodapi:textures/gui/altar/background.png");
    private static final ResourceLocation HORROR = new ResourceLocation(
            "noixmodapi:textures/gui/altar/horror.png");
    public AltarScreen(AltarMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    protected void init() {
        super.init();
        /*this.addRenderableWidget(new Button(
                this.leftPos + 60, this.topPos + 50,
                60, 20,
                Component.literal("Click Me"),
                button -> System.out.println("Button clicked!")
        ));*/
    }

    public void render(GuiGraphics p_283479_, int mouseX, int mouseY, float tick) {
        this.renderBackground(p_283479_);
        super.render(p_283479_, mouseX, mouseY, tick);
        this.renderTooltip(p_283479_, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics guiGraphics, float v, int x, int y) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        guiGraphics.blit(NoixmodAPIMainConfig.HorrorMode.get() ? HORROR : TEXTURE,
                this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
