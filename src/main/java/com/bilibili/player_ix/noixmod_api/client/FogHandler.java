
package com.bilibili.player_ix.noixmod_api.client;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;

import java.util.List;

@OnlyInClient
public class FogHandler {
    public static float currentDensity = 1.0F;
    public static float currentRed = 0.5F;
    public static float currentGreen = 0.5F;
    public static float currentBlue = 0.5F;
    public static float targetRed;
    public static float targetGreen;
    public static float targetBlue;
    public static float limitFogDis = 3.0F;
    public static float maxFogDis = 12.0F;
    public static float targetNear;
    public static float targetFar;

    public static void setFogDynamically(ClientLevel pLevel, Player pPlayer) {
        List<AbstractHorrorMob> list = pLevel.getEntitiesOfClass(AbstractHorrorMob.class,
                pPlayer.getBoundingBox().inflate(32)
                , abstractHorrorMob -> abstractHorrorMob.getLevel() >= 1 && abstractHorrorMob.hasLineOfSight(pPlayer)
        );
        if (list.isEmpty()) {
            turnToNormal(pLevel, pPlayer);
            return;
        }
        float factor = 0.0F;
        int level1Count = list.size();
        factor += Math.min(level1Count * 0.1f, 0.6f);
        int light = Math.max(pLevel.getBrightness(LightLayer.BLOCK, pPlayer.blockPosition()),
                pLevel.getBrightness(LightLayer.SKY, pPlayer.blockPosition()));
        factor += (1.0f - light / 15.0f) * 0.2f;
        targetNear = 2.0F + (1.0f - factor) * 2.0f;
        targetFar = 8.0F + (1.0f - factor) * 10.0f;
        targetRed = 0.1f + factor * 0.8f;
        targetGreen = 0.05f + factor * 0.1f;
        targetBlue = 0.05f;
        limitFogDis = lerp(limitFogDis, targetNear, 0.1f);
        maxFogDis = lerp(maxFogDis, targetFar, 0.1f);
        setFogColor(lerp(currentRed, targetRed, 0.1f), lerp(currentGreen, targetGreen, 0.1f), lerp(currentBlue, targetBlue, 0.1f));
    }

    public static void setFogIntensity(float intensity) {
        // intensity: 0.0 = 无雾, 1.0 = 最浓
        currentDensity = Math.max(0.1f, Math.min(10.0f, intensity * 10.0f));
    }

    public static void turnToNormal(ClientLevel pLevel, Player pPlayer) {
        float factor = 0.0F;
        int light = Math.max(pLevel.getBrightness(LightLayer.BLOCK, pPlayer.blockPosition()),
                pLevel.getBrightness(LightLayer.SKY, pPlayer.blockPosition()));
        factor += (1.0f - light / 15.0f) * 0.2f;
        targetNear = 4.0F + (1.0f - factor) * 2.0F;
        targetFar = 12.0F + (1.0f - factor) * 10.0F;

        targetRed = 0.92f;
        targetGreen = 0.9f;
        targetBlue = 0.95f;

        limitFogDis = lerp(limitFogDis, targetNear, 0.1F);
        maxFogDis = lerp(maxFogDis, targetFar, 0.1F);
        setFogColor(lerp(currentRed, targetRed, 0.1F), lerp(currentGreen, targetGreen, 0.1F), lerp(currentBlue, targetBlue, 0.1F));
    }

    public static void setFogColor(float r, float g, float b) {
        currentRed = r;
        currentGreen = g;
        currentBlue = b;
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
