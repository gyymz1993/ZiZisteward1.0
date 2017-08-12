package com.lsjr.zizi.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.lsjr.zizi.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @创建者 CSDN_LQR
 * @描述 时间工具（需要joda-time）
 */
public class TimeUtils {

    /**
     * 得到仿微信日期格式输出
     *
     * @param msgTimeMillis
     * @return
     */
    public static String getMsgFormatTime(long msgTimeMillis) {
        DateTime nowTime = new DateTime();
//        LogUtils.sf("nowTime = " + nowTime);
        DateTime msgTime = new DateTime(msgTimeMillis);
//        LogUtils.sf("msgTime = " + msgTime);
        int days = Math.abs(Days.daysBetween(msgTime, nowTime).getDays());
//        LogUtils.sf("days = " + days);
        if (days < 1) {
            //早上、下午、晚上 1:40
            return getTime(msgTime);
        } else if (days == 1) {
            //昨天
            return "昨天 " + getTime(msgTime);
        } else if (days <= 7) {
            //星期
            switch (msgTime.getDayOfWeek()) {
                case DateTimeConstants.SUNDAY:
                    return "周日 " + getTime(msgTime);
                case DateTimeConstants.MONDAY:
                    return "周一 " + getTime(msgTime);
                case DateTimeConstants.TUESDAY:
                    return "周二 " + getTime(msgTime);
                case DateTimeConstants.WEDNESDAY:
                    return "周三 " + getTime(msgTime);
                case DateTimeConstants.THURSDAY:
                    return "周四 " + getTime(msgTime);
                case DateTimeConstants.FRIDAY:
                    return "周五 " + getTime(msgTime);
                case DateTimeConstants.SATURDAY:
                    return "周六 " + getTime(msgTime);
            }
            return "";
        } else {
            //12月22日
            return msgTime.toString("MM月dd日 " + getTime(msgTime));
        }
    }

    @NonNull
    private static String getTime(DateTime msgTime) {
        int hourOfDay = msgTime.getHourOfDay();
        String when;
        if (hourOfDay >= 18) {//18-24
            when = "晚上";
        } else if (hourOfDay >= 13) {//13-18
            when = "下午";
        } else if (hourOfDay >= 11) {//11-13
            when = "中午";
        } else if (hourOfDay >= 5) {//5-11
            when = "早上";
        } else {//0-5
            when = "凌晨";
        }
        return when + " " + msgTime.toString("hh:mm");
    }

    // ///s 代表Simple日期格式：yyyy-MM-dd
    // ///f 代表Full日期格式：yyyy-MM-dd hh:mm:ss

