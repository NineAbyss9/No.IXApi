
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Biologist;
import com.bilibili.player_ix.noixmod_api.entities.servant.FreakySkeleton;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FreakySkeletonRenderer<T extends FreakySkeleton> extends HumanoidMobRenderer<T, HumanoidModel<T>> {

    public FreakySkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.SKELETON)), 0.5f);
    }

    @Override
    protected boolean isShaking(T p_115304_) {
        if (p_115304_.getOwner() instanceof Biologist biologist && biologist.isAngry()) {
            return true;
        }
        return false;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/freaky_skeleton.png");
    }
}
