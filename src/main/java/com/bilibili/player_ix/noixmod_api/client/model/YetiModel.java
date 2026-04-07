
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.animations.YetiAnims;
import com.bilibili.player_ix.noixmod_api.entities.servant.ice.Yeti;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class YetiModel<T extends Yeti> extends HierarchicalModel<T> implements HeadedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(NoixmodAPI.location
			("yeti"), "main");
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bone11;
	private final ModelPart bone12;
	private final ModelPart bone13;
	private final ModelPart bone14;
	private final ModelPart bone15;
	private final ModelPart bone6;
	private final ModelPart bone9;
	private final ModelPart bone10;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart bone5;

	public YetiModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.bone2 = this.bone.getChild("bone2");
		this.bone11 = this.bone2.getChild("bone11");
		this.bone12 = this.bone11.getChild("bone12");
		this.bone13 = this.bone2.getChild("bone13");
		this.bone14 = this.bone13.getChild("bone14");
		this.bone15 = this.bone2.getChild("bone15");
		this.bone6 = this.bone.getChild("bone6");
		this.bone9 = this.bone6.getChild("bone9");
		this.bone10 = this.bone6.getChild("bone10");
		this.bone3 = this.bone.getChild("bone3");
		this.bone4 = this.bone3.getChild("bone4");
		this.bone5 = this.bone3.getChild("bone5");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0F, -15.0F, -4.0F, 12.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition cube_r1 = bone2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -3.5F, -6.0F, 15.0F, 10.6F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r2 = bone2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 42).addBox(-0.25F, -3.1F, -3.975F, 2.0F, 7.1F, 7.95F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -12.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r3 = bone2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 66).addBox(-1.75F, -3.1F, -3.975F, 2.0F, 7.1F, 7.95F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -12.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone11 = bone2.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(40, 38).addBox(-3.9167F, -1.0F, -3.5F, 5.75F, 18.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(66, 56).addBox(-3.9167F, 17.0F, -3.5F, 5.75F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.5833F, -23.0F, 0.0F));

		PartDefinition bone12 = bone11.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(0, 42).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 18.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.9167F, 19.0F, 0.0F));

		PartDefinition bone13 = bone2.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(40, 38).mirror().addBox(-1.8333F, -1.0F, -3.5F, 5.75F, 18.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(66, 56).mirror().addBox(-1.8333F, 17.0F, -3.5F, 5.75F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(8.5833F, -23.0F, 0.0F));

		PartDefinition bone14 = bone13.addOrReplaceChild("bone14", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-2.5F, -2.5F, -3.0F, 5.0F, 18.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.9167F, 19.0F, 0.0F));

		PartDefinition bone15 = bone2.addOrReplaceChild("bone15", CubeListBuilder.create().texOffs(40, 19).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -23.0F, -3.0F));

		PartDefinition bone6 = bone.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(4.0F, -3.0F, 0.0F));

		PartDefinition bone9 = bone6.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(22, 63).addBox(-2.25F, 0.0F, -3.25F, 5.5F, 14.0F, 6.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone10 = bone6.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));

		PartDefinition bone3 = bone.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(-4.0F, -3.0F, 0.0F));

		PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(44, 63).addBox(-3.25F, 0.0F, -3.25F, 5.5F, 14.0F, 6.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone5 = bone3.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(64, 38).addBox(-3.0F, 0.0F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public ModelPart root() {
		return bone;
	}

	public ModelPart getHead() {
		return bone15;
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.bone15.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.bone15.xRot = headPitch * ((float) Math.PI / 180F);
		if (entity.getStatus() == 1) {
			this.animateWalk(YetiAnims.stride, limbSwing, limbSwingAmount,
					2.0F, 2.5F);
		} else if (entity.getStatus() == 2) {
			this.animateWalk(YetiAnims.run, limbSwing, limbSwingAmount,
					2.0F, 2.5F);
		} else {
			this.animateWalk(YetiAnims.move, limbSwing, limbSwingAmount,
					2.0F, 2.5F);
		}
		this.animate(entity.pose, YetiAnims.pose, ageInTicks);
		this.animate(entity.idle, YetiAnims.idle, ageInTicks);
		this.animate(entity.hide, YetiAnims.hide, ageInTicks);
		this.animate(entity.hidePose, YetiAnims.hidePose, ageInTicks);
		this.animate(entity.snowball, YetiAnims.snowball, ageInTicks);
		this.animate(entity.attack, YetiAnims.attack, ageInTicks);
		this.animate(entity.attack2, YetiAnims.attack2, ageInTicks);
		this.animate(entity.attack3, YetiAnims.attack3, ageInTicks);
		this.animate(entity.runToMove, YetiAnims.runToMove, ageInTicks);
		this.animate(entity.moveToStride, YetiAnims.moveToStride, ageInTicks);
		this.animate(entity.strideToRun, YetiAnims.strideToRun, ageInTicks);
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}