package com.ymz.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * @author: gyymz1993
 * 创建时间：2017/3/31 13:19
 **/
public class SpUtils {
    public static SharedPreferences sharedPreferences;
    public static SpUtils spUtils;

    public static SpUtils getInstance() {
        if (spUtils == null) {
            spUtils = new SpUtils();
        }
        return spUtils;
    }

    private SpUtils() {

    }

    public void init(Context context) {
        sharedPreferences = context
                .getSharedPreferences("config", 0);
    }

    /**
     * 得到配置文件
     *
     * @return
     */
    public SharedPreferences getSp() {
        return sharedPreferences;
    }

    /**
     * 保存string值到sp文件
     *
     * @param key
     * @param value
     */
    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 保存boolean键值到sp文件
     *
     * @param key
     * @param value
     */
    public void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 得到boolean值
     *
     * @param key
     * @param defValue
     */
    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 取字符串，默认“”
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }


    /**
     * @param key
     * @param object
     */
    public static void putObject(String key, Object object) {
        sharedPreferences.edit().putString(key, new Gson().toJson(object)).commit();
    }

    /**
     * @param key
     * @param clas Class
     * @return T
     */
    public static <T> T getObject(String key, Class<T> clas) {
        String configJson = sharedPreferences.getString(key, null);
        if (!TextUtils.isEmpty(configJson)) {
            return (T) new Gson().fromJson(configJson, clas);
        } else {
            return null;
        }
    }

    /**
     * @param key
     * @param value ֵ
     */
    public static void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }


    /**
     * @param key
     * @param defaultValue
     * @return ֵ
     */
    public static int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
}
