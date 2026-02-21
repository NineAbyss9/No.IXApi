
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.servant.worm.AquaticWorm;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AquaticWormRenderer<T extends AquaticWorm>
extends MobRenderer<T, SilverfishModel<T>> {
    public AquaticWormRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new SilverfishModel<>(p_174304_.bakeLayer(ModelLayers.SILVERFISH)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T t) {
        return new ResourceLocation("noixmodapi:textures/entities/worms/aquatic_worm.png");
    }
}
