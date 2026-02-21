
package com.bilibili.player_ix.noixmod_api.item.util;

import com.github.NineAbyss9.ix_api.util.ItemUtil;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ApiPickaxe extends PickaxeItem {
    public ApiPickaxe(int uses, float miningSpeed, float damage, int level, int EV, Ingredient ingredient, int attackDamage,
                      float attackSpeed, Properties properties) {
        this(ItemUtil.getTier(uses, miningSpeed, damage, level, EV, ingredient), attackDamage, attackSpeed, properties);
    }

    public ApiPickaxe(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }
}
