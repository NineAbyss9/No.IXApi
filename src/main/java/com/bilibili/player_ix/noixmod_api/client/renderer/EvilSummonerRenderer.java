
package com.bilibili.player_ix.noixmod_api.client.renderer;

import com.bilibili.player_ix.noixmod_api.client.NoixmodAPIModelLayer;
import com.bilibili.player_ix.noixmod_api.client.model.NihilistIllagerModel;
import com.bilibili.player_ix.noixmod_api.entities.boss.EvilSummoner;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("ALL")
public class EvilSummonerRenderer<E extends EvilSummoner>
extends NihilistRenderer<E> {
    private final BookModel bookModel;

    public EvilSummonerRenderer(EntityRendererProvider.Context context) {
        super(context, new NihilistIllagerModel<>(context.bakeLayer(NoixmodAPIModelLayer.NIHILIST)), 0.5f);
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void render(E entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        /*if (entityIn.isAlive()) {
            double parameter = 0.9F;
            double up = 1.0F;
            double left = -0.5F;
            Vec2 vector2f = new Vec2(0.0f, Mth.clamp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot));
            Vec3 vector3d = Vec3.ZERO;
            float f = Mth.sin((float)((vector2f.y + 90.0f) * ((float)Math.PI / 180)));
            float f1 = Mth.cos((float)((vector2f.y + 90.0f) * ((float)Math.PI / 180)));
            float f2 = Mth.abs((float)(-vector2f.x * ((float)Math.PI / 180)));
            float f3 = Mth.abs((float)(-vector2f.x * ((float)Math.PI / 180)));
            float f4 = Mth.cos((float)((-vector2f.x + 90.0f) * ((float)Math.PI / 180)));
            float f5 = Mth.abs((float)((-vector2f.x + 90.0f) * ((float)Math.PI / 180)));
            Vec3 vector3d1 = new Vec3((double)(f * f2), (double)f3, (double)(f1 * f2));
            Vec3 vector3d2 = new Vec3((double)(f * f4), (double)f5, (double)(f1 * f4));
            Vec3 vector3d3 = vector3d1.vectorTo(vector3d2).scale(-1.0);
            double d0 = vector3d1.x * parameter + vector3d2.x * (double)up + vector3d3.x * (double)left;
            double d1 = vector3d1.y * parameter + vector3d2.y * (double)up + vector3d3.y * (double)left;
            double d2 = vector3d1.z * parameter + vector3d2.z * (double)up + vector3d3.z * (double)left;
            Vec3 finalVec = new Vec3(vector3d.x + d0, vector3d.y + d1, vector3d.z + d2);
            matrixStackIn.pushPose();
            matrixStackIn.translate(0F, 1.3F, 0F);
            float $$6 = (float)entityIn.tickCount + partialTicks;
            matrixStackIn.translate(finalVec.x(), 0.1F + Mth.sin($$6 * 0.2F) * 0.07F, finalVec.z());
            float $$7;
            for($$7 = entityIn.yBodyRot - entityIn.yBodyRotO; $$7 >= 3.1415927F; $$7 -= 6.2831855F) {
            }
            while($$7 < -3.1415927F) {
                $$7 += 6.2831855F;
            }
            float $$8 = entityIn.yBodyRotO + $$7 * entityYaw;
            matrixStackIn.mulPose(Axis.YP.rotation(-$$8 + 140f));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(40.0F));
            float $$9 = 1f;
            float $$10 = Mth.frac($$9 + 0.25F) * 1.6F - 0.3F;
            float $$11 = Mth.frac($$9 + 0.75F) * 1.6F - 0.3F;
            this.bookModel.setupAnim($$6, Mth.clamp($$10, 0.0F, 1.0F), Mth.clamp($$11, 0.0F, 1.0F),
                    entityIn.isActive() ? 1 : 0);
            VertexConsumer consumer = bufferIn.getBuffer(RenderType.entitySolid(EnchantTableRenderer.BOOK_LOCATION.texture()));
            this.bookModel.render(matrixStackIn, consumer, packedLightIn, EvilSummonerRenderer.getOverlayCoords(entityIn, this.getBob(entityIn, partialTicks)), 1, 1, 1, 1);
            matrixStackIn.popPose();
        }*/
    }

    @Override
    public ResourceLocation getTextureLocation(E e) {
        return new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/evil_summoner.png");
    }
}
