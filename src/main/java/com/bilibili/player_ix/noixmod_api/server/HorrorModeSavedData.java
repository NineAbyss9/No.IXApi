
package com.bilibili.player_ix.noixmod_api.server;

import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;

public class HorrorModeSavedData
extends SavedData {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MANAGER_FILE_ID = "horror_mode";
    private HorrorModeManager manager;
    private long tick;
    public HorrorModeSavedData() {
        this.manager = new HorrorModeManager();
        this.setDirty();
    }

    public void tick(Level pLevel)
    {
        ++tick;
        manager.tick(pLevel);
        if (this.tick % 200L == 0L) {
            this.setDirty();
        }
    }

    public void setMobsWillSpawn(int index, boolean value) {
        this.getHorrorModeManager().setMobsWillSpawn(index, value);
        this.setDirty();
    }

    public void updateNextMobWillSpawn(int index) {
        this.getHorrorModeManager().updateNextMobWillSpawn(index);
        this.setDirty();
    }

    public void updateSpawnCache()
    {
        this.getHorrorModeManager().updateSpawnCache();
        this.setDirty();
    }

    public HorrorModeManager getHorrorModeManager() {
        return this.manager;
    }

    public static HorrorModeSavedData get(ServerLevel pLevel) {
        return pLevel.getDataStorage().get(HorrorModeSavedData::load, MANAGER_FILE_ID);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static HorrorModeSavedData load(ServerLevel pLevel)
    {
        return pLevel.getDataStorage().computeIfAbsent(HorrorModeSavedData::load, HorrorModeSavedData::new,
                HorrorModeSavedData.MANAGER_FILE_ID);
    }

    public static HorrorModeSavedData load(CompoundTag pCompoundTag)
    {
        HorrorModeSavedData savedData = new HorrorModeSavedData();
        savedData.loadInstance(pCompoundTag);
        return savedData;
    }

    public void loadInstance(CompoundTag pCompoundTag)
    {
        LOGGER.info("Loading instance...");
        tick = pCompoundTag.getLong("Tick");
        CompoundTag tag = pCompoundTag.getCompound("HorrorModeManager");
        manager.load(tag);
    }

    public CompoundTag save(CompoundTag pCompound)
    {
        pCompound.putLong("Tick", this.tick);
        CompoundTag compoundtag = new CompoundTag();
        manager.save(compoundtag);
        pCompound.put("HorrorModeManager", compoundtag);
        return pCompound;
    }
}
