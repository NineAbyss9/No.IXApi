
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APIGhastModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.nether.MiniGhast;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MiniGhastRenderer<T extends MiniGhast>
extends MobRenderer<T, APIGhastModel<T>> {
    public MiniGhastRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new APIGhastModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.API_GHAST)), 0.5f);
    }

    private static final ResourceLocation CHARGING = new ResourceLocation("noixmodapi:textures/entities/servants/mini_ghast/mini_ghast_charging.png");

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/servants/mini_ghast/mini_ghast.png");

    public ResourceLocation getTextureLocation(T t) {
        if (t.isCharging()) {
            return CHARGING;
        }
        return LOC;
    }
}
