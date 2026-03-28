
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.nihilist.Superstitious;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SuperstitiousRenderer<T extends Superstitious> extends NihilistRenderer<T> {
    public SuperstitiousRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new NihilistIllagerModel<>($$0.bakeLayer(NoixmodAPIModelLayer.NIHILIST)), 0.5F);
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/superstitious.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
