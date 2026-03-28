
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.PriestModel;
import com.bilibili.player_ix.noixmod_api.entities.boss.priest.Priest;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PriestRenderer<T extends Priest>
extends MobRenderer<T, PriestModel<T>> {
    public PriestRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PriestModel<>(pContext.bakeLayer(PriestModel.LAYER_LOCATION)), 0.6F);
    }

    private static ResourceLocation LOC = NoixmodAPI.location("textures/entities/nihilistic_mobs/priest.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
