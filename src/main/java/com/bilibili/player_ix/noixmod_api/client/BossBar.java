
package com.bilibili.player_ix.noixmod_api.client;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.entities.boss.abyss.Abyss;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**Copy from Polarice3's BossBarEvent*/
@ParametersAreNonnullByDefault
public class BossBar {
    private static final ResourceLocation BOSS_BAR = new ResourceLocation(NoixmodAPI.MOD_ID,
            "textures/gui/boss_bar.png");
    private static final ResourceLocation BOSS_BAR_1 = new ResourceLocation(NoixmodAPI.MOD_ID,
            "textures/gui/boss_bar_1.png");
    private static final ResourceLocation APOSTLE_HORROR = new ResourceLocation(NoixmodAPI.MOD_ID,
            "textures/gui/apostle_boss_bar_horror.png");
    private static final ResourceLocation APOSTLE_HORROR_1 = new ResourceLocation(
            NoixmodAPI.MOD_ID, "textures/gui/apostle_boss_bar_horror_1.png");
    public static Map<UUID, Mob> BOSS_BARS = new HashMap<>();

    @SubscribeEvent
    public static void renderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        Minecraft minecraft = Minecraft.getInstance();
        int i = minecraft.getWindow().getGuiScaledWidth();
        if (BOSS_BARS.containsKey(event.getBossEvent().getId())) {
            Mob boss = BOSS_BARS.get(event.getBossEvent().getId());
            event.setCanceled(true);
            int k = i / 2 - 100;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            drawBar(event.getGuiGraphics(), k, event.getY(), event.getPartialTick(), boss);
            Component itextcomponent = boss.getDisplayName();
            int l = minecraft.font.width(itextcomponent);
            int i1 = i / 2 - l / 2;
            event.getGuiGraphics().drawString(minecraft.font, itextcomponent, i1, event.getY() - 9,
                    16777215);
            if (event.getY() >= minecraft.getWindow().getGuiScaledHeight() / 3) {
                return;
            }
            event.setIncrement(12 + minecraft.font.lineHeight);
        }
    }

    private static void drawBar(GuiGraphics guiGraphics, int pX, int pY, float partialTicks, Mob pEntity) {
        float percent = pEntity.getHealth() / pEntity.getMaxHealth();
        int i = (int)(percent * 182.0F);
        int pX2 = pX + 9;
        int pY2 = pY + 4;
        int offset = (int) ((pEntity.tickCount + partialTicks) % 364);
        if (percent <= 0.25F){
            offset = (int) (((pEntity.tickCount + partialTicks) * 4) % 364);
        } else if (percent <= 0.5F){
            offset = (int) (((pEntity.tickCount + partialTicks) * 2) % 364);
        }
        if (pEntity instanceof Apostle apostleEntity) {
            boolean flag = apostleEntity.isSecondPhase();
            int shake;
            int damage;
            if (i > 0) {
                guiGraphics.blit(apostle1(), pX2, pY2, offset, 0, i, 8, 364, 64);
                if (apostleEntity.getHurtCooldown() >= 5) {
                    damage = 32 + pEntity.getRandom().nextInt(apostleEntity.getHurtCooldown());
                    shake = pEntity.getRandom().nextInt(apostleEntity.getHurtCooldown());
                    RenderSystem.setShaderTexture(0, apostle());
                    guiGraphics.blit(apostle(), pX2, pY2, shake, damage, i, 8, 256, 256);
                }
                if (apostleEntity.getCancelRegenTick() > 0){
                    float smite = 1.0F - ((float) apostleEntity.getCancelRegenTick());
                    guiGraphics.blit(apostle1(), pX2, pY2, offset, 16, i, 8,
                            364, 64);
                    guiGraphics.blit(apostle1(), pX2, pY2, offset, 0, (int)(smite * i),
                            8, 364, 64);
                }
            }
            guiGraphics.blit(apostle(), pX, pY, 0, flag ? 16 : 0, 200,
                    16, 256, 256);
        } else if (pEntity instanceof Abyss) {
            if (i > 0) {
                guiGraphics.blit(BOSS_BAR_1, pX2, pY2, 0, 29, i, 8,
                        364, 64);
            }
            guiGraphics.blit(BOSS_BAR, pX, pY, 0, 64, 200, 16,
                    256, 256);
        }
    }

    private static ResourceLocation apostle() {
        return NoixmodAPIMainConfig.HorrorMode.get() ? APOSTLE_HORROR : BOSS_BAR;
    }

    private static ResourceLocation apostle1() {
        return NoixmodAPIMainConfig.HorrorMode.get() ? APOSTLE_HORROR_1 : BOSS_BAR_1;
    }

    public static boolean contains(UUID id) {
        return BOSS_BARS.containsKey(id);
    }

    public static void addBossBar(UUID id, Mob mob){
        BOSS_BARS.put(id, mob);
    }

    public static void removeBossBar(UUID id, Mob mob){
        BOSS_BARS.remove(id, mob);
    }
}
