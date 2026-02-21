
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.ApiZombieModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.horror.Detractor;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class DetractorRenderer<T extends Detractor> extends MobRenderer<T, ApiZombieModel<T>> {
    public DetractorRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new ApiZombieModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.API_ZOMBIE)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("noixmodapi:textures/entities/monsters/horror/detractor.png");
    }
}
