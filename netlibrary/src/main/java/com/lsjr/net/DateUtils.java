package com.lsjr.net;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.shove.Convert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
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
     * @param cacheTime  需要的日期格式
     * @return 按照需求格式的日期字符串
     */
    private static boolean betweenTime(Date curentTime, Date cacheTime) {
        //当前时间 :: Mon Jul 31 14:29:55 GMT+08:00 2017
        // 07-31 14:29:55.667 29897-29897/com.lsjr.zizisteward2 E/data 缓存时间:: Mon Jul 31 11:13:01 GMT+08:00 2017
        // 07-31 14:29:55.668 29897-29897/com.lsjr.zizisteward2 E/DateUtils:: between:16分
        long between = (curentTime.getTime() - cacheTime.getTime()) / 1000;//除以1000是为
        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
        long second1 = between % 60 / 60;
        //L_.e("相隔：" + day1 + "天" + hour1 + "小时" + minute1 + "分" + second1 + "秒");
        Log.e("DateUtils:", "between:" + minute1 + "分");
        return minute1 > 30;
    }


    /**
     * 判断是否是今天
     *
     * @param time 时间
     * @return 当前日期转换为更容易理解的方式
     */
    @SuppressLint("WrongConstant")
    private static boolean translateDateToday(Long time) {
        long oneDay = 24 * 60 * 60 * 1000;
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayStartTime = today.getTimeInMillis();
        if (time >= todayStartTime && time < todayStartTime + oneDay) { // today
            // return "今天";
            return false;
        } else {
            return true;
        }

    }


    /**
     * 计算两个日期型的时间相差多少时间
     *
     * @param startDate   上一次时间
     * @param currentTime 当前时间
     * @return
     */
    public static boolean twoDateDistance(Date currentTime, Date startDate) {
        //currentTime=new Date("Mon Jul 31 14:29:55 GMT+08:00 2017");
        //startDate=new Date("Mon Jul 32 11:13:01 GMT+08:00 2017");
        if (startDate == null || currentTime == null) {
            return false;
        }
        if (translateDateToday(startDate.getTime())) {
            //Log.e("twoDateDistance：","是否是今天"+(translateDateToday(startDate.getTime())?"是":"否"));
            return true;
        }
        long timeLong = currentTime.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000) {
            // timeLong / 1000 + "秒前";
            return false;
        } else {
            int time = (int) (timeLong / 1000 / 60);
            if (time > 30) {
                // Log.e("twoDateDistance :", time + "分钟前");
                return true;
            } else {
                // Log.e("twoDateDistance :", time + "分钟前");
                return false;
            }
            // return timeLong + "分钟前";
        }
    }

}