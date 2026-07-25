
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APIHumanoidModel;
import com.bilibili.player_ix.noixmod_api.client.model.ApiPoseSkeletonModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.ender.EnderSlime;
import com.bilibili.player_ix.noixmod_api.entities.monster.silent.SilentGhost;
import com.bilibili.player_ix.noixmod_api.entities.monster.undead.BoneSpellcaster;
import com.bilibili.player_ix.noixmod_api.entities.monster.undead.WitherBoneSpellcaster;
import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MonsterRenderer
{
    public static class BoneSpellcasterRenderer<E extends BoneSpellcaster>
    extends MobRenderer<E, ApiPoseSkeletonModel<E>>
    {
        public BoneSpellcasterRenderer(EntityRendererProvider.Context pContext)
        {
            super(pContext, new ApiPoseSkeletonModel<>(pContext.bakeLayer(NoixmodAPIModelLayer.API_SKELETON)),
                    0.5F);
            this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
        }

        private static final ResourceLocation TEX
                = NoixmodAPI.monster("bone_spellcaster");

        public ResourceLocation getTextureLocation(E pEntity)
        {
            return TEX;
        }

        public static class WitherBoneSpellcasterRenderer<E extends WitherBoneSpellcaster>
        extends BoneSpellcasterRenderer<E>
        {
            public WitherBoneSpellcasterRenderer(EntityRendererProvider.Context pContext)
            {
                super(pContext);
            }

            private static final ResourceLocation LOCATION
                    = NoixmodAPI.monster("wither_bone_spellcaster");

            public ResourceLocation getTextureLocation(E pEntity)
            {
                return LOCATION;
            }
        }
    }

    public static class SilentGhostRenderer<E extends SilentGhost>
    extends MobRenderer<E, APIHumanoidModel<E>>
    {
        public SilentGhostRenderer(EntityRendererProvider.Context pContext)
        {
            super(pContext, new APIHumanoidModel<>(pContext.bakeLayer(NoixmodAPIModelLayer.API_HUMANOID)),
                    0.5F);
        }

        private static final ResourceLocation NORMAL =
                NoixmodAPI.monster("sg");
        private static final ResourceLocation HORROR =
                NoixmodAPI.horror("sg");

        public ResourceLocation getTextureLocation(E pEntity)
        {
            return HorrorModeManager.ENABLED ? HORROR : NORMAL;
        }
    }

    public static class EnderSlimeRenderer<E extends EnderSlime>
    extends MobRenderer<E, SlimeModel<E>> {
        public EnderSlimeRenderer(EntityRendererProvider.Context pContext)
        {
            super(pContext, new SlimeModel<E>(pContext.bakeLayer(ModelLayers.SLIME)),
                    0.25F);
            this.addLayer(new SlimeOuterLayer<>(this, pContext.getModelSet()));
        }

        public void render(E pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer,
                           int pPackedLight) {
            this.shadowRadius = 0.25F * (float)pEntity.getSize();
            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }

        protected void scale(E pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
            float f = 0.999F;
            pPoseStack.scale(f, f, f);
            pPoseStack.translate(0.0F, 0.001F, 0.0F);
            float f1 = (float)pLivingEntity.getSize();
            float f2 = Mth.lerp(pPartialTickTime, pLivingEntity.oSquish, pLivingEntity.squish) / (f1 * 0.5F + 1.0F);
            float f3 = 1.0F / (f2 + 1.0F);
            pPoseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
        }

        private static final ResourceLocation TEX = NoixmodAPI.monster("ender_slime");

        public ResourceLocation getTextureLocation(E pEntity)
        {
            return TEX;
        }
    }
}
