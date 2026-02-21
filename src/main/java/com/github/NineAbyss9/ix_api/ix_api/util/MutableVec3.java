
package com.github.NineAbyss9.ix_api.ix_api.util;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.PFMAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;

/**A mutable {@linkplain Vec3}
 *
 * @author Player_IX*/
@PFMAreNonnullByDefault
public class MutableVec3 implements Position {
    public static final MutableVec3 ZERO = new MutableVec3(0, 0, 0);
    private double x;
    private double y;
    private double z;
    /**Creates a new {@linkplain MutableVec3}*/
    public MutableVec3(double pX, double pY, double pZ) {
        x = pX;
        y = pY;
        z = pZ;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public MutableVec3 add(double px, double py, double pz) {
        this.x += px;
        this.y += py;
        this.z += pz;
        return this;
    }

    public MutableVec3 set(double px, double py, double pz) {
        this.x = px;
        this.y = py;
        this.z = pz;
        return this;
    }

    public MutableVec3 setX(double px) {
        this.x = px;
        return this;
    }

    public MutableVec3 setY(double py) {
        this.y = py;
        return this;
    }

    public MutableVec3 setZ(double pz) {
        this.z = pz;
        return this;
    }

    public MutableVec3 copy() {
        return new MutableVec3(x, y, z);
    }

    public BlockPos toBlockPos() {
        return BlockPos.containing(this);
    }

    public BlockPos.MutableBlockPos toMutableBlockPos() {
        return new BlockPos.MutableBlockPos(x, y, z);
    }

    public Vec3 toVec3() {
        return new Vec3(this.x, this.y, this.z);
    }

    public Vec9 toVec9() {
        return Vec9.of(this);
    }

    public static MutableVec3 create(double px, double py, double pz) {
        return new MutableVec3(px, py, pz);
    }
}
