
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.entities.servant.end.EnderManServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EnderManServantRenderer<T extends EnderManServant>
extends MobRenderer<T, EndermanModel<T>> {
    private final RandomSource random = RandomSource.create();
    public static final ResourceLocation ENDERMAN_LOCATION = new ResourceLocation("textures/entity/enderman/enderman.png");
    public EnderManServantRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new EndermanModel<>(p_174304_.bakeLayer(ModelLayers.ENDERMAN)), 0.5F);
    }

    @Override
    public void render(@NotNull T p_114339_, float p_114340_, float p_114341_, @NotNull PoseStack p_114342_, @NotNull MultiBufferSource p_114343_, int p_114344_) {
        LivingEntity $$6 = p_114339_.getHeldMob();
        EndermanModel<T> $$7 = this.getModel();
        $$7.carrying = $$6 != null;
        $$7.creepy = p_114339_.isCreepy();
        /*
        p_114342_.pushPose();
        p_114342_.mulPose();
        */
        super.render(p_114339_, p_114340_, p_114341_, p_114342_, p_114343_, p_114344_);
    }

    @Override
    @NotNull
    public Vec3 getRenderOffset(@NotNull T p_114336_, float p_114337_) {
        if (p_114336_.isCreepy()) {
            return new Vec3(this.random.nextGaussian() * 0.02, 0.0, this.random.nextGaussian() * 0.02);
        } else {
            return super.getRenderOffset(p_114336_, p_114337_);
        }
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T t) {
        return ENDERMAN_LOCATION;
    }
}
