
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticWitherSkull;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class NihilisticWitherSkullRenderer<N extends NihilisticWitherSkull>
extends EntityRenderer<N> {
    private static final ResourceLocation WITHER_LOCATION =
            new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/wither/wither_invulnerable.png");
    private final SkullModel model;

    public NihilisticWitherSkullRenderer(EntityRendererProvider.Context p_174449_) {
        super(p_174449_);
        this.model = new SkullModel(p_174449_.bakeLayer(ModelLayers.WITHER_SKULL));
    }

    protected int getBlockLightLevel(N p_116491_, BlockPos p_116492_) {
        return 15;
    }

    public void render(N p_116484_, float p_116485_, float p_116486_, PoseStack p_116487_, MultiBufferSource p_116488_, int p_116489_) {
        p_116487_.pushPose();
        p_116487_.scale(-1.0F, -1.0F, 1.0F);
        float $$6 = Mth.rotLerp(p_116486_, p_116484_.yRotO, p_116484_.getYRot());
        float $$7 = Mth.lerp(p_116486_, p_116484_.xRotO, p_116484_.getXRot());
        VertexConsumer $$8 = p_116488_.getBuffer(this.model.renderType(this.getTextureLocation(p_116484_)));
        this.model.setupAnim(0.0F, $$6, $$7);
        this.model.renderToBuffer(p_116487_, $$8, p_116489_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_116487_.popPose();
        super.render(p_116484_, p_116485_, p_116486_, p_116487_, p_116488_, p_116489_);
    }

    public ResourceLocation getTextureLocation(N p_116482_) {
        return WITHER_LOCATION;
    }
}
