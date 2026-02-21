

package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import com.bilibili.player_ix.noixmod_api.client.model.animations.BiologistAnimations;
import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Biologist;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

@OnlyInClient
public class BiologistModel<T extends Biologist> extends HierarchicalModel<T> implements HeadedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(NoixmodAPI.MOD_ID, "biologist"), "main");
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart tentacle_base;
	private final ModelPart left_tentacle;
	public final ModelPart bone;
	private final ModelPart right_tentacle;
	public final ModelPart bone2;
	private final ModelPart left_tentacle2;
	private final ModelPart right_tentacle2;
	public final ModelPart bone3;
	private final ModelPart right_tentacle3;
	public final ModelPart bone4;
	private final ModelPart left_tentacle3;
	public final ModelPart bone5;
	private final ModelPart root;

	public BiologistModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.left_hand = root.getChild("left_hand");
		this.right_hand = root.getChild("right_hand");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.tentacle_base = root.getChild("tentacle_base");
		this.left_tentacle = root.getChild("left_tentacle");
		this.bone = this.left_tentacle.getChild("bone");
		this.right_tentacle = root.getChild("right_tentacle");
		this.bone2 = this.right_tentacle.getChild("bone2");
		this.left_tentacle2 = root.getChild("left_tentacle2");
		this.right_tentacle2 = root.getChild("right_tentacle2");
		this.bone3 = this.right_tentacle2.getChild("bone3");
		this.right_tentacle3 = root.getChild("right_tentacle3");
		this.bone4 = this.right_tentacle3.getChild("bone4");
		this.left_tentacle3 = root.getChild("left_tentacle3");
		this.bone5 = this.left_tentacle3.getChild("bone5");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f), PartPose.offset(0.0f, 0.0f, 0.0f));
		head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 12.0f, 8.0f, new CubeDeformation(0.45f)), PartPose.ZERO);
		head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0f, -1.0f, -6.0f, 2.0f, 4.0f, 2.0f), PartPose.offset(0.0f, -2.0f, 0.0f));
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 12.0f, 6.0f).texOffs(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 20.0f, 6.0f, new CubeDeformation(0.5f)), PartPose.offset(0.0f, 0.0f, 0.0f));
		partdefinition.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(40, 46).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(-5.0f, 2.0f, 0.0f));
		partdefinition.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(5.0f, 2.0f, 0.0f));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(-2.0f, 12.0f, 0.0f));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(2.0f, 12.0f, 0.0f));
		PartDefinition $$3 = partdefinition.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(44, 22).addBox(-8.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f).texOffs(40, 38).addBox(-4.0f, 2.0f, -2.0f, 8.0f, 4.0f, 4.0f), PartPose.offsetAndRotation(0.0f, 3.0f, -1.0f, -0.75f, 0.0f, 0.0f));
		$$3.addOrReplaceChild("left_shoulder", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(4.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f), PartPose.ZERO);
		partdefinition.addOrReplaceChild("tentacle_base", CubeListBuilder.create().texOffs(0, 124).addBox(-1.0F, -11.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.mirror().addBox(-1.0F, -8.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.mirror().addBox(-1.0F, -14.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));
		CubeListBuilder builder = CubeListBuilder.create().texOffs(0, 114);
		PartDefinition left_tentacle = partdefinition.addOrReplaceChild("left_tentacle", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 4.0F));
		left_tentacle.addOrReplaceChild("1_r1", builder.addBox(-1.0F, -2.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));
		left_tentacle.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 114).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -4.0F));
		PartDefinition right_tentacle = partdefinition.addOrReplaceChild("right_tentacle", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 15.0F, 3.0F, 0.0F, -1.7453F, 0.0F));
		right_tentacle.addOrReplaceChild("3_r1", CubeListBuilder.create().texOffs(0, 114).addBox(-6.3768F, -1.0F, -1.3005F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3638F, 0.0F, 0.018F, 0.0F, 1.0908F, 0.0F));
		PartDefinition bone2 = right_tentacle.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(-8.6362F, 1.0F, 8.018F));
		bone2.addOrReplaceChild("4_r1", CubeListBuilder.create().texOffs(46, 116).addBox(-0.9071F, -2.0F, 0.6152F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, -3.0F, 0.0F, -1.1781F, 0.0F));
		PartDefinition left_tentacle2 = partdefinition.addOrReplaceChild("left_tentacle2", CubeListBuilder.create().texOffs(0, 114).addBox(7.0F, -2.0F, -11.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 19.0F, 4.0F, 0.0F, 0.0F, 0.3927F));
		left_tentacle2.addOrReplaceChild("1_r2", CubeListBuilder.create().texOffs(46, 116).addBox(-1.0F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, 0.0F, 0.0F, 0.4363F, 0.0F));
		PartDefinition right_tentacle2 = partdefinition.addOrReplaceChild("right_tentacle2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 18.0F, 3.0F, 2.7489F, -1.3963F, 3.1416F));
		right_tentacle2.addOrReplaceChild("3_r2", CubeListBuilder.create().texOffs(46, 116).addBox(-6.3768F, -1.0F, -1.3005F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3638F, 0.0F, 0.018F, 0.0F, 1.0908F, 0.0F));
		PartDefinition bone3 = right_tentacle2.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(-8.6362F, 1.0F, 8.018F));
		bone3.addOrReplaceChild("4_r2", CubeListBuilder.create().texOffs(46, 116).addBox(-0.9071F, -1.0F, 0.6152F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -1.0F, -3.0F, 0.0F, -1.1781F, 0.0F));
		PartDefinition right_tentacle3 = partdefinition.addOrReplaceChild("right_tentacle3", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 12.0F, 3.0F, -2.7489F, -1.3963F, 3.1416F));
		right_tentacle3.addOrReplaceChild("3_r3", CubeListBuilder.create().texOffs(46, 166).addBox(-6.3768F, -1.0F, -1.3005F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3638F, 0.0F, 0.018F, 0.0F, 1.0908F, 0.0F));
		PartDefinition bone4 = right_tentacle3.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(-8.6362F, 1.0F, 8.018F));
		bone4.addOrReplaceChild("4_r3", CubeListBuilder.create().texOffs(0, 114).addBox(-0.9071F, -2.0F, 0.6152F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, -3.0F, 0.0F, -1.1781F, 0.0F));
		PartDefinition left_tentacle3 = partdefinition.addOrReplaceChild("left_tentacle3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 13.0F, 4.0F, 0.0F, 0.0F, -0.3927F));
		left_tentacle3.addOrReplaceChild("1_r3", CubeListBuilder.create().texOffs(46, 116).addBox(-1.0F, -2.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));
		left_tentacle3.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 114).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -4.0F));
		return LayerDefinition.create(meshdefinition, 64, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * (Maths.CLOSER_PI / 180F);
		this.left_tentacle.visible = this.left_tentacle2.visible = this.left_tentacle3.visible =
		this.right_tentacle.visible = this.right_tentacle2.visible = this.right_tentacle3.visible =
		this.tentacle_base.visible = entity.isSecondPhase();
		this.animateWalk(BiologistAnimations.walking, limbSwing, limbSwingAmount, 2.0f, 2.5f);
		this.animate(entity.ambient, BiologistAnimations.ambient, ageInTicks);
		this.animate(entity.attacking, BiologistAnimations.attacking, ageInTicks);
		this.animate(entity.summon, BiologistAnimations.summon1, ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_hand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_hand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tentacle_base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_tentacle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_tentacle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_tentacle2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_tentacle2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_tentacle3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_tentacle3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}
}