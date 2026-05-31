
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.github.NineAbyss9.ix_api.util.Colors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class NihilisticStaff
extends Staff {
    public NihilisticStaff() {
        super(new Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1));
    }

    public int getUseDuration(ItemStack pStack)
    {
        return 90;
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged)
    {
        int i = pStack.getUseDuration() - pTimeCharged;
        if (!pLevel.isClientSide) {
            this.castSpell((ServerLevel)pLevel, pLivingEntity, i);
        }
        if (pLivingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(this, i > 70 ? 60 : 40);
        }
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pUsedTime) {
        ISpell spell;
        if (pUsedTime > 70) {
            spell = Spells.NIHILISTIC_SERVANT.get();
        } else if (pUsedTime > 50) {
            spell = Spells.CRACK.get();
        } else if (pUsedTime > 30) {
            pCaster.heal(5.0F);
            return;
        } else {
            spell = Spells.NIHILISTIC_ROAR.get();
        }
        spell.castSpell(pLevel, pCaster);
    }

    public float[] getSpellColor()
    {
        return Colors.DARK_PURPLE;
    }
}
