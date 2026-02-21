
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.IntruderModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Intruder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class IntruderRenderer<T extends Intruder>
extends MobRenderer<T, IntruderModel<T>> {
    public IntruderRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new IntruderModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.INTRUDER)), 0.5F);
        this.addLayer(new CustomHeadLayer<>(this, p_174304_.getModelSet(), p_174304_.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()) {
            @Override
            public void render(@NotNull PoseStack p_117204_, @NotNull MultiBufferSource p_117205_, int p_117206_, @NotNull T p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isCastingSpell() || p_117207_.isAggressive()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
                }
            }
        });
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull T t) {
        return new ResourceLocation("noixmodapi:textures/entities/illagers/intruder.png");
    }
}
