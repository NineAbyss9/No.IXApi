
package com.bilibili.player_ix.noixmod_api.client.renderer.illager;

import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Mourner;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.resources.ResourceLocation;

public class MournerRenderer<T extends Mourner> extends IllagerRenderer<T> {
    public MournerRenderer(EntityRendererProvider.Context p_174182_) {
        super(p_174182_, new IllagerModel<>(p_174182_.bakeLayer(ModelLayers.EVOKER)), 0.5F);
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/illagers/mourner.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
