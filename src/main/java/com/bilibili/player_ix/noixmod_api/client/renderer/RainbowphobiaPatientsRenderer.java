
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.RainbowphobiaPatientsModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.RainbowphobiaPatients;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RainbowphobiaPatientsRenderer<T extends RainbowphobiaPatients> extends HumanoidMobRenderer<T, RainbowphobiaPatientsModel<T>> {
    public RainbowphobiaPatientsRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new RainbowphobiaPatientsModel<>(p_174304_.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("noixmod:textures/entities/nihilistic_mobs/rp");
    }
}
