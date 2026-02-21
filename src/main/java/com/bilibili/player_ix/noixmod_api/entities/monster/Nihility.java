
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.github.NineAbyss9.ix_api.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class Nihility
extends PathfinderMob {
    public Nihility(EntityType<Nihility> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public void spawnAnim() {
        if (this.level().isClientSide) {
            ParticleUtil.spawnAnim(ParticleTypes.LARGE_SMOKE, this.level(), this);
        }
    }

    protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
        this.spawnAtLocation(ItemStacks.of(NoixmodAPIItems.NIHILISTIC_ESSENCE));
    }

    public static AttributeSupplier createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 5).add(
                Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0).build();
    }
}
