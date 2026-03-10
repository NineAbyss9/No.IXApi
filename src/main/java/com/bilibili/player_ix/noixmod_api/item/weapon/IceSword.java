
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.api.item.IIceItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class IceSword
extends SwordItem
implements IIceItem {
    public IceSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        this.freeze(pTarget);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
