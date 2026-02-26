
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.entities.servant.CreeperServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PowerableMob;

public class CreeperServantRenderer<T extends CreeperServant>
extends MobRenderer<T, CreeperModel<T>> {
    public CreeperServantRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CreeperModel<>(pContext.bakeLayer(ModelLayers.CREEPER)), 0.5F);
        this.addLayer(new CreeperPowerLayer<>(this, pContext.getModelSet()));
    }

    protected void scale(T pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        float $$3 = pLivingEntity.getSwelling(pPartialTickTime);
        float $$4 = 1.0F + Mth.sin($$3 * 100.0F) * $$3 * 0.01F;
        $$3 = Mth.clamp($$3, 0.0F, 1.0F);
        $$3 *= $$3;
        $$3 *= $$3;
        float $$5 = (1.0F + $$3 * 0.4F) * $$4;
        float $$6 = (1.0F + $$3 * 0.1F) / $$4;
        pPoseStack.scale($$5, $$6, $$5);
    }

    protected float getWhiteOverlayProgress(T pLivingEntity, float pPartialTicks) {
        float $$2 = pLivingEntity.getSwelling(pPartialTicks);
        return (int)($$2 * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp($$2, 0.5F, 1.0F);
    }

    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("textures/entity/creeper/creeper.png");
    }

    public static class CreeperPowerLayer<T extends Mob & PowerableMob>
            extends EnergySwirlLayer<T, CreeperModel<T>> {
        private static final ResourceLocation POWER_LOCATION = new ResourceLocation(
                "textures/entity/creeper/creeper_armor.png");
        private final CreeperModel<T> model;

        public CreeperPowerLayer(RenderLayerParent<T, CreeperModel<T>> pRenderer, EntityModelSet pModelSet) {
            super(pRenderer);
            this.model = new CreeperModel<>(pModelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
        }

        protected float xOffset(float pTickCount) {
            return pTickCount * 0.01F;
        }

        protected ResourceLocation getTextureLocation() {
            return POWER_LOCATION;
        }

        protected EntityModel<T> model() {
            return this.model;
        }
    }
}
