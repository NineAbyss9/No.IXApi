
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

@OnlyInClient
public class ApostleModel<T extends Apostle>
extends NihilistIllagerModel<T> {
    public static final ModelLayerLocation APOSTLE = new ModelLayerLocation(new ResourceLocation("noixmodapi", "apostle"), "main");
    public ModelPart hat2 = this.head.getChild("hat2");
    public ModelPart halo = this.getHead().getChild("halo");
    public ModelPart halo1 = this.halo.getChild("halo1");

    public ApostleModel(ModelPart $$0) {
        super($$0);
        this.getHat().visible = true;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = NihilistIllagerModel.createMesh();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.getChild("head");
        PartDefinition halo = head.addOrReplaceChild("halo", CubeListBuilder.create(), PartPose.offsetAndRotation(0f, -12f, 5f, 0.7854f, 0, 0));
        halo.addOrReplaceChild("halo1", CubeListBuilder.create().texOffs(48, 112).addBox(-8.0f, -8.0f, 0.0f, 16.0f, 16f, 0, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        head.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(0, 82).addBox(-8.0f, -7.0f, -8.0f, 16.0f, 7.0f, 16.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, 0.0f));
        return LayerDefinition.create(meshdefinition, 64, 128);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.hat2.visible = true;
        this.halo1.zRot = entity.spin;
        this.halo.visible = this.halo1.visible = (entity.isAggressive() || (entity.getTrueDeathTime() > 0
                && entity.getTrueDeathTime() <= 180));
    }
}
