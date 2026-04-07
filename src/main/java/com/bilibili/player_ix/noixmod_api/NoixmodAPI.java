
package com.bilibili.player_ix.noixmod_api;

import com.bilibili.player_ix.noixmod_api.client.ClientAgent;
import com.bilibili.player_ix.noixmod_api.client.gui.ApiGuis;
import com.bilibili.player_ix.noixmod_api.config.*;
import com.bilibili.player_ix.noixmod_api.network.ApiNetwork;
import com.bilibili.player_ix.noixmod_api.register.*;
import com.bilibili.player_ix.noixmod_api.server.ServerAgent;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.*;

@Mod(NoixmodAPI.MOD_ID)
public class NoixmodAPI {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "noixmodapi";
    public static ApiAgent agent = DistExecutor.unsafeRunForDist(() -> ClientAgent::new,
            () -> ServerAgent::new);

    @SuppressWarnings("removal")
    public NoixmodAPI() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus bus = context.getModEventBus();
        bus.addListener(this::commonSetUp);
        bus.addListener(NoixmodAPIEntities::registerSpawns);
        bus.addListener(NoixmodAPIEntities::registerAttributes);
        NoixmodAPITags.init();
        NoixmodAPIItems.REGISTRY.register(bus);
        NoixmodAPIMobEffects.REGISTER.register(bus);
        ApiRecipes.register(bus);
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

    private void commonSetUp(FMLCommonSetupEvent event) {
        ApiNetwork.register();
        NoixmodAPIEntities.init(event);
    }

    public static ResourceLocation location(String s) {
        return new ResourceLocation(MOD_ID, s);
    }

    /**Code from <a href="https://github.com/Polarice3/Goety-2/blob/1.20/src/main/java/com/Polarice3/Goety/Goety.java">link</a>*/
    private static void createFiles(Path dirPath, String dirLabel) {
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
