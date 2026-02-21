
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.ApiAllayModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.Healing;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class HealingRenderer<T extends Healing>
extends MobRenderer<T, ApiAllayModel<T>> {
    public HealingRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new ApiAllayModel<>(p_174304_.bakeLayer(ModelLayers.ALLAY)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    protected int getBlockLightLevel(T p_114496_, BlockPos p_114497_) {
        return 15;
    }

    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("textures/entity/allay/allay.png");
    }
}
