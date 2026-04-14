
package com.bilibili.player_ix.noixmod_api.client.renderer.horror;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.APIHumanoidModel;
import com.bilibili.player_ix.noixmod_api.client.model.nihilistic.ApostleModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.nihilist.ApostleRenderer;
import com.bilibili.player_ix.noixmod_api.entities.boss.ChasingApostle;
import com.bilibili.player_ix.noixmod_api.entities.monster.horror.ScaringHuman;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class HorrorRenderer {
    public static class ScaringHumanRenderer<E extends ScaringHuman> extends MobRenderer<E, APIHumanoidModel<E>> {
        public ScaringHumanRenderer(EntityRendererProvider.Context pContext) {
            super(pContext, new APIHumanoidModel<>(pContext.bakeLayer(NoixmodAPIModelLayer.API_HUMANOID)),
                    0.1F);
            this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
        }

        public static final ResourceLocation TEX = NoixmodAPI.horror("the_human");

        public ResourceLocation getTextureLocation(E pEntity) {
            return TEX;
        }
    }

    public static class ChasingApostleRenderer<E extends ChasingApostle> extends MobRenderer<E, ApostleModel<E>> {
        public ChasingApostleRenderer(EntityRendererProvider.Context pContext) {
            super(pContext, new ApostleModel<>(pContext.bakeLayer(NoixmodAPIModelLayer.APOSTLE)), 0.5F);
        }

        public ResourceLocation getTextureLocation(E pEntity) {
            return ApostleRenderer.HORROR;
        }
    }
}
