
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.model.ApiZombieModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.AbstractZombieServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public abstract class ApiZombieRenderer<
T
extends AbstractZombieServant,
M
extends ApiZombieModel<T>>
extends HumanoidMobRenderer<T, M> {
    public static final ResourceLocation ZOMBIE;
    public static final ResourceLocation DROWNED;
    public ApiZombieRenderer(EntityRendererProvider.Context p_174304_, M p_174305_) {
        super(p_174304_, p_174305_, 0.5f);
        /*
        this.addLayer(new HumanoidArmorLayer<>(this, p_174305_, p_174305_, p_174304_.getModelManager()));
        */
    }

    protected void scale(T p_115314_, PoseStack p_115315_, float p_115316_) {
        float a = 0.7f;
        float b = 0.975f;
        float c = p_115314_.isBaby() ? a : b;
        p_115315_.scale(c, c, c);
    }

    static {
        DROWNED = new ResourceLocation("noixmodapi:textures/entities/servants/zombies/drowned.png");
        ZOMBIE = new ResourceLocation("noixmodapi:textures/entities/servants/zombies/zombie.png");
    }
}
