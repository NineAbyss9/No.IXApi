
package com.github.NineAbyss9.ix_api.api.mobs;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class APIStats {
    public static final DeferredRegister<StatType<?>> REGISTER =
            DeferredRegister.create(ForgeRegistries.STAT_TYPES, NoixmodAPI.MOD_ID);
    public static final ResourceLocation INTERACT_WITH_ALTAR;

    public static ResourceLocation makeCustomStat(String location, StatFormatter formatter) {
        //REGISTER.register(location, ()->new StatType<>());
        return new ResourceLocation(location);
    }

    static {
        INTERACT_WITH_ALTAR = makeCustomStat("interact_with_altar", StatFormatter.DEFAULT);
    }
}
