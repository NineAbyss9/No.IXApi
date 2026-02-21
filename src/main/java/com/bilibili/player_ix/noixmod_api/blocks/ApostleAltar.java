
package com.bilibili.player_ix.noixmod_api.blocks;

import com.bilibili.player_ix.noixmod_api.entities.projectile.summon.SummonApostle;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class ApostleAltar extends Block {
    public ApostleAltar() {
        super(BlockBehaviour.Properties.of().strength(3, 99999999)
                .requiresCorrectToolForDrops()
                .mapColor(MapColor.STONE).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM));
    }

    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_,
                                 InteractionHand p_60507_, BlockHitResult p_60508_) {
        if (p_60506_.getItemInHand(p_60507_).is(NoixmodAPIItems.BANNED_BOOK.get())) {
            SummonApostle apostle = new SummonApostle(NoixmodAPIEntities.SUMMON_APOSTLE.get(), p_60504_);
            apostle.moveTo(p_60505_.offset(0, 1, 0), 0, 0);
            p_60504_.addFreshEntity(apostle);
        }
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }
}
