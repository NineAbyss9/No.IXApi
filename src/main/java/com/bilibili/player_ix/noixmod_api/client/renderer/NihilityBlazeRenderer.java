
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticBlaze;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class NihilityBlazeRenderer
extends MobRenderer<NihilisticBlaze, BlazeModel<NihilisticBlaze>> {
    private static final ResourceLocation BLAZE_LOCATION = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihility_blaze.png");

    public NihilityBlazeRenderer(EntityRendererProvider.Context p_173933_) {
        super(p_173933_, new BlazeModel(p_173933_.bakeLayer(ModelLayers.BLAZE)), 0.5F);
    }

    protected int getBlockLightLevel(NihilisticBlaze p_113910_, BlockPos p_113911_) {
        return 15;
    }

    public ResourceLocation getTextureLocation(NihilisticBlaze blaze) {
        return BLAZE_LOCATION;
    }
}
