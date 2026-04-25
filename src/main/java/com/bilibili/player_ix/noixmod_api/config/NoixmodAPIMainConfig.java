
package com.bilibili.player_ix.noixmod_api.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;
import java.util.List;

public class NoixmodAPIMainConfig {
    //Villager
    public static final ForgeConfigSpec.DoubleValue VillagerMasterHealAmount;
    public static final ForgeConfigSpec.ConfigValue<List<String>> VILLAGERS_IGNORE;
    //Villager End

    public static final ForgeConfigSpec.IntValue WormBreedCooldown;

    //Illager
    public static final ForgeConfigSpec.BooleanValue TrumpeterJoinRaids;
    public static final ForgeConfigSpec.BooleanValue DrunkennessJoinRaids;
    public static final ForgeConfigSpec.BooleanValue FlagmanJoinRaids;
    public static final ForgeConfigSpec.BooleanValue GraveKeeperJoinRaids;
    public static final ForgeConfigSpec.BooleanValue EvokerIllagerRaid;
    public static final ForgeConfigSpec.BooleanValue BiologistRaid;
    public static final ForgeConfigSpec.BooleanValue HunterCanJoinRaid;

    public static final ForgeConfigSpec.IntValue TrumpeterCheerCooldown;

    //Raid count
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> ArmorerRaidCount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> DrunkennessRaidCount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> FlagmanRaidCount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> GraveKeeperRaidCount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> HunterRaidCount;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> BuglerRaidCount;
    //Raid count End

    public static final ForgeConfigSpec.IntValue ArmorerDistributeArmorConfig;
    public static final ForgeConfigSpec.BooleanValue ArmorerJoinRaid;
    public static final ForgeConfigSpec.IntValue ArmorerArmorEnchantLevel;
    public static final ForgeConfigSpec.DoubleValue MournerDamage;
    //Illager End

    //Abilities
    //Nihilistic
    public static final ForgeConfigSpec.BooleanValue ApostleCanCancelLivingHeal;
    public static final ForgeConfigSpec.IntValue CultistConversionInt;
    public static final ForgeConfigSpec.BooleanValue VampireBurnUnderSun;
    //Nihilistic End
    //Ab End

    //Spawn
    public static final ForgeConfigSpec.BooleanValue AquaticWormWillSpawn;
    public static final ForgeConfigSpec.BooleanValue IntruderWillSpawn;
    public static final ForgeConfigSpec.BooleanValue WormWillSpawn;
    public static final ForgeConfigSpec.BooleanValue VampireWillSpawn;
    public static final ForgeConfigSpec.BooleanValue NihilisticBlazeSpawn;
    public static final ForgeConfigSpec.BooleanValue GirlGhostCanSummon;
    public static final ForgeConfigSpec.BooleanValue PlateauBeastCanSummon;
    public static final ForgeConfigSpec.BooleanValue WindZombieCanSpawn;
    public static final ForgeConfigSpec.BooleanValue NihilisticOrderSpawn;
    public static final ForgeConfigSpec.BooleanValue YetiWillSpawn;
    //Spawn End
    //Horror Mode
    public static final ForgeConfigSpec.BooleanValue TERRIBLE_SKY;
    public static final ForgeConfigSpec.BooleanValue HorrorMode;
    public static final ForgeConfigSpec.BooleanValue SpawnHorror;
    public static final ForgeConfigSpec.BooleanValue disableXMinMap;
    // /Horror Mode
    public static final ForgeConfigSpec.BooleanValue PlayBossMusic;

    public static ForgeConfigSpec SPEC;

