
package com.bilibili.player_ix.noixmod_api.register.data;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Iterator;

public class ApiBlockProvider
extends BlockStateProvider
{
    public static final String ONLY_BLOCK = "only_block";
    public ApiBlockProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NoixmodAPI.MODID, existingFileHelper);
    }

    protected void registerStatesAndModels() {
        Iterator<RegistryObject<? extends Block>> iterator = ApiDataHelper.BLOCKS.iterator();
        while (iterator.hasNext()) {
            RegistryObject<? extends Block> obj = iterator.next();
            this.simpleBlockWithItem(obj.get(), this.models().cubeAll(obj.getId().getPath(),
                    NoixmodAPI.location("block/" + obj.getId().getPath())));
        }
        ApiDataHelper.SPE_BLOCKS.forEach((obj, string) -> {
            Block block = obj.get();
            if (ONLY_BLOCK.equals(string))
            {
                this.simpleBlock(block);
            }
        });
    }
}
