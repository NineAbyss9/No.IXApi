
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.github.NineAbyss9.ix_api.api.renderer.BaseEntityRenderer;
import com.bilibili.player_ix.noixmod_api.entities.projectile.WindEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class WindEntityRenderer<T extends WindEntity>
extends BaseEntityRenderer<T> {
    public WindEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
}
