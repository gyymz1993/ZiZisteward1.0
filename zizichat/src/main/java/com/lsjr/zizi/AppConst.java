package com.lsjr.zizi;

import com.ymz.baselibrary.utils.FileUtils;

/**
 * @描述 全局常量类
 */
public class AppConst {

    //语音存放位置
    public static final String AUDIO_SAVE_DIR = FileUtils.getDir("audio");
    //public static final String AUDIO_SAVE_DIR = FileUtils.createDirs("audio");

    public static final int DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND = 59;
    //视频存放位置
    public static final String VIDEO_SAVE_DIR = FileUtils.getDir("video");
    //照片存放位置
    public static final String PHOTO_SAVE_DIR = FileUtils.getDir("photo");
    //头像保存位置
    public static final String HEADER_SAVE_DIR = FileUtils.getDir("header");
}
