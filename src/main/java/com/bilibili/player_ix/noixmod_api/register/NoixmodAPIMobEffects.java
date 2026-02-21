
package com.bilibili.player_ix.noixmod_api.register;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.mob_effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NoixmodAPIMobEffects {
    public static final DeferredRegister<MobEffect> REGISTER = DeferredRegister.create(
            ForgeRegistries.MOB_EFFECTS, NoixmodAPI.MOD_ID);
    public static final RegistryObject<MobEffect> CORROSION;
    public static final RegistryObject<MobEffect> DESIRE_FOR_BLOOD;
    public static final RegistryObject<MobEffect> NIHILISTIC;
    public static final RegistryObject<MobEffect> STUN;
    public static final RegistryObject<MobEffect> TETANUS;
    public static final RegistryObject<MobEffect> VAMPIRE;
    public static final RegistryObject<MobEffect> WET;
    private NoixmodAPIMobEffects() {}

    static {
        CORROSION = REGISTER.register("corrosion", ()-> new Corrosion(-10092402)
                .addAttributeModifier(Attributes.ARMOR, "20263B89-116E-49AC-9B6B-9971489B9BE5",
                        0, AttributeModifier.Operation.ADDITION));
        DESIRE_FOR_BLOOD = REGISTER.register("desire_for_blood", DesireForBlood::new);
        NIHILISTIC = REGISTER.register("nihilistic_effect", ()-> new NihilisticEffect(-10092442)
                .addAttributeModifier(Attributes.ARMOR, "22653B89-116E-49AC-9B6B-9971489B9BE5", 0,
                        AttributeModifier.Operation.ADDITION)
                .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "22653B89-116E-49AC-9B6B-9971489B5BE9",
                        0, AttributeModifier.Operation.ADDITION)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, "22653B89-116E-49AC-9B6B-9971489B9BE9",
                        0, AttributeModifier.Operation.ADDITION));
        STUN = REGISTER.register("stun", ()-> new Stun().addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "22657B89-116E-49AC-9B6B-9971489B9BE9", 0, AttributeModifier.
                        Operation.MULTIPLY_BASE));
        TETANUS = REGISTER.register("tetanus", Tetanus::new);
        VAMPIRE = REGISTER.register("vampire_effect", ()-> new VampirePotion().addAttributeModifier(Attributes
                .ATTACK_DAMAGE, "22653B89-116E-49AC-9B6B-9971489B5BE5", 0, AttributeModifier
                .Operation.ADDITION));
        WET = REGISTER.register("wet", ()-> new Wet(1950417).addAttributeModifier(Attributes
                .MOVEMENT_SPEED, "22653B89-116A-49EC-9B6D-9971489B5BE5", 0, AttributeModifier
                .Operation.ADDITION));
    }
}
