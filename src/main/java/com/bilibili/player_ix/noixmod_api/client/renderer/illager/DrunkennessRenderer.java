
package com.bilibili.player_ix.noixmod_api.client.renderer.illager;

import com.bilibili.player_ix.noixmod_api.client.renderer.layer.OHCrossedItemLayer;
import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Drunkenness;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class DrunkennessRenderer<T extends Drunkenness> extends IllagerRenderer<T> {
    public DrunkennessRenderer(EntityRendererProvider.Context p_174182_) {
        super(p_174182_, new IllagerModel<>(p_174182_.bakeLayer(ModelLayers.VINDICATOR)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, p_174182_.getItemInHandRenderer()) {
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isAggressive()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
                }
            }
        });
        this.addLayer(new OHCrossedItemLayer<>(this, p_174182_.getItemInHandRenderer()) {
            public void render(PoseStack p_116699_, MultiBufferSource p_116700_, int p_116701_, T p_116702_, float p_116703_, float p_116704_, float p_116705_, float p_116706_, float p_116707_, float p_116708_) {
                if (!p_116702_.isAggressive()) {
                    super.render(p_116699_, p_116700_, p_116701_, p_116702_, p_116703_, p_116704_, p_116705_, p_116706_, p_116707_, p_116708_);
                }
            }
        });
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/illagers/drunkenness_new.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
