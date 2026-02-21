
package com.bilibili.player_ix.noixmod_api.item.magic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class WaterSpellBook
extends SpellBook {
    public WaterSpellBook() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        /*if (p_41433_.isShiftKeyDown()) {

        }*/
        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
