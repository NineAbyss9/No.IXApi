
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.monster.SmallWorm;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SmallWormRenderer<E extends SmallWorm>
extends MobRenderer<E, SilverfishModel<E>> {
    public SmallWormRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new SilverfishModel<>(p_174304_.bakeLayer(ModelLayers.SILVERFISH)), 0.5f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull E e) {
        return WormRenderer.WORM;
    }
}
