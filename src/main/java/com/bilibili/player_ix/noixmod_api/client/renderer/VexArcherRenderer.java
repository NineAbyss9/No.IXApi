
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.VexArcherModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexArcher;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class VexArcherRenderer<
V
extends VexArcher
>
extends MobRenderer<V, VexArcherModel<V>> {
    public VexArcherRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new VexArcherModel<>(p_174304_.bakeLayer(ModelLayers.VEX)), 0.4f);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(V v) {
        return new ResourceLocation("textures/entity/illager/vex.png");
    }
}
