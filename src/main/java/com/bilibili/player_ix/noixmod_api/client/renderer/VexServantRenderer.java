
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.VexServantModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexServant;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class VexServantRenderer<T extends VexServant>
extends MobRenderer<T, VexServantModel<T>> {
    private static final ResourceLocation VEX_LOCATION = new ResourceLocation("textures/entity/illager/vex.png");
    private static final ResourceLocation VEX_CHARGING_LOCATION = new ResourceLocation("textures/entity/illager/vex_charging.png");
    public VexServantRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new VexServantModel<>(p_174304_.bakeLayer(ModelLayers.VEX)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    protected int getBlockLightLevel(T p_114496_, BlockPos p_114497_) {
        return 15;
    }

    public ResourceLocation getTextureLocation(T t) {
        return t.getFlag() == 1 ? VEX_CHARGING_LOCATION : VEX_LOCATION;
    }
}
