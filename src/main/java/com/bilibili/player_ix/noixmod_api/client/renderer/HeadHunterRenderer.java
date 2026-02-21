
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.HeadHunterModel;
import com.bilibili.player_ix.noixmod_api.entities.boss.HeadHunter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HeadHunterRenderer<H extends HeadHunter> extends MobRenderer<H, HeadHunterModel<H>> {
    public HeadHunterRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new HeadHunterModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.HEAD_HUNTER)), 0.7F);
    }

    public ResourceLocation getTextureLocation(H h) {
        //if (h.isSecondPhase()) {
            return new ResourceLocation("noixmodapi:textures/entities/monsters/head_hunter_second.png");
        //}
        //return new ResourceLocation("noixmodapi:textures/entities/monsters/head_hunter.png");
    }
}
