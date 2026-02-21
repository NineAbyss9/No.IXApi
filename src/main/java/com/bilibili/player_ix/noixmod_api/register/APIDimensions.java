
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.OptionalLong;

public class APIDimensions {
    BiomeTags tags;
    public static final DeferredRegister<DimensionType> REGISTER = DeferredRegister.create(Registries.DIMENSION_TYPE, NoixmodAPI.MOD_ID);
    public static final RegistryObject<DimensionType> NIHILISTIC_DIMENSION =
            REGISTER.register("nihilistic_world", ()->new DimensionType(OptionalLong.empty(), true, false,
                    false, true, 1.0, true, false, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD,
                    BuiltinDimensionTypes.OVERWORLD_EFFECTS, 0, new DimensionType.MonsterSettings(false, true
                    , UniformInt.of(0, 7), 0)));
}
