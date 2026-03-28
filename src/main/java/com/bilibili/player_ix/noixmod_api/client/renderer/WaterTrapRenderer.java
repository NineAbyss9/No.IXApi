
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.projectile.WaterTrap;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WaterTrapRenderer
extends EntityRenderer<WaterTrap> {
    private static final ResourceLocation WATER_TRAP = new ResourceLocation("noixmodapi:textures/entities/entity.null.png");

    public WaterTrapRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    public ResourceLocation getTextureLocation(WaterTrap entity) {
		return WATER_TRAP;
	}   
}
