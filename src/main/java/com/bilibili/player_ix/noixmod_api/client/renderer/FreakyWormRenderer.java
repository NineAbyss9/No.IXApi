
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.servant.worm.FreakyWorm;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FreakyWormRenderer<T extends FreakyWorm> extends MobRenderer<T, SilverfishModel<T>> {
    private static final ResourceLocation FREAKY_LOCATION = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/freaky_worm.png");

    public FreakyWormRenderer(EntityRendererProvider.Context p_174378_) {
        super(p_174378_, new SilverfishModel<>(p_174378_.bakeLayer(ModelLayers.SILVERFISH)), 0.5F);
    }

    protected float getFlipDegrees(@NotNull T p_115927_) {
        return 180.0F;
    }

    protected void scale(@NotNull T entity, @NotNull PoseStack poseStack, float f) {
        poseStack.scale(2f, 2f, 2f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T t) {
        return FREAKY_LOCATION;
    }
}
