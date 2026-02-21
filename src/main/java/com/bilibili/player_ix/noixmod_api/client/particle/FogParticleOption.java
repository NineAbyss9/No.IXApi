
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.ix_api.util.Vector9f;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class FogParticleOption implements ParticleOptions {
    private final float alpha;
    private final Vector9f vector;
    public FogParticleOption(float alpha, Vector9f vector9f) {
        this.alpha = alpha;
        this.vector = vector9f;
    }

    public ParticleType<?> getType() {
        return ParticleTypes.LARGE_SMOKE;
    }

    public void writeToNetwork(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeFloat(alpha);
        friendlyByteBuf.writeFloat(vector.getX());
        friendlyByteBuf.writeFloat(vector.getY());
        friendlyByteBuf.writeFloat(vector.getZ());
    }

    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), this.alpha, vector.getX(), vector.getY(), vector.getZ());
    }
}
