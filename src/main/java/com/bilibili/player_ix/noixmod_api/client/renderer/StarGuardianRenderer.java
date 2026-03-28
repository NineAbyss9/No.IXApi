
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.StarGuardianModel;
import com.bilibili.player_ix.noixmod_api.entities.boss.star_guardian.StarGuardian;
import com.bilibili.player_ix.noixmod_api.util.TimeSelector;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class StarGuardianRenderer<S extends StarGuardian> extends MobRenderer<S, StarGuardianModel<S>> {
    public StarGuardianRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new StarGuardianModel<>(p_174304_.bakeLayer(StarGuardianModel.LAYER_LOCATION)),
                0.5F);
    }

    private static final ResourceLocation BIRTHDAY = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/birthday/zhuan_birthday.png");
    private static ResourceLocation DIE = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/zhuan_die.png");
    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/zhuan.png");

    public ResourceLocation getTextureLocation(S s) {
        if (TimeSelector.birthday()) {
            return BIRTHDAY;
        }
        if (s.isFlag(4)) {
            return DIE;
        } else {
            return LOC;
        }
    }

    private static class SGShieldLayer<S extends StarGuardian> extends RenderLayer<S, StarGuardianModel<S>> {
        private static final ResourceLocation ARMOR_LOCATION = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihility_armor.png");
        private final StarGuardianModel<S> model;

        public SGShieldLayer(RenderLayerParent<S, StarGuardianModel<S>> p_117346_, EntityModelSet setter) {
            super(p_117346_);
            this.model = new StarGuardianModel<>(setter.bakeLayer(StarGuardianModel.LAYER_LOCATION));
        }

        public void render(PoseStack p_116970_, MultiBufferSource p_116971_, int p_116972_, S p_116973_, float p_116974_, float p_116975_, float p_116976_, float p_116977_, float p_116978_, float p_116979_) {
            if (p_116973_.getShieldTick() > 0) {
                float $$10 = (float)p_116973_.tickCount + p_116976_;
                EntityModel<S> $$11 = this.model();
                $$11.prepareMobModel(p_116973_, p_116974_, p_116975_, p_116976_);
                this.getParentModel().copyPropertiesTo($$11);
                VertexConsumer $$12 = p_116971_.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset($$10) % 1.0F, $$10 * 0.01F % 1.0F));
                $$11.setupAnim(p_116973_, p_116974_, p_116975_, p_116977_, p_116978_, p_116979_);
                $$11.renderToBuffer(p_116970_, $$12, p_116972_, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
            }
        }

        protected float xOffset(float p_117702_) {
            return Mth.cos(p_117702_ * 0.02F) * 3.0F;
        }

        protected ResourceLocation getTextureLocation() {
            return ARMOR_LOCATION;
        }

        protected StarGuardianModel<S> model() {
            return this.model;
        }
    }
}
