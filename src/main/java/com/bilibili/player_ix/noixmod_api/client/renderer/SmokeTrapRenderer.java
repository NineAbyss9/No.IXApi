
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.SmokeTrap;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SmokeTrapRenderer<T extends SmokeTrap>  extends EntityRenderer<T> {
    private static final ResourceLocation TRAP = new ResourceLocation(
            "noixmodapi:textures/entities/entity.null.png");

    public SmokeTrapRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    public ResourceLocation getTextureLocation(T entity) {
        return TRAP;
    }
}
