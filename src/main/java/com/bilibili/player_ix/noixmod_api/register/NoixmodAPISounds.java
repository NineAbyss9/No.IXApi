
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NoixmodAPISounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, NoixmodAPI.MOD_ID);
    public static final RegistryObject<SoundEvent> APOSTLE_CAST_SPELL = REGISTRY.register("apostle_cast_spell", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "apostle_cast_spell")));
    public static final RegistryObject<SoundEvent> APOSTLE_SUMMON = REGISTRY.register("apostle_summons", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "apostle_summon")));
    public static final RegistryObject<SoundEvent> APOSTLE_PREPARE_SPELL = REGISTRY.register("apostle_prepare_spell", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "apostle_prepare_spell")));
    public static final RegistryObject<SoundEvent> APOSTLE_IDLE = REGISTRY.register("apostle_idle", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "apostle_idle")));
    public static final RegistryObject<SoundEvent> APOSTLE_HURT = REGISTRY.register("apostle_hurts", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "apostle_hurt")));
    public static final RegistryObject<SoundEvent> APOSTLE_HURT_HORROR = REGISTRY.register("apostle_hurt_horror", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "apostle_hurt_horror")));
    public static final RegistryObject<SoundEvent> APOSTLE_DEATH = REGISTRY.register("apostle_death", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "apostle_death")));
    public static final RegistryObject<SoundEvent> APOSTLE_MUSIC = REGISTRY.register("apostle_music_or", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "apostle_music_or")));
    public static final RegistryObject<SoundEvent> CLAP;
    public static final RegistryObject<SoundEvent> CRY = REGISTRY.register("cry", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "cry")));
    public static final RegistryObject<SoundEvent> CULTIST_AMBIENT = REGISTRY.register("cultist_idle", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "cultist_idle")));
    public static final RegistryObject<SoundEvent> CULTIST_HURT = REGISTRY.register("cultist_hurt", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "cultist_hurt")));
    public static final RegistryObject<SoundEvent> CULTIST_DEATH = REGISTRY.register("cultist_death", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "cultist_death")));
    public static final RegistryObject<SoundEvent> EI_MUSIC = REGISTRY.register("ei_music", ()-> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "ei_music")));
    public static final RegistryObject<SoundEvent> EVOKER_ILLAGER_IDLE = REGISTRY.register("evoker_illager_idle", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "evoker_illager_idle")));
    public static final RegistryObject<SoundEvent> EVOKER_ILLAGER_HURT = REGISTRY.register("evoker_illager_hurts", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "evoker_illager_hurts")));
    public static final RegistryObject<SoundEvent> EVOKER_ILLAGER_DEATH = REGISTRY.register("evoker_illager_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "evoker_illager_death")));
    public static final RegistryObject<SoundEvent> EVOKER_ILLAGER_SHOOT_FIREBALL = REGISTRY.register("evoker_illager_shoot_fireball", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("noixmodapi", "evoker_illager_shoot_fireball")));
    public static final RegistryObject<SoundEvent> PARTY_GLITCH;
    private NoixmodAPISounds() {}

    private static SoundEvent createVariableRangeEvent(String location) {
        return SoundEvent.createVariableRangeEvent(new ResourceLocation(NoixmodAPI.MOD_ID,
                location));
    }

    private static Supplier<SoundEvent> createVariableSupplier(String st) {
        return ()->createVariableRangeEvent(st);
    }

    private static RegistryObject<SoundEvent> register(String st) {
        return REGISTRY.register(st, createVariableSupplier(st));
    }

    static {
        CLAP = register("clap");
        PARTY_GLITCH = register("glitch");
    }
}
