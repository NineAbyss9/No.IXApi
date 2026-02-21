
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.VillagerGolemModel;
import com.bilibili.player_ix.noixmod_api.entities.villager.VillagerGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class VillagerGolemRenderer
extends MobRenderer<VillagerGolem, VillagerGolemModel<VillagerGolem>> {
    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

    public VillagerGolemRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new VillagerGolemModel<>(p_174304_.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7f);
    }

    protected void setupRotations(VillagerGolem p_115014_, PoseStack p_115015_, float p_115016_, float p_115017_, float p_115018_) {
        super.setupRotations(p_115014_, p_115015_, p_115016_, p_115017_, p_115018_);
        if (!(p_115014_.walkAnimation.speed() < 0.01F)) {
            float $$5 = 13.0F;
            float $$6 = p_115014_.walkAnimation.position(p_115018_) + 6.0F;
            float $$7 = (Math.abs($$6 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            p_115015_.mulPose(Axis.ZP.rotationDegrees(6.5F * $$7));
        }
    }

    public ResourceLocation getTextureLocation(VillagerGolem villagerGolem) {
        return GOLEM_LOCATION;
    }
}
