
package com.bilibili.player_ix.noixmod_api.server;

import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
//import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Map;

public class HorrorModeSavedData
extends SavedData {
    public static final String MANAGER_FILE_ID = "horror_mode";
    private static HorrorModeSavedData instance;
    private final Map<Integer, HorrorModeManager> managerMap = Maps.newHashMap();
    public int nextAvailableID;
    private long tick;
    public HorrorModeSavedData() {
        nextAvailableID = 1;
        //this.setDirty();
    }

    public void tick(Level pLevel) {
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
                savedData.tick(pLevel);
            }
        }
        if (this.tick % 200L == 0L) {
            this.setDirty();
        }
    }

    public void setMobsWillSpawn(int index, boolean value) {
        if (this.getHorrorModeManager() == null) return;
        this.getHorrorModeManager().setMobsWillSpawn(index, value);
        this.setDirty();
    }

    public void updateNextMobWillSpawn(int index) {
        if (this.getHorrorModeManager() == null) return;
        this.getHorrorModeManager().updateNextMobWillSpawn(index);
        this.setDirty();
    }

    private int getId() {
        return this.nextAvailableID;
    }

    /*public HorrorModeManager getHorrorModeManager() {
        return getHorrorModeManager(null//ServerLifecycleHooks.getCurrentServer().overworld()
        );
    }*/

    public HorrorModeManager getHorrorModeManager() {
        return managerMap.put(this.getId(), new HorrorModeManager(this.getId()));
    }

    @SuppressWarnings("UnusedReturnValue")
    public static HorrorModeSavedData load(ServerLevel pLevel) {
        return pLevel.getDataStorage().computeIfAbsent(HorrorModeSavedData::load, HorrorModeSavedData::new,
                HorrorModeSavedData.MANAGER_FILE_ID);
    }

    public static HorrorModeSavedData load(CompoundTag pCompoundTag) {
        boolean shouldCreate = instance == null || !pCompoundTag.contains("Tick") || !pCompoundTag.contains("NextAvailableID")
                || !pCompoundTag.contains("HorrorModeManagers");
        if (shouldCreate) {
            var data = new HorrorModeSavedData();
            data.managerMap.put(data.getId(), new HorrorModeManager(data.getId()));
            instance = data;
            return data;
        }
        HorrorModeSavedData savedData = new HorrorModeSavedData();
        savedData.loadInstance(pCompoundTag);
        return savedData;
    }

    public void loadInstance(CompoundTag pCompoundTag) {
        nextAvailableID = pCompoundTag.getInt("NextAvailableID");
        tick = pCompoundTag.getLong("Tick");
        ListTag listtag = pCompoundTag.getList("HorrorModeManagers", 10);
        for (int i = 0; i < listtag.size();++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            HorrorModeManager manager = new HorrorModeManager(this.getId());
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
            //instance.getHorrorModeManager(ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD));
        }
        return instance;
    }

    public static HorrorModeSavedData getInstanceSafe() {
        if (instance == null) {
            instance = new HorrorModeSavedData();
        }
        if (instance.getHorrorModeManager() == null) {
            instance.managerMap.put(instance.getId(), new HorrorModeManager(instance.getId()));
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
