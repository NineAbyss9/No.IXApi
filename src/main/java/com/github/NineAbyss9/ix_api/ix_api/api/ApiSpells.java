
package com.github.NineAbyss9.ix_api.ix_api.api;

import com.github.NineAbyss9.ix_api.ix_api.util.Colors;
import net.minecraftforge.common.IExtensibleEnum;

public class ApiSpells
{
    public ApiSpells() {
    }

    public enum ApiSpell implements IExtensibleEnum
    {
        NONE(0, 0.0, 0.0, 0.0),
        RANGE(1, 0.7, 0.7, 0.8),
        ATTACK(2, 0.4, 0.3, 0.35),
        NIHILISTIC(3, 0.9, 0.3, 0.9),
        FIRE(4, 1.0, 0.6, 0.0),
        WATER(5, 0.3, 0.3, 0.8),
        DARK(6, 0.01, 0.01, 0.01),
        UNKNOWN(7, 0.3, 0.3, 0.3),
        POTION(8, 0.3, 0.9, 0.3),
        REGEN(9, 0, 0.7, 0.7),
        WATERS(10, 0.1, 0.1, 0.79),
        ZOMBIE(11, 0.3, 0.85, 0.3),
        BLOOD(12, Colors.RED);
        public final int id;
        public final double[] spellColor;

        ApiSpell(int i, double x, double y, double z) {
            this.id = i;
            this.spellColor = new double[]{x, y, z};
        }

        ApiSpell(int id, float[] floats) {
            this.id = id;
            this.spellColor = Colors.toDouble(floats);
        }

        @SuppressWarnings("unused")
        public static ApiSpell create(String name, int id, double x, double y, double z) {
            throw new IllegalStateException("Enum not extended");
        }

        public static ApiSpell getById(int nt) {
            for (ApiSpell spellType : ApiSpell.values()) {
                if (nt != spellType.id) continue;
                return spellType;
            }
            return NONE;
        }
    }
}
