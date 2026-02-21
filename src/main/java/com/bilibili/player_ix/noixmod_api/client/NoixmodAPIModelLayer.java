
package com.bilibili.player_ix.noixmod_api.client;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class NoixmodAPIModelLayer {
    public static final ModelLayerLocation ABSTRACT_PLAYER = AbstractPlayerModel.ABSTRACT_PLAYER;
    public static final ModelLayerLocation PLAYER_INNER = registerInnerArmor("player");
    public static final ModelLayerLocation PLAYER_OUTER = registerOuterArmor("player");
    public static final ModelLayerLocation API_GHAST = new ModelLayerLocation(NoixmodAPI.location("ag"),
            "main");
    public static final ModelLayerLocation API_HUMANOID = new ModelLayerLocation(NoixmodAPI.location("ah"),
            "main");
    public static final ModelLayerLocation API_SKELETON = new ModelLayerLocation(NoixmodAPI.location("ask"),
            "main");
    public static final ModelLayerLocation API_SPIDER = new ModelLayerLocation(NoixmodAPI.location("as"),
            "main");
    public static final ModelLayerLocation API_ZOMBIE = ApiZombieModel.API_ZOMBIE;
    public static final ModelLayerLocation APOSTLE = ApostleModel.APOSTLE;
    public static final ModelLayerLocation DAN_DING = new ModelLayerLocation(NoixmodAPI.location("dante"), "main");
    public static final ModelLayerLocation DD_INNER_ARMOR = registerInnerArmor("dante");
    public static final ModelLayerLocation DD_OUTER_ARMOR = registerOuterArmor("dante");
    public static final ModelLayerLocation DEAD_ILLAGER_SKULL = new ModelLayerLocation(
      NoixmodAPI.location("dis"), "main"
    );
    public static final ModelLayerLocation EI = new ModelLayerLocation(
            NoixmodAPI.location("eir"), "main"
    );
    public static final ModelLayerLocation GIRL_GHOST = GirlGhostModel.LOCATION;
    public static final ModelLayerLocation HEAD_HUNTER = HeadHunterModel.LAYER_LOCATION;
    public static final ModelLayerLocation GOLEM_MASTER = new ModelLayerLocation(new ResourceLocation(NoixmodAPI.MOD_ID, "gm"), "main");
    public static final ModelLayerLocation INTRUDER = new ModelLayerLocation(NoixmodAPI.location("intruder"), "main");
    public static final ModelLayerLocation LURKER = LurkerModel.LAYER_LOCATION;
    public static final ModelLayerLocation NIHILIST = NihilistIllagerModel.LAYER_LOCATION;
    public static final ModelLayerLocation STATUE = new ModelLayerLocation(new ResourceLocation(NoixmodAPI.MOD_ID, "statue"), "main");
    public static final ModelLayerLocation VILLAGER_FIGHTER = new ModelLayerLocation(new ResourceLocation(NoixmodAPI.MOD_ID, "villager"), "main");

    private static ModelLayerLocation registerInnerArmor(String p_171299_) {
        return register(p_171299_, "inner_armor");
    }

    private static ModelLayerLocation registerOuterArmor(String p_171304_) {
        return register(p_171304_, "outer_armor");
    }

    private static ModelLayerLocation register(String p_171296_, String p_171297_) {
        return createLocation(p_171296_, p_171297_);
    }

    private static ModelLayerLocation createLocation(String p_171301_, String p_171302_) {
        return new ModelLayerLocation(new ResourceLocation("noixmodapi", p_171301_), p_171302_);
    }
}
