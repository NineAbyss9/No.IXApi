
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APIHumanoidModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.Golem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class GolemRenderer<G extends Golem>
extends MobRenderer<G, APIHumanoidModel<G>> {
    public GolemRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new APIHumanoidModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer
                .API_HUMANOID)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(G g) {
        return new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/golem.png");
    }
}
