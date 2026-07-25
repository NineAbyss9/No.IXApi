
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.ApiZombieModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.ApiZombieRenderer;
import com.bilibili.player_ix.noixmod_api.entities.servant.nether.LavaZombieServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LavaZombieRenderer<T extends LavaZombieServant>
extends ApiZombieRenderer<T, ApiZombieModel<T>>
{
    public LavaZombieRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new ApiZombieModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.API_ZOMBIE)));
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T t) {
        return new ResourceLocation("noixmodapi:textures/entities/servants/zombies/lava_zombie.png");
    }
}
