
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.WardenServantModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.sculk.warden.WardenServant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class WardenServantRenderer<E extends WardenServant>
extends MobRenderer<E, WardenServantModel<E>> {
    private static final ResourceLocation TEXTURE;
    private static final ResourceLocation BIOLUMINESCENT_LAYER_TEXTURE =
            new ResourceLocation("textures/entity/warden/warden_bioluminescent_layer.png");
    private static final ResourceLocation HEART_TEXTURE =
            new ResourceLocation("textures/entity/warden/warden_heart.png");
    private static final ResourceLocation PULSATING_SPOTS_TEXTURE_1 =
            new ResourceLocation("textures/entity/warden/warden_pulsating_spots_1.png");
    private static final ResourceLocation PULSATING_SPOTS_TEXTURE_2 =
            new ResourceLocation("textures/entity/warden/warden_pulsating_spots_2.png");
    public WardenServantRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WardenServantModel<>(pContext.bakeLayer(ModelLayers.WARDEN)), 1.0F);
        this.addLayer(new WardenEmissiveLayer<>(this, BIOLUMINESCENT_LAYER_TEXTURE,
                (p_234809_, p_234810_, p_234811_) -> 1.0F,
                WardenServantModel::getBioluminescentLayerModelParts));
        this.addLayer(new WardenEmissiveLayer<>(this, PULSATING_SPOTS_TEXTURE_1,
                (p_234805_, p_234806_, p_234807_) -> Math.max(0.0F, Mth.cos(p_234807_ * 0.045F)
                        * 0.25F), WardenServantModel::getPulsatingSpotsLayerModelParts));
        this.addLayer(new WardenEmissiveLayer<>(this, PULSATING_SPOTS_TEXTURE_2,
                (p_234801_, p_234802_, p_234803_) ->
                        Math.max(0.0F, Mth.cos(p_234803_ * 0.045F + (float)Math.PI) * 0.25F),
                WardenServantModel::getPulsatingSpotsLayerModelParts));
        this.addLayer(new WardenEmissiveLayer<>(this, TEXTURE,
                (p_234797_, p_234798_, p_234799_) -> p_234797_
                        .getTendrilAnimation(p_234798_), WardenServantModel::getTendrilsLayerModelParts));
        this.addLayer(new WardenEmissiveLayer<>(this, HEART_TEXTURE,
                (p_234793_, p_234794_, p_234795_) -> p_234793_.getHeartAnimation(p_234794_),
                WardenServantModel::getHeartLayerModelParts));
    }

    public ResourceLocation getTextureLocation(E pEntity) {
        return TEXTURE;
    }

    static {
        TEXTURE = NoixmodAPI.location("textures/entities/servants/sculk/warden.png");
    }

    private static class WardenEmissiveLayer<T extends WardenServant, M extends WardenServantModel<T>>
    extends RenderLayer<T, M> {
        private final ResourceLocation texture;
        private final WardenEmissiveLayer.AlphaFunction<T> alphaFunction;
        private final WardenEmissiveLayer.DrawSelector<T, M> drawSelector;

        public WardenEmissiveLayer(RenderLayerParent<T, M> pRenderer, ResourceLocation pTexture,
                                   AlphaFunction<T> pAlphaFunction, DrawSelector<T, M> pDrawSelector) {
            super(pRenderer);
            this.texture = pTexture;
            this.alphaFunction = pAlphaFunction;
            this.drawSelector = pDrawSelector;
        }

        public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity,
                           float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw,
                           float pHeadPitch) {
            if (!pLivingEntity.isInvisible()) {
                this.onlyDrawSelectedParts();
                VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentEmissive(this.texture));
                this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, LivingEntityRenderer
                        .getOverlayCoords(pLivingEntity, 0.0F), 1.0F, 1.0F, 1.0F, this.alphaFunction
                        .apply(pLivingEntity, pPartialTick, pAgeInTicks));
                this.resetDrawForAllParts();
            }
        }

        private void onlyDrawSelectedParts() {
            List<ModelPart> list = this.drawSelector.getPartsToDraw(this.getParentModel());
            this.getParentModel().root().getAllParts().forEach((p_234918_) -> {
                p_234918_.skipDraw = true;
            });
            list.forEach((p_234916_) -> {
                p_234916_.skipDraw = false;
            });
        }

        private void resetDrawForAllParts() {
            this.getParentModel().root().getAllParts().forEach((p_234913_) -> {
                p_234913_.skipDraw = false;
            });
        }

        @OnlyIn(Dist.CLIENT)
        public interface AlphaFunction<T extends WardenServant> {
            float apply(T pLivingEntity, float pPartialTick, float pAgeInTicks);
        }

        @OnlyIn(Dist.CLIENT)
        public interface DrawSelector<T extends WardenServant, M extends EntityModel<T>> {
            List<ModelPart> getPartsToDraw(M pParentModel);
        }
    }
}
