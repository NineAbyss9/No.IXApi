
package com.bilibili.player_ix.noixmod_api.register.data;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NoixmodAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ApiDataHelper {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();
        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();
        generator.addProvider(includeServer, new ApiRecipeProvider(packOutput));
    }
}
