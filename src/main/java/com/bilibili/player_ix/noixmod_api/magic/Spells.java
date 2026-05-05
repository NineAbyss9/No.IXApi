
package com.bilibili.player_ix.noixmod_api.magic;

import com.bilibili.player_ix.noixmod_api.magic.end.EndermanSpell;
import com.bilibili.player_ix.noixmod_api.magic.illager.EscapeSpell;
import com.bilibili.player_ix.noixmod_api.magic.illager.SelfFangsSpell;
import com.bilibili.player_ix.noixmod_api.magic.illager.VexArcherSpell;
import com.bilibili.player_ix.noixmod_api.magic.misc.*;
import com.bilibili.player_ix.noixmod_api.magic.nether.LavaTrapSpell;
import com.bilibili.player_ix.noixmod_api.magic.nether.LavaZombieSpell;
import com.bilibili.player_ix.noixmod_api.magic.nether.NetherSoulSpell;
import com.bilibili.player_ix.noixmod_api.magic.nether.WitherSkeletonServantSpell;
import com.bilibili.player_ix.noixmod_api.magic.nihilistic.CrackSpell;
import com.bilibili.player_ix.noixmod_api.magic.nihilistic.NihilisticRoarSpell;
import com.bilibili.player_ix.noixmod_api.magic.nihilistic.NihilisticServantSpell;
import com.bilibili.player_ix.noixmod_api.magic.villager.VillagerGolemSpell;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class Spells {
    public static final Map<String, ISpell> SPELL_REGISTRY = Maps.newHashMap();
    public static final SpellType CRACK = register("crack", CrackSpell::new);
    public static final SpellType CREEPER = register("creeper", CreeperSpell::new);
    public static final SpellType DROWNED = register("drowned", DrownedSpell::new);
    public static final SpellType ENDERMAN = register("enderman", EndermanSpell::new);
    public static final SpellType ESCAPE = register("escape", EscapeSpell::new);
    public static final SpellType GOLD = register("gold", GoldSpell::new);
    public static final SpellType GROUND = register("ground", GroundSpell::new);
    public static final SpellType LAVA_TRAP = register("lava_trap", LavaTrapSpell::new);
    public static final SpellType LAVA_ZOMBIE = register("lava_zombie", LavaZombieSpell::new);
    public static final SpellType NETHER_SOUL = register("nether_soul", NetherSoulSpell::new);
    public static final SpellType NIHILISTIC_ROAR = register("nihilistic_roar", NihilisticRoarSpell::new);
    public static final SpellType NIHILISTIC_SERVANT = register("nihilistic_servant", NihilisticServantSpell::new);
    public static final SpellType SELF_FANGS = register("self_fangs", SelfFangsSpell::new);
    public static final SpellType SKELETON = register("skeleton", SkeletonSpell::new);
    public static final SpellType SMOKE_TRAP = register("smoke_trap", SmokeTrapSpell::new);
    public static final SpellType VAMPIRE = register("vampire", VampireSpell::new);
    public static final SpellType VEX_ARCHER = register("vex_archer", VexArcherSpell::new);
    public static final SpellType VILLAGER_GOLEM = register("villager_golem", VillagerGolemSpell::new);
    public static final SpellType WITHER_SKELETON = register("wither_skeleton", WitherSkeletonServantSpell::new);
    private Spells() {
    }

    public static ISpell get(String name) {
        return SPELL_REGISTRY.get(name);
    }

    public static boolean contains(String name) {
        return SPELL_REGISTRY.containsKey(name);
    }

    private static SpellType register(String name, Supplier<ISpell> spell) {
        if (!SPELL_REGISTRY.containsKey(name)) {
            SPELL_REGISTRY.put(name, spell.get());
        }
        return new SpellType(name, spell);
    }
}
