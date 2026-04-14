
package com.bilibili.player_ix.noixmod_api.client.renderer.nihilist;

import com.bilibili.player_ix.noixmod_api.client.model.nihilistic.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

public abstract class NihilistRenderer<T extends Nihilist>
extends MobRenderer<T, NihilistIllagerModel<T>> {
    protected NihilistRenderer(EntityRendererProvider.Context context, NihilistIllagerModel<T> m, float scale) {
        super(context, m, scale);
    }

    protected void scale(T $$0, PoseStack $$1, float $$2) {
        $$2 = 0.9375f;
        $$1.scale($$2, $$2, $$2);
    }
}
