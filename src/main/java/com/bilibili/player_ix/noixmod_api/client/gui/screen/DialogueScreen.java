
package com.bilibili.player_ix.noixmod_api.client.gui.screen;

import com.bilibili.player_ix.noixmod_api.entities.villager.VillagerFighter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class DialogueScreen extends Screen {
    private final Entity targetEntity;
    private static final Component TITLE = Component.translatable("gui.noixapi.talking.talking");

    public DialogueScreen(Entity targetEntity) {
        super(TITLE);
        this.targetEntity = targetEntity;
    }

    protected void init() {
        super.init();
        // 添加按钮（选项1：友好）
        this.addRenderableWidget(Button.builder(Component.translatable("gui.noixapi.talking.hello"), button -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("村民对你微笑！"));
            }
            Minecraft.getInstance().setScreen(null); // 关闭 GUI
        }).bounds(width / 2 - 100, height / 2, 200, 20).build());
        // 添加按钮（选项2：Trade）
        this.addRenderableWidget(Button.builder(Component.translatable("gui.noixapi.talking.trade"), button -> {
            if (Minecraft.getInstance().player != null) {
                if (targetEntity instanceof VillagerFighter fighter) {
                    fighter.startTrading(Minecraft.getInstance().player);
                }
            }
            Minecraft.getInstance().setScreen(null);
        }).bounds(width / 2 - 100, height / 2 + 30, 200, 20).build());
    }

    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        renderBackground(gui);
        super.render(gui, mouseX, mouseY, delta);
        gui.drawCenteredString(font, TITLE, width / 2, height / 2 - 30, 0xFFFFFF);
    }
}
