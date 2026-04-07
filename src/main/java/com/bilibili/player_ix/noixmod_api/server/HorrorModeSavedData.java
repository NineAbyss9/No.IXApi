
package com.bilibili.player_ix.noixmod_api.server;

import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Iterator;
import java.util.Map;

public class HorrorModeSavedData
extends SavedData {
    public static final String MANAGER_FILE_ID = "horror_modes";
    private final Map<Integer, HorrorModeManager> managerMap = Maps.newHashMap();
    private final ServerLevel level;
    public int nextAvailableID;
    private long tick;
    public HorrorModeSavedData(ServerLevel pLevel) {
        this.level = pLevel;
        nextAvailableID = 1;
        this.setDirty();
    }

    public void tick() {
        ++tick;
        Iterator<HorrorModeManager> iterator = this.managerMap.values().iterator();
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

    public static HorrorModeSavedData create(ServerLevel pLevel) {
        return new HorrorModeSavedData(pLevel);
    }

    public static HorrorModeSavedData load(ServerLevel pLevel, CompoundTag pCompoundTag) {
        boolean shouldCreate = false;
        if (!pCompoundTag.contains("Tick")) shouldCreate = true;
        else if (!pCompoundTag.contains("NextAvailableID")) shouldCreate = true;
        else if (!pCompoundTag.contains("HorrorModeManagers")) shouldCreate = true;
        if (shouldCreate) {
            return create(pLevel);
        }
        HorrorModeSavedData savedData = new HorrorModeSavedData(pLevel);
        savedData.nextAvailableID = pCompoundTag.getInt("NextAvailableID");
        savedData.tick = pCompoundTag.getLong("Tick");
        ListTag listtag = pCompoundTag.getList("HorrorModeManagers", 10);
        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            HorrorModeManager raid = new HorrorModeManager(pLevel);
            raid.save(compoundtag);
            savedData.managerMap.put(savedData.nextAvailableID++, raid);
        }
        return savedData;
    }

    public CompoundTag save(CompoundTag pCompound) {
        pCompound.putInt("NextAvailableID", nextAvailableID);
        pCompound.putLong("Tick", this.tick);
        ListTag listtag = new ListTag();
        for (HorrorModeManager raid : this.managerMap.values()) {
            CompoundTag compoundtag = new CompoundTag();
            raid.save(compoundtag);
            listtag.add(compoundtag);
        }
        pCompound.put("HorrorModeManagers", listtag);
        return pCompound;
    }
}
