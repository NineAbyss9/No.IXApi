
package com.bilibili.player_ix.noixmod_api.blocks;

import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("deprecation")
public class OminousHead
extends SkullBlock {
    public OminousHead() {
        super(Types.WITHER_SKELETON, Properties.of().sound(SoundType.STONE).mapColor(MapColor
                        .COLOR_BLACK).requiresCorrectToolForDrops()
                .strength(3F, 100F).instrument(NoteBlockInstrument.BASEDRUM));
    }

    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_,
                                 InteractionHand p_60507_, BlockHitResult p_60508_) {
        if (p_60504_.isClientSide)
            return InteractionResult.SUCCESS;
        else {
            p_60506_.playSound(SoundEvents.AMBIENT_CAVE.get());
            ServerLevel serverLevel = (ServerLevel)p_60504_;
            var  headHunter = NoixmodAPIEntities.HEAD_HUNTER.get().create(serverLevel);
            if (headHunter != null) {
                headHunter.moveTo(Vec9.of(p_60505_));
                headHunter.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(p_60505_), MobSpawnType.EVENT);
                if (serverLevel.addFreshEntity(headHunter))
                    p_60504_.removeBlock(p_60505_, false);
            }
            return InteractionResult.CONSUME;
        }
    }

    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return 5.0F;
    }
}
