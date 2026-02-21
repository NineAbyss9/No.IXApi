
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.NihilisticArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class NihilisticArrowRenderer
extends ArrowRenderer<NihilisticArrow> {
    public NihilisticArrowRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull NihilisticArrow nihilisticArrow) {
        return new ResourceLocation("noixmodapi:textures/entities/projectile/nihilistic_arrow.png");
    }
}
