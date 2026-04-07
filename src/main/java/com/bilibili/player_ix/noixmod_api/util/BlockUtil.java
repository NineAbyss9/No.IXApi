
package com.bilibili.player_ix.noixmod_api.util;

import net.minecraft.world.level.block.state.BlockState;

public class BlockUtil {
    public BlockUtil() {
    }

    @SuppressWarnings("deprecation")
    public static boolean isSolid(BlockState state) {
        return state.isSolid();
    }

    @SuppressWarnings("deprecation")
    public static boolean isMotion(BlockState state) {
        return state.blocksMotion();
    }
}
