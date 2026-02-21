
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CursedNihilityEvokerRenderer <T extends SpellcasterNihilist>
extends NihilistRenderer {
    private static final ResourceLocation LORD = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/cursed_nihility_evoker.png");

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return LORD;
    }

    public CursedNihilityEvokerRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new NihilistIllagerModel($$0.bakeLayer(NihilistIllagerModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new ItemInHandLayer(this, $$0.getItemInHandRenderer()) {

            public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, SpellcasterNihilist $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
                if ($$3.isCastingSpell() || $$3.isAggressive()) {
                    super.render($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8, $$9);
                }
            }

            @Override
            public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int n, LivingEntity livingEntity, float f, float f2, float f3, float f4, float f5, float f6) {
                this.render(poseStack, multiBufferSource, n, (T) livingEntity, f, f2, f3, f4, f5, f6);
            }
        });
        ((NihilistIllagerModel)this.model).getHat().visible = true;
    }

    @Override
    public void render(LivingEntity livingEntity, float f, float f2, PoseStack poseStack, MultiBufferSource multiBufferSource, int n) {
        this.render((T) livingEntity, f, f2, poseStack, multiBufferSource, n);
    }
}
