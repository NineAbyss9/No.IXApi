
package com.bilibili.player_ix.noixmod_api.register.data;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.item.magic.Staff;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ApiItemProvider
extends ItemModelProvider
{
    public ApiItemProvider(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, NoixmodAPI.MOD_ID, existingFileHelper);
    }

    protected void registerModels()
    {
        for (var obj : ApiDataHelper.ITEMS)
        {
            var item = obj.get();
            if (item instanceof SpawnEggItem) {
                spawnEgg(item);
            } else if (item instanceof Staff) {
                staff(obj, item);
            }
        }
    }

    public void basic(String loc, String texPath)
    {
        getBuilder(modid + ":item/" + loc)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + texPath));
    }

    private void staff(RegistryObject<?> object, Item item)
    {
        getBuilder(regToString(item)).parent(new ModelFile.UncheckedModelFile(
                NoixmodAPI.location("item/staff")
        )).texture("0", NoixmodAPI.location("item/magic/" + object.getId().getPath()));
    }

    private void spawnEgg(Item pItem) {
        getBuilder(regToString(pItem)).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    private String regToString(Item pItem) {
        return ForgeRegistries.ITEMS.getKey(pItem).toString();
    }
}
