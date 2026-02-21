
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.SummonEntityModel;
import com.bilibili.player_ix.noixmod_api.entities.projectile.summon.SummonEntity;
import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@OnlyInClient
public class SummonEntityRenderer<T extends SummonEntity>
extends EntityRenderer<T> {
    private final SummonEntityModel<T> model;
    public SummonEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.model = new SummonEntityModel<>(p_174008_.bakeLayer(SummonEntityModel.LAYER_LOCATION));
    }

    @Override
    public void render(T p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {
        p_114488_.pushPose();
        float f = 10F;
        VertexConsumer consumer = p_114489_.getBuffer(this.model.renderType(this.getTextureLocation(p_114485_)));
        this.model.setupAnim(p_114485_, 0, 0, p_114487_ / f, p_114485_.getYRot(), p_114485_.getXRot());
        p_114488_.scale(1.5F, 1.5F, 1.5F);
        p_114488_.translate(0, 1.6, 0);
        p_114488_.mulPose(Axis.ZP.rotationDegrees(180F));
        this.model.renderToBuffer(p_114488_, consumer, p_114490_, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.1F);
        p_114488_.popPose();
        p_114488_.pushPose();
        Matrix4f matrix4f = p_114488_.last().pose();
        VertexConsumer vertexConsumer = p_114489_.getBuffer(RenderType.endPortal());
        this.renderCube(matrix4f, vertexConsumer);
        p_114488_.popPose();
        super.render(p_114485_, p_114486_, p_114487_, p_114488_, p_114489_, p_114490_);
    }

    public void renderCube(Matrix4f p_254024_, VertexConsumer p_173693_) {
        float $$4 = this.getOffsetUp();
        this.renderFace(p_254024_, p_173693_, 0.0F, 1.0F, $$4, $$4, 0F, 0F, 0.0F, 0.0F);
    }

    public void renderFace(Matrix4f p_254247_, VertexConsumer p_254390_, float p_254147_, float p_253639_, float p_254107_, float p_254109_, float p_254021_, float p_254458_, float p_254086_, float p_254310_) {
        p_254390_.vertex(p_254247_, p_254147_, p_254107_, p_254021_).endVertex();
        p_254390_.vertex(p_254247_, p_253639_, p_254107_, p_254458_).endVertex();
        p_254390_.vertex(p_254247_, p_253639_, p_254109_, p_254086_).endVertex();
        p_254390_.vertex(p_254247_, p_254147_, p_254109_, p_254310_).endVertex();
    }

    protected float getOffsetUp() {
        return 0.75F;
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T t) {
        return new ResourceLocation("noixmodapi:textures/entities/projectile/summon_entity.png");
    }
}
