
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.AbstractPlayerModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.MagicalClone;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class MagicalCloneRenderer<T extends MagicalClone>
extends MobRenderer<T, AbstractPlayerModel<T>> {
    public MagicalCloneRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new AbstractPlayerModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.ABSTRACT_PLAYER)), 0.5f);
        this.addLayer(new CustomHeadLayer<>(this, p_174304_.getModelSet(), p_174304_.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()) {
            @Override
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isAggressive()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
                }
            }
        });
    }

    @Override
    protected void scale(T p_115314_, PoseStack p_115315_, float p_115316_) {
        float s = 0.9375f;
        p_115315_.scale(s, s, s);
    }

    @Override
    protected void setupRotations(T p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) {
        super.setupRotations(p_115317_, p_115318_, p_115319_, p_115320_, p_115321_);
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("noixmodapi:textures/entities/misc/magical_clone.png");
    }
}
