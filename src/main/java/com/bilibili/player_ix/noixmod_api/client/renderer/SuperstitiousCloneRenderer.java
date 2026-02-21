
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.nihilist.SuperstitiousClone;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SuperstitiousCloneRenderer<T extends SuperstitiousClone> extends NihilistRenderer<T> {
    public SuperstitiousCloneRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new NihilistIllagerModel<>($$0.bakeLayer(NoixmodAPIModelLayer.NIHILIST)), 0.5F);
    }

    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/superstitious_clone.png");
    }
}
