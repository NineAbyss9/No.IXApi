
package com.bilibili.player_ix.noixmod_api.register;

import org.NineAbyss9.annotation.PAMAreNonnullByDefault;
import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("all")
@PAMAreNonnullByDefault
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NoixmodAPITabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB, NoixmodAPI.MOD_ID);
    public static final RegistryObject<CreativeModeTab> NOIXAPI = REGISTRY.register("noixapi",
            ()-> CreativeModeTab.builder().title(Component.translatable("item_group.noixmodapi.noixapi")
            .withStyle(ChatFormatting.DARK_PURPLE)).icon(()-> new ItemStack(
            NoixmodAPIItems.HALO_OF_APOSTLE.get()
    )).withSearchBar().displayItems((itemDisplayParameters, output) -> {
        output.accept(NoixmodAPIItems.ALTAR.get());
        output.accept(NoixmodAPIItems.CURSED_CHEST.get());
        output.accept(NoixmodAPIItems.INFERNAL_IRON_ANVIL.get());
        output.accept(NoixmodAPIItems.OMINOUS_HEAD.get());
        output.accept(NoixmodAPIItems.SPIRIT_STONE_ORE.get());
        //output.accept(NoixmodAPIItems.TELEPORT_PLATE.get());
        output.accept(NoixmodAPIItems.WORM_BLOCK.get());
        output.accept(NoixmodAPIItems.WORM_DIRT_ITEM.get());
        output.accept(NoixmodAPIItems.OMINOUS_BOTTLE.get());
        output.accept(NoixmodAPIItems.BANNED_BOOK.get());
        output.accept(NoixmodAPIItems.BLOOD_BOTTLE.get());
        output.accept(NoixmodAPIItems.FRESH_SOUL.get());
        output.accept(NoixmodAPIItems.HALO_OF_APOSTLE.get());
        output.accept(NoixmodAPIItems.HEALING_DOLL.get());
        output.accept(NoixmodAPIItems.NIHILISTIC_ASH.get());
        output.accept(NoixmodAPIItems.NIHILISTIC_ESSENCE.get());
        output.accept(NoixmodAPIItems.PLATEAU_FRAGMENT.get());
        output.accept(NoixmodAPIItems.VAMPIRE_SOUL.get());
        output.accept(NoixmodAPIItems.VILLAGER_AMULET.get());
        output.accept(NoixmodAPIItems.WARDEN_DOLL.get());
        output.accept(NoixmodAPIItems.WITHER_DOLL.get());
        output.accept(NoixmodAPIItems.COPPER_NUGGET.get());
        output.accept(NoixmodAPIItems.GRAVE_AXE.get());
        output.accept(NoixmodAPIItems.GRAVE_SWORD.get());
        output.accept(NoixmodAPIItems.INFERNAL_IRON_INGOT.get());
        output.accept(NoixmodAPIItems.INFERNAL_IRON_AXE.get());
        output.accept(NoixmodAPIItems.INFERNAL_IRON_PICKAXE.get());
        output.accept(NoixmodAPIItems.INFERNAL_IRON_SWORD.get());
        output.accept(NoixmodAPIItems.SPIRIT_STONE.get());
        output.accept(NoixmodAPIItems.WIND_ESSENCE.get());
        output.accept(NoixmodAPIItems.WIND_SWORD.get());
        output.accept(NoixmodAPIItems.AXE_OF_HUNTER.get());
        output.accept(NoixmodAPIItems.BONE_SWORD.get());
        output.accept(NoixmodAPIItems.BOW_BOW.get());
        output.accept(NoixmodAPIItems.COPPER_AXE.get());
        output.accept(NoixmodAPIItems.COPPER_HOE.get());
        output.accept(NoixmodAPIItems.COPPER_PICKAXE.get());
        output.accept(NoixmodAPIItems.COPPER_SHOVEL.get());
        output.accept(NoixmodAPIItems.COPPER_SWORD.get());
        output.accept(NoixmodAPIItems.DISCARD_ITEM.get());;
        output.accept(NoixmodAPIItems.ITEM_KILLER.get());
        output.accept(NoixmodAPIItems.NIHILISTIC_FIREBALL.get());
        output.accept(NoixmodAPIItems.TELEPORTER.get());
        output.accept(NoixmodAPIItems.HEAD_HUNTERS_CROSSBOW.get());
        output.accept(NoixmodAPIItems.ICE_PICKAXE.get());
        output.accept(NoixmodAPIItems.MAGICAL_SWORD.get());
        output.accept(NoixmodAPIItems.STAR_SWORD.get());
        output.accept(NoixmodAPIItems.SPIRIT_STONE_HELMET.get());
        output.accept(NoixmodAPIItems.SPIRIT_STONE_CHESTPLATE.get());
        output.accept(NoixmodAPIItems.SPIRIT_STONE_LEGGINGS.get());
        output.accept(NoixmodAPIItems.SPIRIT_STONE_BOOTS.get());
        output.accept(NoixmodAPIItems.UNINVITED_SWORD.get());
        output.accept(NoixmodAPIItems.WORM_REAGENT.get());
        output.accept(NoixmodAPIItems.OMINOUS_HORN.get());
        output.accept(NoixmodAPIItems.TARGET_SELECTOR.get());
        output.accept(NoixmodAPIItems.COOKED_ROTTEN_FLESH.get());
        output.accept(NoixmodAPIItems.WINE.get());
        output.accept(NoixmodAPIItems.ABOMINATION_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.ABYSS_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.AMBUSHER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.APOSTLE_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.ARMORER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.BIOLOGIST_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.CULTIST_SPAWN_EGG.get());
        //output.accept(NoixmodAPIItems.DAN_ZHEN_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.DEAD_ILLAGER_SKULL.get());
        output.accept(NoixmodAPIItems.DETRACTOR_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.DRUNKENNESS_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.EVIL_SUMMONER_SPAWN_EGG.get());
        //output.accept(NoixmodAPIItems.EVOKER_ILLAGER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.EXORCIST_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.FLAGMAN_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.FREAKY_WORM_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.GRAVE_KEEPER_SPAWN_EGG.get());
        //output.accept(NoixmodAPIItems.HEAD_HUNTER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.HUNTER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.INTRUDER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.MOURNER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.NETHER_SOUL_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.NIHILISTIC_DEATH_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.NIHILISTIC_EVOKER_SPAWN_EGG.get());
        //output.accept(NoixmodAPIItems.NIHILITY_LORD_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.NIHILISTIC_WITHER_BOSS_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.PLATEAU_BEAST_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.PRIEST_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.SHADOW_WALKER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.STAR_GUARDIAN_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.SUICIDE_ZOMBIE_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.SUPERSTITIOUS_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.SWORD_CULTIST_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.TRUMPETER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.VAMPIRE_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.VILLAGER_EVOKER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.VILLAGER_MASTER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.VILLAGER_SPELLCASTER_SPAWN_EGG.get());
        output.accept(NoixmodAPIItems.WORM_MASTER_SPAWN_EGG.get());
    }).build());
    /*public static final RegistryObject<CreativeModeTab> API_POTION
            = REGISTRY.register("api_potion",
            () ->CreativeModeTab.builder().icon(() -> new ItemStack(NoixmodAPIItems.OMINOUS_BOTTLE.get()
    )).withSearchBar().displayItems((displayParameters, output) -> {

    }).title(Component.literal("No.IXAPIPotion"))
                    .withTabsAfter(CreativeModeTabs.FOOD_AND_DRINKS).build());*/
    public static final RegistryObject<CreativeModeTab> API_SERVANT = REGISTRY.register(
            "noixapi_servant", ()->CreativeModeTab.builder().icon(()-> ItemStacks.of(NoixmodAPIItems
                    .ARCHER_SERVANT_SPAWN_EGG)).withSearchBar().title(Component.translatable(
                            "item_group.noixmodapi.api_servant")).displayItems((itemDisplayParameters, output) ->  {
                output.accept(NoixmodAPIItems.APOSTLE_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.ARCHER_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.AQUATIC_WORM_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.CREEPER_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.DROWNED_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.ENDERMAN_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.EVOKER_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.GIRL_GHOST_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.HEALING_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.LAVA_ZOMBIE_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.NEO_ILLAGER_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.NIHILISTIC_BLAZE_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.NIHILISTIC_GHAST_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.NIHILISTIC_WITHER_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.PILLAGER_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.SILVERFISH_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.VAMPIRE_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.VEX_ARCHER_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.VEX_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.VINDICATOR_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.WARDEN_SERVANT_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.WIND_ZOMBIE_SPAWN_EGG.get());
                output.accept(NoixmodAPIItems.WORM_SPAWN_EGG.get());
            }).build());

    private NoixmodAPITabs() {}

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
        if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            tabData.accept(NoixmodAPIItems.DISCARD_ITEM.get());
        }
    }
}
