
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.client.model.IXIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.OwnableIllager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

public abstract class IllagerServantRenderer<T extends OwnableIllager>
extends MobRenderer<T, IXIllagerModel<T>> {
    public IllagerServantRenderer(EntityRendererProvider.Context p_174304_, IXIllagerModel<T> p_174305_,
                                  float scale) {
        super(p_174304_, p_174305_, scale);
    }

    protected void scale(T p_115314_, PoseStack p_115315_, float p_115316_) {
        float a = 0.7f;
        float b = 0.975f;
        float c = p_115314_.isBaby() ? a : b;
        p_115315_.scale(c, c, c);
    }
}
