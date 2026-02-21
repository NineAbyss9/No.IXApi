
package com.bilibili.player_ix.noixmod_api.client.renderer.layer;

import com.bilibili.player_ix.noixmod_api.client.model.APISpiderModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.animal.AbstractSpiderServant;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AbstractSpiderEyesLayer<T extends AbstractSpiderServant, M extends APISpiderModel<T>>
extends EyesLayer<T, M> {
    private final ResourceLocation location;
    public AbstractSpiderEyesLayer(RenderLayerParent<T, M> p_116981_, ResourceLocation layer) {
        super(p_116981_);
        this.location = layer;
    }

    public AbstractSpiderEyesLayer(RenderLayerParent<T, M> parent) {
        this(parent, new ResourceLocation("minecraft:textures/entity/spider_eyes.png"));
    }

    @NotNull
    @Override
    public RenderType renderType() {
        return RenderType.eyes(location);
    }
}
