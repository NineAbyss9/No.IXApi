
package com.bilibili.player_ix.noixmod_api.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

@SuppressWarnings("UnusedReturnValue")
public class ApiSavedData
extends SavedData
{
    public static final String API_SAVED_DATA_FILE_ID
            = "api_saved_data";
    private boolean nihilisticWitherKilled;
    public ApiSavedData() {
        this.setDirty();
    }

    public boolean isNihilisticWitherKilled()
    {
        return nihilisticWitherKilled;
    }

    public void setNihilisticWitherKilled()
    {
        this.nihilisticWitherKilled = true;
        this.setDirty();
    }

    public static ApiSavedData load(ServerLevel serverLevel)
    {
        return serverLevel.getDataStorage().computeIfAbsent(ApiSavedData::load, ApiSavedData::new, API_SAVED_DATA_FILE_ID);
    }

    public static ApiSavedData load(CompoundTag tag)
    {
        ApiSavedData data = new ApiSavedData();
        data.nihilisticWitherKilled = tag.getBoolean("NihilisticWitherKilled");
        return data;
    }

    public static ApiSavedData get(ServerLevel pLevel)
    {
        return pLevel.getDataStorage().get(ApiSavedData::load, API_SAVED_DATA_FILE_ID);
    }

    public CompoundTag save(CompoundTag pCompoundTag)
    {
        pCompoundTag.putBoolean("NihilisticWitherKilled", this.nihilisticWitherKilled);
        return pCompoundTag;
    }
}
