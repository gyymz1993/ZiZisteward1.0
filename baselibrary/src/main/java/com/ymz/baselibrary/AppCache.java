package com.ymz.baselibrary;

import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.ymz.baselibrary.utils.FileUtils;

import java.io.File;
import java.util.IllegalFormatCodePointException;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/12 15:05
 */

public class AppCache {


    private static AppCache appCache;

    private AppCache() {

    }
    public static AppCache getInstance() {
        if (appCache == null) {
            synchronized (AppCache.class) {
                if (appCache == null) {
                    appCache = new AppCache();
                }
            }
        }
        return appCache;
    }


    //照片存放位置

    //语音存放位置

    //视频存放位置

    //照片存放位置

    //头像保存位置

    //Glide

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public boolean initCreataAppCache() {
        return true;
    }



    public File getmAppDir() {
        return new File(mAppDir);
    }

    public File getmPicturesDir() {
        return new File(mPicturesDir);
    }

    public File getmVoicesDir() {
        return new File(mVoicesDir);
    }


    public boolean voiceExists(String filePath,String fileName){
        return new File(mVoicesDir+File.separator+fileName).exists();
    }

    public String voicePath(String fileName){
        return new File(mVoicesDir+File.separator+fileName).getAbsolutePath();
    }


    public boolean videoExists(String filePath,String fileName){
        return new File(mVideosDir+File.separator+fileName).exists();
    }

    public String videoPath(String fileName){
        return new File(mVideosDir+File.separator+fileName).getAbsolutePath();
    }




    public File getmVideosDir() {
        return new File(mVideosDir);
    }

    public String fileNameGenerator(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        int lastIndex = url.lastIndexOf("/");
        if (lastIndex == -1) {
            return url;
        }
        return url.substring(lastIndex, url.length());
    }

    public File getmFilesDir() {
        return new File(mFilesDir);
    }

    /* 文件缓存的目录 */
    public String mAppDir;
    public String mPicturesDir;
    public String mVoicesDir;
    public String mVideosDir;
    public String mFilesDir;
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public void initCreateAppDir() {
        File file = BaseApplication.getApplication().getExternalFilesDir(null);
        assert file != null;
        if (!file.exists()) {
        }
        mAppDir = file.getAbsolutePath();

        file =  BaseApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!file.exists()) {
            file.mkdirs();
        }
        mPicturesDir = file.getAbsolutePath();

        file =  BaseApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        assert file != null;
        if (!file.exists()) {
            file.mkdirs();
        }
        mVoicesDir = file.getAbsolutePath();

        file =  BaseApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        assert file != null;
        if (!file.exists()) {
            file.mkdirs();
        }
        mVideosDir = file.getAbsolutePath();

        file =  BaseApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (!file.exists()) {
            file.mkdirs();
        }
        mFilesDir = file.getAbsolutePath();
    }
}
