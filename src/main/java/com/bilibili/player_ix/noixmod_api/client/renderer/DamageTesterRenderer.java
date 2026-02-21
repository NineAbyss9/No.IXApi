
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.github.NineAbyss9.ix_api.api.mobs.DamageTester;
import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.AbstractPlayerModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DamageTesterRenderer<D extends DamageTester>
extends MobRenderer<D, HumanoidModel<D>> {
    public DamageTesterRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new AbstractPlayerModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.ABSTRACT_PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(D d) {
        return new ResourceLocation("noixmodapi:textures/entities/entity_null.png");
    }
}
