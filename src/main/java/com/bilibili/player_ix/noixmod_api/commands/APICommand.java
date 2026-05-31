
package com.bilibili.player_ix.noixmod_api.commands;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.monster.nihilist.NihilisticOrderSpawner;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;

public class APICommand {
    public APICommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext p_250122_)
    {
        dispatcher.register(Commands.literal("noixapi")
                .requires((stack) -> stack.hasPermission(2))
                .then(Commands.literal("APISpell")
                        .then(Commands.argument("spell", StringArgumentType.string())
                                .executes(commandContext ->
                                        castSpell(commandContext.getSource(),
                                                StringArgumentType.getString(commandContext, "spell"))
                                )))
                .then(Commands.literal("horrorMode")
                        .then(Commands.literal("phase")
                                .executes(commandContext ->
                                        HorrorModeCommand.getCurrentPhase(commandContext.getSource())))
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(commandContext -> setHorror(
                                        commandContext.getSource(), BoolArgumentType.getBool(commandContext, "value"))))
                        .then(Commands.literal("spawn")
                                .then(literal("setMobsWillSpawn")
                                        .then(argument("index", IntegerArgumentType.integer())
                                                .then(argument("flag", BoolArgumentType.bool())
                                                        .executes(commandContext ->
                                                                HorrorModeCommand.setMobsWillSpawn(
                                                                        commandContext.getSource(),
                                                                        IntegerArgumentType.getInteger(commandContext, "index"),
                                                                        BoolArgumentType.getBool(commandContext, "flag"))))))
                                .then(Commands.literal("tracker")
                                        .executes(commandContext ->
                                                HorrorModeCommand.spawnTracker(commandContext.getSource()))
                                        .then(Commands.argument("type", IntegerArgumentType.integer())
                                                .executes(commandContext ->
                                                        HorrorModeCommand.spawnTracker(commandContext.getSource(),
                                                                IntegerArgumentType.getInteger(commandContext, "type")))))
                                .then(Commands.literal("the_ghost")
                                        .executes(commandContext ->
                                                HorrorModeCommand.spawnTheGhost(commandContext.getSource())))))
                .then(Commands.literal("nihilisticOrder")
                        .then(Commands.literal("spawnNow")
                                .executes(context ->
                                        spawnNihilisticOrder(context.getSource())))));
    }

    private static int spawnNihilisticOrder(CommandSourceStack stack) {
        NihilisticOrderSpawner spawner = new NihilisticOrderSpawner();
        stack.sendSuccess(() -> Component.translatable("command.noixapi.nihilistic_order_spawn"), true);
        return spawner.spawn(stack.getLevel()) ? 0 : 1;
    }

    private static int castSpell(CommandSourceStack stack, String name) {
        ISpell spell = Spells.get(name);
        if (stack.getEntity() instanceof LivingEntity living) {
            spell.castSpell(stack.getLevel(), living);
        }
        return 0;
    }

    private static int setHorror(CommandSourceStack stackIn, boolean value) {
        NoixmodAPIMainConfig.HorrorMode.set(value);
        stackIn.sendSuccess(() -> Component.translatable("command.noixapi.horror_mode",
                value ? Component.translatable("command.noixapi.horror_mode_active")
                        : Component.translatable("command.noixapi.horror_mode_disabled")), true);
        return 0;
    }
}
