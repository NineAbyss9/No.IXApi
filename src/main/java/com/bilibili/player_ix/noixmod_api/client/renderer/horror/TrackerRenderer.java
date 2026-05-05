
package com.bilibili.player_ix.noixmod_api.client.renderer.horror;

import com.bilibili.player_ix.noixmod_api.entities.monster.horror.Tracker;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TrackerRenderer<T extends Tracker>
extends MobRenderer<T, HumanoidModel<T>> {
    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/monsters/horror/tracker.png");
    public TrackerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HumanoidModel<>(pContext.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);
    }

    protected boolean isShaking(T pEntity) {
        return true;
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        return LOC;
    }
}
