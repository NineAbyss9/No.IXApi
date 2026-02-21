
package com.bilibili.player_ix.noixmod_api.client.model;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

@OnlyInClient
public class APIGhastModel<T extends Entity>
extends HierarchicalModel<T>
implements HeadedModel {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart[] tentacles = new ModelPart[9];

    public APIGhastModel(ModelPart part) {
        this.root = part;
        this.body = root.getChild("body");
        for(int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i] = body.getChild(createTentacleName(i));
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition body = $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.offset(0.0F, 16F, 0.0F));
        RandomSource $$2 = RandomSource.create(1660L);
        for(int $$3 = 0; $$3 < 9; ++$$3) {
            float $$4 = (((float)($$3 % 3) - (float)($$3 / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
            float $$5 = ((float)($$3 / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
            int $$6 = $$2.nextInt(7) + 8;
            body.addOrReplaceChild(createTentacleName($$3), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, $$6, 2.0F), PartPose.offset($$4, 7F, $$5));
        }
        return LayerDefinition.create($$0, 64, 32);
    }

    public static String createTentacleName(int p_170573_) {
        return "tentacle" + p_170573_;
    }

    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(T p_102681_, float p_102682_, float p_102683_, float p_102684_, float p3, float p4) {
        for(int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].xRot = 0.2F * Mth.sin(p_102684_ * 0.3F + (float)i) + 0.4F;
        }
        this.body.yRot = p3 * (Maths.CLOSER_PI / 180F);
        this.body.xRot = p4 * (Maths.CLOSER_PI / 180F);
    }

    public ModelPart getHead() {
        return this.body;
    }
}
