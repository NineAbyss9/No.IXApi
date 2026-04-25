
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.entities.servant.CreeperServant;
import com.github.NineAbyss9.ix_api.api.item.ApiSpawnEgg;
import com.github.NineAbyss9.ix_api.api.item.UseItem;
import org.NineAbyss9.annotation.PAMAreNonnullByDefault;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.item.*;
import com.bilibili.player_ix.noixmod_api.item.armor.SpiritStoneArmor;
import com.bilibili.player_ix.noixmod_api.item.copper.*;
import com.bilibili.player_ix.noixmod_api.item.food.CookedRottenFlesh;
import com.bilibili.player_ix.noixmod_api.item.food.GoldenRabbitFoot;
import com.bilibili.player_ix.noixmod_api.item.food.Wine;
import com.bilibili.player_ix.noixmod_api.item.grave.GraveAxe;
import com.bilibili.player_ix.noixmod_api.item.grave.GraveSword;
import com.bilibili.player_ix.noixmod_api.item.magic.HealingDoll;
import com.bilibili.player_ix.noixmod_api.item.magic.WormReagent;
import com.bilibili.player_ix.noixmod_api.item.ritual.BannedBook;
import com.bilibili.player_ix.noixmod_api.item.plot.StartItem;
import com.bilibili.player_ix.noixmod_api.item.potion.OminousBottle;
import com.bilibili.player_ix.noixmod_api.item.ritual.*;
import com.bilibili.player_ix.noixmod_api.item.util.*;
import com.bilibili.player_ix.noixmod_api.item.util.InfernalIronAxe;
import com.bilibili.player_ix.noixmod_api.item.weapon.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@PAMAreNonnullByDefault
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NoixmodAPIItems {
    public static Set<RegistryObject<? extends Item>> SPAWN_EGGS = new LinkedHashSet<>();
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, NoixmodAPI.MOD_ID);

    //Spawn eggs
    public static final RegistryObject<Item> ABOMINATION_SPAWN_EGG = spawnEggItem("abomination", NoixmodAPIEntities.ABOMINATION,
            -10066330, -10092544);
    public static final RegistryObject<Item> ABYSS_SPAWN_EGG = spawnEggItem("abyss", NoixmodAPIEntities.ABYSS,
            -10092544, -10092544, Rarity.EPIC);
    public static final RegistryObject<Item> AMBUSHER_SPAWN_EGG = spawnEggItem("ambusher", NoixmodAPIEntities.AMBUSHER,
            5651507, 12422002);
    public static final RegistryObject<Item> APOSTLE_SPAWN_EGG = REGISTRY.register("apostle_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.APOSTLE, 512, -10092442, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> APOSTLE_SERVANT_SPAWN_EGG = REGISTRY.register("apostle_servant_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.APOSTLE_SERVANT, 512, -10092442, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> AQUATIC_WORM_SPAWN_EGG = REGISTRY.register("aquatic_worm_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.AQUATIC_WORM, -16777063, -16737895, new Item.Properties()));
    public static final RegistryObject<Item> ARCHER_SERVANT_SPAWN_EGG = spawnEggItem("archer_servant",
            NoixmodAPIEntities.ARCHER_SERVANT, 5451574, 9804699);
    public static final RegistryObject<Item> ARMORER_SPAWN_EGG = REGISTRY.register("armorer_spawn_egg",
            ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.ARMORER, 9804699, 2580065, new Item.Properties()));
    public static final RegistryObject<Item> BONE_SWORD = REGISTRY.register("bone_sword", BoneSword::new);
    public static final RegistryObject<Item> CREEPER_SERVANT_SPAWN_EGG = spawnEggItem("creeper_servant",
            NoixmodAPIEntities.CREEPER_SERVANT, 894731, 0);
    public static final RegistryObject<Item> CULTIST_SPAWN_EGG = REGISTRY.register("cultist_spawn_egg", ()->
            new ForgeSpawnEggItem(NoixmodAPIEntities.CULTIST, -10092442, -13434727,
                    properties()));
    public static final RegistryObject<Item> DAN_ZHEN_SPAWN_EGG = spawnEggItem("dan_zhen", NoixmodAPIEntities.DAN_ZHEN,
            -10092442, -16777211);
    public static final RegistryObject<Item> DETRACTOR_SPAWN_EGG = spawnEggItem("detractor", NoixmodAPIEntities.DETRACTOR,
            -10092532, -16777211);
    public static final RegistryObject<Item> DEAD_ILLAGER_SKULL = spawnEggItem("dead_illager_skull", NoixmodAPIEntities.DEAD_ILLAGER_SKULL,
            12698049, 1001033);
    public static final RegistryObject<Item> DROWNED_SERVANT_SPAWN_EGG = REGISTRY.register("drowned_servant_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.DROWNED_SERVANT, 9433559, 7969893, new Item.Properties()));
    public static final RegistryObject<Item> ELDER_G_S_E = apiSpawnEgg("elder_guardian_servant", NoixmodAPIEntities.ELDER_G_S,
            13552826, 7632531);
    public static final RegistryObject<Item> EVIL_SUMMONER_SPAWN_EGG = spawnEggItem("evil_summoner", NoixmodAPIEntities.EVIL_SUMMONER,
            -10092442, -10092544, Rarity.RARE);
    public static final RegistryObject<Item> EVOKER_ILLAGER_SPAWN_EGG = REGISTRY.register("evoker_illager_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.EVOKER_ILLAGER, -13421773, -6710887, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> EVOKER_SERVANT_SPAWN_EGG = spawnEggItem("evoker_servant", NoixmodAPIEntities.EVOKER_SERVANT,
            9804699, 1973274);
    public static final RegistryObject<Item> FLAGMAN_SPAWN_EGG = spawnEggItem("flagman", NoixmodAPIEntities.FLAGMAN,
            9804699, 2580065);
    public static final RegistryObject<Item> FREAKY_WORM_SPAWN_EGG = spawnEggItem("freaky_worm", NoixmodAPIEntities.FREAKY_WORM,
            13421773, -10092442);
    public static final RegistryObject<Item> GUARDIAN_S_E = apiSpawnEgg("guardian_servant", NoixmodAPIEntities.GUARDIAN_S,
            5931634, 15826224);
    public static final RegistryObject<Item> HEAD_HUNTER_SPAWN_EGG = spawnEggItem("head_hunter", NoixmodAPIEntities.HEAD_HUNTER,
            10051367, 12623485, Rarity.RARE);
    public static final RegistryObject<Item> HEALING_SPAWN_EGG = spawnEggItem("healling", NoixmodAPIEntities.HEALING,
            56063, 44543);
    public static final RegistryObject<Item> HEALING_DOLL = REGISTRY.register("healling_doll", HealingDoll::new);
    public static final RegistryObject<Item> INTRUDER_SPAWN_EGG = REGISTRY.register("intruder_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.INTRUDER, 9804677, 1973267, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENDERMAN_SERVANT_SPAWN_EGG = REGISTRY.register("enderman_servant_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.ENDER_MAN_SERVANT,
            1447446, 0, new Item.Properties()));
    public static final RegistryObject<Item> LAVA_ZOMBIE_SERVANT_SPAWN_EGG = REGISTRY.register("lava_zombie_servant_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.LAVA_ZOMBIE_SERVANT, 12623485, 10051392, new Item.Properties()));
    public static final RegistryObject<Item> NEO_ILLAGER_SPAWN_EGG = spawnEggItem("neo_illager",
            NoixmodAPIEntities.NEO_ILLAGER, 9804699, 2580065);
    public static final RegistryObject<Item> NETHER_SOUL_SPAWN_EGG = spawnEggItem("nether_soul", NoixmodAPIEntities.NETHER_SOUL,
            12623485, 10051300);
    //public static final RegistryObject<Item> NIHILITY_LORD_SPAWN_EGG = REGISTRY.init("nihility_lord_spawn_egg", () -> new ForgeSpawnEggItem(NoixmodAPIEntities.NIHILISTIC_LORD, -10092442, -13434727, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> NIHILISTIC_WITHER_BOSS_SPAWN_EGG = spawnEggItem("nihilistic_wither_boss", NoixmodAPIEntities.NIHILISTIC_WITHER_BOSS,
            1315860, 5075616, Rarity.RARE);
    public static final RegistryObject<Item> NIHILISTIC_WITHER_SPAWN_EGG = spawnEggItem("nihilistic_wither", NoixmodAPIEntities.NIHILISTIC_WITHER,
            1315860, 5075616, Rarity.RARE);
    public static final RegistryObject<Item> PILLAGER_SERVANT_SPAWN_EGG = spawnEggItem("pillager_servant",
            NoixmodAPIEntities.PILLAGER_SERVANT, 5451574, 9804699);
    public static final RegistryObject<Item> PRIEST_SPAWN_EGG = spawnEggItem("priest",
            NoixmodAPIEntities.PRIEST, -10092442, 0, Rarity.RARE);
    public static final RegistryObject<Item> SHADOW_WALKER_SPAWN_EGG = spawnEggItem("shadow_walker",
            NoixmodAPIEntities.SHADOW_WALKER, -10092442, -13434727, Rarity.UNCOMMON);
    public static final RegistryObject<Item> SILVERFISH_SERVANT_SPAWN_EGG = spawnEggItem("silverfish_servant",
            NoixmodAPIEntities.SILVERFISH_SERVANT, 7237230, 3158064);
    public static final RegistryObject<Item> STAR_GUARDIAN_SPAWN_EGG = spawnEggItem("star_guardian", NoixmodAPIEntities.STAR_GUARDIAN,
            14283506, 1001033, Rarity.EPIC);
    public static final RegistryObject<Item> SUICIDE_ZOMBIE_SPAWN_EGG = spawnEggItem("suicide_zombie", NoixmodAPIEntities.SUICIDE_ZOMBIE,
            44975, 5075616);
    public static final RegistryObject<Item> SUPERSTITIOUS_SPAWN_EGG = spawnEggItem(
            "superstitious", NoixmodAPIEntities.SUPERSTITIOUS, -10092442, 12623485
    );
    public static final RegistryObject<Item> SWORD_CULTIST_SPAWN_EGG = spawnEggItem("sword_cultist", NoixmodAPIEntities.SWORD_CULTIST,
            -10092442, -13434727);
    public static final RegistryObject<Item> TRUMPETER_SPAWN_EGG = REGISTRY.register("trumpeter_spawn_egg", () -> new ForgeSpawnEggItem(NoixmodAPIEntities.BUGLER, 9804699, 2580065, new Item.Properties()));
    public static final RegistryObject<Item> VAMPIRE_SERVANT_SPAWN_EGG =
            spawnEggItem("vampire_servant", NoixmodAPIEntities.VAMPIRE_SERVANT, -10092544, -16777216);
    public static final RegistryObject<Item> VAMPIRE_SOUL = REGISTRY.register("vampire_soul", VampireSoul::new);
    public static final RegistryObject<Item> VAMPIRE_SPAWN_EGG = REGISTRY.register("vampire_spawn_egg",
            ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.VAMPIRE, -10092544, -16777216, new Item.Properties()));
    public static final RegistryObject<Item> VEX_ARCHER_SPAWN_EGG = spawnEggItem("vex_archer", NoixmodAPIEntities.VEX_ARCHER,
            8032420, 15265265);
    public static final RegistryObject<Item> VEX_SERVANT_SPAWN_EGG = spawnEggItem("vex_servant", NoixmodAPIEntities.VEX_SERVANT,
            8032420, 15265265);
    public static final RegistryObject<Item> VINDICATOR_SERVANT_SPAWN_EGG = spawnEggItem("vindicator_servant", NoixmodAPIEntities.VINDICATOR_SERVANT,
            9804699, 2580065);
    public static final RegistryObject<Item> VILLAGER_MASTER_SPAWN_EGG = REGISTRY.register("villager_master_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.VILLAGER_MASTER, -10066330, -6710887, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> VILLAGER_SPELLCASTER_SPAWN_EGG = REGISTRY.register("villager_spellcaster_spawn_egg", () -> new ForgeSpawnEggItem(NoixmodAPIEntities.VILLAGER_SPELLCASTER, -10066330, -6710887, new Item.Properties()));
    public static final RegistryObject<Item> WARDEN_SERVANT_SPAWN_EGG = spawnEggItem("warden_servant", NoixmodAPIEntities.WARDEN_SERVANT,
            1001033, 3790560);
    public static final RegistryObject<Item> WIND_ZOMBIE_SPAWN_EGG = spawnEggItem("wind_zombie", NoixmodAPIEntities.WIND_ZOMBIE,
            11506911, 9529055);
    public static final RegistryObject<Item> WORM_MASTER_SPAWN_EGG = spawnEggItem("worm_master", NoixmodAPIEntities.WORM_MASTER,
            -10066432, -16764160, Rarity.RARE);
    public static final RegistryObject<Item> YETI_SPAWN_EGG = apiSpawnEgg("yeti", NoixmodAPIEntities.YETI, 5592575, 11184810);

    //Ores
    public static final RegistryObject<Item> INFERNAL_IRON_INGOT = REGISTRY.register("infernal_iron_ingot", ()->
            new Item(properties()));
    //public static final RegistryObject<Item> NIHILISTIC_GEM = REGISTRY.register("nihilistic_gem", ()-> new Item(
            //properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> SPIRIT_STONE = REGISTRY.register("spirit_stone", () ->
            new Item(properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WIND_ESSENCE = REGISTRY.register("wind_essence",
            ()-> new Item(properties()));

    //Utils
    public static final RegistryObject<Item> COPPER_AXE = REGISTRY.register("copper_axe", CopperAxe::new);
    public static final RegistryObject<Item> COPPER_HOE = REGISTRY.register("copper_hoe", CopperHoe::new);
    public static final RegistryObject<Item> COPPER_NUGGET = REGISTRY.register("copper_nugget", CopperNugget::new);
    public static final RegistryObject<Item> COPPER_PICKAXE = REGISTRY.register("copper_pickaxe", CopperPickaxe::new);
    public static final RegistryObject<Item> COPPER_SHOVEL = REGISTRY.register("copper_shovel", CopperShovel::new);
    public static final RegistryObject<Item> COPPER_SWORD = REGISTRY.register("copper_sword", CopperSword::new);
    public static final RegistryObject<Item> GRAVE_AXE = REGISTRY.register("grave_axe", GraveAxe::new);
    public static final RegistryObject<Item> ICE_PICKAXE = REGISTRY.register("ice_pickaxe", IcePickaxe::new);
    public static final RegistryObject<Item> INFERNAL_IRON_PICKAXE = REGISTRY.register("infernal_iron_pickaxe",
            InfernalIronPickaxe::new);
    public static final RegistryObject<Item> MAGICAL_SWORD = REGISTRY.register("magical_sword", MagicalSword::new);
    public static final RegistryObject<Item> STAR_SWORD = REGISTRY.register("star_sword", StarSword::new);
    public static final RegistryObject<Item> WORM_REAGENT = REGISTRY.register("worm_reagent", WormReagent::new);

    //CreativeOnly
    public static final RegistryObject<Item> DISCARD_ITEM = REGISTRY.register("discard_item", DiscardItem::new);
    public static final RegistryObject<Item> TELEPORTER = REGISTRY.register("teleporter", Teleporter::new);

    //Food
    public static final RegistryObject<Item> COOKED_ROTTEN_FLESH = REGISTRY.register("cooked_rotten_flesh",
            CookedRottenFlesh::new);
    public static final RegistryObject<Item> GOLDEN_RABBIT_FOOT = REGISTRY.register("golden_rabbit_foot", GoldenRabbitFoot::new);

    //Weapon
    public static final RegistryObject<Item> AXE_OF_HUNTER = REGISTRY.register("hunters_axe",
            AxeOfHunter::new);
    public static final RegistryObject<Item> BOW_BOW = REGISTRY.register("bow_bow", BowBow::new);
    public static final RegistryObject<Item> GRAVE_SWORD = REGISTRY.register(sword("grave"), GraveSword::new);
    public static final RegistryObject<Item> HEAD_HUNTERS_CROSSBOW = REGISTRY.register("head_hunters_crossbow",
            HeadHuntersCrossbow::new);
    public static final RegistryObject<Item> INFERNAL_IRON_AXE = REGISTRY.register("infernal_iron_axe",
            InfernalIronAxe::new);
    public static final RegistryObject<Item> INFERNAL_IRON_SWORD = REGISTRY.register("infernal_iron_sword",
            InfernalIronSword::new);
    public static final RegistryObject<Item> UNINVITED_SWORD = REGISTRY.register("uninvited_sword",
            UninvitedSword::new);
    public static final RegistryObject<Item> WIND_SWORD = REGISTRY.register(sword("wind"), WindSword::new);

    //Misc
    public static final RegistryObject<Item> VILLAGER_AMULET = REGISTRY.register("villager_amulet",
            VillagerAmulet::new);

    //Armor
    public static final RegistryObject<Item> SPIRIT_STONE_BOOTS = REGISTRY.register("spirit_stone_boots",
            SpiritStoneArmor.Boots::new);
    public static final RegistryObject<Item> SPIRIT_STONE_CHESTPLATE = REGISTRY.register("spirit_stone_chestplate",
            SpiritStoneArmor.ChestPlate::new);
    public static final RegistryObject<Item> SPIRIT_STONE_HELMET = REGISTRY.register("spirit_stone_helmet",
            SpiritStoneArmor.Helmet::new);
    public static final RegistryObject<Item> SPIRIT_STONE_LEGGINGS = REGISTRY.register("spirit_stone_leggings",
            SpiritStoneArmor.Leggings::new);

    //Potion
    public static final RegistryObject<Item> OMINOUS_BOTTLE = REGISTRY.register("ominous_bottle", OminousBottle::new);

    public static final RegistryObject<Item> OMINOUS_HORN = REGISTRY.register("ominous_horn", OminousHorn::new);
    public static final RegistryObject<Item> VILLAGER_EVOKER_SPAWN_EGG = REGISTRY.register("villager_evoker_spawn_egg",
            () -> new ForgeSpawnEggItem(NoixmodAPIEntities.VILLAGER_EVOKER, -10066330, -6710887, new Item.Properties()));
    public static final RegistryObject<Item> NIHILISTIC_EVOKER_SPAWN_EGG = REGISTRY.register("nihilistic_evoker_spawn_egg",
            () -> new ForgeSpawnEggItem(NoixmodAPIEntities.NIHILISTIC_EVOKER, -10092442, -13434727,
                    properties()));
    public static final RegistryObject<Item> PLATEAU_BEAST_SPAWN_EGG = REGISTRY.register("plateau_beast_spawn_egg",
            ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.PLATEAU_BEAST, 0xFFFFFF, 0xFFFFFF, properties()));
    public static final RegistryObject<Item> NIHILISTIC_GHAST_SPAWN_EGG = REGISTRY.register("nihilistic_ghast_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.NIHILISTIC_GHAST, -10092442, -13434727, new Item.Properties()));
    public static final RegistryObject<Item> BIOLOGIST_SPAWN_EGG = REGISTRY.register("biologist_spawn_egg", () -> new ForgeSpawnEggItem(NoixmodAPIEntities.BIOLOGIST, 0x272727, 0xDCDCDC, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WORM_SPAWN_EGG = REGISTRY.register("worm_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.WORM, -10066432, -16764160, new Item.Properties()));
    public static final RegistryObject<Item> WORM_BLOCK = blockToItem(NoixmodAPIBlocks.WORM_BLOCK);
    public static final RegistryObject<Item> WORM_DIRT_ITEM = blockToItem("worm_dirt_item", NoixmodAPIBlocks.WORM_DIRT);
    public static final RegistryObject<Item> NIHILISTIC_FIREBALL = REGISTRY.register("nihilistic_fireball_item", NihilisticFireballItem::new);
    public static final RegistryObject<Item> TARGET_SELECTOR = REGISTRY.register("target_selector", TargetSelector::new);
    public static final RegistryObject<Item> NIHILISTIC_BLAZE_SPAWN_EGG = REGISTRY.register("nihilistic_blaze_spawn_egg", ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.NIHILISTIC_BLAZE, -10092000, -13434727, new Item.Properties()));
    public static final RegistryObject<Item> NIHILISTIC_DEATH_SPAWN_EGG = REGISTRY
            .register("nihilistic_death_spawn_egg", ()-> new ForgeSpawnEggItem(
                    NoixmodAPIEntities.NIHILISTIC_DEATH, -10092442, -13434727, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> NIHILISTIC_LORD_S_NOTE = REGISTRY.register(
            "s_note", StartItem::new
    );
    public static final RegistryObject<Item> GIRL_GHOST_SPAWN_EGG = REGISTRY.register("girl_ghost_spawn_egg",
            ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.GIRL_GHOST, 4547222,
                    1001033, properties()));
    public static final RegistryObject<Item> HUNTER_SPAWN_EGG = REGISTRY.register("hunter_spawn_egg",
            ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.HUNTER, 9804699, 10051367,
                    properties()));
    public static final RegistryObject<Item> EXORCIST_SPAWN_EGG = REGISTRY.register("exorcist_spawn_egg",
            ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.EXORCIST, 5651507,
                    12422002, properties()));
    public static final RegistryObject<Item> WINE = REGISTRY.register("wine", Wine::new);
    public static final RegistryObject<Item> DRUNKENNESS_SPAWN_EGG = REGISTRY.register(
            "drunkenness_spawn_egg",
            ()-> new ForgeSpawnEggItem(NoixmodAPIEntities.DRUNKENNESS, 9804699,
                    2580065, properties()));
    public static final RegistryObject<Item> MOURNER_SPAWN_EGG =
            spawnEggItem("mourner", NoixmodAPIEntities.MOURNER, 9804699, 44543);

    //Magic
    /// For index
    public static final Object MAGIC = null;
    public static final RegistryObject<Item> CREEPER_EGG
            = REGISTRY.register("creeper_egg", () -> new UseItem(properties().stacksTo(64),
            (level, player, interactionHand) -> {
                ItemStack stack = player.getItemInHand(interactionHand);
                stack.shrink(1);
                if (!level.isClientSide) {
                    CreeperServant servant = NoixmodAPIEntities.CREEPER_SERVANT.get().create(level);
                    servant.moveTo(player.position());
                    servant.setOwner(player);
                    level.addFreshEntity(servant);
                }
                return ItemUtils.startUsingInstantly(level, player, interactionHand);
            }));

    //Ritual
    /// For index
    public static final Object RITUAL = null;
    public static final RegistryObject<Item> BANNED_BOOK = REGISTRY.register("banned_book",
            BannedBook::new);
    public static final RegistryObject<Item> BLOOD_BOTTLE = REGISTRY.register("blood_bottle",
            BloodBottle::new);
    public static final RegistryObject<Item> FRESH_SOUL = REGISTRY.register("fresh_soul",
            FreshSoul::new);
    public static final RegistryObject<Item> HALO_OF_APOSTLE =
            REGISTRY.register("halo_of_apostle", HaloOfApostleItem::new);
    public static final RegistryObject<Item> ICE_CORE = REGISTRY.register("ice_core", IceCore::new);
    public static final RegistryObject<Item> NIHILISTIC_ASH = REGISTRY.register("nihilistic_ash",
            ()-> new Item(properties().rarity(Rarity.UNCOMMON).stacksTo(64)));
    public static final RegistryObject<Item> NIHILISTIC_ESSENCE = REGISTRY.register("nihilistic_essence",
            NihilisticEssence::new);
    public static final RegistryObject<Item> OMINOUS_HEAD = blockToItem(NoixmodAPIBlocks.OMINOUS_HEAD);
    public static final RegistryObject<Item> PLATEAU_FRAGMENT =
            REGISTRY.register("plateau_fragment", PlateauFragment::new);
    public static final RegistryObject<Item> WARDEN_DOLL = REGISTRY.register("warden_doll", WardenDoll::new);
    public static final RegistryObject<Item> WITHER_DOLL = REGISTRY.register("wither_doll", WitherDoll::new);

    //Blocks
    /// For index
    public static final Object BLOCKS = null;
    public static final RegistryObject<Item> ALTAR = REGISTRY.register("altar",
            ()-> new BlockItem(NoixmodAPIBlocks.ALTAR.get(), properties()) {
                public void appendHoverText(ItemStack p_40572_, @Nullable Level p_40573_, List<Component> p_40574_,
                                            TooltipFlag p_40575_) {
                    p_40574_.add(Component.literal("Not complete yet."));
                    super.appendHoverText(p_40572_, p_40573_, p_40574_, p_40575_);
                }
            });
    public static final RegistryObject<Item> BLOOD = blockToItem(NoixmodAPIBlocks.BLOOD);
    public static final RegistryObject<Item> CURSED_CHEST = blockToItem(NoixmodAPIBlocks.CURSED_CHEST);
    public static final RegistryObject<Item> INFERNAL_IRON_ANVIL = blockToItem(NoixmodAPIBlocks.INFERNAL_IRON_ANVIL);
    public static final RegistryObject<Item> SPIRIT_STONE_ORE = blockToItem(
            NoixmodAPIBlocks.SPIRIT_STONE_ORE
    );
    //public static final RegistryObject<Item> TELEPORT_PLATE = blockToItem(NoixmodAPIBlocks.TELEPORT_PLATE);


    private NoixmodAPIItems() {}

    public static final RegistryObject<Item> GRAVE_KEEPER_SPAWN_EGG
            = REGISTRY.register("grave_keeper_spawn_egg", ()-> new ForgeSpawnEggItem(
                    NoixmodAPIEntities.GRAVE_KEEPER, -16777216, -13697024,
            new Item.Properties().rarity(Rarity.UNCOMMON)));

    private static <T> Supplier<T> supplier(T t) {
        return ()->t;
    }

    @SuppressWarnings("DataFlowIssue")
    public static RegistryObject<Item> blockToItem(RegistryObject<Block> block) {
        return blockToItem(block.getId().getPath(), block);
    }

    public static RegistryObject<Item> blockToItem(String name, RegistryObject<Block> block) {
        return blockToItem(name, block, properties());
    }

    public static RegistryObject<Item> blockToItem(String name, RegistryObject<Block> block, Item.Properties properties) {
        return REGISTRY.register(name, ()-> new BlockItem(block.get(), properties));
    }

    public static Item.Properties properties() {
        return new Item.Properties();
    }

    public static RegistryObject<Item> spawnEggItem(String path, Supplier<? extends EntityType<? extends Mob>>
            supplier, int g, int b) {
        return REGISTRY.register(path + spawnEgg(), ()-> new ForgeSpawnEggItem(supplier,
                g, b, properties()));
    }

    public static RegistryObject<Item> spawnEggItem(String path, Supplier<? extends EntityType<? extends Mob>>
            supplier, int g, int b, Rarity rarity) {
        return spawnEggItem(path, supplier, g, b, properties().rarity(rarity));
    }

    public static RegistryObject<Item> spawnEggItem(String name, Supplier<? extends EntityType<? extends Mob>>
            supplier, int g, int b, Item.Properties properties) {
        return REGISTRY.register(name + spawnEgg(), ()-> new ForgeSpawnEggItem(supplier, g, b, properties));
    }

    public static RegistryObject<Item> apiSpawnEgg(String name, Supplier<? extends EntityType<? extends Mob>>
            supplier, int g, int b) {
        return apiSpawnEgg(name, supplier, g, b, properties());
    }

    public static RegistryObject<Item> apiSpawnEgg(String name, Supplier<? extends EntityType<? extends Mob>>
                                                   supplier, int g, int b, Item.Properties properties) {
        RegistryObject<Item> obj = REGISTRY.register(name + "_spawn_egg",
                () -> new ApiSpawnEgg(supplier, g, b, properties));
        SPAWN_EGGS.add(obj);
        return obj;
    }

    public static String sword(String name) {
        return name + "_sword";
    }

    public static String axe(String name) {
        return name + "_axe";
    }

    public static String spawnEgg() {
        return "_spawn_egg";
    }
}
