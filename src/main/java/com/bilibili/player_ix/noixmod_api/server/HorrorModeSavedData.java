
package com.bilibili.player_ix.noixmod_api.server;

import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Map;

public class HorrorModeSavedData
extends SavedData {
    public static final String MANAGER_FILE_ID = "horror_mode";
    private static HorrorModeSavedData instance;
    private final Map<Integer, HorrorModeManager> managerMap = Maps.newHashMap();
    //private final ServerLevel level;
    public int nextAvailableID;
    private long tick;
    public HorrorModeSavedData() {
        //this.level = pLevel;
        nextAvailableID = 1;
        this.setDirty();
    }

    public void tick() {
        ++tick;
        var iterator = managerMap.values().iterator();
        while (iterator.hasNext()) {
            HorrorModeManager savedData = iterator.next();
            if (!NoixmodAPIMainConfig.HorrorMode.get()) {
                savedData.stop();
            }
            if (savedData.isStopped()) {
                iterator.remove();
                this.setDirty();
            } else {
                savedData.tick();
            }
        }
        if (this.tick % 200L == 0) {
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

    private int getId() {
        return this.nextAvailableID;
    }

    private int nextId() {
        return ++this.nextAvailableID;
    }

    public HorrorModeManager getHorrorModeManager() {
        return managerMap.get(this.getId());
    }

    @SuppressWarnings("UnusedReturnValue")
    public static HorrorModeSavedData load(ServerLevel pLevel) {
        return pLevel.getDataStorage().computeIfAbsent(compound -> load(pLevel,
                compound), HorrorModeSavedData::new, HorrorModeSavedData.MANAGER_FILE_ID);
    }

    public static HorrorModeSavedData load(ServerLevel pLevel, CompoundTag pCompoundTag) {
        boolean shouldCreate = false;
        if (instance == null) shouldCreate = true;
        else if (!pCompoundTag.contains("Tick")) shouldCreate = true;
        else if (!pCompoundTag.contains("NextAvailableID")) shouldCreate = true;
        else if (!pCompoundTag.contains("HorrorModeManagers")) shouldCreate = true;
        if (shouldCreate) {
            var data = new HorrorModeSavedData();
            data.managerMap.put(data.getId(), new HorrorModeManager(data.getId(), pLevel));
            instance = data;
            return data;
        }
        HorrorModeSavedData savedData = new HorrorModeSavedData();
        savedData.loadInstance(pLevel, pCompoundTag);
        return savedData;
    }

    public void loadInstance(ServerLevel pLevel, CompoundTag pCompoundTag) {
        nextAvailableID = pCompoundTag.getInt("NextAvailableID");
        tick = pCompoundTag.getLong("Tick");
        ListTag listtag = pCompoundTag.getList("HorrorModeManagers", 10);
        for (int i = 0; i < listtag.size();++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            HorrorModeManager manager = new HorrorModeManager(this.getId(), pLevel);
            manager.load(compoundtag);
            managerMap.put(nextAvailableID, manager);
        }
    }

    public CompoundTag save(CompoundTag pCompound) {
        pCompound.putInt("NextAvailableID", nextAvailableID);
        pCompound.putLong("Tick", this.tick);
        ListTag listtag = new ListTag();
        for (HorrorModeManager manager : managerMap.values()) {
            CompoundTag compoundtag = new CompoundTag();
            manager.save(compoundtag);
            listtag.add(compoundtag);
        }
        pCompound.put("HorrorModeManagers", listtag);
        return pCompound;
    }

    public static HorrorModeSavedData getInstance() {
        if (instance == null) {
            instance = new HorrorModeSavedData();
        }
        return instance;
    }

    public static HorrorModeSavedData getInstanceUnsafe() {
        return instance;
    }

    public static void setInstance(HorrorModeSavedData pInstance) {
        HorrorModeSavedData.instance = pInstance;
    }
}
