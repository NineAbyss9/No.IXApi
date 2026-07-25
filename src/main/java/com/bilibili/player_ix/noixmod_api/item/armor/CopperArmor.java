
package com.bilibili.player_ix.noixmod_api.item.armor;

import com.bilibili.player_ix.noixmod_api.api.item.ApiArmorMaterials;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CopperArmor
extends ArmorItem
{
    public CopperArmor(Type pType)
    {
        super(ApiArmorMaterials.COPPER, pType, new Item.Properties());
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
    {
        return "noixmodapi:textures/models/armor/copper_armor.png";
    }

    public static class Leggings
    extends CopperArmor
    {
        public Leggings()
        {
            super(Type.LEGGINGS);
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
        {
            return "noixmodapi:textures/models/armor/copper_armor_layer.png";
        }
    }
}
