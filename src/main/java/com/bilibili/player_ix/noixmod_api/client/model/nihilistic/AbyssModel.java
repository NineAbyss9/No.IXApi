
package com.bilibili.player_ix.noixmod_api.client.model.nihilistic;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.animations.AbyssAnimations;
import com.bilibili.player_ix.noixmod_api.entities.boss.abyss.Abyss;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class AbyssModel<T extends Abyss> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new
			ResourceLocation(NoixmodAPI.MOD_ID, "abyssmodel"), "main");
	private final ModelPart root;
	public final ModelPart head;
	public final ModelPart halo;
	public final ModelPart body;
	public final ModelPart right_arm;
	public final ModelPart sword;
	public final ModelPart left_arm;
	public final ModelPart left_leg;
	public final ModelPart right_leg;

	public AbyssModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.halo = head.getChild("halo");
		this.body = root.getChild("body");
		this.right_arm = root.getChild("right_arm");
		this.sword = this.right_arm.getChild("sword");
		this.left_arm = root.getChild("left_arm");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("halo", CubeListBuilder.create().texOffs(32, 112).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.4264F, 6.1809F, 0.6109F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -3.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 32).addBox(-4.0F, -3.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(40, 32).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-6.0F, 1.0F, 0.0F));

		PartDefinition sword = right_arm.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(0, 64).addBox(-0.5F, -2.5455F, -25.3182F, 1.0F, 5.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(0, 91).addBox(-0.5F, -0.5455F, -3.3182F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(7, 88).addBox(-0.5F, -1.5455F, -26.3182F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 88).addBox(-0.5F, -0.5455F, -26.3182F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 93).addBox(-0.5F, -2.5455F, -26.3182F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 88).addBox(-0.5F, -0.5455F, -27.3182F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 93).addBox(-0.5F, -0.5455F, -28.3182F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 93).addBox(-0.5F, 0.4545F, -27.3182F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 93).addBox(-0.5F, 1.4545F, -26.3182F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 93).addBox(-0.5F, -1.5455F, -27.3182F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(7, 88).addBox(-0.5F, 0.4545F, -26.3182F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 8.5455F, 2.3182F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(50, 48).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 1.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 48).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(2.0F, 13.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 32).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-2.0F, 13.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * (Maths.CLOSER_PI / 180F);
		this.head.xRot = headPitch * (Maths.CLOSER_PI / 180F);
		this.animateWalk(AbyssAnimations.walk, limbSwing, limbSwingAmount, 2.0f, 2.5f);
		this.animate(entity.attack, AbyssAnimations.attack, ageInTicks);
		this.animate(entity.attack2, AbyssAnimations.attack2, ageInTicks);
		this.animate(entity.clap, AbyssAnimations.clap, ageInTicks);
		this.animate(entity.ground, AbyssAnimations.ground, ageInTicks);
		this.animate(entity.attack3, AbyssAnimations.attack3, ageInTicks);
		this.animate(entity.summon, AbyssAnimations.summon, ageInTicks);
		this.animate(entity.throw_item, AbyssAnimations.throw_item, ageInTicks);
		this.animate(entity.clap_second, AbyssAnimations.clap_second, ageInTicks);
		this.animate(entity.attack4, AbyssAnimations.attack4, ageInTicks);
		this.animate(entity.attack5, AbyssAnimations.attack5, ageInTicks);
		this.animate(entity.idle, AbyssAnimations.idle, ageInTicks);
	}

	@Override
	public ModelPart root() {
		return root;
	}
}