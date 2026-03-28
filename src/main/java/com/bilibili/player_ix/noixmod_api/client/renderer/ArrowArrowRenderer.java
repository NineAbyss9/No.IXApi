
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.ArrowArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;

public class ArrowArrowRenderer<A extends ArrowArrowEntity> extends ArrowRenderer<A> {
    public ArrowArrowRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    public ResourceLocation getTextureLocation(A a) {
        return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
