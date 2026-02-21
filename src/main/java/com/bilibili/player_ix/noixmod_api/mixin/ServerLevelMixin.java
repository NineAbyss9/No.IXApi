
package com.bilibili.player_ix.noixmod_api.mixin;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements WorldGenLevel {
    @Shadow public abstract void setDayTime(long p_8616_);

    @Shadow @Nullable public abstract EndDragonFight getDragonFight();

    @Shadow @Final List<ServerPlayer> players;

    @Unique
    public int noIXAPIForge$apostleLookingTime = 0;
    @Unique
    public float noIXAPIForge$apostleSummonChance = random.nextFloat();
    private ServerLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_,
                             Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_,
                             boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(BooleanSupplier p_8794_, CallbackInfo ci) {
        if (this.noIXAPIForge$apostleLookingTime > 0) {
            --this.noIXAPIForge$apostleLookingTime;
            this.setDayTime(0);
            if (this.noIXAPIForge$apostleLookingTime == 90) {
                for (Player player : players) {
                    this.playSound(player, player.blockPosition(), NoixmodAPISounds.APOSTLE_CAST_SPELL.get(),
                            SoundSource.HOSTILE);
                }
            }
        }
        EndDragonFight data = this.getDragonFight();
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            if (data != null) {
                if (this.noIXAPIForge$apostleSummonChance + (data.saveData().dragonKilled() ? 0.7F : 0) >= 0.9995F) {
                    this.noIXAPIForge$apostleLookingTime = 99;
                }
            }
        }
    }
}
