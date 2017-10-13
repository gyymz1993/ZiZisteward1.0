//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys.utils;

import android.os.Build;
import android.os.Build.VERSION;

public class DeviceUtil {
    private static String[] huaweiRongyao = new String[]{"hwH60", "hwPE", "hwH30", "hwHol", "hwG750", "hw7D", "hwChe2"};

    public DeviceUtil() {
    }

    public static String getDeviceInfo() {
        String handSetInfo = "手机型号：" + Build.DEVICE + "\n系统版本：" + VERSION.RELEASE + "\nSDK版本：" + VERSION.SDK_INT;
        return handSetInfo;
    }

    public static String getDeviceModel() {
        return Build.DEVICE;
    }

    public static boolean isHuaWeiRongyao() {
        int length = huaweiRongyao.length;

        for(int i = 0; i < length; ++i) {
            if(huaweiRongyao[i].equals(getDeviceModel())) {
                return true;
            }
        }

        return false;
    }
}
