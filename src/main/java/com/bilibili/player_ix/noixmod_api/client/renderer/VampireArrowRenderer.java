
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.VampireArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class VampireArrowRenderer<E extends VampireArrow>
extends ArrowRenderer<E> {
    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/projectile/vampire_arrow.png");
    public VampireArrowRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    public ResourceLocation getTextureLocation(E e) {
        return LOC;
    }
}
