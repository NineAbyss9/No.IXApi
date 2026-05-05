
package com.bilibili.player_ix.noixmod_api.register;

import org.NineAbyss9.annotation.PAMAreNonnullByDefault;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIAttributesConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

@PAMAreNonnullByDefault
public class NoixmodAPIAttributes {
    private NoixmodAPIAttributes(){}
    public static double getAttributeValue(Attribute attribute, LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(attribute);
        double d;
        if (instance == null) {
            d = 0;
        } else {
            d = instance.getValue();
        }
        return d;
    }

    public static AttributeSupplier.Builder baseAttributes(double attackDamage, double speed, double knockbackResistance) {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.ATTACK_DAMAGE, attackDamage).add(Attributes.MOVEMENT_SPEED, speed)
                .add(Attributes.KNOCKBACK_RESISTANCE, knockbackResistance);
    }

    public static AttributeSupplier.Builder baseAttributes(double attackDamage, double speed) {
        return NoixmodAPIAttributes.baseAttributes(attackDamage, speed, 0.25);
    }

    public static AttributeSupplier.Builder createBeeAttributes() {
        return NoixmodAPIAttributes.baseAttributes(3, 0.3).add(Attributes.MAX_HEALTH, 12).add(Attributes.FLYING_SPEED, 1);
    }

    public static AttributeSupplier.Builder createDrownedAttributes() {
        return NoixmodAPIAttributes.baseAttributes(4, 0.23, 0.1).add(Attributes.ARMOR, 2).add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.MAX_HEALTH, 24);
    }

    public static AttributeSupplier.Builder createLavaAttributes() {
        return NoixmodAPIAttributes.baseAttributes(NoixmodAPIAttributesConfig.lavaZombieDamage.get(), 0.23, 0.2).add(Attributes.ARMOR, NoixmodAPIAttributesConfig.lavaZombieArmor.get()).add(Attributes.FOLLOW_RANGE, 72)
                .add(Attributes.MAX_HEALTH, NoixmodAPIAttributesConfig.lavaZombieHealth.get());
    }

    public static AttributeSupplier.Builder createVampireAttributes() {
        return NoixmodAPIAttributes.baseAttributes(3, 0.4)
                .add(Attributes.MAX_HEALTH, NoixmodAPIAttributesConfig.vampireHealth.get())
                .add(Attributes.FOLLOW_RANGE, 42)
                .add(Attributes.ARMOR, NoixmodAPIAttributesConfig.vampireArmor.get());
    }

    public static AttributeSupplier.Builder createWhiteDeathAttributes() {
        return NoixmodAPIAttributes.baseAttributes(7, 0.25, 0.75).add(Attributes.MAX_HEALTH, 450)
                .add(Attributes.ARMOR, 10).add(Attributes.FOLLOW_RANGE, 100);
    }

    public static AttributeSupplier.Builder createEnderManAttributes() {
        return NoixmodAPIAttributes.baseAttributes(7, 0.35, 0)
                .add(Attributes.FOLLOW_RANGE, 72).add(Attributes.MAX_HEALTH, 40);
    }

    public static AttributeSupplier.Builder createMushroomSpiderAttributes() {
        return NoixmodAPIAttributes.baseAttributes(3, 0.35, 0).add(Attributes.MAX_HEALTH, 18)
                .add(Attributes.FOLLOW_RANGE, 64);
    }

    public static AttributeSupplier.Builder createArmorerAttributes() {
        return NoixmodAPIAttributes.baseAttributes(3, 0.3, 0.25).add(Attributes.MAX_HEALTH, 24)
                .add(Attributes.ARMOR, 6).add(Attributes.FOLLOW_RANGE, 64);
    }

    public static AttributeSupplier.Builder createIntruderAttributes() {
        return NoixmodAPIAttributes.baseAttributes(7, 0.4, 0.5)
                .add(Attributes.MAX_HEALTH, 90).add(Attributes.ARMOR, 2).add(Attributes.FOLLOW_RANGE, 120);
    }

    public static AttributeSupplier.Builder createWormMasterAttributes() {
        return NoixmodAPIAttributes.baseAttributes(5, 0.25, 0.75)
                .add(Attributes.FOLLOW_RANGE, 72).add(Attributes.MAX_HEALTH, 300)
                .add(Attributes.ARMOR, 4);
    }

    public static AttributeSupplier.Builder createWitherSkeletonServantAttributes() {
        return NoixmodAPIAttributes.baseAttributes(4.0D, 0.27D, 0.1D)
                .add(Attributes.ARMOR, 2.0D).add(Attributes.FOLLOW_RANGE, 42)
                .add(Attributes.MAX_HEALTH, 20.0D);
    }

    public static AttributeSupplier.Builder createGirlGhostAttributes() {
        return NoixmodAPIAttributes.baseAttributes(NoixmodAPIAttributesConfig.girlGhostDamage.get(),
                        0.25, 0)
                .add(Attributes.MAX_HEALTH, NoixmodAPIAttributesConfig.girlGhostHealth.get())
                .add(Attributes.FOLLOW_RANGE, 42)
                .add(Attributes.ARMOR, NoixmodAPIAttributesConfig.girlGhostArmor.get());
    }

    public static AttributeSupplier.Builder createHunterAttributes() {
        return NoixmodAPIAttributes.baseAttributes(5, 0.35, 0)
                .add(Attributes.MAX_HEALTH, 26).add(Attributes.FOLLOW_RANGE, 52)
                .add(Attributes.ARMOR, 4);
    }

    public static AttributeSupplier.Builder createExorcistAttributes() {
        return NoixmodAPIAttributes.baseAttributes(4, 0.35, 0.25)
                .add(Attributes.ARMOR, 2).add(Attributes.MAX_HEALTH, 90)
                .add(Attributes.FOLLOW_RANGE, 90);
    }

    public static AttributeSupplier.Builder createDrunkennessAttributes() {
        return NoixmodAPIAttributes.baseAttributes(5, 0.37, 0.1)
                .add(Attributes.MAX_HEALTH, 30).add(Attributes.FOLLOW_RANGE, 32);
    }

    public static AttributeSupplier.Builder createMournerAttributes() {
        return NoixmodAPIAttributes.baseAttributes(5, 0.4, 0.25)
                .add(Attributes.FOLLOW_RANGE, 64).add(Attributes.MAX_HEALTH, 24);
    }

    public static AttributeSupplier.Builder createDIKAttributes() {
        return NoixmodAPIAttributes.baseAttributes(1, 0.3, 0)
                .add(Attributes.FOLLOW_RANGE, 42).add(Attributes.MAX_HEALTH, 2);
    }

    public static AttributeSupplier.Builder createSuperstitiousAttributes() {
        return NoixmodAPIAttributes.baseAttributes(13, 0.4, 1)
                .add(Attributes.MAX_HEALTH, 198).add(Attributes.FOLLOW_RANGE, 64);
    }

    public static AttributeSupplier.Builder createSuperstitiousCloneAttributes() {
        return NoixmodAPIAttributes.baseAttributes(13, 0.4, 1)
                .add(Attributes.MAX_HEALTH, 148).add(Attributes.FOLLOW_RANGE, 64);
    }

    public static AttributeSupplier.Builder createDetractorAttributes() {
        return NoixmodAPIAttributes.baseAttributes(5, 0.2314523421, 0.5)
                .add(Attributes.MAX_HEALTH, 30).add(Attributes.FOLLOW_RANGE, 42);
    }

    public static AttributeSupplier.Builder createSwordCultistAttributes() {
        return NoixmodAPIAttributes.baseAttributes(5, 0.3, 0.5)
                .add(Attributes.MAX_HEALTH, 26).add(Attributes.FOLLOW_RANGE, 52).add(
                        Attributes.ARMOR, 2
                );
    }

    public static AttributeSupplier.Builder createAmbusherAttributes() {
        return NoixmodAPIAttributes.baseAttributes(4, 0.3, 0.4)
                .add(Attributes.MAX_HEALTH, 32).add(Attributes.FOLLOW_RANGE, 90)
                .add(Attributes.ARMOR, 5);
    }
}
