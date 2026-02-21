
package com.bilibili.player_ix.noixmod_api.client.model;

import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

@OnlyInClient
public class ThrownAxeModel<T extends Entity> extends EntityModel<T> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/item/iron_axe.png");
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("noixmodapi", "thrown"), "main");
    private final ModelPart root;

    public ThrownAxeModel(ModelPart p_171016_) {
        this.root = p_171016_.getChild("root");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 16.0f));
        return LayerDefinition.create($$0, 16, 16);
    }

    @Override
    public void setupAnim(Entity entity, float v, float v1, float v2, float v3, float v4) {
        this.root.yRot = v2 * 25.0f * ((float)Math.PI / 180);
    }

    public void renderToBuffer(PoseStack p_103919_, VertexConsumer p_103920_, int p_103921_, int p_103922_, float p_103923_, float p_103924_, float p_103925_, float p_103926_) {
        this.root.render(p_103919_, p_103920_, p_103921_, p_103922_, p_103923_, p_103924_, p_103925_, p_103926_);
    }
}
