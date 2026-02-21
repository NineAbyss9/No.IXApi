
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.entities.servant.AbstractStatue;
import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@OnlyInClient
public class StatueModel<T extends AbstractStatue>
extends HierarchicalModel<T> {
    public final ModelPart statue;

    public StatueModel(ModelPart part) {
        this.statue = part.getChild("statue");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("statue", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0f, -50.0f, -7.0f, 14.0f, 50.0f, 14.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 24.0f, 0.0f));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T $$0, float swing, float swingAmount, float ageInTicks, float headYaw, float headPitch) {
    }

    @Override
    public ModelPart root() {
        return this.statue;
    }
}
