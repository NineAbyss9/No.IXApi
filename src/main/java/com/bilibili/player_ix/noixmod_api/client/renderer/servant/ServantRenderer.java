
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APISkeletonModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.SkeletonServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
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
}
