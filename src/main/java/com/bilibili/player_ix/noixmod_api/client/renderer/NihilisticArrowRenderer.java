
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.NihilisticArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NihilisticArrowRenderer
extends ArrowRenderer<NihilisticArrow> {
    public NihilisticArrowRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/projectile/nihilistic_arrow.png");

    public ResourceLocation getTextureLocation(NihilisticArrow nihilisticArrow) {
        return LOC;
    }
}
