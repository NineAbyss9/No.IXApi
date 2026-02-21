
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.VillagerFighterModel;
import com.bilibili.player_ix.noixmod_api.entities.villager.VillagerEvoker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class VillagerEvokerRenderer<T extends VillagerEvoker>
extends VillagerFighterRenderer<T> {
    private static final ResourceLocation VILLAGER_EVOKER = new ResourceLocation("noixmodapi:textures/entities/villagers/evoker.png");

    public VillagerEvokerRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new VillagerFighterModel<>($$0.bakeLayer(ModelLayers.ILLUSIONER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, $$0.getItemInHandRenderer()) {

            public void render(@NotNull PoseStack $$0, @NotNull MultiBufferSource $$1, int $$2, @NotNull T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
                if ($$3.isCastingSpell() || $$3.isAggressive()) {
                    super.render($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8, $$9);
                }
            }
        });
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@Nonnull T entity) {
        return VILLAGER_EVOKER;
    }
}
