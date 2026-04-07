
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.client.model.YetiModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.ice.Yeti;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class YetiRenderer<E extends Yeti>
extends MobRenderer<E, YetiModel<E>> {
    public YetiRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new YetiModel<>(pContext.bakeLayer(YetiModel.LAYER_LOCATION)), 0.5f);
    }

    public static final ResourceLocation TEXTURE = new ResourceLocation("noixmodapi:textures/entities/servants/ice/yeti.png");
    /*public static final ResourceLocation TEXTURE_1 = new ResourceLocation(
            "noixmodapi:textures/entities/servants/ice/yeti1.png");*/
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation(
            "noixmodapi:textures/entities/servants/ice/yeti2.png");
    public static final ResourceLocation TEXTURE_3 = new ResourceLocation(
            "noixmodapi:textures/entities/servants/ice/yeti3.png");

    public ResourceLocation getTextureLocation(E entity) {
        if (entity.getStatus() == 3) {
            return TEXTURE;
        } else if (entity.getStatus() == 1) {
            return TEXTURE_2;
        } else if (entity.getStatus() == 2) {
            return TEXTURE_3;
        }
        return TEXTURE;
    }
}
