
package com.bilibili.player_ix.noixmod_api.client.renderer.block;

import com.bilibili.player_ix.noixmod_api.blocks.entities.CursedChestBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;

public class CursedChestR<E extends CursedChestBlockEntity>
extends ChestRenderer<E> {
    public CursedChestR(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
    }
}
