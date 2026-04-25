
package com.bilibili.player_ix.noixmod_api.client.model;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class APIHumanoidModel<T extends LivingEntity & ApiPoseMob>
extends HierarchicalModel<T>
implements ArmedModel {
    public final ModelPart root;
    public final ModelPart head;
    public final ModelPart hat;
    public final ModelPart body;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;

    public APIHumanoidModel(ModelPart part) {
        this.root = part;
        this.head = part.getChild("head");
        this.hat = part.getChild("hat");
        this.body = part.getChild("body");
        this.rightArm = part.getChild("right_arm");
        this.leftArm = part.getChild("left_arm");
        this.rightLeg = part.getChild("right_leg");
        this.leftLeg = part.getChild("left_leg");
    }

    public static MeshDefinition createMesh() {
        CubeDeformation p_170682_ = new CubeDeformation(0);
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, p_170682_), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, p_170682_.extend(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, p_170682_), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170682_), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170682_), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170682_), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170682_), PartPose.offset(1.9F, 12.0F, 0.0F));
        return meshdefinition;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(createMesh(), 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void prepareMobModel(T p_102861_, float p_102862_, float p_102863_, float p_102864_) {
        super.prepareMobModel(p_102861_, p_102862_, p_102863_, p_102864_);
    }

    @Override
    public void setupAnim(T entity, float v, float v1, float v2, float v3, float v4) {
        this.head.yRot = v3 * (Maths.CLOSER_PI / 180F);
        this.head.xRot = v4 * (Maths.CLOSER_PI / 180);
        this.rightArm.xRot = Mth.cos(v * 0.6662f + (float) Math.PI) * 2.0f * v1 * 0.5f;
        this.rightArm.yRot = 0.0f;
        this.rightArm.zRot = 0.0f;
        this.leftArm.xRot = Mth.cos(v * 0.6662f) * 2.0f * v1 * 0.5f;
        this.leftArm.yRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        this.rightLeg.xRot = Mth.cos(v * 0.6662f) * 1.4f * v1 * 0.5f;
        this.rightLeg.yRot = 0.0f;
        this.rightLeg.zRot = 0.0f;
        this.leftLeg.xRot = Mth.cos(v * 0.6662f + (float) Math.PI) * 1.4f * v1 * 0.5f;
        this.leftLeg.yRot = 0.0f;
        this.leftLeg.zRot = 0.0f;
        ApiPose pose = entity.getPoses();
        switch (pose) {
            case ATTACKING: {
                if (entity.getMainHandItem().isEmpty()) {
                    AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, v3);
                } else {
                    if (entity instanceof Mob mob) {
                        AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, mob, this.attackTime, v3);
                    }
                }
                break;
            }
            case BOW_AND_ARROW: {
                this.rightArm.yRot = -0.1f + this.head.yRot;
                this.leftArm.yRot = 0.1f + this.head.yRot + 0.4f;
                this.rightArm.xRot = -1.5707964f + this.head.xRot;
                this.leftArm.xRot = -1.5707964f + this.head.xRot;
                break;
            }
            case CROSSBOW_CHARGE: {
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
                break;
            }
            case CROSSBOW_HOLD: {
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
                break;
            }
            case SPELL_CASTING: {
                this.rightArm.z = 0;
                this.leftArm.z = 0;
                this.rightArm.xRot = Mth.cos(v3 * 0.6662f) * 0.25f;
                this.leftArm.xRot = Mth.cos(v3 * 0.6662f) * 0.25f;
                this.rightArm.zRot = 2.3561945f;
                this.leftArm.zRot = -2.3561945f;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
                break;
            }
            case ZOMBIE_ATTACKING: {
                AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, v3);
                break;
            }
        }
    }

    public ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        this.getArm(humanoidArm).translateAndRotate(poseStack);
    }
}
