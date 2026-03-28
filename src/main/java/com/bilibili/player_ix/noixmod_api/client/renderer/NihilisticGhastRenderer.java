
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APIGhastModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticGhast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NihilisticGhastRenderer<T extends NihilisticGhast>
extends MobRenderer<T, APIGhastModel<T>> {
    private static final ResourceLocation GHAST_LOCATION = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihility_ghast.png");
    private static final ResourceLocation GHAST_SHOOTING_LOCATION = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihility_ghast_shooting.png");

    public NihilisticGhastRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new APIGhastModel<>($$0.bakeLayer(NoixmodAPIModelLayer.API_GHAST)), 1.0f);
    }

    public ResourceLocation getTextureLocation(T $$0) {
        if ($$0.isCharging()) {
            return GHAST_SHOOTING_LOCATION;
        }
        return GHAST_LOCATION;
    }

    protected void scale(T $$0, PoseStack $$1, float $$2) {
        float $$4 = 2f;
        $$1.scale($$4, $$4, $$4);
    }
}
