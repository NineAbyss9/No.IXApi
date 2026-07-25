
package com.bilibili.player_ix.noixmod_api.item.enchantment;

import com.github.NineAbyss9.ix_api.api.mobs.ApiMobType;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.concurrent.ThreadLocalRandom;

public class NihilisticKillerEnchantment extends Enchantment {
    public NihilisticKillerEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.values());
    }

    public int getMinCost(int pLevel) {
        return 1 + (pLevel - 1) * 12;
    }

    public int getMaxCost(int pLevel) {
        return this.getMinCost(pLevel) + 17;
    }

    public int getMaxLevel() {
        return 9;
    }

    public boolean canEnchant(ItemStack p_44689_) {
        if (p_44689_.getItem() instanceof AxeItem) {
            return true;
        }
        return super.canEnchant(p_44689_);
    }

    @SuppressWarnings("deprecation")
    public float getDamageBonus(int pLevel, MobType pType) {
        if (ApiMobType.isNihilistic(pType)) {
            return Maths.smite(pLevel);
        }
        return 0.0f;
    }

    public void doPostHurt(LivingEntity pTarget, Entity pAttacker, int pLevel) {
        if (!(pAttacker instanceof LivingEntity $$3) || !ApiMobType.isNihilistic($$3.getMobType())) {
            return;
        }
        $$3.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
                20 + ThreadLocalRandom.current().nextInt(10 * pLevel), 3));
    }
}
