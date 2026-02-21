
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.BiologistModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Biologist;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BiologistRenderer<T extends Biologist>
        extends MobRenderer<T, BiologistModel<T>> {
    private static final ResourceLocation BIOLOGIST = new ResourceLocation("noixmodapi:textures/entities/illagers/biologist.png");

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return BIOLOGIST;
    }

    public BiologistRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new BiologistModel<>($$0.bakeLayer(BiologistModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    protected void scale(@NotNull T p_115314_, @NotNull PoseStack p_115315_, float p_115316_) {
        float f = 0.975f;
        p_115315_.scale(f, f, f);
    }
}
