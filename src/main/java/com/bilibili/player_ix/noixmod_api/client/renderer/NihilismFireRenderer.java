
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticFire;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class NihilismFireRenderer<T extends NihilisticFire>
extends EntityRenderer<T> {
    public NihilismFireRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull T t) {
        return new ResourceLocation("noixmodapi:textures/entities/entity_null.png");
    }
}
