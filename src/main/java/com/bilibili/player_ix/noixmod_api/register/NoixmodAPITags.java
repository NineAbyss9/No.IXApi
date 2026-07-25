
package com.bilibili.player_ix.noixmod_api.register;

import org.NineAbyss9.annotation.PAMAreNonnullByDefault;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Predicate;

@SuppressWarnings("unused")
@PAMAreNonnullByDefault
public class NoixmodAPITags {
    //MobEffectTags
    public static final Predicate<MobEffectInstance> CAN_NOT_EFFECT_APOSTLE;
    public static final Predicate<MobEffect> CAN_EFFECT_APOSTLE;

    //EntityTypeTags
    public static TagKey<EntityType<?>> NIHILISTIC_MOBS
            = entityTypeTag("nihilistic_mobs");
    public static TagKey<EntityType<?>> NOT_AFFECT_BY_WIND
            = entityTypeTag("not_affect_by_wind");
    public static TagKey<EntityType<?>> SILVER_FISHES
            = entityTypeTag("silver_fishes");

    //DamageTypeTags
    //public static TagKey<DamageType> NIHILISTIC;

    //BlockTags
    public static TagKey<Block> AMBUSHER_CAN_HIDE
            = blockTag("ambusher_can_hide");

    //ItemTags
    public static TagKey<Item> SUMMON_APOSTLE_ITEMS
            = itemTag("summon_apostle_items");
    public static TagKey<Item> SCULKS = itemTag("sculks");
    public static TagKey<Item> ICES = itemTag("ices");

    private NoixmodAPITags() {
    }

    public static void init() {
    }

    public static boolean canEffectApostleTest(MobEffect mobEffect) {
        return mobEffect == MobEffects.DAMAGE_RESISTANCE || mobEffect == MobEffects.HEAL ||
                mobEffect == MobEffects.MOVEMENT_SPEED || mobEffect == MobEffects.REGENERATION ||
                mobEffect == MobEffects.DIG_SPEED;
    }

    public static TagKey<EntityType<?>> entityTypeTag(String st) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(NoixmodAPI.MOD_ID, st));
    }

    public static TagKey<Item> itemTag(String st) {
        return ItemTags.create(new ResourceLocation(NoixmodAPI.MOD_ID, st));
    }

    public static TagKey<Block> blockTag(String st) {
        return BlockTags.create(new ResourceLocation(NoixmodAPI.MOD_ID, st));
    }

    public static TagKey<DamageType> damageTag(String name) {
        return createDamageTag(NoixmodAPI.location(name));
    }

    public static TagKey<DamageType> createDamageTag(ResourceLocation location) {
        return TagKey.create(Registries.DAMAGE_TYPE, location);
    }

    static {
        //MobEffectTags
        CAN_EFFECT_APOSTLE = NoixmodAPITags::canEffectApostleTest;
        CAN_NOT_EFFECT_APOSTLE = instance -> !NoixmodAPITags
                .canEffectApostleTest(instance.getEffect());
        //DamageTypeTags
        //NIHILISTIC = damageTag("nihilistic");
    }
}
