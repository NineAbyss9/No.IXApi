
package com.bilibili.player_ix.noixmod_api.client.renderer.layer;

import com.bilibili.player_ix.noixmod_api.client.model.NihilistHumanoidModel;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HumanoidNihilityArmorLayer<T extends Nihilist>
extends EnergySwirlLayer<T, NihilistHumanoidModel<T>> {
    private static final ResourceLocation ARMOR_LOCATION = new ResourceLocation(
            "noixmodapi:textures/entities/nihilistic_mobs/nihility_armor.png");
    private final NihilistHumanoidModel<T> model;

    public HumanoidNihilityArmorLayer(RenderLayerParent<T, NihilistHumanoidModel<T>> p_174554_,
                                      EntityModelSet p_174555_) {
        super(p_174554_);
        this.model = new NihilistHumanoidModel<>(p_174555_.bakeLayer(NihilistHumanoidModel.LAYER_LOCATION));
    }

    protected float xOffset(float p_117702_) {
        return Mth.cos(p_117702_ * 0.02F) * 3.0F;
    }

    protected ResourceLocation getTextureLocation() {
        return ARMOR_LOCATION;
    }

    protected NihilistHumanoidModel<T> model() {
        return this.model;
    }
}
