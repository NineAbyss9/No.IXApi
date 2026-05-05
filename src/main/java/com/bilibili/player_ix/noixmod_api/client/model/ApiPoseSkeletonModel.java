
package com.bilibili.player_ix.noixmod_api.client.model;

import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;

public class ApiPoseSkeletonModel<T extends Mob & ApiPoseMob> extends HumanoidModel<T> {
    public ApiPoseSkeletonModel(ModelPart p_170677_) {
        super(p_170677_);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, 2.0F, 0.0F));
        $$1.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
        $$1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-2.0F, 12.0F, 0.0F));
        $$1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(2.0F, 12.0F, 0.0F));
        return LayerDefinition.create($$0, 64, 32);
    }

    public void prepareMobModel(T p_103793_, float p_103794_, float p_103795_, float p_103796_) {}

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount,
                          float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        boolean flag = pEntity.getFallFlyingTicks() > 4;
        boolean flag1 = pEntity.isVisuallySwimming();
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        if (flag) {
            this.head.xRot = (-(float)Math.PI / 4F);
        } else if (this.swimAmount > 0.0F) {
            if (flag1) {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, (-(float)Math.PI / 4F));
            } else {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, pHeadPitch * ((float)Math.PI / 180F));
            }
        } else {
            this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        }
        this.body.yRot = 0.0F;
        this.rightArm.z = 0.0F;
        this.rightArm.x = -5.0F;
        this.leftArm.z = 0.0F;
        this.leftArm.x = 5.0F;
        float f = 1.0F;
        if (flag) {
            f = (float)pEntity.getDeltaMovement().lengthSqr();
            f /= 0.2F;
            f *= f * f;
        }
        if (f < 1.0F) {
            f = 1.0F;
        }
        this.rightArm.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 2.0F * pLimbSwingAmount * 0.5F / f;
        this.leftArm.xRot = Mth.cos(pLimbSwing * 0.6662F) * 2.0F * pLimbSwingAmount * 0.5F / f;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount / f;
        this.leftLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount / f;
        this.rightLeg.yRot = 0.005F;
        this.leftLeg.yRot = -0.005F;
        this.rightLeg.zRot = 0.005F;
        this.leftLeg.zRot = -0.005F;
        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        switch (pEntity.getPoses())
        {
            case ATTACKING -> {
                if (pEntity.getMainHandItem().isEmpty()) {
                    AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, pAgeInTicks);
                } else {
                    AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, pEntity, this.attackTime, pAgeInTicks);
                }
            }
            case BOW_AND_ARROW -> {
                this.rightArm.yRot = -0.1f + this.head.yRot;
                this.leftArm.yRot = 0.1f + this.head.yRot + 0.4f;
                this.rightArm.xRot = -1.5707964f + this.head.xRot;
                this.leftArm.xRot = -1.5707964f + this.head.xRot;
            }
            case SPELL_CASTING -> {
                this.rightArm.z = 0;
                this.leftArm.z = 0;
                this.rightArm.xRot = Mth.cos(pAgeInTicks * 0.6662f) * 0.25f;
                this.leftArm.xRot = Mth.cos(pAgeInTicks * 0.6662f) * 0.25f;
                this.rightArm.zRot = 2.3561945f;
                this.leftArm.zRot = -2.3561945f;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
            }
            case ZOMBIE_ATTACKING ->
                    AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, pAgeInTicks);
        }
        this.hat.copyFrom(this.head);
    }

    public void translateToHand(HumanoidArm p_103778_, PoseStack p_103779_) {
        float $$2 = p_103778_ == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        ModelPart $$3 = this.getArm(p_103778_);
        $$3.x += $$2;
        $$3.translateAndRotate(p_103779_);
        $$3.x -= $$2;
    }
}
