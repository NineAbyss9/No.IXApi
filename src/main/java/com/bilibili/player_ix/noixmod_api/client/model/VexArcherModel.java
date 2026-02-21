
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.entities.servant.illager.VexArcher;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class VexArcherModel<V extends VexArcher>
extends HierarchicalModel<V>
implements ArmedModel, HeadedModel {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart head;

    public VexArcherModel(ModelPart p_171045_) {
        super(RenderType::entityTranslucent);
        this.root = p_171045_.getChild("root");
        this.body = this.root.getChild("body");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
        this.head = this.root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -2.5F, 0.0F));
        $$2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));
        PartDefinition $$3 = $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-1.5F, 1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 20.0F, 0.0F));
        $$3.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-1.25F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(-1.75F, 0.25F, 0.0F));
        $$3.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.75F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(1.75F, 0.25F, 0.0F));
        $$3.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 1.0F, 1.0F));
        $$3.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 1.0F, 1.0F));
        return LayerDefinition.create($$0, 32, 32);
    }

    public void translateToHand(HumanoidArm p_259770_, PoseStack p_260351_) {
        boolean $$2 = p_259770_ == HumanoidArm.RIGHT;
        ModelPart $$3 = $$2 ? this.rightArm : this.leftArm;
        this.root.translateAndRotate(p_260351_);
        this.body.translateAndRotate(p_260351_);
        $$3.translateAndRotate(p_260351_);
        p_260351_.scale(0.55F, 0.55F, 0.55F);
        this.offsetStackPosition(p_260351_, $$2);
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(V v, float v2, float v1, float v3, float v4, float v5) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = v4 * 0.017453292F;
        this.head.xRot = v5 * 0.017453292F;
        float $$6 = Mth.cos(v3 * 5.5F * 0.017453292F) * 0.1F;
        this.rightArm.zRot = 0.62831855F + $$6;
        this.leftArm.zRot = -(0.62831855F + $$6);
        this.setArmsCharging();
        this.leftWing.yRot = 1.0995574F + Mth.cos(v3 * 45.836624F * 0.017453292F) * 0.017453292F * 16.2F;
        this.rightWing.yRot = -this.leftWing.yRot;
        this.leftWing.xRot = 0.47123888F;
        this.leftWing.zRot = -0.47123888F;
        this.rightWing.xRot = 0.47123888F;
        this.rightWing.zRot = 0.47123888F;
    }

    private void setArmsCharging() {
        this.rightArm.yRot = -0.1f + this.head.yRot;
        this.leftArm.yRot = 0.1f + this.head.yRot + 0.4f;
        this.rightArm.xRot = -1.5707964f + this.head.xRot;
        this.leftArm.xRot = -1.5707964f + this.head.xRot;
    }

    private void offsetStackPosition(PoseStack p_263343_, boolean p_263414_) {
        if (p_263414_) {
            p_263343_.translate(0.046875, -0.15625, 0.078125);
        } else {
            p_263343_.translate(-0.046875, -0.15625, 0.078125);
        }
    }
}
