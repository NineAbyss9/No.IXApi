
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.NihilistHumanoidModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

public abstract class HumanoidNihilistRenderer<T extends Nihilist>
extends MobRenderer<T, NihilistHumanoidModel<T>> {
    protected HumanoidNihilistRenderer(EntityRendererProvider.Context $$0, NihilistHumanoidModel<T> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    protected void scale(T $$0, PoseStack $$1, float $$2) {
        $$1.scale(0.9375f, 0.9375f, 0.9375f);
    }
}
