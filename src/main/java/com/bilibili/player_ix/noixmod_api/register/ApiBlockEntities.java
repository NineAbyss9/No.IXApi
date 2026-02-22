
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.blocks.entities.AltarBlockEntity;
import com.bilibili.player_ix.noixmod_api.blocks.entities.CursedChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.NineAbyss9.code.Instance;

@SuppressWarnings("all")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ApiBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES, NoixmodAPI.MOD_ID);
    public static final RegistryObject<BlockEntityType<AltarBlockEntity>> ALTAR =
            REGISTER.register("altar",
            () -> BlockEntityType.Builder.of(AltarBlockEntity::new, NoixmodAPIBlocks.ALTAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<CursedChestBlockEntity>> CURSED_CHEST
            = REGISTER.register("cursed_chest", ()-> BlockEntityType.Builder.of(CursedChestBlockEntity::new,
            NoixmodAPIBlocks.SPIRIT_STONE_ORE.get()).build(Instance.nullOf()));
}
