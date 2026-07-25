
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.animations.PlateauBeastAnimations;
import com.bilibili.player_ix.noixmod_api.entities.monster.hostile.ice.PlateauBeast;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

@OnlyInClient
public class PlateauBeastModel<T extends PlateauBeast>
		extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(NoixmodAPI.MOD_ID, "plateau"), "main");
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart tail;
	private final ModelPart right_foreleg;
	private final ModelPart left_foreleg;
	private final ModelPart right_hind_leg;
	private final ModelPart left_hind_leg;
	private final ModelPart bb_main;
	private final ModelPart root;

	public PlateauBeastModel(ModelPart root) {
		super();
		this.root = root;
		this.bone = root.getChild("bone");
		this.bone2 = this.bone.getChild("bone2");
		this.tail = root.getChild("tail");
		this.right_foreleg = root.getChild("right_foreleg");
		this.left_foreleg = root.getChild("left_foreleg");
		this.right_hind_leg = root.getChild("right_hind_leg");
		this.left_hind_leg = root.getChild("left_hind_leg");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(24, 14).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 31).addBox(1.0F, -5.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 0).addBox(-3.0F, -5.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(24, 22).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 18.0F, -7.0F, 0, 3.1415926F,0));
		PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 1.0F));
		bone2.addOrReplaceChild("mouse_r1", CubeListBuilder.create().texOffs(44, 15).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 3.0F, -0.3054F, 0.0F, 0.0F));
		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 18.0F, 7.0F, 0.0F, 0.0F, 0.0873F));
		tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0872F, 0.9962F, 3.0F, 0.7056F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_foreleg", CubeListBuilder.create().texOffs(8, 25).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 20.0F, 7.0F));
		partdefinition.addOrReplaceChild("left_foreleg", CubeListBuilder.create().texOffs(24, 28).addBox(-1.0F, -0.0904F, -0.9992F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 20.0F, 7.0F));
		partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(8, 31).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 20.0F, -3.0F));
		partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(16, 25).addBox(-1.0F, 1.9213F, -0.9266F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 18.0F, -3.0F));
		partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -5.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 14).addBox(-3.0F, -9.0F, 3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_foreleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_foreleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_hind_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_hind_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T t, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		bone2.getAllParts();
		this.animate(t.attacking, PlateauBeastAnimations.attacking, ageInTicks);
		this.animateWalk(PlateauBeastAnimations.walking, limbSwing, limbSwingAmount, 2.0f, 2.5f);
	}
}