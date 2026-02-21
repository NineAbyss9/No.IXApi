
package com.github.NineAbyss9.ix_api.ix_api.api.mobs.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Supplier;

public class EffectInstance {
    public EffectInstance() {
    }

    public static MobEffectInstance create(MobEffect effect, int during, int level) {
        return new MobEffectInstance(effect, during, level);
    }

    public static MobEffectInstance create(MobEffect effect, int during) {
        return new MobEffectInstance(effect, during);
    }

    public static MobEffectInstance create(Supplier<MobEffect> supplier, int during, int level) {
        return create(supplier.get(), during, level);
    }
}
