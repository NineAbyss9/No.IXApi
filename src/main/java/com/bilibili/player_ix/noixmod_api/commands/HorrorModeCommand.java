
package com.bilibili.player_ix.noixmod_api.commands;

import com.bilibili.player_ix.noixmod_api.server.HorrorModeSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class HorrorModeCommand {
    public static int setMobsWillSpawn(int index, boolean flag) {
        HorrorModeSavedData.getInstance().setMobsWillSpawn(index, flag);
        return 0;
    }

    public static int spawnTracker(CommandSourceStack stack) {
        ServerLevel ser = stack.getLevel();
        int r = HorrorModeSavedData.load(ser).getHorrorModeManager().spawnTracker(ser) == null ? 1 : 0;
        if (r == 0) {
            stack.sendSuccess(() -> Component.translatable("info.noixmodapi.tracker_look"), false);
        }
        return r;
    }

    public static int spawnTheGhost(CommandSourceStack stack) {
        ServerLevel ser = stack.getLevel();
        int r = HorrorModeSavedData.load(ser).getHorrorModeManager().spawnTheGhost(ser) == null ? 1 : 0;
        if (r == 0) {
            stack.sendSuccess(() -> Component.translatable("info.noixmodapi.ghost_look"), false);
        }
        return r;
    }
}
