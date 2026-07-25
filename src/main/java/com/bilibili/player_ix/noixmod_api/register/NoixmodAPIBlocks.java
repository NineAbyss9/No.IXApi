
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.blocks.*;
import com.bilibili.player_ix.noixmod_api.blocks.grave.CursedChestBlock;
import com.bilibili.player_ix.noixmod_api.blocks.horror.BloodBlock;
import com.bilibili.player_ix.noixmod_api.blocks.worms.WormBlock;
import com.bilibili.player_ix.noixmod_api.blocks.worms.WormDirt;
import com.bilibili.player_ix.noixmod_api.register.data.ApiDataHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class NoixmodAPIBlocks {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, NoixmodAPI.MOD_ID);
    public static final RegistryObject<Block> ALTAR = REGISTER.register("altar", Altar::new);
    public static final RegistryObject<Block> BLOOD = REGISTER.register("blood", BloodBlock::new);
    public static final RegistryObject<Block> CURSED_CHEST = REGISTER.register("cursed_chest", CursedChestBlock::new);
    public static final RegistryObject<Block> INFERNAL_IRON_ANVIL = REGISTER.register("infernal_iron_anvil",
            InfernalIronAnvil::new);
    public static final RegistryObject<Block> INFERNAL_IRON_BLOCK = register("infernal_iron_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)
                    .requiresCorrectToolForDrops()
                    .strength(60.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK)));
    public static final RegistryObject<Block> OMINOUS_HEAD = REGISTER.register("ominous_head", OminousHead::new);
    public static final RegistryObject<Block> SPIRIT_STONE_ORE = REGISTER.register("spirit_stone_ore", SpiritStoneOre::new);
    //public static final RegistryObject<Block> TELEPORT_PLATE = REGISTER.register("teleport_plate", TeleportPlate::new);
    //public static final RegistryObject<Block> TOMBSTONE = REGISTER.init("tombstone", ()-> new Block(BlockBehaviour.Properties.of().strength(1.5f, 6f)));
    public static final RegistryObject<Block> VOID_BLOCK = register("void_block", () -> {
        return new Block(BlockBehaviour.Properties.of().strength(3.0F, 9999.0F)
                .mapColor(DyeColor.PURPLE).lightLevel(state -> {
                    return 5;
                }).pushReaction(PushReaction.DESTROY));
    });
    public static final RegistryObject<Block> WORM_BLOCK = REGISTER.register("worm_block", WormBlock::new);
    public static final RegistryObject<Block> WORM_DIRT = REGISTER.register("worm_dirt", WormDirt::new);
    private NoixmodAPIBlocks() {
    }

    private static RegistryObject<Block> register(String name, Supplier<Block> block) {
        RegistryObject<Block> object = REGISTER.register(name, block);
        ApiDataHelper.BLOCKS.add(object);
        return object;
    }

    private static Supplier<Block> special(String name, Supplier<Block> block, String message) {
        RegistryObject<Block> object = REGISTER.register(name, block);
        ApiDataHelper.SPE_BLOCKS.put(object, message);
        return object;
    }
}
