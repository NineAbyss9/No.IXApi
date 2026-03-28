
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.ApiZombieModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticZombie;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NihilityZombieRenderer
extends ApiZombieRenderer<NihilisticZombie, ApiZombieModel<NihilisticZombie>> {
    private static final ResourceLocation LORD = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihilistic_zombie.png");

    public ResourceLocation getTextureLocation(NihilisticZombie entity) {
        return LORD;
    }

    public NihilityZombieRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new ApiZombieModel<>($$0.bakeLayer(NoixmodAPIModelLayer.API_ZOMBIE)));
    }
}
