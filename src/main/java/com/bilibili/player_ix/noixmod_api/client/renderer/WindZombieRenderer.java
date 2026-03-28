
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.ApiZombieModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.WindZombie;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class WindZombieRenderer<T extends WindZombie>
extends ApiZombieRenderer<T, ApiZombieModel<T>> {
    private static ResourceLocation LOC = NoixmodAPI.location("textures/entities/servants/zombies/wind_zombie.png");
    public WindZombieRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new ApiZombieModel<>(p_174304_.bakeLayer(ApiZombieModel.API_ZOMBIE)));
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
