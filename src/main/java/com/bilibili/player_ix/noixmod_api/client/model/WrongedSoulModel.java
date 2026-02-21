
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.entities.servant.WrongedSoul;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

@OnlyInClient
public class WrongedSoulModel<T extends Entity>
extends HierarchicalModel<T>
implements ArmedModel,
HeadedModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("noixmodapi", "wronged"), "main");
    public ModelPart arms;
    public ModelPart root;
    public ModelPart nose;
    public ModelPart leftArm;
    public ModelPart rightArm;
    public ModelPart body;
    public ModelPart head;

    public WrongedSoulModel(@NotNull ModelPart part) {
        this.root = part;
        this.leftArm = part.getChild("left_arm");
        this.rightArm = part.getChild("right_arm");
        this.head = part.getChild("head");
        this.nose = this.head.getChild("nose");
        this.body = part.getChild("body");
        this.arms = part.getChild("arms");
    }

    @NotNull
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();
        PartDefinition $$2 = part.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f), PartPose.offset(0.0f, 0.0f, 0.0f));
        $$2.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 12.0f, 8.0f, new CubeDeformation(0.45f)), PartPose.ZERO);
        $$2.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0f, -1.0f, -6.0f, 2.0f, 4.0f, 2.0f), PartPose.offset(0.0f, -2.0f, 0.0f));
        part.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 12.0f, 6.0f).texOffs(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 20.0f, 6.0f, new CubeDeformation(0.5f)), PartPose.offset(0.0f, 0.0f, 0.0f));
        PartDefinition $$3 = part.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(44, 22).addBox(-8.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f).texOffs(40, 38).addBox(-4.0f, 2.0f, -2.0f, 8.0f, 4.0f, 4.0f), PartPose.offsetAndRotation(0.0f, 3.0f, -1.0f, -0.75f, 0.0f, 0.0f));
        $$3.addOrReplaceChild("left_shoulder", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(4.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f), PartPose.ZERO);
        part.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(-5.0f, 2.0f, 0.0f));
        part.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(5.0f, 2.0f, 0.0f));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @NotNull
    @Override
    public ModelPart root() {
        return this.root;
    }

    @NotNull
    private ModelPart getArm(@Nonnull HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    @Override
    public void translateToHand(@NotNull HumanoidArm humanoidArm, @NotNull PoseStack poseStack) {
        this.getArm(humanoidArm).translateAndRotate(poseStack);
    }

    @Override
    public void setupAnim(@NotNull T entity, float $$1, float $$2, float $$3, float $$4, float $$5) {
        if (entity instanceof WrongedSoul soul) {
            if (soul.isAggressive()) {
                AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, soul.isAggressive(), this.attackTime, $$3);
            } else {
                this.head.yRot = $$4 * ((float) Math.PI / 180);
                this.head.xRot = $$5 * ((float) Math.PI / 180);
                this.rightArm.xRot = Mth.cos($$1 * 0.6662f + (float) Math.PI) * 2.0f * $$2 * 0.5f;
                this.rightArm.yRot = 0.0f;
                this.rightArm.zRot = 0.0f;
                this.leftArm.xRot = Mth.cos($$1 * 0.6662f) * 2.0f * $$2 * 0.5f;
                this.leftArm.yRot = 0.0f;
                this.leftArm.zRot = 0.0f;
            }
            this.arms.visible = !soul.isAggressive();
            this.rightArm.visible = soul.isAggressive();
            this.leftArm.visible = soul.isAggressive();
        }
    }

    @NotNull
    @Override
    public ModelPart getHead() {
        return this.head;
    }
}
