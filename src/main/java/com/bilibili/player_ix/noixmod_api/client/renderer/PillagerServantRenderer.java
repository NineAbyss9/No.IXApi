
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.IXIllagerModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.servant.IllagerServantRenderer;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.PillagerServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class PillagerServantRenderer<T extends PillagerServant>
extends IllagerServantRenderer<T> {
    public PillagerServantRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new IXIllagerModel<>(p_174304_.bakeLayer(IXIllagerModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return NoixmodAPI.location("textures/entities/illagers/servant/pillager_servant.png");
    }
}
