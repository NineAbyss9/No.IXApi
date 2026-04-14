
package com.bilibili.player_ix.noixmod_api.client.model.nihilistic;

import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.animations.PriestAnimations;
import com.bilibili.player_ix.noixmod_api.entities.boss.priest.Priest;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PriestModel<T extends Priest>
		extends HierarchicalModel<T>
implements HeadedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(NoixmodAPI.location(
			"priest"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart sword;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public PriestModel(ModelPart root) {
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.hat = this.head.getChild("hat");
		this.body = this.root.getChild("body");
		this.left_hand = this.root.getChild("left_hand");
		this.right_hand = this.root.getChild("right_hand");
		this.sword = this.right_hand.getChild("sword");
		this.left_leg = this.root.getChild("left_leg");
		this.right_leg = this.root.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 1.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 60).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -36.0F, -1.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, -6.0F));

		PartDefinition hat1_r1 = hat.addOrReplaceChild("hat1_r1", CubeListBuilder.create().texOffs(48, 60).addBox(-6.0F, -1.0F, -5.5F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 1.5F, 0.5672F, 0.0F, 0.0F));

		PartDefinition hat_r1 = hat.addOrReplaceChild("hat_r1", CubeListBuilder.create().texOffs(0, 38).addBox(-9.5F, -1.5F, -11.0F, 20.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.5F, 4.0F, 0.5672F, 0.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(70, 0).addBox(-5.0F, -13.0F, 0.0F, 10.0F, 21.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -23.0F, -4.0F));

		PartDefinition left_hand = root.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(48, 74).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -34.0F, -1.0F));

		PartDefinition right_hand = root.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(72, 74).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -34.0F, -1.0F));

		PartDefinition sword = right_hand.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.5F, -36.125F, 2.0F, 5.0F, 33.0F, new CubeDeformation(0.0F))
		.texOffs(80, 47).addBox(-1.0F, -1.5F, -3.125F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(20, 84).addBox(-0.5F, -4.5F, -10.125F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 84).addBox(-0.5F, -6.5F, -8.125F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 84).addBox(-0.5F, 2.5F, -10.125F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 84).addBox(-0.5F, 4.5F, -8.125F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(70, 27).addBox(-1.0F, -1.5F, -38.125F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(70, 32).addBox(-1.0F, -0.5F, -40.125F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.5F, 0.125F, 2.5744F, 0.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(80, 27).addBox(-2.25F, -1.0F, -2.5F, 5.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.25F, -14.0F, -1.0F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(80, 27).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -14.0F, -1.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * (Maths.CLOSER_PI / 180F);
		this.head.xRot = headPitch * (Maths.CLOSER_PI / 180F);
		this.animateWalk(PriestAnimations.walk, limbSwing, limbSwingAmount, 2.0F,
				2.5F);
		this.animate(entity.attack, PriestAnimations.attack, ageInTicks);
		this.animate(entity.sweep, PriestAnimations.sweep, ageInTicks);
		this.animate(entity.attack1, PriestAnimations.attack1, ageInTicks);
		this.animate(entity.attack2, PriestAnimations.attack2, ageInTicks);
		this.animate(entity.thrust, PriestAnimations.thrust, ageInTicks);
		this.animate(entity.attack3, PriestAnimations.attack3, ageInTicks);
		this.animate(entity.circle, PriestAnimations.circle, ageInTicks);
		this.animate(entity.fate, PriestAnimations.fate, ageInTicks);
		this.animate(entity.ranged, PriestAnimations.ranged, ageInTicks);
	}

	public ModelPart root() {
		return root;
	}

	public ModelPart getHead() {
		return head;
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}