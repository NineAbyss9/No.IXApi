
package com.bilibili.player_ix.noixmod_api.api.item;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPITags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum ApiTier implements Tier {
    COPPER(100, 5F, 4F, 1, 10,
            Ingredient.of(Items.COPPER_INGOT)),
    NETHERITE(2891, 9.9F, 4.9F, 4, 12,
            Ingredient.of(NoixmodAPIItems.INFERNAL_IRON_INGOT.get())),
    ICE(899, 7.0F, 3.F, 3, 16, Ingredient.of(NoixmodAPITags.ICES));
    final int uses;
    final int level;
    final int ev;
    final float speed;
    final float damage;
    final Ingredient ingredient;
    ApiTier(int pUses, float diggingSpeed, float pDamage, int pLevel, int enchantmentValue, Ingredient craft) {
        uses = pUses;
        speed = diggingSpeed;
        damage = pDamage;
        level = pLevel;
        ev = enchantmentValue;
        ingredient = craft;
    }

    public int getUses() {
        return uses;
    }

    public float getSpeed() {
        return speed;
    }

    public float getAttackDamageBonus() {
        return damage;
    }

    public int getLevel() {
        return level;
    }

    public int getEnchantmentValue() {
        return ev;
    }

    public Ingredient getRepairIngredient() {
        return ingredient;
    }
}
