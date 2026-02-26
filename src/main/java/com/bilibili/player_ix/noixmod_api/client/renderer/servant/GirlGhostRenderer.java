
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.GirlGhostModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.GirlGhost;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class GirlGhostRenderer<T extends GirlGhost> extends MobRenderer<T, GirlGhostModel<T>> {
    public GirlGhostRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new GirlGhostModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.GIRL_GHOST)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    @Override
    protected void scale(T p_115314_, PoseStack p_115315_, float p_115316_) {
        float var = 0.9F;
        p_115315_.scale(var, var, var);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation("noixmodapi:textures/entities/monsters/girl_ghost.png");
    }
}
