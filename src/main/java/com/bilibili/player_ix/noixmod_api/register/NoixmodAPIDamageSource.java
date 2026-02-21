
package com.bilibili.player_ix.noixmod_api.register;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.PAMAreNonnullByDefault;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@PAMAreNonnullByDefault
public class NoixmodAPIDamageSource
extends DamageSource {
    public static ResourceKey<DamageType> nihilisticFire;

    public NoixmodAPIDamageSource(Holder<DamageType> p_270906_, @Nullable Entity p_270796_, @Nullable Entity p_270459_, @Nullable Vec3 p_270623_) {
        super(p_270906_, p_270796_, p_270459_, p_270623_);
    }

    public static DamageSource nihilisticFire(Entity entity, @Nullable Entity e) {
        return nihilisticFire(entity, e, entity.level());
    }

    public static DamageSource nihilisticFire(@Nullable Entity entity, @Nullable Entity directEntity, LevelAccessor world) {
        return new DamageSource(world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(nihilisticFire),
                entity, directEntity);
    }

    public static ResourceKey<DamageType> createDamage(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, NoixmodAPI.location(name));
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(nihilisticFire, new DamageType("noixmodapi:nihilistic_fire",
                0.1F, DamageEffects.BURNING));
    }

    @Nullable
    public static Entity sourceEntity(DamageSource source) {
        return Optional.ofNullable(source.getDirectEntity()).orElse(source.getEntity());
    }

    static {
        nihilisticFire = createDamage("nihilistic_fire");
    }
}
