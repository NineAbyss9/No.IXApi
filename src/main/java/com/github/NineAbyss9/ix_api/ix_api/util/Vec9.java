
package com.github.NineAbyss9.ix_api.ix_api.util;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.PFMAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

@PFMAreNonnullByDefault
public final class Vec9 extends Vec3 {
    public Vec9(double pX, double pY, double pZ) {
        super(pX, pY, pZ);
    }

    public Vec9(Vector3f vector3f) {
        super(vector3f);
    }

    public Vec9(Vector9f vector9f) {
        this(new Vector3f(vector9f.getX(), vector9f.getY(), vector9f.getZ()));
    }

    public static CompoundTag createVec3Tag(Vec3 vec3, String main) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble(main + "X", vec3.x());
        tag.putDouble(main + "Y", vec3.y());
        tag.putDouble(main + "Z", vec3.z());
        return tag;
    }

    public static Vec3 readVec3Tag(CompoundTag tag, String string) {
        return new Vec9(new Vector3f((float)tag.getDouble(string + "X"), (float)tag
                .getDouble(string + "Y"), (float)tag.getDouble(string + "Z")));
    }

    public static Vec3 moveToVec(Entity entity, Entity target, double speed) {
        double xDPower = entity.getX() - target.getX();
        double yDPower = entity.getY() - target.getY();
        double zDPower = entity.getZ() - target.getZ();
        double d = Math.sqrt(xDPower * xDPower + yDPower * yDPower + zDPower * zDPower);
        double xPower = -(xDPower / d * 5.0 * speed);
        double yPower = -(yDPower / d * 5.0 * speed);
        double zPower = -(zDPower / d * 5.0 * speed);
        return new Vec9(xPower, yPower, zPower);
    }

    public static Vec9 of() {
        return new Vec9(0.0, 0.0, 0.0);
    }

    public static Vec9 of(double x, double y, double z) {
        return new Vec9(x, y, z);
    }

    public static Vec9 of(BlockPos pos) {
        return new Vec9(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec9 of(MutableVec3 vec3) {
        return new Vec9(vec3.x(), vec3.y(), vec3.z());
    }

    public boolean equals(Object pOther) {
        if (this == pOther) {
            return true;
        } else if (pOther instanceof Vec3 vec3) {
            if (Double.compare(vec3.x, this.x) != 0) {
                return false;
            } else if (Double.compare(vec3.y, this.y) != 0) {
                return false;
            } else {
                return Double.compare(vec3.z, this.z) == 0;
            }
        } else {
            return false;
        }
    }
}
