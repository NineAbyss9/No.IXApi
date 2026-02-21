
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;

public class ApiRegistries {
    public static final ResourceKey<Registry<ISpell>> SPELL = createRegistryKey("spell");
    public static final DeferredRegister<ISpell> SPELLS =
            DeferredRegister.create(SPELL, NoixmodAPI.MOD_ID);
    public static void init() {
    }

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String pName) {
        return ResourceKey.createRegistryKey(new ResourceLocation(pName));
    }
}
