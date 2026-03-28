
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.VampireServantModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.VampireServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class VampireServantRenderer<V extends VampireServant>
extends MobRenderer<V, VampireServantModel<V>> {
    public VampireServantRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new VampireServantModel<>(p_174304_.bakeLayer(ModelLayers.ILLUSIONER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()) {
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, V p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isAggressive()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
                }
            }
        });
    }

    protected void scale(V p_115314_, PoseStack p_115315_, float p_115316_) {
        float f = 0.9375f;
        p_115315_.scale(f, f, f);
    }

    public ResourceLocation getTextureLocation(V v) {
        return VampireRenderer.VAMPIRE;
    }
}
