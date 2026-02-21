
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.github.NineAbyss9.ix_api.ix_api.util.ItemUtil;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ApiAxe
extends AxeItem {
    public ApiAxe(int uses, float miningSpeed, float damage, int level, int EV, Ingredient craft, int attackDamage,
                  float attackSpeed, Properties p_43272_) {
        this(ItemUtil.getTier(uses, miningSpeed, damage, level, EV, craft), attackDamage, attackSpeed, p_43272_);
    }

    public ApiAxe(Tier tier, float p_40522_, float p_40523_, Properties p_40524_) {
        super(tier, p_40522_, p_40523_, p_40524_);
    }
}
