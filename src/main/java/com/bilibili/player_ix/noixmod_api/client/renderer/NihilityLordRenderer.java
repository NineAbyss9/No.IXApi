
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.NihilistHumanoidModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.layer.HumanoidNihilityArmorLayer;
import com.bilibili.player_ix.noixmod_api.entities.boss.NihilisticLord;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class NihilityLordRenderer <T extends NihilisticLord> extends HumanoidNihilistRenderer<T>
{
    private static final ResourceLocation NO_IX_APOSTLE = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/no_ix_apostle.png");
    private static final ResourceLocation NIHILITY_LORD = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihility_lord.png");

    public NihilityLordRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new NihilistHumanoidModel<>($$0.bakeLayer(NihilistHumanoidModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new HumanoidNihilityArmorLayer<>(this, $$0.getModelSet()));
        this.addLayer(new ItemInHandLayer<>(this, $$0.getItemInHandRenderer()) {
            public void render(PoseStack $$10, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
                if ($$3.isCastingSpell() || $$3.isAggressive()) {
                    super.render($$10, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8, $$9);
                }
            }
        });
        this.model.getHat().visible = true;
    }

    public ResourceLocation getTextureLocation(T lord) {
        if (lord.isPowered()) {
            return NIHILITY_LORD;
        }
        return NO_IX_APOSTLE;
    }
}
