
package com.github.NineAbyss9.ix_api.ix_api.api.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class BaseEntityRenderer<T extends Entity>
extends EntityRenderer<T> {
    public BaseEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("noixmodapi:textures/entities/entity_null.png");
    }
}
