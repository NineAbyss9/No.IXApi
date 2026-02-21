
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.WormIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.boss.WormMaster;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class WormMasterRenderer<W extends WormMaster>
extends MobRenderer<W, WormIllagerModel<W>> {
    public WormMasterRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new WormIllagerModel<>(p_174304_.bakeLayer(WormIllagerModel.LAYER_LOCATION)),
                0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()) {
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, W p_117207_, float p_117208_,
                               float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isAggressive() || p_117207_.isCastingSpell()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_,
                            p_117212_, p_117213_);
                }
            }
        });
    }

    public ResourceLocation getTextureLocation(W w) {
        return new ResourceLocation("noixmodapi:textures/entities/worms/worm_master.png");
    }
}
