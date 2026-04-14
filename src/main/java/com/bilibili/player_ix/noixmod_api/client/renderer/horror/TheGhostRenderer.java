
package com.bilibili.player_ix.noixmod_api.client.renderer.horror;

import com.bilibili.player_ix.noixmod_api.client.model.horror.TheGhostModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.horror.TheGhost;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TheGhostRenderer<E extends TheGhost>
extends MobRenderer<E, TheGhostModel<E>> {
    public TheGhostRenderer(EntityRendererProvider.Context context) {
        super(context, new TheGhostModel<>(context.bakeLayer(TheGhostModel.LAYER_LOCATION)), 0.5f);
    }

    public static final ResourceLocation TEXTURE = new ResourceLocation("noixmodapi:textures/entities/monsters/horror/the_ghost.png");

    public ResourceLocation getTextureLocation(E pEntity) {
        return TEXTURE;
    }
}
