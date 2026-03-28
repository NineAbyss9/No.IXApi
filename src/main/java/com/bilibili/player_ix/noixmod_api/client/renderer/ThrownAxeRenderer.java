
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.ThrownAxeModel;
import com.bilibili.player_ix.noixmod_api.entities.projectile.ThrownAxe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class ThrownAxeRenderer<T extends ThrownAxe> extends EntityRenderer<T> {
    private final ItemRenderer model;

    public ThrownAxeRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.model = p_174008_.getItemRenderer();
    }

    public void render(T p_114656_, float p_114657_, float p_114658_, PoseStack p_114659_, MultiBufferSource p_114660_, int p_114661_) {
        p_114659_.pushPose();
        p_114659_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        p_114659_.mulPose(Axis.YP.rotationDegrees(180.0F));
        p_114659_.mulPose(Axis.ZP.rotationDegrees(180.0F));
        p_114659_.mulPose(Axis.XP.rotationDegrees(90.0F));
        this.model.renderStatic(p_114656_.getItem(), ItemDisplayContext.GROUND, p_114661_, OverlayTexture.NO_OVERLAY, p_114659_, p_114660_, p_114656_.level(), p_114656_.getId());
        p_114659_.popPose();
        super.render(p_114656_, p_114657_, p_114658_, p_114659_, p_114660_, p_114661_);
    }

    public ResourceLocation getTextureLocation(ThrownAxe thrownAxe) {
        return ThrownAxeModel.TEXTURE;
    }
}
