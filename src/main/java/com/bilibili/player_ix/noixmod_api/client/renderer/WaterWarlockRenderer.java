
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.illager.WaterWarlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class WaterWarlockRenderer<T extends WaterWarlock>
extends IllagerRenderer<T>
{
    private static final ResourceLocation LOCATION = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/cursed_nihility_evoker.png");

    public WaterWarlockRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new IllagerModel<>($$0.bakeLayer(NihilistIllagerModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, $$0.getItemInHandRenderer()) {
            public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
                if ($$3.isCastingSpell() || $$3.isAggressive()) {
                    super.render($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8, $$9);
                }
            }
        });
        this.model.getHat().visible = true;
    }

    public ResourceLocation getTextureLocation(T entity) {
        return LOCATION;
    }
}
