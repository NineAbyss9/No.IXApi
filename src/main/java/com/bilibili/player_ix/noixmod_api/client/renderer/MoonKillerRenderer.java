
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.illager.EIModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.MoonKiller;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MoonKillerRenderer<T extends MoonKiller>
extends MobRenderer<T, EIModel<T>> {
    public MoonKillerRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new EIModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.EI)), 0.5f);
    }

    public ResourceLocation getTextureLocation(T t) {
        return NoixmodAPI.location("textures/entities/special/moon_killer.png");
    }
}
