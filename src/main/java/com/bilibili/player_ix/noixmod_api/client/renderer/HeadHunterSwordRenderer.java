
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.HeadHunterSwordModel;
import com.bilibili.player_ix.noixmod_api.entities.projectile.HeadHunterSword;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class HeadHunterSwordRenderer<H extends HeadHunterSword>
extends EntityRenderer<H> {
    private final HeadHunterSwordModel<H> model;
    public HeadHunterSwordRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.model = new HeadHunterSwordModel<>(p_174008_.bakeLayer(HeadHunterSwordModel.LAYER_LOCATION));
    }

    @Override
    public void render(H p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {
        p_114488_.pushPose();
        VertexConsumer consumer = p_114489_.getBuffer(this.model.renderType(this.getTextureLocation(p_114485_)));
        this.model.setupAnim(p_114485_, p_114486_, 0, p_114490_, p_114485_.getYRot(), p_114485_.getXRot());
        p_114488_.scale(1.5F, 1.5F, 1.5F);
        p_114488_.translate(0, 1.6, 0);
        p_114488_.mulPose(Axis.ZP.rotationDegrees(180F));
        this.model.renderToBuffer(p_114488_, consumer, p_114490_, OverlayTexture.NO_OVERLAY, 1, 1 , 1, 0.1F);
        p_114488_.popPose();
        super.render(p_114485_, p_114486_, p_114487_, p_114488_, p_114489_, p_114490_);
    }

    @Override
    public ResourceLocation getTextureLocation(H h) {
        return new ResourceLocation("noixmodapi:textures/entities/projectile/head_hunter_sword.png");
    }
}
