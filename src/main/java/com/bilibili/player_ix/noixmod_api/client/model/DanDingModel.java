
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.entities.npc.DanDing.DanDa;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.InteractionHand;

import java.util.List;

public class DanDingModel<D extends DanDa> extends HumanoidModel<D> {
    public final ModelPart clothes;
    public final List<ModelPart> parts;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    private final ModelPart ear;
    public DanDingModel(ModelPart p_170821_) {
        super(p_170821_, RenderType::entityTranslucent);
        this.hat.visible = true;
        this.ear = p_170821_.getChild("ear");
        this.leftPants = p_170821_.getChild("left_pants");
        this.rightPants = p_170821_.getChild("right_pants");
        this.parts = p_170821_.getAllParts().filter((p_170824_) -> !p_170824_.isEmpty()
        ).collect(ImmutableList.toImmutableList());
        this.clothes = p_170821_.getChild("clothes");
    }

    public static MeshDefinition createMesh() {
        CubeDeformation p_170826_ = new CubeDeformation(0, 0, 0);
        MeshDefinition $$2 = HumanoidModel.createMesh(p_170826_, 0.0F);
        PartDefinition partDefinition = $$2.getRoot();
        partDefinition.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, p_170826_), PartPose.ZERO);
        PartDefinition ra = partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(-5.0F, 2.0F, 0.0F));
        PartDefinition la = partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(5.0F, 2.0F, 0.0F));
        ra.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(-1, 0, 0));
        la.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(1, 0, 0));
        partDefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(1.9F, 12.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        partDefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partDefinition.addOrReplaceChild("clothes", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.ZERO);
        return $$2;
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(DanDingModel.createMesh(), 64, 64);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.clothes));
    }

    @Override
    public void setupAnim(D entity, float var, float var1, float var2, float var3, float var4) {
        super.setupAnim(entity, var, var1, var2, var3, var4);
        this.clothes.copyFrom(this.body);
        this.ear.copyFrom(this.head);
        this.hat.copyFrom(this.head);
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        if (entity.getUsedItemHand().equals(InteractionHand.OFF_HAND)) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
            this.leftArm.yRot = 0.5235988F;
            this.copyModelParts();
        }
    }

    private void copyModelParts() {
        this.clothes.copyFrom(this.body);
        this.ear.copyFrom(this.head);
        this.hat.copyFrom(this.head);
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
    }
}
