
package com.bilibili.player_ix.noixmod_api.register.data;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.world.item.Item;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = NoixmodAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ApiDataHelper {
    public static final Set<RegistryObject<? extends Item>>
    ITEMS = new LinkedHashSet<>();
    public static final Set<RegistryObject<? extends Item>>
    BLOCKS = new LinkedHashSet<>();
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();
        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();
        generator.addProvider(includeServer, new ApiRecipeProvider(packOutput));
        generator.addProvider(includeClient, new ApiItemProvider(packOutput, existingFileHelper));
        //generator.addProvider(includeServer, new ApiLootProvider.Entity());
    }
}
