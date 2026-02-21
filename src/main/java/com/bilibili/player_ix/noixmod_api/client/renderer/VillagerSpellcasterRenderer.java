
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.VillagerFighterModel;
import com.bilibili.player_ix.noixmod_api.entities.villager.VillagerFighter;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class VillagerSpellcasterRenderer <T extends VillagerFighter>
extends VillagerFighterRenderer<T> {
    private static final ResourceLocation VILLAGER_SPELLCASTER = new ResourceLocation("noixmodapi:textures/entities/villagers/villager_spellcaster.png");

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return VILLAGER_SPELLCASTER;
    }

    public VillagerSpellcasterRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new VillagerFighterModel<>($$0.bakeLayer(NoixmodAPIModelLayer.VILLAGER_FIGHTER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, $$0.getItemInHandRenderer()){

            public void render(@NotNull PoseStack $$0, @NotNull MultiBufferSource $$1, int $$2, @NotNull T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
                if ($$3.isCastingSpell() || $$3.isAggressive()) {
                    super.render($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8, $$9);
                }
            }
        });
    }
}
