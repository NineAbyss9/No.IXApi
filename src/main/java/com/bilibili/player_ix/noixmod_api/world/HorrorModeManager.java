
package com.bilibili.player_ix.noixmod_api.world;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HorrorModeManager {
    public static final Map<Level, HorrorModeManager> horrorModeManagers = new HashMap<>();
    public int apostleLookingTime = 0;
    public float apostleSummonChance = 0F;
    private Level level;
    private Status status;
    public HorrorModeManager(Level pLevel) {
        this.level = pLevel;
    }

    public void tick() {
        if (level.isClientSide) {
            if (apostleLookingTime > 0) {
                --apostleLookingTime;
            }
        } else {
            ServerLevel serverLevel = (ServerLevel)level;
            if (apostleLookingTime > 0) {
                serverLevel.setDayTime(0L);
                --apostleLookingTime;
                if (apostleLookingTime == 90) {
                    for (Player player : serverLevel.players()) {
                        serverLevel.playSound(player, player.blockPosition(), NoixmodAPISounds.APOSTLE_CAST_SPELL.get(),
                                SoundSource.HOSTILE);
                    }
                }
            }
            var dragonFight = serverLevel.getDragonFight();
            if (dragonFight != null) {
                if (dragonFight.hasPreviouslyKilledDragon() && apostleSummonChance < 1F) {
                    apostleSummonChance += 0.0005F;
                }
            }
            if (this.apostleSummonChance >= 1F) {
                this.apostleLookingTime = 99;
            }
        }
    }

    public void stop() {
        this.status = Status.STOPPED;
    }

    public boolean isStopped() {
        return this.status == Status.STOPPED;
    }

    public void save(CompoundTag pCompound) {
        pCompound.putInt("ApostleLookingTime", this.apostleLookingTime);
        pCompound.putFloat("ApostleSummonChance", this.apostleSummonChance);
        pCompound.putString("Status", this.status.getName());
    }

    public static enum Status {
        ONGOING,
        STOPPED;

        public static Status getByName(String pName) {
            for(Status raid$raidstatus : values()) {
                if (pName.equalsIgnoreCase(raid$raidstatus.name())) {
                    return raid$raidstatus;
                }
            }
            return ONGOING;
        }

        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
