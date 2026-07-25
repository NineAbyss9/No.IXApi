
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;

public class AxeOfHunter
extends ApiAxe {
    public AxeOfHunter() {
        super(250, 5f, 5.5f, 2, 17, Ingredient.of(Items.IRON_INGOT),
                3, -3.0f, new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.level().isClientSide) {
            EntityEventHandler.broadcastEntityEvent(pTarget, 4);
            pTarget.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.TETANUS.get(), 40));
        }
        pStack.hurtAndBreak(1, pAttacker, (p_41007_) -> {
            p_41007_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }
}
