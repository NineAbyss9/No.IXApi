
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.ApiZombieModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.layer.DrownedServantLayer;
import com.bilibili.player_ix.noixmod_api.entities.servant.aquatic.DrownedServant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DrownedServantRenderer<T extends DrownedServant>
extends ApiZombieRenderer<T, ApiZombieModel<T>> {
    public DrownedServantRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new ApiZombieModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.API_ZOMBIE)));
        this.addLayer(new DrownedServantLayer<>(this, p_174304_.getModelSet()));
    }

    public ResourceLocation getTextureLocation(T t) {
        return DROWNED;
    }

    protected void setupRotations(T p_114109_, PoseStack p_114110_, float p_114111_, float p_114112_, float p_114113_) {
        super.setupRotations(p_114109_, p_114110_, p_114111_, p_114112_, p_114113_);
        float $$5 = p_114109_.getSwimAmount(p_114113_);
        if ($$5 > 0.0F) {
            float $$6 = -10.0F - p_114109_.getXRot();
            float $$7 = Mth.lerp($$5, 0.0F, $$6);
            p_114110_.rotateAround(Axis.XP.rotationDegrees($$7), 0.0F, p_114109_.getBbHeight()
                    / 2.0F, 0.0F);
        }
    }
}
