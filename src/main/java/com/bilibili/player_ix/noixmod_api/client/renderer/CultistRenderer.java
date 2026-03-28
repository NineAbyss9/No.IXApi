
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.nihilist.Cultist;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CultistRenderer<T extends Cultist>
extends NihilistRenderer<T> {
    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/cultist.png");
    public CultistRenderer(EntityRendererProvider.Context context) {
        super(context, new NihilistIllagerModel<>(context.bakeLayer(NoixmodAPIModelLayer.NIHILIST)), 0.5f);
    }

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