    public static void load(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("NoixmodAPIMainConfig");
        builder.push("HorrorMode");
        HorrorMode = builder.comment("No.IXAPI will be different to normal......, Default:false.Enable the following settings for a better experience.").define("API恐怖模式", false);
        TERRIBLE_SKY = builder.comment("If true, the sky will become ???.").define("TerribleSky", false);
        SpawnHorror = builder.comment("Will scaring mobs spawn?Default:false").worldRestart().define("SpawnScaringMobs", false);
        disableXMinMap = builder.comment("Disable Xaero's Minimap?Default:true(Enable horror mode first)").define("DisableXsMinimap", true);
        builder.pop();
        builder.push("Sounds");
        PlayBossMusic = builder.comment("Will bosses play music?Default:true(Deprecated)").define("是否播放Boss音乐", true);
        builder.pop();
        builder.push("Nihilists");
        builder.push("Order");
        NihilisticOrderSpawn = builder.comment("Will NihilisticOrder spawn ?Default:true").define("虚无教团是否生成", true);
        builder.pop();
        builder.push("Apostle");
        ApostleCanCancelLivingHeal = builder.comment("Can apostle(s) cancel LivingEntities heal, Default:true (If the horror mode has been enabled, the value returned by this configuration is invalid.;如果恐怖模式被打开了，那么这条配置返回的值无用)").define("使徒可以取消实体回复生命值", true);
        builder.pop();
        builder.push("Cultist");
        CultistConversionInt = builder.comment("What will Cultists convert, Default:0, 0 = ZombieVindicator, 1 = NihilisticServant, 2 = NihilisticBlaze").defineInRange("邪教徒转化的生物", 0, 0, 2);
        builder.pop();
        builder.pop();
        builder.push("Villagers");
        builder.push("All");
        VILLAGERS_IGNORE = builder.translation("config.noixmodapi.villager_ignore").define("VillagersIgnore",
                List.of("guardvillagers:guard","jerotesvillage:carved_hound","jerotesvillage:carved_llama"
                        ,"jerotesvillage:carved_iron_golem",
                        "jerotesvillage:carved_allay","jerotesvillage:carved_villager","noixmodapi:villager_golem",
                        "noixmodapi:villager_master","noixmodapi:villager_spellcaster","noixmodapi:ambusher","noixmodapi:exorcist"
                        ,"noixmodapi:villager_evoker"));
        builder.pop();
        builder.push("VillagerMaster");
        VillagerMasterHealAmount = builder.comment("Heal amount of VillagerMasters, Default : 4.0F").defineInRange("村民主宰回血量", 4f, 0f, Float.MAX_VALUE);
        builder.pop();
        builder.pop();
        builder.push("Worms");
        builder.push("All");
        WormBreedCooldown = builder.comment("How long will worms breed, Default: 100(The unit is seconds)").defineInRange("蠕虫繁殖冷却", 100, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.push("AquaticWorm");
        AquaticWormWillSpawn = builder.comment("Will aquatic worms spawn, Default:false").define("水生蠕虫是否生成", false);
        builder.pop();
        builder.push("Worm");
        WormWillSpawn = builder.comment("Will worms spawn, Default:false").worldRestart().define("蠕虫是否生成", false);
        builder.pop();
        builder.pop();
        builder.push("Illagers");
        builder.push("Biologist");
        BiologistRaid = builder.comment("Will biologists join raids?Default:true").worldRestart().define("生物学家加入袭击", true);
        builder.pop();
        builder.push("Bugler");
        TrumpeterCheerCooldown = builder.comment("How long will trumpeters cheer illagers, Default:500").defineInRange("吹号手鼓舞灾厄村民冷却", 500, 1, Integer.MAX_VALUE);
        TrumpeterJoinRaids = builder.comment("Can trumpeters join raids, Default:true").worldRestart().define("吹号手是否加入袭击", true);
        BuglerRaidCount = builder.comment("Bugler number in Raids.Default:0, 1, 3, 0, 2, 7, 4, 6")
                .define("号手袭击数量", List.of(0, 1, 3, 0, 2, 7, 4, 6), (i) -> i instanceof Integer);
        builder.pop();
        builder.push("Drunkenness");
        DrunkennessJoinRaids = builder.comment("Will drunkenness join raids?Default:true").worldRestart().define("酒徒是否加入袭击", true);
        DrunkennessRaidCount = builder.comment("Drunkenness number in Raids").worldRestart()
                .define("酒徒袭击数量", List.of(0, 0, 0, 1, 2, 1, 3, 2));
        builder.pop();
        builder.push("Bloodsucker");
        VampireWillSpawn = builder.comment("Can bloodsuckers spawn, Default:true").worldRestart().define("吸血鬼是否生成", true);
        VampireBurnUnderSun = builder.comment("Will bloodsuckers burn under sun, Default:true").define("吸血鬼是否在阳光下燃烧", true);
        builder.pop();
        builder.push("Flagman");
        FlagmanJoinRaids = builder.comment("Will flagmen join Raids?Default:true").worldRestart().define("旗手是否生成", true);
        FlagmanRaidCount = builder.comment("Flagman number in Raids.Default:1,1,1,1,1,1,1,1").worldRestart()
                .define("旗手袭击数量", List.of(1, 1, 1, 1, 1, 1, 1, 1), i -> i instanceof Integer);
        builder.pop();
        builder.push("GraveKeeper");
        GraveKeeperJoinRaids = builder.comment("Will GraveKeepers join raids?Default:true").worldRestart()
                .define("守墓人是否加入袭击", true);
        GraveKeeperRaidCount = builder.comment("GraveKeepers number in Raids.Default:0,0,0,0,0,0,0,1")
                .define("守墓人袭击数量", List.of(0, 0, 0, 0, 0, 0, 0, 1));
        builder.pop();
        builder.push("Intruder");
        IntruderWillSpawn = builder.comment("Will Intruders spawn?Default:true").worldRestart().define("不速之客是否生成", true);
        builder.pop();
        builder.push("EvokerIllager");
        EvokerIllagerRaid = builder.comment("Will EvokerIllager join raids?Default:true").worldRestart().define("EvokerIllager加入袭击", true);
        builder.pop();
        builder.push("Armorer");
        ArmorerArmorEnchantLevel = builder.comment("Level for armorers enchanting, Default:0").defineInRange("盔甲师附魔盔甲等级", 0, 0, 9);
        ArmorerDistributeArmorConfig = builder.comment("What armor will armorers distribute, Default:Iron. 0 = Iron, 1 = Gold, 2 = Diamond, 3 = Netherite, 4 = Advancednetherite:NetheriteDiamond")
                .defineInRange("盔甲师分发何种盔甲", 0, 0, 4);
        ArmorerJoinRaid = builder.comment("Will armorers join raids ?Default:true").worldRestart().define("盔甲师加入袭击", true);
        ArmorerRaidCount = builder.comment("Armorer number in Raids.Default:0, 1, 0, 3, 4, 0, 4, 6").worldRestart().define("盔甲师加入袭击数量",
                List.of(0, 1, 0, 3, 4, 0, 4, 6), (count) -> count instanceof Integer);
        builder.pop();
        builder.push("Hunter");
        HunterCanJoinRaid = builder.comment("Will hunters join raids ? Default:true").worldRestart().define("猎人加入袭击", true);
        HunterRaidCount = builder.comment("Hunter number in Raids.Default:0, 0, 1, 0, 3, 4, 6, 6").define("猎人袭击数量",
                List.of(0, 0, 1, 0, 3, 4, 6, 6), (count) -> count instanceof Integer);
        builder.pop();
        builder.push("Mourner");
        MournerDamage = builder.comment("Max damage amount for mourners, Default:17").defineInRange("哀悼者最大伤害", 17D, 0D, 114514D);
        builder.pop();
        builder.pop();
        builder.push("OwnableEntity");
        builder.push("GirlGhost");
        GirlGhostCanSummon = builder.comment("Will girl ghosts spawn ?Default:true").worldRestart().define("女孩幽灵是否生成", true);
        builder.pop();
        builder.push("WindZombie");
        WindZombieCanSpawn = builder.comment("Will wind zombies spawn ?Default:true").worldRestart().define("风尸是否生成", true);
        builder.pop();
        builder.push("Monster");
        builder.push("PlateauBeast");
        PlateauBeastCanSummon = builder.comment("Will plateau beasts spawn ?Default:true").worldRestart().define("高原野兽是否生成", true);
        builder.pop();
        builder.push("Yeti");
        YetiWillSpawn = builder.comment("Will yetis spawn ?Default:true").worldRestart().define("雪怪是否生成", true);
        builder.pop();
        builder.push("NihilisticBlaze");
        NihilisticBlazeSpawn = builder.comment("Will nihilistic blazes spawn?Default:true").worldRestart().define("SpawnNihilisticBlaze",
                true);
        builder.pop();
        builder.pop();
        builder.pop();
        SPEC = builder.build();
    }
}
