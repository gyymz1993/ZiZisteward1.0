package com.lsjr.zizisteward.activity.contentprovider;

import android.content.Context;

public class Global {
    private static Context mContext;

    public static void init(Context context){
        mContext = context;
    }

    public static Context getInstance() {
        if(mContext == null)
            throw new NullPointerException("Global must be inited");
        return mContext;
    }
}
