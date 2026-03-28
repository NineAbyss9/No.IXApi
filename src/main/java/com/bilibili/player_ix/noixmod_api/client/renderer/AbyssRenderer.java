
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.AbyssModel;
import com.bilibili.player_ix.noixmod_api.entities.boss.abyss.Abyss;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AbyssRenderer<T extends Abyss>
extends MobRenderer<T, AbyssModel<T>> {
    public static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/abyss.png");
    public AbyssRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new AbyssModel<>(p_174304_.bakeLayer(AbyssModel.LAYER_LOCATION)),
                0.5f);
    }

    public ResourceLocation getTextureLocation(T abyss) {
        return LOC;
    }
}
