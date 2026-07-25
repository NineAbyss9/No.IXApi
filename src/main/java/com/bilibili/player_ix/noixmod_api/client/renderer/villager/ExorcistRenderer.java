
package com.bilibili.player_ix.noixmod_api.client.renderer.villager;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.VillagerFighterModel;
import com.bilibili.player_ix.noixmod_api.entities.villager.Exorcist;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class ExorcistRenderer<T extends Exorcist>
extends VillagerFighterRenderer<T>
{
    public ExorcistRenderer(EntityRendererProvider.Context context) {
        super(context, new VillagerFighterModel<>(context.bakeLayer(NoixmodAPIModelLayer.VILLAGER_FIGHTER)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()) {
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isAggressive() || p_117207_.isCastingSpell()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
                }
            }
        });
        this.model.getHat().visible = true;
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/villagers/exorcist.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
