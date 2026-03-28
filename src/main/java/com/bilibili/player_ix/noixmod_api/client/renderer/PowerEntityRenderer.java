
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.PowerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PowerEntityRenderer<T extends PowerEntity>
extends EntityRenderer<T> {
    public PowerEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/entity_null.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
