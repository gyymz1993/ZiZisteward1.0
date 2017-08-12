package com.lsjr;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;



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
