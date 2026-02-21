
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APISpiderModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.layer.AbstractSpiderEyesLayer;
import com.bilibili.player_ix.noixmod_api.client.renderer.layer.MushroomSpiderMushroomLayer;
import com.bilibili.player_ix.noixmod_api.entities.servant.animal.MushroomSpider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MushroomSpiderRenderer<T extends MushroomSpider>
extends MobRenderer<T, APISpiderModel<T>> {
    public MushroomSpiderRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new APISpiderModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.API_SPIDER)), 0.8F);
        this.addLayer(new AbstractSpiderEyesLayer<>(this));
        this.addLayer(new MushroomSpiderMushroomLayer<>(this, p_174304_.getBlockRenderDispatcher()));
    }

    @Override
    protected float getFlipDegrees(T p_115337_) {
        return 180F;
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        if (t.getSpiderColor() == MushroomSpider.Color.BROWN) {
            return new ResourceLocation("noixmodapi:textures/entities/servants/spiders/mushroom_spider_brown.png");
        }
        if (t.getSpiderColor() == MushroomSpider.Color.RED) {
            return new ResourceLocation("noixmodapi:textures/entities/servants/spiders/mushroom_spider_red.png");
        }
        return new ResourceLocation("noixmodapi:textures/entities/servants/spiders/mushroom_spider_both.png");
    }
}
