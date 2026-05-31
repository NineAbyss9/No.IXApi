
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.github.NineAbyss9.ix_api.api.annotation.MaybeDeprecated;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

@MaybeDeprecated
public class NoixmodAPIDamageSource {
    private final RegistryAccess access;
    public static final ResourceKey<DamageType> nihility;

    public NoixmodAPIDamageSource(
            RegistryAccess registry
            //Holder<DamageType> p_270906_, @Nullable Entity p_270796_, @Nullable Entity p_270459_, @Nullable Vec3 p_270623_
    ) {
        //super(p_270906_, p_270796_, p_270459_, p_270623_);
        this.access = registry;
    }

    public static DamageSource damage(Level pLevel, @Nullable Entity pDirect, @Nullable Entity pEntity,
                                      @Nullable Vec3 pPos, ResourceKey<DamageType> pType) {
        return new DamageSource(pLevel.registryAccess().registry(Registries.DAMAGE_TYPE)
                .get().getHolder(pType).get(), pDirect, pEntity, pPos);
    }

    public static DamageSource nihilityOwner(TraceableEntity ownable)
    {
        return nihility((Entity)ownable, ownable.getOwner());
    }

    public static DamageSource nihility(Level level)
    {
        return damage(level, null, null, null, nihility);
    }

    public static DamageSource nihility(Entity entity) {
        return nihility(entity, entity);
    }

    public static DamageSource nihility(Entity entity, @Nullable Entity e) {
        return nihility(entity, e, entity.level());
    }

    public static DamageSource nihility(@Nullable Entity entity, @Nullable Entity directEntity, Level world) {
        return damage(world, entity, directEntity, null, nihility);
    }

    public DamageSource nihility()
    {
        return new DamageSource(access.registry(Registries.DAMAGE_TYPE).get().getHolder(nihility).get(), null,
                null, null);
    }

    public DamageSource nihilityIn(Entity pDirect, Entity pCausing)
    {
        return new DamageSource(access.registry(Registries.DAMAGE_TYPE).get().getHolder(nihility).get(), pDirect,
                pCausing, null);
    }

    public DamageSource nihilityIn(Entity pDirect, Entity pCausing, Vec3 pPos)
    {
        return new DamageSource(access.registry(Registries.DAMAGE_TYPE).get().getHolder(nihility).get(), pDirect,
                pCausing, pPos);
    }

    public static ResourceKey<DamageType> createDamage(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, NoixmodAPI.location(name));
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(nihility, new DamageType("noixmodapi:nihility",
                0.2F, DamageEffects.HURT));
    }

    @Nullable
    public static Entity sourceEntity(DamageSource source) {
        return Optional.ofNullable(source.getDirectEntity()).orElse(source.getEntity());
    }

    static {
        nihility = createDamage("nihility");
    }
}
