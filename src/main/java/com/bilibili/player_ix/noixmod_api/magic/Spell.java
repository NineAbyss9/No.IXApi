
package com.bilibili.player_ix.noixmod_api.magic;

import com.bilibili.player_ix.noixmod_api.register.event.SpellCastEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;
import java.util.function.BiConsumer;

public abstract class Spell
implements ISpell {
    protected final Random random;
    protected Spell() {
        this.random = new Random();
    }

    public abstract Type getSpellType();

    public abstract float spellPower();

    public abstract void castSpell(ServerLevel pLevel, LivingEntity pCaster);

    public boolean defaultCastSpell(ServerLevel pLevel, LivingEntity pCaster) {
        return MinecraftForge.EVENT_BUS.post(new SpellCastEvent(pLevel, pCaster, this.getSpellType(), this));
    }

    public static ISpell of(Type type, float spellPower, BiConsumer<ServerLevel, LivingEntity> both) {
        return new Spell() {
            public Type getSpellType() {
                return type;
            }

            public float spellPower() {
                return spellPower;
            }

            public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
                if (defaultCastSpell(pLevel, pCaster)) {
                    both.accept(pLevel, pCaster);
                }
            }
        };
    }

    public enum Type {
        OVER_WORLD,
        WATER,
        NETHER,
        END,
        NIHILISTIC,
        VILLAGER,
        ILLAGER,
        MISC
    }
}
