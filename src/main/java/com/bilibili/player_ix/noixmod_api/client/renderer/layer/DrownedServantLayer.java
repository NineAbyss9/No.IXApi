
package com.bilibili.player_ix.noixmod_api.client.renderer.layer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.ApiZombieModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.AbstractZombieServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class DrownedServantLayer<E extends AbstractZombieServant>
extends RenderLayer<E, ApiZombieModel<E>> {
    public static final ResourceLocation DROWNED_OUTER_LAYER_LOCATION;
    private final ApiZombieModel<E> model;
    public DrownedServantLayer(RenderLayerParent<E, ApiZombieModel<E>> p_117346_, @NotNull EntityModelSet set) {
        super(p_117346_);
        this.model = new ApiZombieModel<>(set.bakeLayer(NoixmodAPIModelLayer.API_ZOMBIE));
    }

    @Override
    public void render(@NotNull PoseStack p_116924_, @NotNull MultiBufferSource p_116925_, int p_116926_, @NotNull E p_116927_, float p_116928_, float p_116929_, float p_116930_, float p_116931_, float p_116932_, float p_116933_) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, DROWNED_OUTER_LAYER_LOCATION, p_116924_, p_116925_, p_116926_, p_116927_, p_116928_, p_116929_, p_116931_, p_116932_, p_116933_, p_116930_, 1.0F, 1.0F, 1.0F);
    }

    static {
        DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation("noixmodapi:textures/entities/servants/zombies/drowned_outer_layer.png");
    }
}
