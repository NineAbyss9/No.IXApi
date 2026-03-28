
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticArrowRain;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class NihilisticArrowRainRenderer<T extends NihilisticArrowRain>
extends EntityRenderer<T> {
    public NihilisticArrowRainRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("noixmodapi:textures/entities/entity_null.png");
    }
}
