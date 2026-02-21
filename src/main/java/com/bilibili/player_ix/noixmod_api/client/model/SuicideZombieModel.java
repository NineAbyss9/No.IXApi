
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.animations.SuicideZombieAnimations;
import com.bilibili.player_ix.noixmod_api.entities.servant.SuicideZombie;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class SuicideZombieModel<T extends SuicideZombie> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(NoixmodAPI.MOD_ID, "suicide"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart right_hand;
	private final ModelPart left_hand;
	private final ModelPart tnt;

	public SuicideZombieModel(@Nonnull ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
		this.right_hand = root.getChild("right_hand");
		this.left_hand = root.getChild("left_hand");
		this.tnt = root.getChild("tnt");
	}

	@Nonnull
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -30.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-3.0F, -5.0F, -2.0F, 8.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 14.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 4.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 4.0F, 0.0F));
		PartDefinition tnt = partdefinition.addOrReplaceChild("tnt", CubeListBuilder.create(), PartPose.offset(-4.0F, 12.0F, 0.0F));
		tnt.addOrReplaceChild("tnt_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.3491F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@Nonnull SuicideZombie entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * (Maths.CLOSER_PI / 180F);
		this.animateWalk(SuicideZombieAnimations.walking, limbSwing, limbSwingAmount, 2.0f, 2.5f);
		this.animate(entity.aggressive, SuicideZombieAnimations.aggressive, ageInTicks);
	}

	@Override
	public void renderToBuffer(@Nonnull PoseStack poseStack,@Nonnull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_hand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_hand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tnt.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Nonnull
	@Override
	public ModelPart root() {
		return this.root;
	}
}