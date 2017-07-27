package com.lsjr.zizisteward.contrl;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;


import com.ymz.baselibrary.BaseApplication;
import com.ymz.baselibrary.utils.SpUtils;

import java.util.Set;

import static com.lsjr.zizisteward.contrl.ConstantUtil.CONTENT_URI;
import static com.lsjr.zizisteward.contrl.ConstantUtil.NULL_STRING;
import static com.lsjr.zizisteward.contrl.ConstantUtil.SEPARATOR;
import static com.lsjr.zizisteward.contrl.ConstantUtil.TYPE_BOOLEAN;
import static com.lsjr.zizisteward.contrl.ConstantUtil.TYPE_CLEAN;
import static com.lsjr.zizisteward.contrl.ConstantUtil.TYPE_CONTAIN;
import static com.lsjr.zizisteward.contrl.ConstantUtil.TYPE_FLOAT;
import static com.lsjr.zizisteward.contrl.ConstantUtil.TYPE_INT;
import static com.lsjr.zizisteward.contrl.ConstantUtil.TYPE_LONG;
import static com.lsjr.zizisteward.contrl.ConstantUtil.TYPE_STRING;
import static com.lsjr.zizisteward.contrl.ConstantUtil.VALUE;


public class SPHelper {

    private static Context mContext = BaseApplication.getApplication();



    public synchronized static void save(String name, Boolean t) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_BOOLEAN + SEPARATOR + name);
            ContentValues cv = new ContentValues();
            cv.put(VALUE, t);
            cr.update(uri, cv, null, null);
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(name, t);
            editor.commit();
        }
    }

    public synchronized static void save(String name, String t) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_STRING + SEPARATOR + name);
            ContentValues cv = new ContentValues();
            cv.put(VALUE, t);
            cr.update(uri, cv, null, null);
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(name, t);
            editor.commit();
        }
    }

    public synchronized static void save(String name, Integer t) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_INT + SEPARATOR + name);
            ContentValues cv = new ContentValues();
            cv.put(VALUE, t);
            cr.update(uri, cv, null, null);
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(name, t);
            editor.commit();
        }
    }

    public synchronized static void save(String name, Long t) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_LONG + SEPARATOR + name);
            ContentValues cv = new ContentValues();
            cv.put(VALUE, t);
            cr.update(uri, cv, null, null);
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(name, t);
            editor.commit();
        }
    }

    public synchronized static void save(String name, Float t) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_BOOLEAN + SEPARATOR + name);
            ContentValues cv = new ContentValues();
            cv.put(VALUE, t);
            cr.update(uri, cv, null, null);
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat(name, t);
            editor.commit();
        }
    }

    public static String getString(String name, String defaultValue) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_STRING + SEPARATOR + name);
            String rtn = cr.getType(uri);
            if (rtn == null || rtn.equals(NULL_STRING)) {
                return defaultValue;
            }
            return rtn;
        } else {
            return sp.getString(name, defaultValue);
        }
    }

    public static int getInt(String name, int defaultValue) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_INT + SEPARATOR + name);
            String rtn = cr.getType(uri);
            if (rtn == null || rtn.equals(NULL_STRING)) {
                return defaultValue;
            }
            return Integer.parseInt(rtn);
        } else {
            return sp.getInt(name, defaultValue);
        }
    }

    public static float getFloat(String name, float defaultValue) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_FLOAT + SEPARATOR + name);
            String rtn = cr.getType(uri);
            if (rtn == null || rtn.equals(NULL_STRING)) {
                return defaultValue;
            }
            return Float.parseFloat(rtn);
        } else {
            return sp.getFloat(name, defaultValue);
        }
    }

    public static boolean getBoolean(String name, boolean defaultValue) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_BOOLEAN + SEPARATOR + name);
            String rtn = cr.getType(uri);
            if (rtn == null || rtn.equals(NULL_STRING)) {
                return defaultValue;
            }
            return Boolean.parseBoolean(rtn);
        } else {
            return sp.getBoolean(name, defaultValue);
        }
    }

    public static long getLong(String name, long defaultValue) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_LONG + SEPARATOR + name);
            String rtn = cr.getType(uri);
            if (rtn == null || rtn.equals(NULL_STRING)) {
                return defaultValue;
            }
            return Long.parseLong(rtn);
        } else {
            return sp.getLong(name, defaultValue);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getStringSet(String name, Set<String> defaultValue) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        return sp.getStringSet(name, defaultValue);
    }

    public static boolean contains(String name) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_CONTAIN + SEPARATOR + name);
            String rtn = cr.getType(uri);
            if (rtn == null || rtn.equals(NULL_STRING)) {
                return false;
            } else {
                return Boolean.parseBoolean(rtn);
            }
        } else {
            return sp.contains(name);
        }
    }

    public static void remove(String name) {
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_LONG + SEPARATOR + name);
            cr.delete(uri, null, null);
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(name);
            editor.commit();
        }
    }

    public static void clear(){
        SharedPreferences sp = SpUtils.getInstance().getSp();
        if (sp == null) {
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_CLEAN);
            cr.getType(uri);
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
        }
    }
}