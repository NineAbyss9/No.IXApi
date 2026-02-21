
package com.bilibili.player_ix.noixmod_api.client.model;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.animations.NewHeadHunterAnis;
import com.bilibili.player_ix.noixmod_api.entities.boss.NewHeadHunter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class NewHeadHunterModel<T extends NewHeadHunter> extends HierarchicalModel<T>
implements HeadedModel {
	public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(NoixmodAPI.location("newheadhunter"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart crossbow;
	private final ModelPart right_arm;
	private final ModelPart sword;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart rope;

	public NewHeadHunterModel(ModelPart root) {
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.hat = this.head.getChild("hat");
		this.body = this.root.getChild("body");
		this.left_arm = this.root.getChild("left_arm");
		this.crossbow = this.left_arm.getChild("crossbow");
		this.right_arm = this.root.getChild("right_arm");
		this.sword = this.right_arm.getChild("sword");
		this.right_leg = this.root.getChild("right_leg");
		this.left_leg = this.root.getChild("left_leg");
		this.rope = this.root.getChild("rope");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -1.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(86, 57).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -25.0F, 1.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(3, 2).addBox(-9.0F, 13.0F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(60, 24).addBox(-6.0F, 10.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -23.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(40, 80).addBox(-4.0F, -13.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, -11.0F, 1.0F));

		PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(76, 79).addBox(0.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -22.0F, 1.0F));

		PartDefinition crossbow = left_arm.addOrReplaceChild("crossbow", CubeListBuilder.create().texOffs(0, 59).addBox(0.0F, -10.0F, -8.5F, 0.0F, 17.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 12.0F, 0.0F));

		PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, -21.0F, 1.0F));

		PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(76, 79).addBox(-3.0F, -1.0F, -2.5F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

		PartDefinition sword = right_arm.addOrReplaceChild("sword", CubeListBuilder.create(), PartPose.offset(-2.0F, 5.0F, -8.0F));

		PartDefinition sword_r1 = sword.addOrReplaceChild("sword_r1", CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, -5.7133F, -44.1414F, 0.0F, 11.0F, 46.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, -2.2253F, 0.0F, 0.0F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(84, 2).addBox(-1.5F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -11.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(100, 79).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.75F, -11.0F, 0.0F));

		PartDefinition rope = root.addOrReplaceChild("rope", CubeListBuilder.create(), PartPose.offset(0.5F, -23.0F, 4.0F));

		PartDefinition rope_r1 = rope.addOrReplaceChild("rope_r1", CubeListBuilder.create().texOffs(9, 106).addBox(-2.0F, 5.0F, -1.0F, 8.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -6.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

    public ModelPart root() {
        return root;
    }

    public ModelPart getHead() {
        return head;
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * (Maths.CLOSER_PI / 180F);
        this.animateWalk(NewHeadHunterAnis.walking, limbSwing, limbSwingAmount, 2.0f,
                2.5f);
        this.animate(entity.dying, NewHeadHunterAnis.die, ageInTicks);
        this.animate(entity.swordAttacking, NewHeadHunterAnis.attacking, ageInTicks);
        this.animate(entity.changingPhase, NewHeadHunterAnis.changePhase, ageInTicks);
        this.animate(entity.swordGroundAttack, NewHeadHunterAnis.sword_ground_attack,  ageInTicks);
        this.animate(entity.shooting, NewHeadHunterAnis.crossbowAttacking, ageInTicks);
        this.animate(entity.exposition, NewHeadHunterAnis.exposition, ageInTicks);
        this.animate(entity.summoning, NewHeadHunterAnis.ground, ageInTicks);
        this.animate(entity.charge, NewHeadHunterAnis.charge, ageInTicks);
        this.animate(entity.sword_ground_explode, NewHeadHunterAnis.sword_ground_explode, ageInTicks);
        this.animate(entity.attack_explode, NewHeadHunterAnis.attack_explode, ageInTicks);
        this.animate(entity.avoid, NewHeadHunterAnis.avoiding, ageInTicks);
        this.animate(entity.groundExplode, NewHeadHunterAnis.ground_ex, ageInTicks);
        this.animate(entity.circleAttack, NewHeadHunterAnis.circle_attack, ageInTicks);
        this.animate(entity.attack1, NewHeadHunterAnis.attack1, ageInTicks);
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}