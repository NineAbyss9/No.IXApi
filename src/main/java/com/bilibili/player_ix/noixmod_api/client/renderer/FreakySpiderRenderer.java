
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APISpiderModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.layer.AbstractSpiderEyesLayer;
import com.bilibili.player_ix.noixmod_api.entities.servant.FreakySpider;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FreakySpiderRenderer<T extends FreakySpider> extends MobRenderer<T, APISpiderModel<T>> {
    private static final ResourceLocation SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/spider.png");

    public FreakySpiderRenderer(EntityRendererProvider.Context p_174401_) {
        this(p_174401_, NoixmodAPIModelLayer.API_SPIDER);
    }

    public FreakySpiderRenderer(EntityRendererProvider.Context p_174403_, ModelLayerLocation p_174404_) {
        super(p_174403_, new APISpiderModel<>(p_174403_.bakeLayer(p_174404_)), 0.8F);
        this.addLayer(new AbstractSpiderEyesLayer<>(this));
    }

    protected float getFlipDegrees(@NotNull T p_116011_) {
        return 180.0F;
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull T p_116009_) {
        return SPIDER_LOCATION;
    }
}
