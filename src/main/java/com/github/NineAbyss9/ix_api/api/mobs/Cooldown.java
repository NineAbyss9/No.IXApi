
package com.github.NineAbyss9.ix_api.api.mobs;

import com.google.common.collect.Maps;
import net.minecraft.util.Mth;

import java.util.Map;

/**The {@code Cooldown} class is a util that save cooldowns in entities.
 */
public class Cooldown {
    public final Map<String, CooldownInstance> cooldowns = Maps.newHashMap();
    protected int tickCount;
    public Cooldown() {
    }

    public boolean isOnCooldown(String  p_41520_) {
        return this.getCooldownPercent(p_41520_, 0.0F) > 0.0F;
    }

    public float getCooldownPercent(String  p_41522_, float p_41523_) {
        CooldownInstance $$2 = this.cooldowns.get(p_41522_);
        if ($$2 != null) {
            float $$3 = (float)($$2.endTime - $$2.startTime);
            float $$4 = (float)$$2.endTime - ((float)this.tickCount + p_41523_);
            return Mth.clamp($$4 / $$3, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public void tick() {
        ++this.tickCount;
        if (!this.cooldowns.isEmpty()) {
            this.cooldowns.entrySet().removeIf(parameter -> parameter.getValue().endTime <= this.tickCount);
        }
    }

    public int getCooldown(String st) {
        CooldownInstance instance = this.cooldowns.get(st);
        return instance.endTime - this.tickCount;
    }

    public void addCooldown(String p_41525_, int p_41526_) {
        this.cooldowns.put(p_41525_, new CooldownInstance(this.tickCount, this.tickCount + p_41526_));
    }

    public void removeCooldown(String p_41528_) {
        this.cooldowns.remove(p_41528_);
    }

    public static class CooldownInstance {
        protected final int startTime;
        protected final int endTime;

        CooldownInstance(int p_186358_, int p_186359_) {
            this.startTime = p_186358_;
            this.endTime = p_186359_;
        }

        public static CooldownInstance create(int start, int stop) {
            return new CooldownInstance(start, stop);
        }
    }
}
