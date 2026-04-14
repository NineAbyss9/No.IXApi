
package com.bilibili.player_ix.noixmod_api.client;

import com.bilibili.player_ix.noixmod_api.client.model.horror.TheGhostModel;
import com.bilibili.player_ix.noixmod_api.client.model.illager.BiologistModel;
import com.bilibili.player_ix.noixmod_api.client.model.illager.EIModel;
import com.bilibili.player_ix.noixmod_api.client.model.illager.IXIllagerModel;
import com.bilibili.player_ix.noixmod_api.client.model.illager.IntruderModel;
import com.bilibili.player_ix.noixmod_api.client.model.nihilistic.*;
import com.bilibili.player_ix.noixmod_api.client.renderer.block.CursedChestR;
import com.bilibili.player_ix.noixmod_api.client.renderer.horror.*;
import com.bilibili.player_ix.noixmod_api.client.renderer.illager.*;
import com.bilibili.player_ix.noixmod_api.client.renderer.nb.*;
import com.bilibili.player_ix.noixmod_api.client.renderer.nihilist.*;
import com.bilibili.player_ix.noixmod_api.client.renderer.servant.*;
import com.bilibili.player_ix.noixmod_api.client.renderer.servant.ice.YetiRenderer;
import com.bilibili.player_ix.noixmod_api.client.renderer.servant.illager.*;
import com.bilibili.player_ix.noixmod_api.client.renderer.servant.nihilistic.ApostleServantRenderer;
import com.bilibili.player_ix.noixmod_api.client.renderer.servant.nihilistic.GolemRenderer;
import com.bilibili.player_ix.noixmod_api.client.renderer.servant.nihilistic.WrongedSoulRenderer;
import com.bilibili.player_ix.noixmod_api.client.renderer.villager.AmbusherRenderer;
import net.minecraft.world.item.Item;
import com.github.NineAbyss9.ix_api.api.renderer.BaseEntityRenderer;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.gui.ApiGuis;
import com.bilibili.player_ix.noixmod_api.client.gui.screen.AltarScreen;
import com.bilibili.player_ix.noixmod_api.client.gui.screen.InfernalIronAnvilScreen;
import com.bilibili.player_ix.noixmod_api.client.model.*;
import com.bilibili.player_ix.noixmod_api.client.particle.*;
import com.bilibili.player_ix.noixmod_api.client.renderer.*;
import com.bilibili.player_ix.noixmod_api.client.renderer.block.AltarRenderer;
import com.bilibili.player_ix.noixmod_api.register.ApiBlockEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = NoixmodAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class ClientInitEvents {
    private ClientInitEvents() {
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AbstractPlayerModel.ABSTRACT_PLAYER, AbstractPlayerModel::createBodyLayer);
        event.registerLayerDefinition(AbyssModel.LAYER_LOCATION, AbyssModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.PLAYER_INNER, AbstractPlayerModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.PLAYER_OUTER, AbstractPlayerModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.API_GHAST, APIGhastModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.API_HUMANOID, APIHumanoidModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.API_SKELETON, APISkeletonModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.API_SPIDER, APISpiderModel::createSpiderBodyLayer);
        event.registerLayerDefinition(ApostleModel.APOSTLE, ApostleModel::createBodyLayer);
        event.registerLayerDefinition(BiologistModel.LAYER_LOCATION, BiologistModel::createBodyLayer);
        event.registerLayerDefinition(CageModel.LAYER_LOCATION, CageModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.DAN_DING, DanDingModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.DD_INNER_ARMOR, DanDingModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.DD_OUTER_ARMOR, DanDingModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.DEAD_ILLAGER_SKULL, DeadIllagerSkullModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.EI, EIModel::createBodyLayer);
        event.registerLayerDefinition(GirlGhostModel.LOCATION, GirlGhostModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.GOLEM_MASTER, GolemMasterModel::createBodyLayer);
        event.registerLayerDefinition(HeadHunterModel.LAYER_LOCATION, HeadHunterModel::createBodyLayer);
        event.registerLayerDefinition(HeadHunterSwordModel.LAYER_LOCATION, HeadHunterSwordModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.INTRUDER, IntruderModel::createBodyLayer);
        event.registerLayerDefinition(IXIllagerModel.LAYER_LOCATION, IXIllagerModel::createBodyLayer);
        event.registerLayerDefinition(LurkerModel.LAYER_LOCATION, LurkerModel::createBodyLayer);
        event.registerLayerDefinition(NetherSoulModel.LAYER_LOCATION, NetherSoulModel::createBodyLayer);
        event.registerLayerDefinition(NewHeadHunterModel.LAYER_LOCATION, NewHeadHunterModel::createBodyLayer);
        event.registerLayerDefinition(NihilistIllagerModel.LAYER_LOCATION, NihilistIllagerModel::createBodyLayer);
        event.registerLayerDefinition(NihilistHumanoidModel.LAYER_LOCATION, NihilistHumanoidModel::createBodyLayer);
        event.registerLayerDefinition(NihilisticWitherModel.WITHER, NihilisticWitherModel::createBodyLayer);
        event.registerLayerDefinition(PlateauBeastModel.LAYER_LOCATION, PlateauBeastModel::createBodyLayer);
        event.registerLayerDefinition(PriestModel.LAYER_LOCATION, PriestModel::createBodyLayer);
        event.registerLayerDefinition(StarGuardianModel.LAYER_LOCATION, StarGuardianModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.STATUE, StatueModel::createBodyLayer);
        event.registerLayerDefinition(SuicideZombieModel.LAYER_LOCATION, SuicideZombieModel::createBodyLayer);
        event.registerLayerDefinition(SummonEntityModel.LAYER_LOCATION, SummonEntityModel::createBodyLayer);
        event.registerLayerDefinition(TheGhostModel.LAYER_LOCATION, TheGhostModel::createBodyLayer);
        event.registerLayerDefinition(ThrownAxeModel.LAYER_LOCATION, ThrownAxeModel::createLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.VILLAGER_FIGHTER, VillagerFighterModel::createBodyLayer);
        event.registerLayerDefinition(WormIllagerModel.LAYER_LOCATION, WormIllagerModel::createBodyLayer);
        event.registerLayerDefinition(WrongedSoulModel.LAYER_LOCATION, WrongedSoulModel::createBodyLayer);
        event.registerLayerDefinition(YetiModel.LAYER_LOCATION, YetiModel::createBodyLayer);
        event.registerLayerDefinition(NoixmodAPIModelLayer.API_ZOMBIE, ApiZombieModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(NoixmodAPIParticleTypes.API_LAVA.get(), APILava::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.BLACK_CLOUD.get(), Cloud.BlackProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.BLOOD.get(), Blood.BloodParticleProvide::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.BLOOD_SPELL.get(), NihilisticSpell.BloodSpellProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.CIRCLE.get(), CircleParticle.Provider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.CLOUD.get(), Cloud.Provider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.COLORED_ASH.get(), ColoredAsh.ColoredAshProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.CORRUPTION.get(), Corruption.Provider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.CRACK.get(), CrackParticle.Provider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.DARK_SPELL.get(), NihilisticSpell.DarkSpellProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.GOLDEN_FLAME.get(), GoldenFlame.GoldenFlameProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.NIHILISM_IMPART.get(), NihilismImpart.Provider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get(), NihilisticFireParticle.FireProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get(), NihilisticSpell.NihilisticSpellProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.NORMAL_SPELL.get(), NihilisticSpell.NihilisticSpellProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.PURPLE_ATTACK.get(), PurpleAttack.PurpleAttackProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.PURPLE_FLAME.get(), PurpleFlame.PurpleFlameProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.RED_SKULL.get(), RedSkullParticle.Provider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.RISING_PURPLE_FLAME.get(),
                PurpleFlame.RisingPurpleFlameProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.SMALL_FIRE.get(), NihilisticFireParticle.SmallFireProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.SMALL_POOF.get(), SmallPoof.Provider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.SUMMON_PARTICLE.get(), SummonParticle.SummonProvider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.WORM_PARTICLE.get(), WormParticle.Provider::new);
        event.registerSpriteSet(NoixmodAPIParticleTypes.WIND.get(), WindParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NoixmodAPIEntities.ABOMINATION.get(), AbominationRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.ABYSS.get(), AbyssRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.AMBUSHER.get(), AmbusherRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.APOSTLE.get(), ApostleRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.APOSTLE_SERVANT.get(), ApostleServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.APOSTLE_SHADOW.get(), ApostleShadowRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.AQUATIC_WORM.get(), AquaticWormRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.ARCHER_SERVANT.get(), ArcherServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.ARMORER.get(), ArmorerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.ARROW_ARROW.get(), ArrowArrowRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.BIOLOGIST.get(), BiologistRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.BLACK_HOLE.get(), BlackHoleRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.CAGE.get(), CageRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.CH_APOSTLE.get(), HorrorRenderer.ChasingApostleRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.CREEPER_SERVANT.get(), CreeperServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.CULTIST.get(), CultistRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.CURSED_NIHILISTIC_EVOKER.get(), CursedNihilityEvokerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.DAMAGE_ENTITY.get(), DamageEntityRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.DAN_ZHEN.get(), DanDaRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.DEAD_ILLAGER_SKULL.get(), DeadIllagerSkullRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.DETRACTOR.get(), DetractorRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.DROWNED_SERVANT.get(), DrownedServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.DRUNKENNESS.get(), DrunkennessRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.ELDER_G_S.get(), GuardianRenderer.Elder::new);
        event.registerEntityRenderer(NoixmodAPIEntities.ENDER_MAN_SERVANT.get(), EnderManServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.EVIL_SUMMONER.get(), EvilSummonerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.EVOKER_ILLAGER.get(), EvokerIllagerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.EVOKER_SERVANT.get(), EvokerServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.EXORCIST.get(), ExorcistRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.FIGHTER.get(), FighterRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.FLAGMAN.get(), FlagmanRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.FREAKY_SPIDER.get(), FreakySpiderRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.FREAKY_WORM.get(), FreakyWormRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.FREAKY_SKELETON.get(), FreakySkeletonRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.GIRL_GHOST.get(), GirlGhostRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.GOLEM.get(), GolemRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.GRAVE_GHOST.get(), GraveGhostRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.GRAVE_KEEPER.get(), GraveKeeperRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.GREEN_SINGER.get(), GreenSingerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.GUARDIAN_S.get(), GuardianRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.OLD_HEAD_HUNTER.get(), HeadHunterRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.HEAD_HUNTER.get(), NewHeadhunterRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.HEALING.get(), HealingRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.HHS.get(), HeadHunterSwordRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.HORROR_CAMERA.get(), HorrorCameraRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.H_WIND_ZOMBIE.get(), HostileWindZombieRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.HUNTED_VILLAGER.get(), HuntedVillagerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.HUNTER.get(), HunterRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.INTRUDER.get(), IntruderRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.LAVA_TRAP.get(), LavaTrapRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.LAVA_ZOMBIE_SERVANT.get(), LavaZombieRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.LITTLE_FIREBALL.get(), LittleFireballRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.LURKER.get(), LurkerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.MAGICAL_CLONE.get(), MagicalCloneRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.MINI_GHAST.get(), MiniGhastRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.MOON_KILLER.get(), MoonKillerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.MOURNER.get(), MournerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.MUSHROOM_SPIDER.get(), MushroomSpiderRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.PRIEST.get(), PriestRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.RAINBOWPHOBIA_PATIENTS.get(), RainbowphobiaPatientsRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NEO_ILLAGER.get(), NeoIllagerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NETHER_SOUL.get(), NetherSoulRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_ARROW.get(), NihilisticArrowRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_ARROW_RAIN.get(), NihilisticArrowRainRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_BLAZE.get(), NihilisticBlazeRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.HOSTILE_NB.get(), NihilisticBlazeRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.HOSTILE_YETI.get(), YetiRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_CRACK.get(), BaseEntityRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_DEATH.get(), NihilisticDeathRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_EVOKER.get(), NihilisticEvokerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_FIRE.get(), NihilismFireRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_FIREBALL.get(), NihilisticFireballRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_GHAST.get(), NihilisticGhastRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_LORD.get(), NihilityLordRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_SERVANT.get(), NihilisticServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_STATUE.get(), NihilisticStatueRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_WITHER.get(), NihilisticWitherRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_WITHER_BOSS.get(), NihilisticWitherBossRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_WITHER_SKULL.get(), NihilisticWitherSkullRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.NIHILISTIC_ZOMBIE.get(), NihilityZombieRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.PILLAGER_SERVANT.get(), PillagerServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.PLATEAU_BEAST.get(), PlateauBeastRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.POWER_ENTITY.get(), PowerEntityRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SCULK_ZOMBIE.get(), SculkZombieRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SHADOW_WALKER.get(), ShadowWalkerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SILVERFISH_SERVANT.get(), SilverfishServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SMALL_WORM.get(), SmallWormRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SMOKE_TRAP.get(), SmokeTrapRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.STAR_GUARDIAN.get(), StarGuardianRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SUICIDE_ZOMBIE.get(), SuicideZombieRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SUMMON_APOSTLE.get(), SummonApostleRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SUMMON_ENTITY.get(), SummonEntityRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SUMMON_STAR_GUARDIAN.get(), BaseEntityRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SUPERSTITIOUS.get(), SuperstitiousRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SUPERSTITIOUS_CLONE.get(), SuperstitiousCloneRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.SWORD_CULTIST.get(), SwordCultistRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.THROWN_AXE.get(), ThrownAxeRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.BUGLER.get(), TrumpeterRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.THE_GHOST.get(), TheGhostRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.THE_HUMAN.get(), HorrorRenderer.ScaringHumanRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.TRACKER.get(), TrackerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VAMPIRE.get(), VampireRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VAMPIRE_ARROW.get(), VampireArrowRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VAMPIRE_SERVANT.get(), VampireServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VEX_ARCHER.get(), VexArcherRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VEX_SERVANT.get(), VexServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VINDICATOR_SERVANT.get(), VindicatorServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VILLAGER_EVOKER.get(), VillagerEvokerRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VILLAGER_FANGS.get(), VillagerFangsRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VILLAGER_GOLEM.get(), VillagerGolemRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VILLAGER_MASTER.get(), VillagerMasterRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.VILLAGER_SPELLCASTER.get(), VillagerSpellcasterRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.WARDEN_SERVANT.get(), WardenServantRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.WATER_TRAP.get(), WaterTrapRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.WATER_WARLOCK.get(), WaterWarlockRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.WIND_ENTITY.get(), BaseEntityRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.WIND_ZOMBIE.get(), WindZombieRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.WITHER_SKELETON_SERVANT.get(), WitherSkeletonSRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.WORM.get(), WormRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.WORM_MASTER.get(), WormMasterRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.WRONGED_SOUL.get(), WrongedSoulRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.YETI.get(), YetiRenderer::new);
        event.registerEntityRenderer(NoixmodAPIEntities.ZOMBIE_VINDICATOR.get(), ZombieVindicatorRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetUp(FMLClientSetupEvent event) {
        registerScreens();
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(BossBar::renderBossBar);
        registerBlockRenderers();
        createApiPacket();
        registerItemStates();
    }

    public static void registerScreens() {
        MenuScreens.register(ApiGuis.ALTAR.get(), AltarScreen::new);
        MenuScreens.register(ApiGuis.INFERNAL_IRON_ANVIL.get(), InfernalIronAnvilScreen::new);
    }

    public static void registerBlockRenderers() {
        BlockEntityRenderers.register(ApiBlockEntities.ALTAR.get(), AltarRenderer::new);
        BlockEntityRenderers.register(ApiBlockEntities.CURSED_CHEST.get(), CursedChestR::new);
    }

    public static void registerItemStates() {
        makeBow(NoixmodAPIItems.BOW_BOW);
    }

    private static void makeBow(Supplier<? extends Item> item) {
        ItemProperties.register(item.get(), new ResourceLocation("pull"),
                (stack, level, living, i) -> {
                    if (living == null) {
                        return 0.0F;
                    } else {
                        return living.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration()
                                - living.getUseItemRemainingTicks()) / 20.0F;
                    }
                });
        ItemProperties.register(item.get(), new ResourceLocation("pulling"),
                (itemStack, clientLevel, livingEntity, i) -> livingEntity != null &&
                        livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
    }

    @SuppressWarnings("all")
    public static void createApiPacket() {
        File resourcepacks = new File(".", "resourcepacks");
        File textures = new File(resourcepacks, "No.IXModMoreTextures.zip");
        if (!textures.exists()) {
            try {
                resourcepacks.mkdirs();
                InputStream in = NoixmodAPI.class.getResourceAsStream("/assets/noixmodapi/noixmod_more_textures.zip");
                FileOutputStream out = new FileOutputStream(textures);
                byte[] buf = new byte[16384];
                int len;
                if (in != null) {
                    while ((len = in.read(buf)) > 0)
                        out.write(buf, 0, len);
                    in.close();
                }
                out.close();
            } catch (IOException ignored) {
            }
        }
    }
}
