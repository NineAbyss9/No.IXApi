
package com.bilibili.player_ix.noixmod_api.client.renderer.nb;

import com.bilibili.player_ix.noixmod_api.client.model.nihilistic.NihilisticWitherModel;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticWither;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class NihilisticWitherRenderer<N extends NihilisticWither>
extends MobRenderer<N, NihilisticWitherModel<N>> {
    public NihilisticWitherRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new NihilisticWitherModel<>(p_174304_.bakeLayer(NihilisticWitherModel.WITHER)), 0.8f);
    }

    protected int getBlockLightLevel(N p_114496_, BlockPos p_114497_) {
        return 15;
    }

    protected void scale(N p_116439_, PoseStack p_116440_, float p_116441_) {
        float $$3 = 2.0F;
        int $$4 = p_116439_.getInvulnerableTicks();
        if ($$4 > 0) {
            $$3 -= ((float)$$4 - p_116441_) / 220.0F * 0.5F;
        }
        p_116440_.scale($$3, $$3, $$3);
    }

    private static final ResourceLocation POWERED = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/wither/wither_invulnerable.png");
    private static ResourceLocation LOC = new ResourceLocation("noixmodapi:textures/entities/nihilistic_mobs/wither/wither.png");

    public ResourceLocation getTextureLocation(N n) {
        int i = n.getInvulnerableTicks();
        boolean flag = i > 0 && (i > 80 || i / 5 % 2 != 1);
        boolean flag1 = n.isGivingBackDamage();
        if (flag || flag1) {
            return POWERED;
        }
        return LOC;
    }
}