    public static final SimpleDateFormat ss_format = new SimpleDateFormat("MM-dd");
    public static final SimpleDateFormat s_format = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat f_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static long s_str_2_long(String dateString) {
        try {
            Date d = s_format.parse(dateString);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long f_str_2_long(String dateString) {
        try {
            Date d = f_format.parse(dateString);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String ss_long_2_str(long timestamp) {
        return ss_format.format(new Date(timestamp));
    }

    public static String s_long_2_str(long timestamp) {
        return s_format.format(new Date(timestamp));
    }

    public static String f_long_2_str(long timestamp) {
        return f_format.format(new Date(timestamp));
    }

    /**
     * 获取字符串时间的年份
     *
     * @param dateString
     *            格式为yyyy-MM-ss，或者yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static int getYear(String dateString) {
        try {
            Date d = s_format.parse(dateString);
            return d.getYear() + 1900;// 年份是基于格林威治时间，所以加上1900
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取字符串时间的月份
     *
     * @param dateString
     *            格式为yyyy-MM-ss，或者yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static int getMonth(String dateString) {
        try {
            Date d = s_format.parse(dateString);
            return d.getMonth();// 月份从0-11
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取字符串时间的天
     *
     * @param dateString
     *            格式为yyyy-MM-ss，或者yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static int getDayOfMonth(String dateString) {
        try {
            Date d = s_format.parse(dateString);
            return d.getDate();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static int getHours(String timeString) {
        SimpleDateFormat formart = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = formart.parse(timeString);
            return date.getHours();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static int getMinutes(String timeString) {
        SimpleDateFormat formart = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = formart.parse(timeString);
            return date.getMinutes();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static int getSeconds(String timeString) {
        SimpleDateFormat formart = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = formart.parse(timeString);
            return date.getSeconds();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCurrentTime() {
        return f_format.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 在当前时间上加上多少毫秒，返回这个时间
     *
     * @param mask
     * @return
     */
    public static String getCurrentTimeMask(long mask) {
        return f_format.format(new Date(System.currentTimeMillis() + mask));
    }

    // /////////////////////以上是通用的，下面为特殊需求的////////////////////////
    // /**
    // * 时间戳转换日期格式
    // *
    // * @param timestamp
    // * 单位秒
    // * @return
    // */
    // public static String getCurrentTime(long timestamp) {
    // SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // return f.format(new Date(timestamp * 1000));
    // }

    /**
     * 获取精简的日期
     *
     * @param time
     * @return
     */
    public static String getSimpleDate(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = f_format.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     *
     * @param time
     * @return
     */
    public static String getSimpleDateTime(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd HH:mm");
        Date date = null;
        try {
            date = f_format.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSimpleTime(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = f_format.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getChatSimpleDate(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd");
        Date date = null;
        try {
            date = f_format.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTimeHM(String time) {
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = f_format.parse(time);
            return formater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static SimpleDateFormat friendly_format1 = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat friendly_format2 = new SimpleDateFormat("MM-dd HH:mm");

    /**
     * 获取友好的时间显示
     *
     * @param time
     *            秒级别的时间戳
     * @return
     */
    public static String getFriendlyTimeDesc(Context context, int time) {
        String desc = "";
        Date timeDate = new Date(time * 1000L);
        Date nowDate = new Date();
        long delaySeconds = nowDate.getTime() / 1000 - time;// 相差的秒数

        if (delaySeconds < 10) {// 小于10秒，显示刚刚
            desc = context.getString(R.string.friendly_time_just_now);// 显示刚刚
        } else if (delaySeconds <= 60) {// 小于1分钟，显示如“25秒前”
            desc = delaySeconds + context.getString(R.string.friendly_time_before_seconds);
        } else if (delaySeconds < 60 * 30) {// 小于30分钟，显示如“25分钟前”
            desc = (delaySeconds / 60) + context.getString(R.string.friendly_time_before_minute);
        } else if (delaySeconds < 60 * 60 * 24) {// 小于1天之内
            if (nowDate.getDay() - timeDate.getDay() == 0) {// 同一天
                desc = friendly_format1.format(timeDate);
            } else {// 前一天
                desc = context.getString(R.string.friendly_time_yesterday) + " " + friendly_format1.format(timeDate);
            }
        } else if (delaySeconds < 60 * 60 * 24 * 2) {// 小于2天之内
            if (nowDate.getDay() - timeDate.getDay() == 1 || nowDate.getDay() - timeDate.getDay() == -6) {// 昨天
                desc = context.getString(R.string.friendly_time_yesterday) + " " + friendly_format1.format(timeDate);
            } else {// 前天
                desc = context.getString(R.string.friendly_time_before_yesterday) + " " + friendly_format1.format(timeDate);
            }
        } else if (delaySeconds < 60 * 60 * 24 * 3) {// 小于三天
            if (nowDate.getDay() - timeDate.getDay() == 2 || nowDate.getDay() - timeDate.getDay() == -5) {// 前天
                desc = context.getString(R.string.friendly_time_before_yesterday) + " " + friendly_format1.format(timeDate);
            }
            // else 超过前天
        }

        if (TextUtils.isEmpty(desc)) {
            desc = friendly_format2.format(timeDate);
        }
        return desc;
    }

    public static String sk_time_friendly_format2(long time) {
        return friendly_format2.format(new Date(time * 1000));
    }

    public static String sk_time_s_long_2_str(long time) {
        return s_long_2_str(time * 1000);
    }

    public static String sk_time_ss_long_2_str(long time) {
        return ss_long_2_str(time * 1000);
    }

    public static long sk_time_s_str_2_long(String dateString) {
        return s_str_2_long(dateString) / 1000;
    }

    public static int sk_time_current_time() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    private static SimpleDateFormat hm_formater = new SimpleDateFormat("HH:mm");

    public static String sk_time_long_to_hm_str(long time) {
        try {
            return hm_formater.format(new Date(time * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String sk_time_long_to_chat_time_str(long time) {
        String date1 = sk_time_s_long_2_str(time);
        String date2 = sk_time_s_long_2_str(System.currentTimeMillis() / 1000);
        if (date1.compareToIgnoreCase(date2) == 0) {// 是同一天
            return sk_time_long_to_hm_str(time);
        } else {
            return long_to_yMdHm_str(time * 1000);
        }
    }

    public static final SimpleDateFormat sk_format_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // 日期加小时的字符串
    public static String long_to_yMdHm_str(long time) {
        return sk_format_1.format(new Date(time));
    }

    public static long sk_time_yMdHm_str_to_long(String time) {
        try {
            return sk_format_1.parse(time).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getYear(String dateString) {
        try {
            Date d = sk_format_1.parse(dateString);
            return d.getYear() + 1900;// 年份是基于格林威治时间，所以加上1900
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getMonth(String dateString) {
        try {
            Date d = sk_format_1.parse(dateString);
            return d.getMonth();// 月份从0-11
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getDayOfMonth(String dateString) {
        try {
            Date d = sk_format_1.parse(dateString);
            return d.getDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getHours(String timeString) {
        try {
            Date date = sk_format_1.parse(timeString);
            return date.getHours();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int yMdHm_getMinutes(String timeString) {
        try {
            Date date = sk_format_1.parse(timeString);
            return date.getMinutes();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *
     * @param textView
     * @param time
     *            时间戳/1000
     * @return
     */
    public static long getSpecialBeginTime(TextView textView, long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (time > currentTime) {
            time = currentTime;
        }
        textView.setText(sk_time_s_long_2_str(time));
        return time;
    }

    /**
     *
     * @param textView
     * @param time
     *            时间戳/1000
     * @return
     */
    public static long getSpecialEndTime(TextView textView, long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (time == 0 || time > currentTime - 24 * 60 * 60) {
            textView.setText(R.string.to_this_day);
            return 0;
        }
        textView.setText(sk_time_s_long_2_str(time));
        return time;
    }

    public static int sk_time_age(long birthday) {
        int age = (new Date().getYear()) - (new Date(birthday * 1000).getYear());
        if (age < 0 || age > 100) {
            return 25;
        }
        return age;
    }
}
