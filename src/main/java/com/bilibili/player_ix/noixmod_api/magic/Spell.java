
package com.bilibili.player_ix.noixmod_api.magic;

import com.bilibili.player_ix.noixmod_api.register.event.SpellCastEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

public abstract class Spell
implements ISpell {
    protected Spell() {
    }

    public abstract Type getSpellType();

    public abstract float spellPower();

    public abstract void castSpell(ServerLevel pLevel, LivingEntity pCaster);

    public boolean defaultCastSpell(ServerLevel pLevel, LivingEntity pCaster) {
        return MinecraftForge.EVENT_BUS.post(new SpellCastEvent(pLevel, pCaster, this.getSpellType(), this));
    }

    protected ThreadLocalRandom random()
    {
        return ThreadLocalRandom.current();
    }

    protected double nextDouble(double start, double end)
    {
        return random().nextDouble(start, end);
    }

    protected float nextFloat(float start, float end)
    {
        return random().nextFloat(start, end);
    }

    protected double nextDouble()
    {
        return random().nextDouble();
    }

    protected float nextFloat()
    {
        return random().nextFloat();
    }

    public static ISpell of(Type type, float spellPower, BiConsumer<ServerLevel, LivingEntity> consumer) {
        return new Spell() {
            public Type getSpellType() {
                return type;
            }

            public float spellPower() {
                return spellPower;
            }

            public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
                if (defaultCastSpell(pLevel, pCaster)) {
                    consumer.accept(pLevel, pCaster);
                }
            }
        };
    }

    public enum Type {
        OVERWORLD,
        WATER,
        NETHER,
        END,
        ICE,
        NIHILISTIC,
        VILLAGER,
        ILLAGER,
        MISC
    }
}
