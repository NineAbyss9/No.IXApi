
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class VampireSoul
extends RitualSupplies {
    public VampireSoul() {
        super(new Properties().stacksTo(64).fireResistant());
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if (!p_41432_.isClientSide) {
            ISpell spell = Spells.VAMPIRE.get();
            spell.castSpell((ServerLevel)p_41432_, p_41433_);
        }
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }
}
