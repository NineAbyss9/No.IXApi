
package com.bilibili.player_ix.noixmod_api.register;

import org.NineAbyss9.annotation.PFMAreNonnullByDefault;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.particle.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@PFMAreNonnullByDefault
public class NoixmodAPIParticleTypes {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries
            .PARTICLE_TYPES, NoixmodAPI.MOD_ID);
    public static final RegistryObject<SimpleParticleType> API_LAVA;
    public static final RegistryObject<SimpleParticleType> BLACK_CLOUD;
    public static final RegistryObject<SimpleParticleType> BLOOD;
    public static final RegistryObject<SimpleParticleType> BLOOD_SPELL;
    public static final RegistryObject<ParticleType<CircleParticleOption>> CIRCLE;
    public static final RegistryObject<SimpleParticleType> CLOUD;
    public static final RegistryObject<SimpleParticleType> COLORED_ASH;
    public static final RegistryObject<SimpleParticleType> CRACK;
    public static final RegistryObject<SimpleParticleType> DARK_SPELL = REGISTRY.register("dark_spell", supplier());
    public static final RegistryObject<SimpleParticleType> NIHILISTIC_FIRE = REGISTRY.register("nihilistic_fire_particle", supplier());
    public static final RegistryObject<SimpleParticleType> NIHILISTIC_SPELL = REGISTRY.register("nihilistic_spell", supplier());
    public static final RegistryObject<SimpleParticleType> NIHILISM_IMPART = REGISTRY.register("nihilism_ash", supplier());
    public static final RegistryObject<SimpleParticleType> NORMAL_SPELL;
    public static final RegistryObject<SimpleParticleType> PURPLE_ATTACK = REGISTRY.register("purple_attack", supplier());
    public static final RegistryObject<SimpleParticleType> PURPLE_FLAME = REGISTRY.register("purple_flame", supplier());
    public static final RegistryObject<SimpleParticleType> RED_SKULL = REGISTRY.register("red_skull", supplier());
    public static final RegistryObject<SimpleParticleType> RISING_PURPLE_FLAME = REGISTRY.register("rising_purple_flame", supplier());
    public static final RegistryObject<SimpleParticleType> SMALL_FIRE = REGISTRY.register("small_fire_particle", supplier());
    public static final RegistryObject<SimpleParticleType> SMALL_POOF = REGISTRY.register("small_poof", supplier());
    public static final RegistryObject<SimpleParticleType> WORM_PARTICLE = REGISTRY.register("worm_particle", supplier());
    public static final RegistryObject<SimpleParticleType> GOLDEN_FLAME;
    public static final RegistryObject<SimpleParticleType> SUMMON_PARTICLE;
    public static final RegistryObject<SimpleParticleType> WIND;

    private NoixmodAPIParticleTypes() {}

    private static SimpleParticleType simpleParticleType() {
        return new SimpleParticleType(false);
    }

    private static Supplier<SimpleParticleType> supplier() {
        return NoixmodAPIParticleTypes::simpleParticleType;
    }

    static {
        API_LAVA = REGISTRY.register("api_lava", supplier());
        BLACK_CLOUD = REGISTRY.register("black_cloud", supplier());
        BLOOD = REGISTRY.register("blood", supplier());
        BLOOD_SPELL = REGISTRY.register("blood_spell", supplier());
        CRACK = REGISTRY.register("crack", supplier());
        CIRCLE = REGISTRY.register("circle_particle", ()-> new ParticleType<>(false,
                CircleParticleOption.DESERIALIZER) {
            public Codec<CircleParticleOption> codec() {
                return CircleParticleOption.CODEC;
            }
        });
        CLOUD = REGISTRY.register("cloud", supplier());
        COLORED_ASH = REGISTRY.register("colored_ash", supplier());
        NORMAL_SPELL = REGISTRY.register("normal_spell", supplier());
        GOLDEN_FLAME = REGISTRY.register("golden_flame", supplier());
        SUMMON_PARTICLE = REGISTRY.register("summon_particle", supplier());
        WIND = REGISTRY.register("wind", supplier());
    }
}
