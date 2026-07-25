
package com.bilibili.player_ix.noixmod_api.client.renderer.illager;

import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Armorer;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.resources.ResourceLocation;

public class ArmorerRenderer<T extends Armorer> extends IllagerRenderer<T> {
    public ArmorerRenderer(EntityRendererProvider.Context p_174182_) {
        super(p_174182_, new IllagerModel<>(p_174182_.bakeLayer(ModelLayers.EVOKER)), 0.5F);
        this.addLayer(new CrossedArmsItemLayer<>(this, p_174182_.getItemInHandRenderer()));
        this.model.getHat().visible = true;
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/illagers/armorer.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
