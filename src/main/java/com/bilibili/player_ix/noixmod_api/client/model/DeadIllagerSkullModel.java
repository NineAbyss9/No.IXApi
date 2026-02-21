
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.entities.monster.illager.DeadIllagerSkull;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class DeadIllagerSkullModel<T extends DeadIllagerSkull> extends HierarchicalModel<T>
implements HeadedModel {
    public final ModelPart root;
    public final ModelPart head;
    public DeadIllagerSkullModel(ModelPart part) {
        super();
        this.root = part;
        this.head = part.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();
        part.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).
                        addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10f, 8.0f, CubeDeformation.NONE),
                PartPose.offset(0, 24, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }

    @Override
    public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {
        this.head.xRot = v3 * (Maths.CLOSER_PI / 180F);
        if (t.getTrueDeathTime() > 0) {
            this.head.yRot = t.getTrueDeathTime() * (Maths.CLOSER_PI / 180) * 10;
        } else {
            this.head.yRot = v4 * (Maths.CLOSER_PI / 180);
        }
    }
}
