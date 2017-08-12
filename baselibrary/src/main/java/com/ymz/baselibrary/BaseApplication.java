package com.ymz.baselibrary;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.ymz.baselibrary.utils.SpUtils;

import java.io.File;
public  class BaseApplication {
    private static Looper mMainThreadLooper = null;
    private static Handler mMainThreadHandler = null;
    private static int mMainThreadId;
    private static Thread mMainThread = null;
    private static Application mApplication;
    private static BaseApplication mBaseApplication;
    private BaseApplication(){
    }

    public static BaseApplication instance(){
        if (mBaseApplication==null){
            synchronized (BaseApplication.class){
                if (mApplication==null){
                    mBaseApplication=new BaseApplication();
                }
            }
        }
        return mBaseApplication;
    }

    public  void initialize(Application application) {
        mApplication=application;
        if (mApplication!=null){
            mMainThreadLooper = mApplication.getMainLooper();
            mMainThreadHandler = new Handler();
            mMainThreadId = android.os.Process.myTid();
            mMainThread = Thread.currentThread();
            initException();
            initSputils();
            initLeakCanary();
            initImageLoader();
        }
    }

    private void initImageLoader() {
        //ImageLoader.init(mApplication);
    }

    /*检测内存泄露*/
    private void initLeakCanary(){
        //LeakCanary.install(mApplication);
    }


    private void initSputils() {
        SpUtils.getInstance().init(mApplication);
    }


    /***
     * 全局异常处理
     * */
    public void initException(){
           /* 全局异常崩溃处理 */
        ExceptionCrashHander.getInstance().init(mApplication);
        // 获取上次的崩溃信息
        File crashFile = ExceptionCrashHander.getInstance().getCrashFile();
    }

    public static Application getApplication() {
        if (mApplication==null)
            throw new NullPointerException("mApplication 为空");
            return mApplication;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public  static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

}
