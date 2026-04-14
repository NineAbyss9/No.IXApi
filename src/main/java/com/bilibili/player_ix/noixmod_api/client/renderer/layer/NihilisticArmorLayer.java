
package com.bilibili.player_ix.noixmod_api.client.renderer.layer;

import com.bilibili.player_ix.noixmod_api.client.model.nihilistic.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class NihilisticArmorLayer<T extends Nihilist>
extends RenderLayer<T, NihilistIllagerModel<T>> {
    private static final ResourceLocation ARMOR_LOCATION =
            new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihilistic_armor.png");
    private final NihilistIllagerModel<T> model;

    public NihilisticArmorLayer(RenderLayerParent<T, NihilistIllagerModel<T>> p_174554_,
                                EntityModelSet p_174555_) {
        super(p_174554_);
        this.model = new NihilistIllagerModel<>(p_174555_.bakeLayer(NihilistIllagerModel.LAYER_LOCATION));
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pLight, T pEntity, float p_116974_,
                       float p_116975_, float p_116976_, float p_116977_, float p_116978_, float p_116979_) {
        if (pEntity.isPowered() || pEntity.isInvulnerable() || (pEntity instanceof Apostle apostle
                && apostle.getHurtCooldown() > 0)) {
            float $$10 = (float)pEntity.tickCount + p_116976_;
            EntityModel<T> $$11 = this.model();
            $$11.prepareMobModel(pEntity, p_116974_, p_116975_, p_116976_);
            this.getParentModel().copyPropertiesTo($$11);
            VertexConsumer $$12 = pBuffer.getBuffer(RenderType.energySwirl(this.getTextureLocation(),
                    this.xOffset($$10) % 1.0F, $$10 * 0.01F % 1.0F));
            $$11.setupAnim(pEntity, p_116974_, p_116975_, p_116977_, p_116978_, p_116979_);
            $$11.renderToBuffer(pPoseStack, $$12, pLight, OverlayTexture.NO_OVERLAY,
                    0.65F, 0.65F, 0.65F, 1.0F);
        }
    }

    protected float xOffset(float p_117702_) {
        return Mth.cos(p_117702_ * 0.02F) * 3.0F;
    }

    protected ResourceLocation getTextureLocation() {
        return ARMOR_LOCATION;
    }

    protected NihilistIllagerModel<T> model() {
        return this.model;
    }
}
