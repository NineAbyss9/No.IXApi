
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.StatueModel;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticStatue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class NihilisticStatueRenderer<T extends NihilisticStatue>
extends MobRenderer<T, StatueModel<T>> {
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(ApostleRenderer.GUARDIAN_BEAM_LOCATION);
    public NihilisticStatueRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new StatueModel<>(p_174304_.bakeLayer(NoixmodAPIModelLayer.STATUE)), 0.5f);
    }

    private Vec3 getPosition(LivingEntity p_114803_, double p_114804_, float p_114805_) {
        double d0 = Mth.lerp(p_114805_, p_114803_.xOld, p_114803_.getX());
        double d1 = Mth.lerp(p_114805_, p_114803_.yOld, p_114803_.getY()) + p_114804_;
        double d2 = Mth.lerp(p_114805_, p_114803_.zOld, p_114803_.getZ());
        return new Vec3(d0, d1, d2);
    }

    public void render(T pEntity, float pEntityYaw, float partialTicks, PoseStack stack, MultiBufferSource source, int i) {
        super.render(pEntity, pEntityYaw, partialTicks, stack, source, i);
        LivingEntity livingentity = pEntity.getOwner();
        if (livingentity != null) {
            float f1 = pEntity.tickCount + partialTicks;
            float f2 = f1 * 0.5F % 1.0F;
            float f3 = pEntity.getEyeHeight();
            stack.pushPose();
            stack.translate(0.0F, f3, 0.0F);
            Vec3 vec3 = this.getPosition(livingentity, (double)livingentity.getBbHeight() * 0.5D, partialTicks);
            Vec3 vec31 = this.getPosition(pEntity, f3, partialTicks);
            Vec3 vec32 = vec3.subtract(vec31);
            float f4 = (float)(vec32.length() + 1.0D);
            vec32 = vec32.normalize();
            float f5 = (float)Math.acos(vec32.y);
            float f6 = (float)Math.atan2(vec32.z, vec32.x);
            stack.mulPose(Axis.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
            stack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
            float f7 = f1 * 0.05F * -1.5F;
            int j = (0xe079fa >> 16 & 255) / 255;
            int k = (0xe079fa >> 8 & 255) / 255;
            int l = (0xe079fa & 255) / 255;
            float f9 = 0.2F;
            float f10 = 0.282F;
            float f11 = Mth.cos(f7 + 2.3561945F) * f10;
            float f12 = Mth.sin(f7 + 2.3561945F) * f10;
            float f13 = Mth.cos(f7 + ((float)Math.PI / 4F)) * f10;
            float f14 = Mth.sin(f7 + ((float)Math.PI / 4F)) * f10;
            float f15 = Mth.cos(f7 + 3.926991F) * f10;
            float f16 = Mth.sin(f7 + 3.926991F) * f10;
            float f17 = Mth.cos(f7 + 5.4977875F) * f10;
            float f18 = Mth.sin(f7 + 5.4977875F) * f10;
            float f19 = Mth.cos(f7 + (float)Math.PI) * f9;
            float f20 = Mth.sin(f7 + (float)Math.PI) * f9;
            float f21 = Mth.cos(f7 + 0.0F) * f9;
            float f22 = Mth.sin(f7 + 0.0F) * f9;
            float f23 = Mth.cos(f7 + ((float)Math.PI / 2F)) * f9;
            float f24 = Mth.sin(f7 + ((float)Math.PI / 2F)) * f9;
            float f25 = Mth.cos(f7 + ((float)Math.PI * 1.5F)) * f9;
            float f26 = Mth.sin(f7 + ((float)Math.PI * 1.5F)) * f9;
            float f27 = 0.0F;
            float f28 = 0.4999F;
            float f29 = -1.0F + f2;
            float f30 = f4 * 2.5F + f29;
            VertexConsumer vertexconsumer = source.getBuffer(BEAM_RENDER_TYPE);
            PoseStack.Pose posestack$pose = stack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, j, k, l, f28, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f19, f27, f20, j, k, l, f28, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, f27, f22, j, k, l, f27, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, j, k, l, f27, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, j, k, l, f28, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, f27, f24, j, k, l, f28, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, f27, f26, j, k, l, f27, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, j, k, l, f27, f30);
            float f31 = 0.0F;
            if (pEntity.tickCount % 2 == 0) {
                f31 = 0.5F;
            }
            float f32 = 0.5F;
            float f33 = 1.0F;
            vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, f32, f31 + f32);
            vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, f33, f31 + f32);
            vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, f33, f31);
            vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, f32, f31);
            stack.popPose();
        }
    }

    public static void vertex(VertexConsumer consumer, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, int red, int green, int blue, float u, float v) {
        consumer.vertex(matrix4f, x, y, z).color(red, green, blue, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(T t) {
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            return new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihilistic_statue_horror.png");
        }
        return new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/nihilistic_statue.png");
    }
}
