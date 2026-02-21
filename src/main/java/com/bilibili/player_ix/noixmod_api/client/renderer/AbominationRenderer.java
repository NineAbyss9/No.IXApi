
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Abomination;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class AbominationRenderer<A extends Abomination>
extends IllagerRenderer<A> {
    public AbominationRenderer(EntityRendererProvider.Context p_174182_) {
        super(p_174182_, new IllagerModel<>(p_174182_.bakeLayer(ModelLayers.EVOKER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174182_.getItemInHandRenderer()) {
            @Override
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, A p_117207_,
                               float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isCastingSpell() || p_117207_.isAggressive()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_,
                            p_117210_, p_117211_, p_117212_, p_117213_);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(A a) {
        return new ResourceLocation("noixmodapi:textures/entities/illagers/abomination.png");
    }
}
