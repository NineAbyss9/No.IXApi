
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.LurkerModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.Lurker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LurkerRenderer<T extends Lurker>
extends MobRenderer<T, LurkerModel<T>> {
    private static final ResourceLocation LORD = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/lurker.png");

    public LurkerRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new LurkerModel<>($$0.bakeLayer(NoixmodAPIModelLayer.LURKER)), 0.5f);
    }

    @Override
    protected void scale(@NotNull T p_115314_, @NotNull PoseStack p_115315_, float p_115316_) {
        p_115315_.scale(2f, 2f, 2f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T lurker) {
        return LORD;
    }
}
