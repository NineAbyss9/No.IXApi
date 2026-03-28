
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

    public void render(E entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/evil_summoner.png");

    public ResourceLocation getTextureLocation(E e) {
        return LOC;
    }
}
