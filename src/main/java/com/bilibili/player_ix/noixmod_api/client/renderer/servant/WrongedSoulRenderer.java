
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.client.model.WrongedSoulModel;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.servant.WrongedSoul;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WrongedSoulRenderer<T extends WrongedSoul>
extends MobRenderer<T, WrongedSoulModel<T>> {
    public WrongedSoulRenderer(EntityRendererProvider.Context context) {
        super(context, new WrongedSoulModel<>(context.bakeLayer(WrongedSoulModel.LAYER_LOCATION)), 0.5f);
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull T t) {
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            return new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/wronged_soul_horror.png");
        }
        return new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/wronged_soul.png");
    }

    protected void scale(@NotNull T e, @NotNull PoseStack x, float t) {
        float f = 0.9375f;
        x.scale(f, f, f);
    }

    protected int getBlockLightLevel(@NotNull T p_114496_, @NotNull BlockPos p_114497_) {
        if (p_114496_.isAggressive()) {
            return 15;
        }
        return super.getBlockLightLevel(p_114496_, p_114497_);
    }
}
