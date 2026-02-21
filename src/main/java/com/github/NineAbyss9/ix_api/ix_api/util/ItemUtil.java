
package com.github.NineAbyss9.ix_api.ix_api.util;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.PAMAreNonnullByDefault;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;

@PAMAreNonnullByDefault
public class ItemUtil {
    public ItemUtil() {
    }

    public static void shrink(ItemStack stack, Entity entity, int count) {
        if (entity instanceof Player player) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(count);
            }
        } else {
            stack.shrink(count);
        }
    }

    public static void shrink(ItemStack stack, Entity player) {
        shrink(stack, player, 1);
    }

    public static void addCooldown(Player player, Item item, int pCooldown) {
        if (!player.getAbilities().instabuild) {
            player.getCooldowns().addCooldown(item, pCooldown);
        }
    }

    public static Tier getTier(int uses, float speed, float damage, int level, int EV, Ingredient ingredient) {
        return new TierInstance(uses, speed, damage, level, EV, ingredient);
    }

    public static ArmorMaterial getMaterial(int pDurability, int pDefense, int value, SoundEvent event, Ingredient craft,
                                            String name, float tou, float resistance) {
        return new ArmorMaterialInstance(pDurability, pDefense, value, event, craft, name, tou, resistance);
    }

    public static class TierInstance implements Tier {
        int uses, level, EV; float speed, damage; Ingredient ingredient;

        public TierInstance(int uses, float diggingSpeed, float damage, int level, int EV, Ingredient ingredient) {
            this.uses = uses;
            this.speed = diggingSpeed;
            this.damage = damage;
            this.level = level;
            this.EV = EV;
            this.ingredient = ingredient;
        }

        public int getUses() {
            return this.uses;
        }

        public float getSpeed() {
            return this.speed;
        }

        public float getAttackDamageBonus() {
            return this.damage;
        }

        public int getLevel() {
            return this.level;
        }

        public int getEnchantmentValue() {
            return this.EV;
        }

        public Ingredient getRepairIngredient() {
            return this.ingredient;
        }
    }

    public static class ArmorMaterialInstance implements ArmorMaterial {
        private final int durability, defense, enchantmentValue;
        private final SoundEvent equipSound;
        private final Ingredient repairCraft;
        private final String name;
        private final float toughness, knockbackResistance;

        public ArmorMaterialInstance(int pDurability, int pDefense, int value, SoundEvent event, Ingredient craft, String name,
                                     float tou, float resistance) {
            this.durability = pDurability;
            this.defense = pDefense;
            this.enchantmentValue = value;
            this.equipSound = event;
            this.repairCraft = craft;
            this.name = name;
            this.toughness = tou;
            this.knockbackResistance = resistance;
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return this.durability;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return this.defense;
        }

        @Override
        public int getEnchantmentValue() {
            return this.enchantmentValue;
        }

        @Override
        public SoundEvent getEquipSound() {
            return this.equipSound;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairCraft;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public float getToughness() {
            return this.toughness;
        }

        @Override
        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }
    }
}
