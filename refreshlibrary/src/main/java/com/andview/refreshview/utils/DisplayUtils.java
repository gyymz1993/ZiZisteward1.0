package com.andview.refreshview.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by wxy on 2017/6/14.
 */

public class DisplayUtils {
    public static int getDisplayWidth(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }

    public static int px2dip(Context context,float var1) {
        float var2 = context.getResources().getDisplayMetrics().density;
        return (int) (var1 / var2 + 0.5F);
    }


}
