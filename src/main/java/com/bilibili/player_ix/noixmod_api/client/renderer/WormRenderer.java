
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.servant.worm.AbstractWorm;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WormRenderer<T extends AbstractWorm>
extends MobRenderer<T, SilverfishModel<T>> {
    public static final ResourceLocation WORM = new ResourceLocation("noixmodapi:textures/entities/worms/worm.png");
    public WormRenderer(EntityRendererProvider.Context context) {
        super(context, new SilverfishModel<>(context.bakeLayer(ModelLayers.SILVERFISH)), 0.5f);
    }

    public ResourceLocation getTextureLocation(T t) {
        return WORM;
    }
}
