
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.bilibili.player_ix.noixmod_api.commands.APICommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = NoixmodAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ApiRegisterEvents {
    private ApiRegisterEvents() {
    }

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        APICommand.register(commandDispatcher, event.getBuildContext());
    }

    //@SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if (event.getType() == ApiVillagerProfessions.WORM_MASTER.get()) {
            Map<Integer, List<VillagerTrades.ItemListing>> map = event.getTrades();
            map.get(1).add(new BasicItemListing(ItemStacks.of(NoixmodAPIItems.WORM_DIRT_ITEM, 4),
                    ItemStacks.of(Items.EMERALD), 30,
                    2, 0.005f));
        }
    }

    /*public static void addDamages(GatherDataEvent event) {
        event.createDatapackRegistryObjects(new RegistrySetBuilder()
                        // Add a datapack builtin entry provider for damage types. If this lambda becomes longer,
                        // this should probably be extracted into a separate method for the sake of readability.
                        .add(Registries.DAMAGE_TYPE, bootstrap -> {
                            // Use new DamageType() to create an in-code representation of a damage type.
                            // The parameters map to the values of the JSON file, in the order seen above.
                            // All parameters except for the message id and the exhaustion value are optional.
                            bootstrap.register(NoixmodAPIDamageSource.nihilisticFire, new DamageType(NoixmodAPIDamageSource.nihilisticFire
                                    .location(),
                                    DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                                    0.1f,
                                    DamageEffects.HURT,
                                    DeathMessageType.DEFAULT)
                            );
                        })
                // Add datapack providers for other datapack entries, if applicable.
        );
    }*/
}
