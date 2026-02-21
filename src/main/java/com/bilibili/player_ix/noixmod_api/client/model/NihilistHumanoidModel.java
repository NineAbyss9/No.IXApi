
package com.bilibili.player_ix.noixmod_api.client.model;

import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

@OnlyInClient
public class NihilistHumanoidModel<T extends Nihilist>
extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("noixmodapi", "nihilisthumanoidmodel"), "main");
    private final ModelPart root;
    private final ModelPart halo;
    private final ModelPart halo1;
    public final ModelPart clothes;

    public NihilistHumanoidModel(ModelPart $$0) {
        super($$0);
        this.root = $$0;
        this.hat.visible = true;
        this.clothes = $$0.getChild("clothes");
        this.halo = this.head.getChild("halo");
        this.halo1 = this.halo.getChild("halo1");
    }

    public static MeshDefinition createMesh() {
        MeshDefinition $$0 = HumanoidModel.createMesh(new CubeDeformation(0.0f), 0f);
        PartDefinition $$1 = $$0.getRoot();
        CubeDeformation cube = new CubeDeformation(0.0f);
        PartDefinition $$2 = $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 1f, 0.0F));
        $$1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-1.9F, 12f, 0.0F));
        $$1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(1.9F, 12f, 0.0F));
        $$1.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(-5.0f, 2.0f, 0.0f));
        $$1.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.offset(5.0f, 2.0f, 0.0f));
        $$1.addOrReplaceChild("clothes", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, cube.extend(0.25F)), PartPose.ZERO);
        PartDefinition halo = $$2.addOrReplaceChild("halo", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, -12.0f, 5.0f, 0.7854f, 0.0f, 0.0f));
        halo.addOrReplaceChild("halo1", CubeListBuilder.create().texOffs(48, 112).addBox(-8.0f, -8.0f, 0.0f, 16.0f, 16.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        return $$0;
    }

    @NotNull
    public static LayerDefinition createBodyLayer() {
      return LayerDefinition.create(NihilistHumanoidModel.createMesh(), 64, 128);
    }

    @Override
    public void setupAnim(@NotNull T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        this.head.yRot = $$4 * ((float) Math.PI / 180);
        this.head.xRot = $$5 * ((float) Math.PI / 180);
        this.rightArm.xRot = Mth.cos($$1 * 0.6662f + (float) Math.PI) * 2.0f * $$2 * 0.5f;
        this.rightArm.yRot = 0.0f;
        this.rightArm.zRot = 0.0f;
        this.leftArm.xRot = Mth.cos($$1 * 0.6662f) * 2.0f * $$2 * 0.5f;
        this.leftArm.yRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        this.rightLeg.xRot = Mth.cos($$1 * 0.6662f) * 1.4f * $$2 * 0.5f;
        this.rightLeg.yRot = 0.0f;
        this.rightLeg.zRot = 0.0f;
        this.leftLeg.xRot = Mth.cos($$1 * 0.6662f + (float) Math.PI) * 1.4f * $$2 * 0.5f;
        this.leftLeg.yRot = 0.0f;
        this.leftLeg.zRot = 0.0f;
        this.clothes.copyFrom(this.body);
        this.hat.copyFrom(this.head);
        Nihilist.NihilistArmPose $$6 = $$0.getArmPose();
        switch ($$6) {
            case ATTACKING:
                if ($$0.getMainHandItem().isEmpty()) {
                    AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, $$3);
                } else {
                    AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, $$0, this.attackTime, $$3);
                }
                this.clothes.copyFrom(this.body);
                this.hat.copyFrom(this.head);
                break;
            case SPELL_CASTING:
                this.rightArm.xRot = Mth.cos($$3 * 0.6662f) * 0.25f;
                this.leftArm.xRot = Mth.cos($$3 * 0.6662f) * 0.25f;
                this.rightArm.zRot = 2.3561945f;
                this.leftArm.zRot = -2.3561945f;
                this.rightArm.yRot = Mth.PI;
                this.leftArm.yRot = Mth.PI;
                this.clothes.copyFrom(this.body);
                this.hat.copyFrom(this.head);
                break;
            case ROAR:
                this.rightArm.yRot = 20f;
                this.rightArm.xRot = -179.5f;
                this.leftArm.yRot = -20f;
                this.leftArm.xRot = 179.5f;
                this.clothes.copyFrom(this.body);
                this.hat.copyFrom(this.head);
                break;
            case BOW_AND_ARROW:
                this.rightArm.yRot = -0.1f + this.head.yRot;
                this.leftArm.yRot = 0.1f + this.head.yRot + 0.4f;
                this.rightArm.xRot = -1.5707964f + this.head.xRot;
                this.leftArm.xRot = -1.5707964f + this.head.xRot;
                this.clothes.copyFrom(this.body);
                this.hat.copyFrom(this.head);
                break;
            case CROSSBOW_HOLD:
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
                this.clothes.copyFrom(this.body);
                this.hat.copyFrom(this.head);
                break;
            case CROSSBOW_CHARGE:
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, $$0, true);
                this.clothes.copyFrom(this.body);
                this.hat.copyFrom(this.head);
                break;
            case THROWING:
                this.rightArm.xRot = -Mth.PI / 3;
                this.leftArm.xRot = Mth.PI / 3;
                this.clothes.copyFrom(this.body);
                this.hat.copyFrom(this.head);
                break;
            case SPELL_AND_WEAPON:
                if ($$0.getMainArm() == HumanoidArm.RIGHT) {
                    this.leftArm.z = 0.0f;
                    this.leftArm.x = 5.0f;
                    this.leftArm.xRot = Mth.cos(($$3 * 0.6662f)) * 0.25f;
                    this.leftArm.zRot = -2.3561945f;
                    this.leftArm.yRot = 0.0f;
                    this.rightArm.yRot = -0.1f + this.head.yRot;
                    this.rightArm.xRot = -1.5707964f + this.head.xRot;
                } else {
                    this.rightArm.z = 0.0f;
                    this.rightArm.x = -5.0f;
                    this.rightArm.xRot = Mth.cos(($$3 * 0.6662f)) * 0.25f;
                    this.rightArm.zRot = 2.3561945f;
                    this.rightArm.yRot = 0.0f;
                    this.leftArm.yRot = 0.1f + this.head.yRot;
                    this.leftArm.xRot = -1.5707964f + this.head.xRot;
                }
                this.clothes.copyFrom(this.body);
                this.hat.copyFrom(this.head);
                break;
            case DIE:
                this.rightArm.xRot = Mth.cos($$3 * 0.6662f) * 0.25f;
                this.leftArm.xRot = Mth.cos($$3 * 0.6662f) * 0.25f;
                this.rightArm.zRot = 2.3561945f;
                this.leftArm.zRot = -2.3561945f;
                this.rightArm.yRot = Mth.PI + 10f;
                this.leftArm.yRot = Mth.PI - 10f;
                this.clothes.copyFrom(this.body);
                this.hat.copyFrom(this.head);
                break;
        }
    }

    @Override
    protected @NotNull Iterable<ModelPart> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.head, this.root, this.clothes));
    }

    protected @NotNull ModelPart getArm(@NotNull HumanoidArm $$0) {
        if ($$0 == HumanoidArm.LEFT) {
            return this.leftArm;
        }
        return this.rightArm;
    }

    public ModelPart getHat() {
        return this.hat;
    }

    @Override
    public @NotNull ModelPart getHead() {
        return this.head;
    }

    @Override
    public void translateToHand(@NotNull HumanoidArm $$0, @NotNull PoseStack $$1) {
        this.getArm($$0).translateAndRotate($$1);
    }
}
