
package com.bilibili.player_ix.noixmod_api.client.renderer.nihilist;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.nihilistic.ApostleModel;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.ApostleShadow;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class ApostleShadowRenderer<T extends ApostleShadow>
extends NihilistRenderer<T> {
    public ApostleShadowRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new ApostleModel<>($$0.bakeLayer(NoixmodAPIModelLayer.APOSTLE)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, $$0.getItemInHandRenderer()) {
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_, float p_117208_,
                               float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isCastingSpell() || p_117207_.isAggressive() || p_117207_.isSettingSecondPhase()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
                }
            }
        });
    }

    public ResourceLocation getTextureLocation(T t) {
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            return ApostleRenderer.HORROR;
        }
        return ApostleRenderer.APOSTLE;
    }
}
