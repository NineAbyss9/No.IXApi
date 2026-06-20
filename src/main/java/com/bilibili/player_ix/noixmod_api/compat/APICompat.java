
package com.bilibili.player_ix.noixmod_api.compat;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.compat.bo.BlueOceansCompat;
import com.bilibili.player_ix.noixmod_api.compat.goety.GoetyCompat;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class APICompat {
    private static final Map<String, Supplier<Compatable>> COMPAT_MODS
            = ImmutableMap.<String, Supplier<Compatable>>builder().put("goety", GoetyCompat::new)
            .put("blue_oceans", BlueOceansCompat::new).build();
    private static final Map<String, Compatable> MODS = new HashMap<>();

    public static void setup(FMLCommonSetupEvent event) {
        populateModules(ModList.get()::isLoaded);
        MODS.values().forEach(compatable -> compatable.setup(event));
    }

    private static void populateModules(Predicate<String> isLoaded) {
        for (Map.Entry<String, Supplier<Compatable>> entry : COMPAT_MODS.entrySet()) {
            String id = entry.getKey();
            if (isLoaded.test(id)) {
                MODS.put(id, entry.getValue().get());
                NoixmodAPI.LOGGER.debug("Loading compat module for mod {}", id);
            }
        }
    }
}
