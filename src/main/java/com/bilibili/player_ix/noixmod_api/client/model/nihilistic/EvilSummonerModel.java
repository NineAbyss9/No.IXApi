
package com.bilibili.player_ix.noixmod_api.client.model.nihilistic;

import com.bilibili.player_ix.noixmod_api.entities.boss.EvilSummoner;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class EvilSummonerModel<E extends EvilSummoner>
extends NihilistIllagerModel<E> {
    public EvilSummonerModel(ModelPart p_170688_) {
        super(p_170688_);
    }

    public static MeshDefinition createMesh() {
        MeshDefinition $$0 = NihilistIllagerModel.createMesh();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("left_lid", CubeListBuilder.create().texOffs(64, 0).addBox(-6.0F, -5.0F, -0.005F, 6.0F, 10.0F, 0.005F), PartPose.offset(0, 0, -1));
        $$1.addOrReplaceChild("right_lid", CubeListBuilder.create().texOffs(80, 0).addBox(0.0F, -5.0F, -0.005F, 6.0F, 10.0F, 0.005F), PartPose.offset(0, 0, 1));
        $$1.addOrReplaceChild("seam", CubeListBuilder.create().texOffs(76, 0).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 10.0F, 0.005F), PartPose.rotation(0, Maths.CLOSER_HALF_PI, 0));
        $$1.addOrReplaceChild("left_pages", CubeListBuilder.create().texOffs(64, 10).addBox(0.0F, -4.0F, -0.99F, 5.0F, 8.0F, 1.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("right_pages", CubeListBuilder.create().texOffs(76, 10).addBox(0.0F, -4.0F, -0.01F, 5.0F, 8.0F, 1.0F), PartPose.ZERO);
        CubeListBuilder $$2 = CubeListBuilder.create().texOffs(88, 10).addBox(0.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.005F);
        $$1.addOrReplaceChild("flip_page1", $$2, PartPose.ZERO);
        $$1.addOrReplaceChild("flip_page2", $$2, PartPose.ZERO);
        return $$0;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(EvilSummonerModel.createMesh(), 64, 128);
    }
}
