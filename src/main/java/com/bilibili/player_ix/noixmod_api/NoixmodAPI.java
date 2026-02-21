
package com.bilibili.player_ix.noixmod_api;

import com.bilibili.player_ix.noixmod_api.client.ClientAgent;
import com.bilibili.player_ix.noixmod_api.client.gui.ApiGuis;
import com.bilibili.player_ix.noixmod_api.config.*;
import com.bilibili.player_ix.noixmod_api.entities.mod.APIMonster;
import com.bilibili.player_ix.noixmod_api.network.ApiNetwork;
import com.bilibili.player_ix.noixmod_api.register.*;
import com.bilibili.player_ix.noixmod_api.server.ServerAgent;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.*;

@Mod(NoixmodAPI.MOD_ID)
public class NoixmodAPI {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "noixmodapi";
    public static ApiAgent agent = DistExecutor.unsafeRunForDist(() -> ClientAgent::new,
            () -> ServerAgent::new);
    /*public static Unsafe UNSAFE;
    public static MethodHandles.Lookup LOOKUP_IMPL;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe)theUnsafe.get(null);
            Field lookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            LOOKUP_IMPL = (MethodHandles.Lookup)UNSAFE.getObject(
                    UNSAFE.staticFieldBase(lookup), UNSAFE.staticFieldOffset(lookup));
        } catch (Exception ignore) {
        }
    }*/

    @SuppressWarnings("removal")
    public NoixmodAPI() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus bus = context.getModEventBus();
        bus.addListener(this::commonSetUp);
        bus.addListener(this::registerSpawns);
        NoixmodAPITags.init();
        NoixmodAPIItems.REGISTRY.register(bus);
        NoixmodAPIMobEffects.REGISTER.register(bus);
        ApiGuis.REGISTER.register(bus);
        ApiEnchantments.REGISTER.register(bus);
        ApiBlockEntities.REGISTER.register(bus);
        NoixmodAPISounds.REGISTRY.register(bus);
        NoixmodAPIBlocks.REGISTER.register(bus);
        NoixmodAPIEntities.SENSORS.register(bus);
        NoixmodAPIEntities.REGISTRY.register(bus);
        NoixmodAPIParticleTypes.REGISTRY.register(bus);
        NoixmodAPITabs.REGISTRY.register(bus);
        createFiles(FMLPaths.CONFIGDIR.get().resolve("noixmodapi"), "noixmodapi");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NoixmodAPIMainConfig.SPEC,
                "noixmodapi/noixmodapi-main_config.toml");
        NoixmodAPIMainConfig.load(NoixmodAPIMainConfig.SPEC, FMLPaths.CONFIGDIR.get()
                .resolve("noixmodapi/noixmodapi-main_config.toml").toString());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NoixmodAPIAttributesConfig.SPEC,
                "noixmodapi/noixmodapi-attributes_config.toml");
        NoixmodAPIAttributesConfig.load(NoixmodAPIAttributesConfig.SPEC, FMLPaths.CONFIGDIR.get()
                .resolve("noixmodapi/noixmodapi-attributes_config.toml").toString());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetUp(@Nonnull FMLCommonSetupEvent event) {
        ApiNetwork.register();
    }

    private void registerSpawns(@Nonnull SpawnPlacementRegisterEvent event) {
        event.register(NoixmodAPIEntities.AQUATIC_WORM.get(), SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR, (entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) ->
                        APIMonster.checkAPIMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos,
                                randomSource) && NoixmodAPIMainConfig.AquaticWormWillSpawn.get()
                                && randomSource.nextDouble() <= 0.05,
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(NoixmodAPIEntities.GIRL_GHOST.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) ->
                        APIMonster.checkAPIMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType,
                                blockPos, randomSource)
                        && NoixmodAPIMainConfig.GirlGhostCanSummon.get(), SpawnPlacementRegisterEvent.Operation.AND);
        event.register(NoixmodAPIEntities.PLATEAU_BEAST.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, APIMonster::checkAPIMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(NoixmodAPIEntities.VAMPIRE.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) ->
                        APIMonster.checkAPIMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType,
                                blockPos, randomSource)
                        && NoixmodAPIMainConfig.VampireWillSpawn.get(), SpawnPlacementRegisterEvent.Operation.AND);
        event.register(NoixmodAPIEntities.H_WIND_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) ->
                        APIMonster.checkAPIMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType,
                                blockPos, randomSource) && NoixmodAPIMainConfig.WindZombieCanSpawn.get(),
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(NoixmodAPIEntities.WORM.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) ->
                        randomSource.nextDouble() <= 0.05 && APIMonster.checkAPIMonsterSpawnRules(entityType,
                                serverLevelAccessor, mobSpawnType, blockPos, randomSource)
                                && NoixmodAPIMainConfig.WormWillSpawn.get(), SpawnPlacementRegisterEvent.Operation.AND);
    }

    @Nonnull
    public static ResourceLocation location(String s) {
        return new ResourceLocation(MOD_ID, s);
    }

    private static void createFiles(@Nonnull Path dirPath, String dirLabel) {
        if (!Files.isDirectory(dirPath.getParent())) {
            createFiles(dirPath.getParent(), "parent of " + dirLabel);
        }
        if (!Files.isDirectory(dirPath)) {
            LOGGER.debug("Try create file {}......", dirPath);
            try {
                Files.createDirectory(dirPath);
            } catch (IOException e) {
                if (e instanceof FileAlreadyExistsException) {
                    LOGGER.error("Try to create file {}, but the file already exists.", dirPath);
                } else {
                    LOGGER.error("Try to create file {}, but failed.", dirPath);
                }
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.debug("Found {} successful.Label: {}", dirPath, dirLabel);
        }
    }
}
