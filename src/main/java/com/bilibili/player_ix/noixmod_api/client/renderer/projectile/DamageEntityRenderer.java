
package com.bilibili.player_ix.noixmod_api.client.renderer.projectile;

import com.bilibili.player_ix.noixmod_api.entities.projectile.DamageEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DamageEntityRenderer<D extends DamageEntity> extends EntityRenderer<D> {
    public DamageEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    public ResourceLocation getTextureLocation(D d) {
        return new ResourceLocation("noixmodapi:textures/entities/entity_null.png");
    }
}
