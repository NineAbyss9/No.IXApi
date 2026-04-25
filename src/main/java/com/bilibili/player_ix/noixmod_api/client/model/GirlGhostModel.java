
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractGhost;
import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

import java.util.List;

public class GirlGhostModel<T extends AbstractGhost> extends HumanoidModel<T> {
    public static final ModelLayerLocation LOCATION
            = new ModelLayerLocation(NoixmodAPI.location("ggm"), "main");
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart ear;
    public final ModelPart clothes;
    public final List<ModelPart> parts;

    public GirlGhostModel(ModelPart p_170821_) {
        super(p_170821_);
        this.hat.visible = true;
        this.ear = p_170821_.getChild("ear");
        this.leftSleeve = p_170821_.getChild("left_sleeve");
        this.rightSleeve = p_170821_.getChild("right_sleeve");
        this.leftPants = p_170821_.getChild("left_pants");
        this.rightPants = p_170821_.getChild("right_pants");
        this.parts = p_170821_.getAllParts().filter((p_170824_) -> !p_170824_.isEmpty()
        ).collect(ImmutableList.toImmutableList());
        this.clothes = p_170821_.getChild("clothes");
    }

    public static MeshDefinition createMesh() {
        CubeDeformation p_170826_ = new CubeDeformation(0, 0, 0);
        MeshDefinition $$2 = HumanoidModel.createMesh(p_170826_, 0.0F);
        PartDefinition $$3 = $$2.getRoot();
        $$3.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, p_170826_), PartPose.ZERO);
        $$3.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(5.0F, 2.5F, 0.0F));
        $$3.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(-5.0F, 2.5F, 0.0F));
        $$3.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
        $$3.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));
        $$3.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(1.9F, 12.0F, 0.0F));
        $$3.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        $$3.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        $$3.addOrReplaceChild("clothes", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.ZERO);
        return $$2;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(GirlGhostModel.createMesh(), 64, 64);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.clothes));
    }

    @Override
    public void setupAnim(T entity, float v, float v1, float v2, float v3, float v4) {
        super.setupAnim(entity, v, v1, v2, v3, v4);
        ApiPose pose = entity.getPoses();
        this.ear.copyFrom(this.head);
        this.clothes.copyFrom(this.body);
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.rightSleeve.copyFrom(this.rightArm);
        this.leftSleeve.copyFrom(this.leftArm);
        if (pose == ApiPose.ATTACKING) {
            if (entity.getMainHandItem().isEmpty()) {
                AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, v2);
            } else {
                AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, entity, this.attackTime, v2);
            }
            this.leftSleeve.copyFrom(this.leftArm);
            this.rightSleeve.copyFrom(this.rightArm);
            this.leftPants.copyFrom(this.leftLeg);
            this.rightPants.copyFrom(this.rightLeg);
        }
    }

    public ModelPart getHead() {
        return this.head;
    }

    @Override
    protected ModelPart getArm(HumanoidArm p_102852_) {
        if (p_102852_ == HumanoidArm.LEFT) {
            return this.leftArm;
        } else {
            return this.rightArm;
        }
    }

    @Override
    public void translateToHand(HumanoidArm p_102854_, PoseStack p_102855_) {
        this.getArm(p_102854_).translateAndRotate(p_102855_);
    }
}
