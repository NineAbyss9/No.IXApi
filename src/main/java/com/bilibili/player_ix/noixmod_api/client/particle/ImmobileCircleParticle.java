
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;

public abstract class ImmobileCircleParticle extends TextureSheetParticle {
    public static final Vector3f ROTATION_VECTOR = Util.make(new Vector3f(0.5F, 0.5f, 0.5f),
            Vector3f::normalize);
    public static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    protected ImmobileCircleParticle(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    protected ImmobileCircleParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed,
                                     double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
    }

    public void render(VertexConsumer p_233985_, Camera p_233986_, float p_233987_) {
        this.alpha = 1.0F - Mth.clamp(((float)this.age + p_233987_) / (float)this.lifetime, 0.0F, 1.0F);
        this.renderRotatedParticle(p_233985_, p_233986_, p_233987_, (p_234005_) -> {
            p_234005_.mul(new Quaternionf()).rotationX(-Maths.modelDegrees(90));
        });
        this.renderRotatedParticle(p_233985_, p_233986_, p_233987_, (p_234000_) -> {
            p_234000_.mul((new Quaternionf()).rotationYXZ(-Maths.CLOSER_PI, Maths.modelDegrees(90), 0));
        });
    }

    public void renderRotatedParticle(VertexConsumer p_233989_, Camera p_233990_, float p_233991_,
                                      Consumer<Quaternionf> p_233992_) {
        Vec3 vec3 = p_233990_.getPosition();
        float f = (float)(Mth.lerp(p_233991_, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp(p_233991_, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp(p_233991_, this.zo, this.z) - vec3.z());
        Quaternionf quaternionf = new Quaternionf().setAngleAxis(0.0F, ROTATION_VECTOR.x(),
                ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        p_233992_.accept(quaternionf);
        quaternionf.transform(TRANSFORM_VECTOR);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F,
                0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(p_233991_);
        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternionf);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }
        int j = this.getLightColor(p_233991_);
        this.makeCornerVertex(p_233989_, avector3f[0], this.getU1(), this.getV1(), j);
        this.makeCornerVertex(p_233989_, avector3f[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(p_233989_, avector3f[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(p_233989_, avector3f[3], this.getU0(), this.getV1(), j);
    }

    public void makeCornerVertex(VertexConsumer consumer, Vector3f vector, float v, float v1, int v2) {
        consumer.vertex(vector.x(), vector.y(), vector.z()).uv(v, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(v2)
                .endVertex();
    }
}
