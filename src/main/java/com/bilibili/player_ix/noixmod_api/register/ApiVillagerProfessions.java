
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.github.NineAbyss9.ix_api.api.annotation.MaybeDeprecated;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

@MaybeDeprecated
public class ApiVillagerProfessions {
    private static final Map<String, ProfessionPoiType> POI_TYPES = Maps.newHashMap();
    public static final DeferredRegister<VillagerProfession> REGISTER = DeferredRegister.create(
            ForgeRegistries.VILLAGER_PROFESSIONS, NoixmodAPI.MOD_ID);
    public static final RegistryObject<VillagerProfession> WORM_MASTER = register("worm_master",
            NoixmodAPIBlocks.WORM_BLOCK, ()-> SoundEvents.MUD_BREAK);

    public static RegistryObject<VillagerProfession> register(String name, Supplier<Block> block,
                                                                 Supplier<SoundEvent> supplier) {
        POI_TYPES.put(name, new ProfessionPoiType(block, null));
        return REGISTER.register(name, ()-> {
            Predicate<Holder<PoiType>> predicate = poiTypeHolder -> POI_TYPES.get(name) != null
                    && (poiTypeHolder.get() == POI_TYPES.get(name).poiTypeHolder.get());
            return new VillagerProfession(NoixmodAPI.MOD_ID + ":" + name, predicate, predicate, ImmutableSet.of(),
                    ImmutableSet.of(), supplier.get());
        });
    }

    static class ProfessionPoiType {
        final Supplier<Block> blockSupplier;
        Holder<PoiType> poiTypeHolder;
        public ProfessionPoiType(Supplier<Block> block, Holder<PoiType> poiType) {
            this.blockSupplier = block;
            this.poiTypeHolder = poiType;
        }
    }
}
