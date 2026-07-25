
package com.bilibili.player_ix.noixmod_api.client.renderer.projectile;

import com.bilibili.player_ix.noixmod_api.entities.projectile.LittleFireball;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LittleFireballRenderer<T extends LittleFireball>
extends EntityRenderer<T> {
    public LittleFireballRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T t) {
        return new ResourceLocation("noixmodapi:textures/entities/entity_null.png");
    }
}
