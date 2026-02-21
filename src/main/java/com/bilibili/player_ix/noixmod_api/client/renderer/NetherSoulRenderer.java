
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.NetherSoulModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.nether.NetherSoul;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class NetherSoulRenderer<N extends NetherSoul> extends MobRenderer<N, NetherSoulModel<N>> {
    public NetherSoulRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new NetherSoulModel<>(p_174304_.bakeLayer(NetherSoulModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    protected int getBlockLightLevel(N p_114496_, BlockPos p_114497_) {
        return 10;
    }

    @Override
    public ResourceLocation getTextureLocation(N n) {
        return new ResourceLocation("noixmodapi:textures/entities/monsters/nether_soul.png");
    }
}
