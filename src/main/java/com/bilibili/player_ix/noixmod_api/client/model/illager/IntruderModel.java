
package com.bilibili.player_ix.noixmod_api.client.model.illager;

import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Intruder;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

@OnlyInClient
public class IntruderModel<T extends Intruder>
extends HierarchicalModel<T>
implements ArmedModel, HeadedModel {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart hat;
    private final ModelPart arms;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart hats;
    public IntruderModel(ModelPart p_170688_) {
        this.root = p_170688_;
        this.head = p_170688_.getChild("head");
        this.hat = this.head.getChild("hat");
        this.arms = p_170688_.getChild("arms");
        this.leftLeg = p_170688_.getChild("left_leg");
        this.rightLeg = p_170688_.getChild("right_leg");
        this.leftArm = p_170688_.getChild("left_arm");
        this.rightArm = p_170688_.getChild("right_arm");
        this.hats = this.head.getChild("hats");
    }

    public static MeshDefinition createMesh() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f), PartPose.offset(0.0f, 0.0f, 0.0f));
        $$2.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 12.0f, 8.0f, new CubeDeformation(0.45f)), PartPose.ZERO);
        $$2.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0f, -1.0f, -6.0f, 2.0f, 4.0f, 2.0f), PartPose.offset(0.0f, -2.0f, 0.0f));
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 12.0f, 6.0f).texOffs(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 20.0f, 6.0f, new CubeDeformation(0.5f)), PartPose.offset(0.0f, 0.0f, 0.0f));
        PartDefinition $$3 = $$1.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(44, 22).addBox(-8.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f).texOffs(40, 38).addBox(-4.0f, 2.0f, -2.0f, 8.0f, 4.0f, 4.0f), PartPose.offsetAndRotation(0.0f, 3.0f, -1.0f, -0.75f, 0.0f, 0.0f));
        $$3.addOrReplaceChild("left_shoulder", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(4.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f), PartPose.ZERO);
        $$1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(-2.0f, 12.0f, 0.0f));
        $$1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(2.0f, 12.0f, 0.0f));
        $$1.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(-5.0f, 2.0f, 0.0f));
        $$1.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(5.0f, 2.0f, 0.0f));
        return $$0;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = createMesh();
        PartDefinition partdefinition = mesh.getRoot();
        PartDefinition head = partdefinition.getChild("head");
        head.addOrReplaceChild("hats", CubeListBuilder.create().texOffs(0, 82).addBox(-8.0F, -7.0F, -8.0F, 16.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 99).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float p_102929_, float p_102930_, float p_102931_, float p_102932_, float p_102933_) {
        this.head.yRot = p_102932_ * 0.017453292F;
        this.head.xRot = p_102933_ * 0.017453292F;
        this.rightArm.xRot = Mth.cos(p_102929_ * 0.6662F + 3.1415927F) * 2.0F * p_102930_ * 0.5F;
        this.rightArm.yRot = 0.0F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.xRot = Mth.cos(p_102929_ * 0.6662F) * 2.0F * p_102930_ * 0.5F;
        this.leftArm.yRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos(p_102929_ * 0.6662F) * 1.4F * p_102930_ * 0.5F;
        this.rightLeg.yRot = 0.0F;
        this.rightLeg.zRot = 0.0F;
        this.leftLeg.xRot = Mth.cos(p_102929_ * 0.6662F + 3.1415927F) * 1.4F * p_102930_ * 0.5F;
        this.leftLeg.yRot = 0.0F;
        this.leftLeg.zRot = 0.0F;
        this.hats.visible = true;
        this.hat.visible = true;
        Intruder.IntruderArmPose pose = entity.getPoses();
        switch (pose) {
            case ATTACKING: {
                if (entity.getMainHandItem().isEmpty()) {
                    AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, p_102931_);
                } else {
                    AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, entity, this.attackTime, p_102931_);
                }
                break;
            }
            case SPELL_CASTING: {
                this.rightArm.z = 0;
                this.leftArm.z = 0;
                this.rightArm.xRot = Mth.cos(p_102931_ * 0.6662f) * 0.25f;
                this.leftArm.xRot = Mth.cos(p_102931_ * 0.6662f) * 0.25f;
                this.rightArm.zRot = 2.3561945f;
                this.leftArm.zRot = -2.3561945f;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
                break;
            }
            case SWEEPING: {
                break;
            }
        }
        boolean flag = pose == Intruder.IntruderArmPose.CROSSED;
        this.rightArm.visible = this.leftArm.visible = !flag;
        this.arms.visible = flag;
    }

    public ModelPart getArm(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        this.getArm(humanoidArm).translateAndRotate(poseStack);
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }
}
