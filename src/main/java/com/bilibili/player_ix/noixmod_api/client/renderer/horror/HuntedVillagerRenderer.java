
package com.bilibili.player_ix.noixmod_api.client.renderer.horror;

import com.bilibili.player_ix.noixmod_api.client.model.illager.IXIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.horror.HuntedVillager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class HuntedVillagerRenderer<E extends HuntedVillager>
extends MobRenderer<E, IXIllagerModel<E>> {
    public HuntedVillagerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new IXIllagerModel<>(pContext.bakeLayer(IXIllagerModel.LAYER_LOCATION)), 0.1f);
        this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
    }

    public static final ResourceLocation TEXTURE = new ResourceLocation("noixmodapi:textures/entities/monsters/horror/hunted_villager.png");

    public ResourceLocation getTextureLocation(E pEntity) {
        return TEXTURE;
    }
}
