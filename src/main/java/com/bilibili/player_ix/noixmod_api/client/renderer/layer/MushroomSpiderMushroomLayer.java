
package com.bilibili.player_ix.noixmod_api.client.renderer.layer;

import com.bilibili.player_ix.noixmod_api.client.model.APISpiderModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.animal.MushroomSpider;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@OnlyInClient
public class MushroomSpiderMushroomLayer<T extends MushroomSpider>
extends RenderLayer<T, APISpiderModel<T>> {
    protected final BlockRenderDispatcher blockRenderer;
    public MushroomSpiderMushroomLayer(RenderLayerParent<T, APISpiderModel<T>> p_117346_,
                                       BlockRenderDispatcher p_234851_) {
        super(p_117346_);
        this.blockRenderer = p_234851_;
    }

    public void render(PoseStack pStack, MultiBufferSource pBuffer, int pLight, T pEntity, float p_117260_, float p_117261_, float p_117262_, float p_117263_, float p_117264_, float p_117265_) {
        if (!pEntity.isBaby()) {
            Minecraft $$10 = Minecraft.getInstance();
            boolean $$11 = $$10.shouldEntityAppearGlowing(pEntity) && pEntity.isInvisible();
            if (!pEntity.isInvisible() || $$11) {
                BlockState $$12 = pEntity.getSpiderColor() == MushroomSpider.Color.BROWN ? Blocks.BROWN_MUSHROOM
                        .defaultBlockState() : Blocks.RED_MUSHROOM.defaultBlockState();
                int $$13 = LivingEntityRenderer.getOverlayCoords(pEntity, 0.0F);
                BakedModel $$14 = this.blockRenderer.getBlockModel($$12);
                float var = 0.6F;
                pStack.pushPose();
                this.getParentModel().getHead().translateAndRotate(pStack);
                pStack.translate(0.0F, -0.85F, 0);
                pStack.mulPose(Axis.YP.rotationDegrees(-48.0F));
                pStack.scale(-var, -var, var);
                pStack.translate(-0.5F, -1F, -0.5F);
                this.renderMushroomBlock(pStack, pBuffer, pLight, $$11, $$12, $$13, $$14);
                pStack.popPose();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void renderMushroomBlock(PoseStack stack, MultiBufferSource buffer, int light, boolean p_234856_, BlockState state, int p_234858_, BakedModel p_234859_) {
        if (p_234856_) {
            this.blockRenderer.getModelRenderer().renderModel(stack.last(), buffer.getBuffer(RenderType
                    .outline(TextureAtlas.LOCATION_BLOCKS)), state, p_234859_, 0.0F, 0.0F, 0.0F,
                    light, p_234858_);
        } else {
            this.blockRenderer.renderSingleBlock(state, stack, buffer, light, p_234858_);
        }
    }
}
