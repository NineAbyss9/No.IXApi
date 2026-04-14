
package com.bilibili.player_ix.noixmod_api.client.renderer.nihilist;

import com.bilibili.player_ix.noixmod_api.client.model.nihilistic.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.nihilist.Fighter;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class FighterRenderer<T extends Fighter>
extends NihilistRenderer<T> {
    public FighterRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new NihilistIllagerModel<>($$0.bakeLayer(NihilistIllagerModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, $$0.getItemInHandRenderer()) {
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isAggressive()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
                }
            }
        });
        this.model.getHat().visible = true;
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/fighter.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
