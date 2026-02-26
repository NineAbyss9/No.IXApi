
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.IXIllagerModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.servant.IllagerServantRenderer;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VindicatorServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class VindicatorServantRenderer<T extends VindicatorServant>
extends IllagerServantRenderer<T> {
    public VindicatorServantRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new IXIllagerModel<>(p_174304_.bakeLayer(IXIllagerModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()) {
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_,
                               float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isAggressive()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_,
                            p_117211_, p_117212_, p_117213_);
                }
            }
        });
    }

    public ResourceLocation getTextureLocation(T t) {
        return NoixmodAPI.location("textures/entities/illagers/servant/vindicator.png");
    }
}
