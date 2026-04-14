
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.client.model.SuicideZombieModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.SuicideZombie;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class SuicideZombieRenderer<T extends SuicideZombie>
extends MobRenderer<T, SuicideZombieModel<T>> {
    public SuicideZombieRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new SuicideZombieModel<>(p_174304_.bakeLayer(SuicideZombieModel.LAYER_LOCATION)),
                0.5f);
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/servants/suicide_zombie.png");

    public ResourceLocation getTextureLocation(@Nonnull T t) {
        return LOC;
    }
}
