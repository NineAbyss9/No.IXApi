
package com.bilibili.player_ix.noixmod_api.item.grave;

import com.bilibili.player_ix.noixmod_api.item.weapon.ApiAxe;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.concurrent.ThreadLocalRandom;

public class GraveAxe
extends ApiAxe
implements IGraveItem
{
    public GraveAxe() {
        super(1561, 5, 4.29F, 11, 10, Ingredient.EMPTY,
                5, -3.1f, new Properties().rarity(Rarity.UNCOMMON)
                        .stacksTo(1).fireResistant());
    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving)
    {
        if (ThreadLocalRandom.current().nextFloat() < 0.05F && !pLevel.isClientSide) {
            this.spawnSilentGhost(pLevel, pEntityLiving.position());
        }
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int p_41407_, boolean p_41408_) {
        if (pEntity.tickCount % 20 == 0 && ThreadLocalRandom.current().nextFloat() < 0.001F) {
            pEntity.playSound(SoundEvents.SOUL_ESCAPE);
        }
    }
}
