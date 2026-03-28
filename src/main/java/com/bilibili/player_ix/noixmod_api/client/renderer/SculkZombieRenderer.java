
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.ApiZombieModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.sculk.SculkZombie;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SculkZombieRenderer<E extends SculkZombie>
extends ApiZombieRenderer<E, ApiZombieModel<E>> {
    public SculkZombieRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new ApiZombieModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.API_ZOMBIE)));
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    private static ResourceLocation LOC = NoixmodAPI.location("textures/entities/servants/sculk/zombie.png");

    public ResourceLocation getTextureLocation(E pEntity) {
        return LOC;
    }
}
