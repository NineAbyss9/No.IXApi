
package com.bilibili.player_ix.noixmod_api.client.renderer.monster;

import com.bilibili.player_ix.noixmod_api.client.model.PlateauBeastModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.hostile.ice.PlateauBeast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PlateauBeastRenderer<T extends PlateauBeast>
extends MobRenderer<T, PlateauBeastModel<T>> {
    public PlateauBeastRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new PlateauBeastModel<>(p_174304_.bakeLayer(PlateauBeastModel.LAYER_LOCATION)), 0.75f);
    }

    protected void scale(T p_115314_, PoseStack p_115315_, float p_115316_) {
        float f = 1.75f;
        p_115315_.scale(f, f, f);
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/monsters/plateau_beast.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
