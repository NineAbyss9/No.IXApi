
package com.bilibili.player_ix.noixmod_api.client.renderer.villager;

import com.bilibili.player_ix.noixmod_api.client.model.VillagerFighterModel;
import com.bilibili.player_ix.noixmod_api.entities.villager.VillagerFighter;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;

public abstract class VillagerFighterRenderer<T extends VillagerFighter>
extends MobRenderer<T, VillagerFighterModel<T>> {
    protected VillagerFighterRenderer(EntityRendererProvider.Context context, VillagerFighterModel<T> fighterModel, float scale) {
        super(context, fighterModel, scale);
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    }

    protected void scale(T $$0, PoseStack $$1, float $$2) {
        float $$3 = 0.9375f;
        if ($$0.isBaby()) {
            $$1.scale(0.5f, 0.5f, 0.5f);
        } else {
            $$1.scale($$3, $$3, $$3);
        }
    }
}
