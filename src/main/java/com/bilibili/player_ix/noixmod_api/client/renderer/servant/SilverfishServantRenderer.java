
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.entities.servant.SilverfishServant;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SilverfishServantRenderer<T extends SilverfishServant>
extends MobRenderer<T, SilverfishModel<T>> {
    public SilverfishServantRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new SilverfishModel<>(p_174304_.bakeLayer(ModelLayers.SILVERFISH)), 0.3f);
    }

    protected float getFlipDegrees(T p_115337_) {
        return 180F;
    }

    private static ResourceLocation LOC = new ResourceLocation("textures/entity/silverfish.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
