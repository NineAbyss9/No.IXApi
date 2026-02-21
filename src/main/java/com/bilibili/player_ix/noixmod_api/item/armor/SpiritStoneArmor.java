
package com.bilibili.player_ix.noixmod_api.item.armor;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.github.NineAbyss9.ix_api.ix_api.util.ItemUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class SpiritStoneArmor
extends ArmorItem {
    public static final SoundEvent WEAR_SOUND = SoundEvents.ARMOR_EQUIP_DIAMOND;
    public SpiritStoneArmor(ArmorMaterial p_40386_, Type p_266831_) {
        super(p_40386_, p_266831_, new Properties().fireResistant().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "noixmodapi:textures/models/armor/spirit_stone_armor.png";
    }

    public static class Helmet
    extends SpiritStoneArmor {
        public Helmet() {
            super(ItemUtil.getMaterial(1334, 5, 9, WEAR_SOUND
            , Ingredient.of(NoixmodAPIItems.SPIRIT_STONE.get()), "spirit_stone_helmet", 5F, 0.1F),
                    Type.HELMET);
        }
    }

    public static class ChestPlate
    extends SpiritStoneArmor {
        public ChestPlate() {
            super(ItemUtil.getMaterial(1899, 9, 10, WEAR_SOUND,
                    Ingredient.of(NoixmodAPIItems.SPIRIT_STONE.get()), "spirit_stone_chestplate",
                    9F, 0.2F), Type.CHESTPLATE);
        }
    }

    public static class Leggings
    extends SpiritStoneArmor {
        public Leggings() {
            super(ItemUtil.getMaterial(1678, 7, 9, WEAR_SOUND,
                    Ingredient.of(NoixmodAPIItems.SPIRIT_STONE.get()), "spirit_stone_leggings",
                    8F, 0.1F), Type.LEGGINGS);
        }

        @Nullable
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "noixmodapi:textures/models/armor/spirit_stone_armor_layer.png";
        }
    }

    public static class Boots
    extends SpiritStoneArmor {
        public Boots() {
            super(ItemUtil.getMaterial(1556, 4, 7, WEAR_SOUND,
                    Ingredient.of(NoixmodAPIItems.SPIRIT_STONE.get()), "spirit_stone_boots",
                    3F, 0.1F), Type.BOOTS);
        }
    }
}
