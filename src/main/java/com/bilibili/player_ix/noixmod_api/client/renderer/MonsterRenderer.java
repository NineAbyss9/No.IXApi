
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.ApiPoseSkeletonModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.undead.BoneSpellcaster;
import com.bilibili.player_ix.noixmod_api.entities.monster.undead.WitherBoneSpellcaster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

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
}
