
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.blocks.*;
import com.bilibili.player_ix.noixmod_api.blocks.grave.CursedChestBlock;
import com.bilibili.player_ix.noixmod_api.blocks.horror.BloodBlock;
import com.bilibili.player_ix.noixmod_api.blocks.worms.WormBlock;
import com.bilibili.player_ix.noixmod_api.blocks.worms.WormDirt;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NoixmodAPIBlocks {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, NoixmodAPI.MOD_ID);
    public static final RegistryObject<Block> ALTAR = REGISTER.register("altar", Altar::new);
    public static final RegistryObject<Block> BLOOD = REGISTER.register("blood", BloodBlock::new);
    public static final RegistryObject<Block> CURSED_CHEST = REGISTER.register("cursed_chest", CursedChestBlock::new);
    public static final RegistryObject<Block> INFERNAL_IRON_ANVIL = REGISTER.register("infernal_iron_anvil",
            InfernalIronAnvil::new);
    public static final RegistryObject<Block> OMINOUS_HEAD = REGISTER.register("ominous_head", OminousHead::new);
    public static final RegistryObject<Block> SPIRIT_STONE_ORE = REGISTER.register("spirit_stone_ore", SpiritStoneOre::new);
    public static final RegistryObject<Block> TELEPORT_PLATE = REGISTER.register("teleport_plate", TeleportPlate::new);
    //public static final RegistryObject<Block> TOMBSTONE = REGISTER.init("tombstone", ()-> new Block(BlockBehaviour.Properties.of().strength(1.5f, 6f)));
    public static final RegistryObject<Block> WORM_BLOCK = REGISTER.register("worm_block", WormBlock::new);
    public static final RegistryObject<Block> WORM_DIRT = REGISTER.register("worm_dirt", WormDirt::new);
    private NoixmodAPIBlocks() {}
}
