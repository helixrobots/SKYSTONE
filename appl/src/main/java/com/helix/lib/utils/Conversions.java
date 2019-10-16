package com.helix.lib.utils;

public class Conversions {

    public static double degreesToRadians(double x) {
        return x * 2 * Math.PI / 360;
    }

    public static double radiansToDegrees(double x) {
        return x * 360 / (2 * Math.PI);
    }
}
