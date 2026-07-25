
package com.bilibili.player_ix.noixmod_api.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class NoixmodAPIAttributesConfig {
    private static ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec.DoubleValue apostleArmor;
    public static ForgeConfigSpec.DoubleValue apostleArmorToughness;
    public static ForgeConfigSpec.DoubleValue apostleMaxHealth;
    public static ForgeConfigSpec.DoubleValue apostleDamageCap;
    public static ForgeConfigSpec.IntValue apostleArcheryCooldown;
    public static ForgeConfigSpec.DoubleValue girlGhostHealth;
    public static ForgeConfigSpec.DoubleValue girlGhostArmor;
    public static ForgeConfigSpec.DoubleValue girlGhostDamage;
    public static ForgeConfigSpec.DoubleValue lavaZombieArmor;
    public static ForgeConfigSpec.DoubleValue lavaZombieDamage;
    public static ForgeConfigSpec.DoubleValue lavaZombieHealth;
    public static ForgeConfigSpec.DoubleValue vampireHealth;
    public static ForgeConfigSpec.DoubleValue vampireArmor;
    public static ForgeConfigSpec.DoubleValue headhunterHealth;
    public static ForgeConfigSpec.DoubleValue headhunterArmor;
    public static ForgeConfigSpec.DoubleValue headhunterDamageCap;
    public static ForgeConfigSpec SPEC;

    public static void load(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

    private static void push(String mes) {
        builder.push(mes);
    }

    private static ForgeConfigSpec.Builder comment(String mes) {
        return builder.comment(mes);
    }

    private static void pop() {
        builder.pop();
    }

    static {
        builder.push("NoixmodAPIAttributes");
        comment("You need to quit the game to enable changes.");
        builder.push("Boss");
        builder.push("Nihilist");
        builder.push("Apostle");
        apostleArmor = builder.comment("Armor amount of Apostle(s), Default : 10.0")
                .defineInRange("使徒盔甲值", 10.0, 0, Double.MAX_VALUE);
        apostleArmorToughness = builder.comment("ArmorToughness amount of Apostle(s), Default : 6.0")
                .defineInRange("使徒盔甲韧性", 6.0, 0, Double.MAX_VALUE);
        apostleMaxHealth = builder.comment("Max health amount of Apostle(s), Default : 320.0")
                .defineInRange("使徒最大生命值", 320.0, 0, Double.MAX_VALUE);
        apostleDamageCap = builder.comment("Damage cap of Apostle(s), Default : 12.5")
                .defineInRange("使徒限伤", 12.5D, Double.MIN_VALUE, Float.MAX_VALUE);
        apostleArcheryCooldown = builder.comment("ArcheryCooldown of apostles, Default:30").
                defineInRange("使徒射箭冷却", 30, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.pop();
        builder.push("Headhunter");
        headhunterHealth = builder.comment("Max health amount of Headhunters, Default : 450.0")
                .defineInRange("猎头者最大生命值", 450.0, 0, Double.MAX_VALUE);
        headhunterArmor = builder.comment("Armor amount of Headhunters, Default : 16.0")
                .defineInRange("猎头者护甲", 16.0, 0.0, Double.MAX_VALUE);
        headhunterDamageCap = builder.comment("Damage cap of Headhunters, Default : 17.0")
                .defineInRange("猎头者限伤", 17.0, 0.0, Double.MAX_VALUE);
        builder.pop();
        builder.pop();
        builder.push("OwnableEntity");
        builder.push("GirlGhost");
        girlGhostHealth = builder.comment("Max health amount of GirlGhosts, Default:16.0")
                .defineInRange("女孩幽灵最大生命值", 16.0, 0.0, Double.MAX_VALUE);
        girlGhostArmor = builder.comment("Armor amount of GirlGhosts, Default:0.0")
                .defineInRange("女孩幽灵盔甲值", 0.0, 0.0, Double.MAX_VALUE);
        girlGhostDamage = builder.comment("Damage amount of GirlGhosts, Default:3.0")
                .defineInRange("女孩幽灵伤害值", 3.0, Double.MIN_VALUE, Double.MAX_VALUE);
        builder.pop();
        builder.push("LavaZombie");
        lavaZombieArmor = builder.comment("Armor amount of LavaZombies, Default:3.0")
                .defineInRange("熔岩僵尸盔甲值", 3.0, 0, Double.MAX_VALUE);
        lavaZombieDamage = builder.comment("Damage amount of LavaZombies, Default:5.0")
                .defineInRange("熔岩僵尸伤害值", 5.0, Double.MIN_VALUE, Double.MAX_VALUE);
        lavaZombieHealth = builder.comment("Max health amount of LavaZombies, Default : 24.0")
                .defineInRange("熔岩僵尸最大生命值", 24.0, Double.MIN_VALUE, Double.MAX_VALUE);
        builder.pop();
        builder.pop();
        builder.push("NaturalMonsters");
        builder.push("Bloodsucker");
        vampireArmor = builder.comment("Armor amount of Bloodsuckers(Vampires), Default:0.0")
                .defineInRange("吸血鬼盔甲值", 0.0, 0.0, Double.MAX_VALUE);
        vampireHealth = builder.comment("Max health amount of Bloodsuckers(Vampires), Default:40.0")
                .defineInRange("吸血鬼最大生命值", 40.0, Double.MIN_VALUE, Double.MAX_VALUE);
        builder.pop();
        builder.pop();
        builder.pop();
        SPEC = builder.build();
    }
}
