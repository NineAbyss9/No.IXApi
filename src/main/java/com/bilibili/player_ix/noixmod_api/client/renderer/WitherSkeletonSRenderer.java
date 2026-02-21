
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APISkeletonModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.nether.WitherSkeletonServant;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class WitherSkeletonSRenderer<T extends WitherSkeletonServant>
extends MobRenderer<T, APISkeletonModel<T>> {
    public WitherSkeletonSRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new APISkeletonModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.API_SKELETON)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("noixmodapi:textures/entities/servants/skeleton/wss.png");
    }
}
