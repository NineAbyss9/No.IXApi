
package com.bilibili.player_ix.noixmod_api.client.renderer.layer;

import com.bilibili.player_ix.noixmod_api.client.model.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@OnlyInClient
public class ApostleEyesLayer<T extends Apostle>
extends EyesLayer<T, NihilistIllagerModel<T>> {
    public ApostleEyesLayer(RenderLayerParent<T, NihilistIllagerModel<T>> p_116981_) {
        super(p_116981_);
    }

    @NotNull
    @Override
    public RenderType renderType() {
        return RenderType.eyes(new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/apostle_eyes.png"));
    }

    @Override
    public void render(@NotNull PoseStack p_116983_, @NotNull MultiBufferSource p_116984_, int p_116985_, @NotNull T p_116986_,
                       float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_, float p_116992_) {
        if (!NoixmodAPIMainConfig.HorrorMode.get() && !p_116986_.isSecondPhase()) {
            super.render(p_116983_, p_116984_, p_116985_, p_116986_, p_116987_, p_116988_, p_116989_, p_116990_, p_116991_, p_116992_);
        }
    }
}
