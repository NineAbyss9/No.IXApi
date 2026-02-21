
package com.bilibili.player_ix.noixmod_api.item.weapon;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;

public class BoneSword extends ApiSword {
    public BoneSword() {
        super(199, 4f, 4f, 0,  20, Ingredient.of(Items.BONE), 1,
                -1.8f, new Properties().rarity(Rarity.UNCOMMON).stacksTo(1).fireResistant());
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        itemEntity.playSound(SoundEvents.BONE_BLOCK_BREAK);
        super.onDestroyed(itemEntity, damageSource);
    }
}
