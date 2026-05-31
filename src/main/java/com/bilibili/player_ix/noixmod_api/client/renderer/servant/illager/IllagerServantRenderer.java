
package com.bilibili.player_ix.noixmod_api.client.renderer.servant.illager;

import com.bilibili.player_ix.noixmod_api.client.model.illager.IXIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.OwnableIllager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

public abstract class IllagerServantRenderer<T extends OwnableIllager>
extends MobRenderer<T, IXIllagerModel<T>> {
    public IllagerServantRenderer(EntityRendererProvider.Context pContext, IXIllagerModel<T> pModel,
                                  float scale) {
        super(pContext, pModel, scale);
    }

    protected void scale(T pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        float a = 0.7f;
        float b = 0.975f;
        float c = pLivingEntity.isBaby() ? a : b;
        pPoseStack.scale(c, c, c);
    }
}
