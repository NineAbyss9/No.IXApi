
package com.bilibili.player_ix.noixmod_api.item.enchantment;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiMobType;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
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

public class NihilisticKillerEnchantment extends Enchantment {
    public NihilisticKillerEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.values());
    }

    public int getMinCost(int p_44572_) {
        return 1 + (p_44572_ - 1) * 12;
    }

    public int getMaxCost(int p_44574_) {
        return this.getMinCost(p_44574_) + 17;
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
    public float getDamageBonus(int p_44682_, MobType p_44683_) {
        if (ApiMobType.isNihilistic(p_44683_)) {
            return Maths.smite(p_44682_);
        }
        return 0.0f;
    }

    public void doPostHurt(LivingEntity p_44692_, Entity p_44693_, int p_44694_) {
        if (p_44693_ instanceof LivingEntity $$3 && ApiMobType.isNihilistic($$3.getMobType())){
            int $$4 = 20 + p_44692_.getRandom().nextInt(10 * p_44694_);
            $$3.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, $$4, 3));
        }
    }
}
