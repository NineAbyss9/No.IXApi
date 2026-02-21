
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.WaterTrap;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WaterTrapRenderer
extends EntityRenderer<WaterTrap> {
    private static final ResourceLocation WATER_TRAP = new ResourceLocation("noixmodapi:textures/entities/entity.null.png");

    public WaterTrapRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        EvokerFangsModel<WaterTrap> model = new EvokerFangsModel<>($$0.bakeLayer(ModelLayers.EVOKER_FANGS));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull WaterTrap entity) {
		return WATER_TRAP;
	}   
}
