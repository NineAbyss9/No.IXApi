
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IllagerStaff
extends Staff
{
    public IllagerStaff(Properties pProperties)
    {
        super(pProperties);
    }

    public int getUseDuration(ItemStack pStack)
    {
        return 80;
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged)
    {
        if (!pLevel.isClientSide) {
            int i = pStack.getUseDuration() - pTimeCharged;
            this.castSpell((ServerLevel)pLevel, pLivingEntity, i);
        }
        if (pLivingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(this, 40);
        }
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pUsedTime)
    {
        ISpell spell;
        if (pUsedTime < 20) {
            spell = Spells.ESCAPE.get();
        } else if (pUsedTime < 40) {
            spell = Spells.SELF_FANGS.get();
        } else if (pUsedTime < 60) {
            spell = Spells.TARGET_FANGS.get();
        } else {
            spell = Spells.VEX_ARCHER.get();
        }
        spell.castSpell(pLevel, pCaster);
    }
}
