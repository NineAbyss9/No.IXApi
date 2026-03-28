
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.DanDingModel;
import com.bilibili.player_ix.noixmod_api.entities.npc.DanDing.DanDa;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class DanDaRenderer<D extends DanDa> extends MobRenderer<D, DanDingModel<D>> {
    public DanDaRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new DanDingModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.DAN_DING)),
                0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    private static ResourceLocation LOC =new ResourceLocation("noixmodapi:textures/entities/special/dd.png");

    public ResourceLocation getTextureLocation(D d) {
        return LOC;
    }
}
