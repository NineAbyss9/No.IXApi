
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.github.NineAbyss9.ix_api.ix_api.api.annotation.CodeFrom;
import com.github.NineAbyss9.ix_api.ix_api.util.Vector9f;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

@CodeFrom(from = "Polarice:Goety:CircleExplosionParticleOption")
public class CircleParticleOption implements ParticleOptions {
    public static final Codec<CircleParticleOption> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
            Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
            Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
            Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
            Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
            Codec.FLOAT.fieldOf("speed").forGetter(d -> d.speed)
    ).apply(instance, CircleParticleOption::new));
    @SuppressWarnings("deprecation")
    public static final Deserializer<CircleParticleOption> DESERIALIZER = new Deserializer<>() {
        public CircleParticleOption fromCommand(ParticleType<CircleParticleOption> pType, StringReader pReader)
                throws CommandSyntaxException {
            pReader.expect(' ');
            float r = pReader.readFloat();
            pReader.expect(' ');
            float g = pReader.readFloat();
            pReader.expect(' ');
            float b = pReader.readFloat();
            pReader.expect(' ');
            float s = pReader.readFloat();
            pReader.expect(' ');
            float s2 = pReader.readFloat();
            return new CircleParticleOption(r, g, b, s, s2);
        }

        public CircleParticleOption fromNetwork(ParticleType<CircleParticleOption> pType, FriendlyByteBuf pBuffer) {
            return new CircleParticleOption(pBuffer.readFloat(), pBuffer.readFloat(), pBuffer.readFloat(),
                    pBuffer.readFloat(), pBuffer.readInt());
        }
    };
    private final float red;
    private final float green;
    private final float blue;
    private final float size;
    private final float speed;

    public CircleParticleOption(float r, float g, float b, float size, float speed) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.size = size;
        this.speed = speed;
    }

    public CircleParticleOption(Vector9f vector9f, float size, int speed) {
        this(new float[]{vector9f.getX(), vector9f.getY(), vector9f.getZ()}, size, speed);
    }

    public CircleParticleOption(float[] rgb, float size, int speed) {
        this(rgb[0], rgb[1], rgb[2], size, speed);
    }

    public void writeToNetwork(FriendlyByteBuf p_235956_) {
        p_235956_.writeFloat(this.red);
        p_235956_.writeFloat(this.green);
        p_235956_.writeFloat(this.blue);
        p_235956_.writeFloat(this.size);
        p_235956_.writeFloat(this.speed);
    }

    @SuppressWarnings("deprecation")
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %s",
                BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.red, this.green, this.blue, this.size, this.speed);
    }

    public ParticleType<CircleParticleOption> getType() {
        return NoixmodAPIParticleTypes.CIRCLE.get();
    }

    public float getRed() {
        return this.red;
    }

    public float getGreen() {
        return this.green;
    }

    public float getBlue() {
        return this.blue;
    }

    public float getSize(){
        return this.size;
    }

    public float getSpeed(){
        return this.speed;
    }
}
