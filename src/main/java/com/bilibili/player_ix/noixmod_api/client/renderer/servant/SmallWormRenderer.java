
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.client.renderer.WormRenderer;
import com.bilibili.player_ix.noixmod_api.entities.monster.SmallWorm;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SmallWormRenderer<E extends SmallWorm>
extends WormRenderer<E> {
    public SmallWormRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_);
    }

    protected void scale(E pLivingEntity, PoseStack pPoseStack, float pPartialTickTime)
    {
        pPoseStack.scale(0.75f, 0.75f, 0.75f);
    }

    public ResourceLocation getTextureLocation(E e) {
        return WormRenderer.WORM;
    }
}
