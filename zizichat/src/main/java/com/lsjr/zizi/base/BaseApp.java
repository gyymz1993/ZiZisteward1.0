package com.lsjr.zizi.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * @创建者 CSDN_LQR
 * @描述 Application基类
 */
public class BaseApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
