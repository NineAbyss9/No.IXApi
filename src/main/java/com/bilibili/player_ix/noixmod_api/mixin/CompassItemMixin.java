
package com.bilibili.player_ix.noixmod_api.mixin;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CompassItem.class)
public abstract class CompassItemMixin {

    @Inject(method = "getSpawnPosition", at =
            @At("HEAD"), cancellable = true)
    private static void getSpawnPosition(Level p_220020_, CallbackInfoReturnable<GlobalPos> cir) {
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            cir.setReturnValue(null);
        }
    }
}
