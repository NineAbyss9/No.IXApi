
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.client.model.animations.LurkerAnimations;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.Lurker;
import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@OnlyInClient
public class LurkerModel<T extends Lurker>
extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("noixmodapi", "lurker"), "main");
	private final ModelPart root;
	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart hands;

	public LurkerModel(@NotNull ModelPart $$0) {
		this.root = $$0;
		this.bone = $$0.getChild("bone");
		this.head = $$0.getChild("head");
		this.hands = $$0.getChild("hands");
	}

	@NotNull
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		partdefinition.addOrReplaceChild("hands", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -1.0F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(16, 8).addBox(2.0F, -1.0F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@NotNull
	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(@NotNull T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
		bone.getAllParts();
		head.getAllParts();
		hands.getAllParts();
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate($$0.attacking, LurkerAnimations.attacking, $$1, $$2);
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}