
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.DeadIllagerSkullModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.illager.DeadIllagerSkull;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class DeadIllagerSkullRenderer<D extends DeadIllagerSkull>
extends MobRenderer<D, DeadIllagerSkullModel<D>> {
    public DeadIllagerSkullRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new DeadIllagerSkullModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.DEAD_ILLAGER_SKULL)), 0.5F);
        this.addLayer(new CustomHeadLayer<>(this, p_174304_.getModelSet(), p_174304_.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(D d) {
        return new ResourceLocation("noixmodapi:textures/entities/illagers/dead_illager_skull.png");
    }
}
