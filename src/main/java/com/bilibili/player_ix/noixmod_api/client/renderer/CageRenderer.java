
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.CageModel;
import com.bilibili.player_ix.noixmod_api.entities.projectile.Cage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CageRenderer<T extends Cage, M extends CageModel<T>>
extends EntityRenderer<T>
implements RenderLayerParent<T, M> {
    private final M cageModel;
    private final CageLayer<T, M> cageLayer;
    @SuppressWarnings("unchecked")
    public CageRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.cageModel = (M)new CageModel<T>(p_174008_.bakeLayer(CageModel.LAYER_LOCATION));
        this.cageLayer = new CageLayer<T, M>(this, p_174008_.getModelSet());
    }

    public void render(T p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {
        super.render(p_114485_, p_114486_, p_114487_, p_114488_, p_114489_, p_114490_);
        float f = Mth.rotLerp(p_114487_, p_114485_.yBodyRotO, p_114485_.getYRot());
        float f1 = Mth.rotLerp(p_114487_, p_114485_.yHeadRotO, p_114485_.getYHeadRot());
        float f2 = f1 - f;
        float f6 = Mth.lerp(p_114487_, p_114485_.xRotO, p_114485_.getXRot());
        float f7 = this.getBob(p_114485_, p_114487_);
        this.cageLayer.render(p_114488_, p_114489_, p_114490_, p_114485_, 0.0F, 0.0F, p_114487_,
                f7, f2, f6);
    }

    protected float getBob(T p_115305_, float p_115306_) {
        return (float)p_115305_.tickCount + p_115306_;
    }

    public M getModel() {
        return this.cageModel;
    }

    public ResourceLocation getTextureLocation(T t) {
        return null;
    }

    private static class CageLayer <T extends Cage, M extends CageModel<T>>
            extends RenderLayer<T, M> {
        private static final ResourceLocation ARMOR_LOCATION =
                new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihilistic_armor.png");
        private final CageModel<T> model;

        public CageLayer(RenderLayerParent<T, M> p_174554_, EntityModelSet p_174555_) {
            super(p_174554_);
            this.model = new CageModel<>(p_174555_.bakeLayer(CageModel.LAYER_LOCATION));
        }

        public void render(PoseStack p_116970_, MultiBufferSource p_116971_, int p_116972_, T p_116973_, float p_116974_,
                           float p_116975_, float p_116976_, float p_116977_, float p_116978_, float p_116979_) {
            float $$10 = (float) p_116973_.tickCount + p_116976_;
            EntityModel<T> $$11 = this.model();
            $$11.prepareMobModel(p_116973_, p_116974_, p_116975_, p_116976_);
            this.getParentModel().copyPropertiesTo($$11);
            VertexConsumer $$12 = p_116971_.getBuffer(RenderType.energySwirl(this.getTextureLocation(),
                    this.xOffset($$10) % 1.0F, $$10 * 0.01F % 1.0F));
            $$11.setupAnim(p_116973_, p_116974_, p_116975_, p_116977_, p_116978_, p_116979_);
            $$11.renderToBuffer(p_116970_, $$12, p_116972_, OverlayTexture.NO_OVERLAY,
                    1F, 1F, 1F, 1.0F);
        }

        protected float xOffset(float p_117702_) {
            return Mth.cos(p_117702_ * 0.02F) * 3.0F;
        }

        protected ResourceLocation getTextureLocation() {
            return ARMOR_LOCATION;
        }

        protected CageModel<T> model() {
            return this.model;
        }
    }
}
