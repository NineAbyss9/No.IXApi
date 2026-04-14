
package com.bilibili.player_ix.noixmod_api.client.renderer.nihilist;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.nihilistic.ApostleModel;
import com.bilibili.player_ix.noixmod_api.client.model.nihilistic.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.client.renderer.layer.ApostleEyesLayer;
import com.bilibili.player_ix.noixmod_api.client.renderer.layer.NihilisticArmorLayer;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.boss.ApostleBoss;
import com.bilibili.player_ix.noixmod_api.util.TimeSelector;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class ApostleRenderer<T extends ApostleBoss>
extends MobRenderer<T, NihilistIllagerModel<T>> {
    public static final RenderType BEAM;
    public static final ResourceLocation HORROR = new ResourceLocation(
            "noixmodapi:textures/entities/nihilistic_mobs/apostle_horror.png");
    public static final ResourceLocation APOSTLE = new ResourceLocation(
            "noixmodapi:textures/entities/nihilistic_mobs/apostle.png");
    public static final ResourceLocation SECOND = new ResourceLocation(
            "noixmodapi:textures/entities/nihilistic_mobs/apostle_second.png");
    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation(
            "noixmodapi:textures/entities/nihilistic_mobs/nihilistic_beam.png");
    public static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation(
            "textures/entity/guardian_beam.png");
    public static final ResourceLocation BIRTHDAY = new ResourceLocation(
            "noixmodapi:textures/entities/nihilistic_mobs/birthday/apostle_birthday.png");
    private static final RenderType DECAL = RenderType.entityDecal(APOSTLE);
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3) / 2);

    public ApostleRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new ApostleModel<>($$0.bakeLayer(NoixmodAPIModelLayer.APOSTLE)), 0.5f);
        this.addLayer(new NihilisticArmorLayer<>(this, $$0.getModelSet()));
        this.addLayer(new CustomHeadLayer<>(this, $$0.getModelSet(), $$0.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer<>(this, $$0.getItemInHandRenderer()) {
            public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_,
                               float p_117208_, float p_117209_,
                               float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
                if (p_117207_.isCastingSpell() || p_117207_.isAggressive() || p_117207_.isSettingSecondPhase()) {
                    super.render(p_117204_, p_117205_, p_117206_, p_117207_, p_117208_, p_117209_, p_117210_, p_117211_,
                            p_117212_, p_117213_);
                }
            }
        });
        this.addLayer(new ApostleEyesLayer<>(this));
    }

    protected void scale(T pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        pPartialTickTime = 0.9375f;
        pPoseStack.scale(pPartialTickTime, pPartialTickTime, pPartialTickTime);
    }

    protected boolean isShaking(T pEntity) {
        return pEntity.isDeadOrDying() || pEntity.isNihilistic();
    }

    public ResourceLocation getTextureLocation(T t) {
        if (NoixmodAPIMainConfig.HorrorMode.get())
            return HORROR;
        else if (TimeSelector.birthday())
            return BIRTHDAY;
        else if (t.isSecondPhase())
            return SECOND;
        return APOSTLE;
    }

    public void render(T entity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource
            pBuffer, int pPackedLight) {
        boolean b = entity.getHurtCooldown() > 0;
        this.model.prepareMobModel(entity, 0.0F, 0.0F, pPartialTicks);
        if (entity.getTrueDeathTime() > 0) {
            float $$9 = entity.getTrueDeathTime() / 200.0F;
            VertexConsumer $$10 = pBuffer.getBuffer(RenderType.entityDecal(SECOND));
            this.model.renderToBuffer(pPoseStack, $$10, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F,
                    1.0F, 1.0F, $$9);
            VertexConsumer $$11 = pBuffer.getBuffer(DECAL);
            this.model.renderToBuffer(pPoseStack, $$11, pPackedLight, OverlayTexture.pack(0.0F, b),
                    1.0F, 1.0F, 1.0F, 1.0F);
        }
        float $$14;
        float $$15;
        if (entity.getTrueDeathTime() > 0) {
            $$14 = (entity.getTrueDeathTime() + pPartialTicks) / 200.0F;
            $$15 = Math.min($$14 > 0.8F ? ($$14 - 0.8F) / 0.2F : 0.0F, 1.0F);
            RandomSource $$16 = RandomSource.create(999L);
            VertexConsumer $$17 = pBuffer.getBuffer(RenderType.lightning());
            pPoseStack.pushPose();
            pPoseStack.translate(0, 1, 0);
            for(int $$18 = 0; (float)$$18 < ($$14 + $$14 * $$14) / 2.0F * 60.0F; ++$$18) {
                pPoseStack.mulPose(Axis.XP.rotationDegrees($$16.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.YP.rotationDegrees($$16.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees($$16.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.XP.rotationDegrees($$16.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.YP.rotationDegrees($$16.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees($$16.nextFloat() * 360.0F + $$14 * 90.0F));
                float $$19 = $$16.nextFloat() * 2F + 5.0F + $$15 * 10.0F;
                float $$20 = $$16.nextFloat() + 1.0F + $$15;
                Matrix4f $$21 = pPoseStack.last().pose();
                int $$22 = (int)(255.0F * (1.0F - $$15));
                vertex01($$17, $$21, $$22);
                vertex2($$17, $$21, $$19, $$20);
                vertex3($$17, $$21, $$19, $$20);
                vertex01($$17, $$21, $$22);
                vertex3($$17, $$21, $$19, $$20);
                vertex4($$17, $$21, $$19, $$20);
                vertex01($$17, $$21, $$22);
                vertex4($$17, $$21, $$19, $$20);
                vertex2($$17, $$21, $$19, $$20);
            }
            pPoseStack.popPose();
        }
        if (entity.isNihilistic()) {
            Vec3[] $$6 = entity.getIllusionOffsets(pPartialTicks);
            float $$7 = this.getBob(entity, pPartialTicks);
            for (int $$8 = 0; $$8 < $$6.length; ++$$8) {
                pPoseStack.pushPose();
                pPoseStack.translate($$6[$$8].x + Mth.cos($$8 + $$7 * 0.5F) * 0.025,
                        $$6[$$8].y + Mth.cos($$8 + $$7 * 0.75F) * 0.0125, $$6[$$8].z +
                                Mth.cos($$8 + $$7 * 0.7F) * 0.025);
                super.render(entity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
                pPoseStack.popPose();
            }
        }
        super.render(entity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    protected boolean isBodyVisible(T p_115341_) {
        return true;
    }

    public static void vertex01(VertexConsumer p_254498_, Matrix4f p_253891_, int p_254278_) {
        p_254498_.vertex(p_253891_, 0.0F, 0.0F, 0.0F).color(255, 255, 255,
                p_254278_).endVertex();
    }

    public static void vertex2(VertexConsumer p_253956_, Matrix4f p_254053_, float p_253704_, float p_253701_) {
        int c = NoixmodAPIMainConfig.HorrorMode.get() ? 0 : 255;
        p_253956_.vertex(p_254053_, -HALF_SQRT_3 * p_253701_, p_253704_, -0.5F * p_253701_)
                .color(255, 0, c, 0).endVertex();
    }

    public static void vertex3(VertexConsumer p_253850_, Matrix4f p_254379_, float p_253729_, float p_254030_) {
        int c = NoixmodAPIMainConfig.HorrorMode.get() ? 0 : 255;
        p_253850_.vertex(p_254379_, HALF_SQRT_3 * p_254030_, p_253729_, -0.5F * p_254030_)
                .color(255, 0, c, 0).endVertex();
    }

    public static void vertex4(VertexConsumer p_254184_, Matrix4f p_254082_, float p_253649_, float p_253694_) {
        int c = NoixmodAPIMainConfig.HorrorMode.get() ? 0 : 255;
        p_254184_.vertex(p_254082_, 0.0F, p_253649_, p_253694_).color(255, 0, c, 0).endVertex();
    }

    @Nullable
    protected RenderType getRenderType(T p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        if (p_115322_.getTrueDeathTime() > 0) {
            return RenderType.dragonExplosionAlpha(HORROR);
        } else {
            return super.getRenderType(p_115322_, p_115323_, p_115324_, p_115325_);
        }
    }

    static {
        BEAM = RenderType.entitySmoothCutout(BEAM_LOCATION);
    }
}
