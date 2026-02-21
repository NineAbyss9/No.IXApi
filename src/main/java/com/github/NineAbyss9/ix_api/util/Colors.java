
package com.github.NineAbyss9.ix_api.util;

import com.org.NineAbyss9.annotation.PAMAreNonnullByDefault;
import net.minecraft.client.particle.Particle;

import java.awt.*;

/**Nutshell, the first {@code float} is red, and the second is green, the end is blue.(The "RGB")
 *
 * @author Player_IX
 */
@PAMAreNonnullByDefault
@SuppressWarnings("unused")
public class Colors {
    public static final float[] RED = new float[] {
            0.7F, 0.3F, 0.3F
    };
    public static final float[] LIGHT_RED = new float[] {
            1, 0, 0
    };
    public static final float[] GREEN = new float[] {
            0.3F, 0.7F, 0.3F
    };
    public static final float[] BLUE = new float[] {
            0.3F, 0.3F, 0.7F
    };
    public static final float[] LIGHT_BLUE = new float[] {
            0.3F, 0.7F, 0.7F
    };
    public static final float[] PURPLE = new float[] {
            0.7F, 0.3F, 0.7F
    };
    public static final float[] DARK_PURPLE = new float[] {
            0.3F, 0, 0.3F
    };
    public static final float[] LIGHT_PURPLE = new float[] {
            1, 0, 1
    };

    public Colors() {
    }

    public static double[] toDouble(float[] floats) {
        return new double[] {
                floats[0], floats[1], floats[2]
        };
    }

    public static Color newColor(int colorCode) {
        return Color.decode(String.valueOf(colorCode));
    }

    public static void setColor(Particle particle, double d, double d1, double d2) {
        particle.setColor((float)d, (float)d1, (float)d2);
    }
}
