
package com.bilibili.player_ix.noixmod_api.client.renderer.illager;

import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Bugler;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class TrumpeterRenderer<T extends Bugler>
extends IllagerRenderer<T> {
    public TrumpeterRenderer(EntityRendererProvider.Context p_174182_) {
        super(p_174182_, new IllagerModel<>(p_174182_.bakeLayer(ModelLayers.VINDICATOR)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174182_.getItemInHandRenderer()));
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/illagers/trumpeter.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
