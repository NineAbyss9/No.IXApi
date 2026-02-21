// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings,
// Paste this class into your mod and generate all required imports
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.animations.HeadHunterAnimations;
import com.bilibili.player_ix.noixmod_api.entities.boss.HeadHunter;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

public class HeadHunterModel<H extends HeadHunter> extends HierarchicalModel<H>
		implements HeadedModel, ArmedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new
			ModelLayerLocation(new ResourceLocation(NoixmodAPI.MOD_ID, "headhuntermodel"), "main");
	private final ModelPart head;
	protected final ModelPart hat;
	private final ModelPart body;
	private final ModelPart left_arm;
	protected final ModelPart crossbow;
	private final ModelPart right_arm;
	protected final ModelPart sword;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart rope;
	private final ModelPart root;

	public HeadHunterModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.hat = this.head.getChild("hat");
		this.body = root.getChild("body");
		this.left_arm = root.getChild("left_arm");
		this.crossbow = this.left_arm.getChild("crossbow");
		this.right_arm = root.getChild("right_arm");
		this.sword = this.right_arm.getChild("sword");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.rope = root.getChild("rope");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(80, 53).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -22.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(3, 2).addBox(-9.0F, 0.0F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(56, 22).addBox(-7.0F, -15.0F, -7.0F, 14.0F, 15.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(36, 77).addBox(-6.0F, 0.0F, -3.0F, 12.0F, 21.0F, 6.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(72, 77).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -8.0F, 0.0F));

		PartDefinition crossbow = left_arm.addOrReplaceChild("crossbow", CubeListBuilder.create().texOffs(0, 59).addBox(2.0F, 8.0F, -10.75F, 0.0F, 17.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.0F, 2.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-10.0F, -8.0F, 0.0F));

		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(72, 77).addBox(-5.0F, -2.0F, -4.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.0F, 1.0F, -0.7418F, 0.0F, 0.0F));

		PartDefinition sword = right_arm.addOrReplaceChild("sword", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, -10.0F));

		PartDefinition sword_r1 = sword.addOrReplaceChild("sword_r1", CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, -3.1828F, -44.7042F, 0.0F, 11.0F, 46.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -1.0F, -2.2253F, 0.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(80, 0).addBox(-3.5F, 0.0F, -1.0F, 6.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 13.0F, -2.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(96, 77).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.75F, 13.0F, 0.0F));

		PartDefinition rope = partdefinition.addOrReplaceChild("rope", CubeListBuilder.create(), PartPose.offset(0.5F, -9.0F, 5.0F));

		PartDefinition rope_r1 = rope.addOrReplaceChild("rope_r1", CubeListBuilder.create().texOffs(5, 106).addBox(-6.0F, 0.0F, -1.0F, 12.0F, 21.0F, 1.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(H entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * (Maths.CLOSER_PI / 180F);
		this.animateWalk(HeadHunterAnimations.walking, limbSwing, limbSwingAmount, 2.0f, 2.5f);
		this.animate(entity.swordAttacking, HeadHunterAnimations.attacking, ageInTicks);
		this.animate(entity.changingPhase, HeadHunterAnimations.changingPhase, ageInTicks);
		this.animate(entity.swordGroundAttack, HeadHunterAnimations.sword_ground_attack,  ageInTicks);
		this.animate(entity.shooting, HeadHunterAnimations.crossbowAttacking, ageInTicks);
		this.animate(entity.exposition, HeadHunterAnimations.exposition, ageInTicks);
		this.animate(entity.summoning, HeadHunterAnimations.ground, ageInTicks);
		this.animate(entity.charge, HeadHunterAnimations.charge, ageInTicks);
		this.animate(entity.sword_ground_explode, HeadHunterAnimations.sword_ground_explode, ageInTicks);
		this.animate(entity.attack_explode, HeadHunterAnimations.attack_explode, ageInTicks);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int
			packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rope.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public ModelPart getArm(HumanoidArm arm) {
		return arm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
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