
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.NewHeadHunterModel;
import com.bilibili.player_ix.noixmod_api.entities.boss.NewHeadHunter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NewHeadhunterRenderer<E extends NewHeadHunter>
extends MobRenderer<E, NewHeadHunterModel<E>> {
    public NewHeadhunterRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new NewHeadHunterModel<>(pContext.bakeLayer(NewHeadHunterModel.LAYER_LOCATION)),
                0.6F);
    }

    public ResourceLocation getTextureLocation(E pEntity) {
        return new ResourceLocation("noixmodapi:textures/entities/monsters/head_hunter.png");
    }
}
