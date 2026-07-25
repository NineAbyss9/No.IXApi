
package com.bilibili.player_ix.noixmod_api.client.renderer.projectile;

import com.bilibili.player_ix.noixmod_api.entities.projectile.LavaTrap;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LavaTrapRenderer<T extends LavaTrap> extends EntityRenderer<T> {
    public LavaTrapRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("noixmodapi:textures/entities/entity_null.png");
    }
}
