
package com.bilibili.player_ix.noixmod_api.client.model.nihilistic;

import com.bilibili.player_ix.noixmod_api.entities.boss.abyss.Abyss;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

@OnlyInClient
public class GolemMasterModel<T extends Abyss>
extends NihilistIllagerModel<T> {
    public GolemMasterModel(ModelPart $$0) {
        super($$0);
        this.hat.visible = true;
    }

    @NotNull
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = NihilistIllagerModel.createMesh();
        PartDefinition part = meshdefinition.getRoot();
        return LayerDefinition.create(meshdefinition, 64, 128);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}
