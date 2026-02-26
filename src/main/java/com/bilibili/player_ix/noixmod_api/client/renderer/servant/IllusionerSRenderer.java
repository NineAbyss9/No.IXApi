
package com.bilibili.player_ix.noixmod_api.client.renderer.servant;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.IXIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.illager.IllusionerServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class IllusionerSRenderer<E extends IllusionerServant>
extends IllagerServantRenderer<E> {
    public IllusionerSRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new IXIllagerModel<>(p_174304_.bakeLayer(IXIllagerModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()) {
            public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, E pLivingEntity, float pLimbSwing,
                               float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
                if (pLivingEntity.isAggressive() || pLivingEntity.isCastingSpell())
                    super.render(pPoseStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks,
                            pAgeInTicks, pNetHeadYaw, pHeadPitch);
            }
        });
    }

    public ResourceLocation getTextureLocation(E pEntity) {
        return NoixmodAPI.location("textures/entities/illagers/servant/illusioner.png");
    }
}
