package com.ymz.baselibrary.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author jingle1267@163.com
 */
public final class DateUtils {

    /**
     * 日期类型 *
     */
    public static final String yyyyMMDD = "yyyy-MM-dd";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String HHmmss = "HH:mm:ss";
    public static final String LOCALE_DATE_FORMAT = "yyyy年M月d日 HH:mm:ss";
    public static final String DB_DATA_FORMAT = "yyyy-MM-DD HH:mm:ss";
    public static final String NEWS_ITEM_DATE_FORMAT = "hh:mm M月d日 yyyy";


    public static String dateToString(Date date, String pattern)
            throws Exception {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date stringToDate(String dateStr, String pattern)
            throws Exception {
        return new SimpleDateFormat(pattern).parse(dateStr);
    }


    /**
     * 将Date类型转换为日期字符串
     *
     * @param curentTime Date对象
     * @param cacheTime 需要的日期格式
     * @return 按照需求格式的日期字符串
     */
    public static boolean betweenTime(Date curentTime, Date cacheTime) {
        long between = (curentTime.getTime() - cacheTime.getTime()) / 1000;//除以1000是为
        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
        long second1 = between % 60 / 60;
        //L_.e("相隔：" + day1 + "天" + hour1 + "小时" + minute1 + "分" + second1 + "秒");
        L_.e("between:" + minute1 + "分");
        return minute1 > 30;
    }

}