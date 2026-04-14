
package com.bilibili.player_ix.noixmod_api.client.renderer.illager;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.illager.GreenSingerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.GreenSinger;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class GreenSingerRenderer<G extends GreenSinger>
extends MobRenderer<G, GreenSingerModel<G>> {
    public GreenSingerRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new GreenSingerModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.EI)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()) {
            @Override
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, G p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isAggressive() || p_117207_.isCastingSpell()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(G g) {
        return new ResourceLocation("noixmodapi:textures/entities/special/green_singer.png");
    }
}
