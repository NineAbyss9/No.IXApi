
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APISkeletonModel;
import com.bilibili.player_ix.noixmod_api.client.model.illager.IXIllagerModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.layer.OHCrossedItemLayer;
import com.bilibili.player_ix.noixmod_api.client.renderer.servant.illager.IllagerServantRenderer;
import com.bilibili.player_ix.noixmod_api.entities.servant.SkeletonServant;
import com.bilibili.player_ix.noixmod_api.entities.servant.ice.StrayServant;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.DrunkennessServant;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.HunterServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.StrayClothingLayer;
import net.minecraft.resources.ResourceLocation;

public class ServantRenderer
{
    public static class Skeleton<E extends SkeletonServant>
    extends MobRenderer<E, APISkeletonModel<E>>
    {
        public Skeleton(EntityRendererProvider.Context pContext)
        {
            super(pContext, new APISkeletonModel<>(pContext.bakeLayer(NoixmodAPIModelLayer.API_SKELETON)),
                    0.5F);
            this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
        }

        private static final ResourceLocation TEXTURE
                = NoixmodAPI.entity("servants/skeleton/skeleton");

        public ResourceLocation getTextureLocation(E pEntity)
        {
            return TEXTURE;
        }
    }

    public static class Stray<E extends StrayServant>
    extends MobRenderer<E, APISkeletonModel<E>>
    {
        public Stray(EntityRendererProvider.Context pContext)
        {
            super(pContext, new APISkeletonModel<>(pContext.bakeLayer(NoixmodAPIModelLayer.API_SKELETON)),
                    0.5F);
            this.addLayer(new StrayClothingLayer<>(this, pContext.getModelSet()));
        }

        private static final ResourceLocation TEX
                = NoixmodAPI.servant("skeleton/stray");

        public ResourceLocation getTextureLocation(E pEntity)
        {
            return TEX;
        }
    }

    public static class Drunkenness<E extends DrunkennessServant>
    extends IllagerServantRenderer<E>
    {
        public Drunkenness(EntityRendererProvider.Context pContext)
        {
            super(pContext, new IXIllagerModel<>(pContext.bakeLayer(IXIllagerModel.LAYER_LOCATION)), 0.5F);
            this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()) {
                public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, E p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                    if (p_117207_.isAggressive()) {
                        super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
                    }
                }
            });
            this.addLayer(new OHCrossedItemLayer<>(this, pContext.getItemInHandRenderer()) {
                public void render(PoseStack p_116699_, MultiBufferSource p_116700_, int p_116701_, E p_116702_, float p_116703_, float p_116704_, float p_116705_, float p_116706_, float p_116707_, float p_116708_) {
                    if (!p_116702_.isAggressive()) {
                        super.render(p_116699_, p_116700_, p_116701_, p_116702_, p_116703_, p_116704_, p_116705_, p_116706_, p_116707_, p_116708_);
                    }
                }
            });
        }

        private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/illagers/drunkenness_new.png");

        public ResourceLocation getTextureLocation(E t) {
            return LOC;
        }
    }

    public static class Hunter<E extends HunterServant>
    extends IllagerServantRenderer<E>
    {
        public Hunter(EntityRendererProvider.Context p_174304_)
        {
            super(p_174304_, new IXIllagerModel<>(p_174304_.bakeLayer(IXIllagerModel.LAYER_LOCATION)), 0.5F);
            this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()) {
                public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, E pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
                {
                    if (pLivingEntity.isAggressive())
                        super.render(pPoseStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                }
            });
            this.model.getHead().visible = true;
        }

        private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/illagers/hunter.png");

        public ResourceLocation getTextureLocation(E t) {
            return LOC;
        }
    }
}
