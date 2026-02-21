
package com.bilibili.player_ix.noixmod_api.client.model;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.entities.servant.DrownedServant;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@OnlyInClient
public class ApiZombieModel<E extends Mob>
extends HumanoidModel<E> {
    public static final ModelLayerLocation API_ZOMBIE = new ModelLayerLocation(NoixmodAPI.location("az"),
            "main");
    public ApiZombieModel(ModelPart p_170677_) {
        super(p_170677_);
    }

    public static LayerDefinition createBodyLayer() {
        CubeDeformation p_170536_ = CubeDeformation.NONE;
        MeshDefinition $$1 = HumanoidModel.createMesh(p_170536_, 0.0F);
        PartDefinition $$2 = $$1.getRoot();
        $$2.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170536_), PartPose.offset(5.0F, 2.0F, 0.0F));
        $$2.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170536_), PartPose.offset(1.9F, 12.0F, 0.0F));
        return LayerDefinition.create($$1, 64, 64);
    }

    @Override
    public void prepareMobModel(E p_102521_, float p_102522_, float p_102523_, float p_102524_) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        ItemStack $$4 = p_102521_.getItemInHand(InteractionHand.MAIN_HAND);
        if ($$4.is(Items.TRIDENT) && p_102521_.isAggressive() && p_102521_.isUsingItem()) {
            if (p_102521_.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = ArmPose.THROW_SPEAR;
            } else {
                this.leftArmPose = ArmPose.THROW_SPEAR;
            }
        }
        super.prepareMobModel(p_102521_, p_102522_, p_102523_, p_102524_);
    }

    public boolean isDrowned(E e) {
        return e instanceof DrownedServant;
    }

    @Override
    public void setupAnim(E p_102526_, float p_102527_, float p_102528_, float p_102529_, float p_102530_, float p_102531_) {
        super.setupAnim(p_102526_, p_102527_, p_102528_, p_102529_, p_102530_, p_102531_);
        if (this.isDrowned(p_102526_)) {
            if (!this.leftArmPose.equals(ArmPose.THROW_SPEAR)) {
                AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, p_102526_.isAggressive(), this.attackTime, p_102529_);
            }
        } else {
            AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, p_102526_.isAggressive(), this.attackTime, p_102529_);
        }
        if (this.leftArmPose == ArmPose.THROW_SPEAR) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - 3.1415927F;
            this.leftArm.yRot = 0.0F;
        }
        if (this.rightArmPose == ArmPose.THROW_SPEAR) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - Mth.PI;
            this.rightArm.yRot = 0.0F;
        }
        if (this.swimAmount > 0.0F && this.isDrowned(p_102526_)) {
            this.rightArm.xRot = this.rotlerpRad(this.swimAmount, this.rightArm.xRot, -2.513F) + this.swimAmount * 0.35F * Mth.sin(0.1F * p_102529_);
            this.leftArm.xRot = this.rotlerpRad(this.swimAmount, this.leftArm.xRot, -2.513F) - this.swimAmount * 0.35F * Mth.sin(0.1F * p_102529_);
            this.rightArm.zRot = this.rotlerpRad(this.swimAmount, this.rightArm.zRot, -0.15F);
            this.leftArm.zRot = this.rotlerpRad(this.swimAmount, this.leftArm.zRot, 0.15F);
            ModelPart var10000 = this.leftLeg;
            var10000.xRot -= this.swimAmount * 0.55F * Mth.sin(0.1F * p_102529_);
            var10000 = this.rightLeg;
            var10000.xRot += this.swimAmount * 0.55F * Mth.sin(0.1F * p_102529_);
            this.head.xRot = 0.0F;
        }
    }
}
