
package com.github.NineAbyss9.ix_api.ix_api.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import java.math.BigDecimal;
import java.util.Random;

public final class Maths
extends Mth {
    private final BigDecimal localCalculation;
    public static final float CLOSER_PI = 3.141592653589793F;
    public static final float CLOSER_HALF_PI = CLOSER_PI / 2F;
    public static final float PI_DIVIDING_180 = CLOSER_PI / 180F;
    public static final double PI_DIVIDING_180_D = PI_DIVIDING_180;
    public static final RandomSource random = RandomSource.create();
    public static final Random RANDOM = new Random();
    public static int nextInt = random.nextInt();
    public static double nextDouble = random.nextDouble();
    public static float nextFloat = random.nextFloat();
    public static final byte ZERO_BYTE = (byte)0;
    public static final byte ONE_BYTE = (byte)1;
    public static final byte TWO_BYTE = (byte)2;
    public Maths(float number) {
        localCalculation = BigDecimal.valueOf(number);
    }

    public static int randomInt(int x) {
        if (x == 0) {
            return 0;
        }
        return (RANDOM.nextInt(x)) * Maths.trueOrFalse();
    }

    public static int randomInteger(int i) {
        return Maths.randomInteger(i, RANDOM);
    }

    public static int randomInteger(int i, Random source) {
        if (i == 0) {
            return 0;
        }
        return (i + source.nextInt(i)) * Maths.trueOrFalse();
    }

    public static int randomInteger(int i, RandomSource pSource) {
        if (i == 0) {
            return 0;
        }
        return (i + pSource.nextInt(i)) * Maths.trueOrFalse();
    }

    public static BigDecimal newBigDecimal(double number) {
        return BigDecimal.valueOf(number);
    }

    public BigDecimal plus() {
        return localCalculation.plus();
    }

    public BigDecimal add(float f) {
        return localCalculation.add(newBigDecimal(f));
    }

    public static float modelDegrees(float degree){
        return (float) ((degree * Math.PI)/180.0F);
    }

    public static int trueOrFalse() {
        return RANDOM.nextBoolean() ? 1 : -1;
    }

    public static double trueOrFalse(double d) {
        return d * trueOrFalse();
    }

    public static int toInt() {
        return toInt(RANDOM.nextBoolean());
    }

    public static int toInt(boolean b) {
        return b ? 1 : 0;
    }

    public static int toInteger() {
        return toInteger(RANDOM.nextBoolean());
    }

    public static int toInteger(boolean b) {
        return b ? 2 : 1;
    }

    public static float smite(float f) {
        return 1F + (f * 0.5F);
    }

    public static int toTick(int tick) {
        return tick * 20;
    }

    public static float toTick(float tick) {
        return tick * 20F;
    }

    public static double toTick(double tick) {
        return tick * 20.0;
    }

    public static float randomBetween(float min, float max) {
        return randomBetween(RandomSource.create(), min, max);
    }

    public static double randomBetween(double min, double max) {
        return randomBetween((float)min, (float)max);
    }

    public static float healthLessThan(LivingEntity entity, float percent, float max) {
        return Math.min(entity.getMaxHealth() / percent, max);
    }

    public static byte toByte(int pValue) {
        return (byte)pValue;
    }
}
