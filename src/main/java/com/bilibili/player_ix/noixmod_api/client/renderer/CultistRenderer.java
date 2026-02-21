
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.nihilist.Cultist;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllusionerRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CultistRenderer<T extends Cultist>
extends NihilistRenderer<T> {
    public CultistRenderer(EntityRendererProvider.Context context) {
        super(context, new NihilistIllagerModel<>(context.bakeLayer(NoixmodAPIModelLayer.NIHILIST)), 0.5f);
        IllusionerRenderer renderer;
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/cultist.png");
    }
}
