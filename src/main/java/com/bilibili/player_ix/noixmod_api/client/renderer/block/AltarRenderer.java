
package com.bilibili.player_ix.noixmod_api.client.renderer.block;

import com.bilibili.player_ix.noixmod_api.blocks.entities.AltarBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AltarRenderer implements BlockEntityRenderer<AltarBlockEntity> {
    public AltarRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(AltarBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack displayItem = blockEntity.getItems().get(blockEntity.getDisplayItemIndex());
        if (!displayItem.isEmpty()) {
            poseStack.pushPose();
            // 调整物品位置和旋转
            poseStack.translate(0.5, 1.1, 0.5);
            Level level = blockEntity.getLevel();
            assert level != null;
            poseStack.mulPose(Axis.YP.rotationDegrees((level.getGameTime() + partialTick) * 2));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            Minecraft.getInstance().getItemRenderer().renderStatic(displayItem,
                    ItemDisplayContext.GROUND,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    level,
                    (int)blockEntity.getBlockPos().asLong());
            poseStack.popPose();
        }
    }
}
