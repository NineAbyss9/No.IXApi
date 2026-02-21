
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractWormIllager;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

@OnlyInClient
public class WormIllagerModel<T extends AbstractWormIllager>
extends HierarchicalModel<T>
implements HeadedModel, ArmedModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(NoixmodAPI.location("will"),
            "main");
    protected final ModelPart root;
    protected final ModelPart head;
    protected final ModelPart hat;
    protected final ModelPart arms;
    protected final ModelPart leftLeg;
    protected final ModelPart rightLeg;
    protected final ModelPart rightArm;
    protected final ModelPart leftArm;

    public WormIllagerModel(ModelPart part) {
        this.root = part;
        this.head = part.getChild("head");
        this.hat = this.head.getChild("hat");
        this.hat.visible = false;
        this.arms = part.getChild("arms");
        this.leftLeg = part.getChild("left_leg");
        this.rightLeg = part.getChild("right_leg");
        this.leftArm = part.getChild("left_arm");
        this.rightArm = part.getChild("right_arm");
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
        return LayerDefinition.create(WormIllagerModel.createMesh(), 64, 64);
    }

    @Override
    public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {
        this.head.yRot = v3 * (Maths.CLOSER_PI / 180F);
        this.head.xRot = v4 * (Maths.CLOSER_PI / 180);
        this.rightArm.xRot = Mth.cos(v * 0.6662f + Maths.CLOSER_PI) * 2.0f * v1 * 0.5f;
        this.rightArm.yRot = 0.0f;
        this.rightArm.zRot = 0.0f;
        this.leftArm.xRot = Mth.cos(v * 0.6662f) * 2.0f * v1 * 0.5f;
        this.leftArm.yRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        this.rightLeg.xRot = Mth.cos(v * 0.6662f) * 1.4f * v1 * 0.5f;
        this.rightLeg.yRot = 0.0f;
        this.rightLeg.zRot = 0.0f;
        this.leftLeg.xRot = Mth.cos(v * 0.6662f + Maths.CLOSER_PI) * 1.4f * v1 * 0.5f;
        this.leftLeg.yRot = 0.0f;
        this.leftLeg.zRot = 0.0f;
        AbstractWormIllager.WormIllagerArmPose pose = t.getArmPose();
        boolean flag = pose == AbstractWormIllager.WormIllagerArmPose.CROSSED;
        switch (pose) {
            case ATTACKING: {
                if (t.getMainHandItem().isEmpty()) {
                    AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, v2);
                } else {
                    AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, t, this.attackTime, v2);
                }
                break;
            }
            case SPELL_CASTING:{
                this.rightArm.z = 0.0f;
                this.rightArm.x = -5.0f;
                this.leftArm.z = 0.0f;
                this.leftArm.x = 5.0f;
                this.rightArm.xRot = Mth.cos(v2 * 0.6662f) * 0.25f;
                this.leftArm.xRot = Mth.cos(v2 * 0.6662f) * 0.25f;
                this.rightArm.zRot = 2.3561945f;
                this.leftArm.zRot = -2.3561945f;
                this.rightArm.yRot = 0.0f;
                this.leftArm.yRot = 0.0f;
                break;
            }
            default:break;
        }
        this.leftArm.visible = this.rightArm.visible = !flag;
        this.arms.visible = flag;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    private ModelPart getArm(HumanoidArm arm) {
        return arm ==HumanoidArm.LEFT?this.leftArm:this.rightArm;
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
