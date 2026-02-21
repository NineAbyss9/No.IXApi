
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.VillagerFangs;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class VillagerFangsRenderer<T extends VillagerFangs>
extends EntityRenderer<T> {
    private final EvokerFangsModel<T> model;
    public VillagerFangsRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.model = new EvokerFangsModel<>(p_174008_.bakeLayer(ModelLayers.EVOKER_FANGS));
    }

    @Override
    public void render(@NotNull T p_114528_, float p_114529_, float p_114530_, @NotNull PoseStack p_114531_, @NotNull MultiBufferSource p_114532_, int p_114533_) {
        float $$6 = p_114528_.getAnimationProgress(p_114530_);
        if ($$6 != 0.0F) {
            float $$7 = 2.0F;
            if ($$6 > 0.9F) {
                $$7 *= (1.0F - $$6) / 0.1F;
            }
            p_114531_.pushPose();
            p_114531_.mulPose(Axis.YP.rotationDegrees(90.0F - p_114528_.getYRot()));
            p_114531_.scale(-$$7, -$$7, $$7);
            p_114531_.translate(0.0, -0.626, 0.0);
            p_114531_.scale(0.5F, 0.5F, 0.5F);
            this.model.setupAnim(p_114528_, $$6, 0.0F, 0.0F, p_114528_.getYRot(), p_114528_.getXRot());
            VertexConsumer $$9 = p_114532_.getBuffer(this.model.renderType(this.getTextureLocation(p_114528_)));
            this.model.renderToBuffer(p_114531_, $$9, p_114533_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            p_114531_.popPose();
            super.render(p_114528_, p_114529_, p_114530_, p_114531_, p_114532_, p_114533_);
        }
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull T t) {
        return new ResourceLocation("noixmodapi:textures/entities/projectile/villager_fangs.png");
    }
}
