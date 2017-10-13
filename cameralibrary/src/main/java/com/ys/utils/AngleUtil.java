//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys.utils;

public class AngleUtil {
    public AngleUtil() {
    }

    public static int getSensorAngle(float x, float y) {
        return Math.abs(x) > Math.abs(y)?(x > 7.0F?270:(x < -7.0F?90:0)):(y > 7.0F?0:(y < -7.0F?180:0));
    }
}
