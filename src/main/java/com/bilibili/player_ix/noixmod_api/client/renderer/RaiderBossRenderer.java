
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.monster.illager.RaiderBoss;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.resources.ResourceLocation;

public class RaiderBossRenderer<T extends RaiderBoss>
extends IllagerRenderer<T> {
    public RaiderBossRenderer(EntityRendererProvider.Context p_174182_, IllagerModel<T> p_174183_, float p_174184_) {
        super(p_174182_, p_174183_, p_174184_);
    }

    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("");
    }
}
