
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.nihilist.NihilisticEvoker;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class NihilisticEvokerRenderer<T extends NihilisticEvoker>
extends NihilistRenderer<T> {
    public NihilisticEvokerRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new NihilistIllagerModel<>($$0.bakeLayer(NoixmodAPIModelLayer.NIHILIST)), 0.5f);
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihilistic_evoker.png");

    public ResourceLocation getTextureLocation(T t) {
        return LOC;
    }
}
