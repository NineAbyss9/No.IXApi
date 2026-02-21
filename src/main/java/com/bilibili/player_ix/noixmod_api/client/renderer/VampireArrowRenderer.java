
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.VampireArrow;
import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@OnlyInClient
public class VampireArrowRenderer<E extends VampireArrow>
extends ArrowRenderer<E> {
    public VampireArrowRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull E e) {
        return new ResourceLocation("noixmodapi:textures/entities/projectile/vampire_arrow.png");
    }
}
