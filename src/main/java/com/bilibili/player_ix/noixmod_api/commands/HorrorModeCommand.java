
package com.bilibili.player_ix.noixmod_api.commands;

import com.bilibili.player_ix.noixmod_api.server.HorrorModeSavedData;
import net.minecraft.commands.CommandSourceStack;

public class HorrorModeCommand {
    public static int setMobsWillSpawn(int index, boolean flag) {
        HorrorModeSavedData.getInstance().setMobsWillSpawn(index, flag);
        return 0;
    }

    public static int spawnTheGhost(CommandSourceStack stack) {
        //ServerLevel serverLevel = stack.getLevel();
        HorrorModeSavedData.getInstance().getHorrorModeManager().spawnTheGhost();
        return 0;
    }
}
