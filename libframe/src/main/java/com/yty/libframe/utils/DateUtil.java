package com.yty.libframe.utils;


import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将指定的日期转换成格式"yyyy-MM-dd HH:mm:ss"
     */
    public static String getStringYearMonthDay(String dateStr) {
        if (!"".equals(dateStr)) {
            String result = null;
            SimpleDateFormat format2 = new SimpleDateFormat("MM-dd");
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format1.parse(dateStr);
                result = format1.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return "";
        }

    }


    /**
     * 将指定的日期转换成格式"yyyy-MM-dd HH:mm:ss"
     */
    public static String getStringMonthDay(String dateStr) {
        if (!"".equals(dateStr)) {
            String result = null;
            SimpleDateFormat format2 = new SimpleDateFormat("MM-dd");
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format1.parse(dateStr);
                result = format2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return "";
        }

    }

    /**
     * 是不是昨天
     *
     * @param when the when
     * @return the boolean
     */
    public static boolean isYesterday(long when) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        if (thenYear == time.year && thenMonth == time.month) {
            int i = thenMonthDay - time.monthDay;
            if (i == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取友好的时间格式
     *
     * @param time the time
     * @return the string
     */
    public static String friendlyTime(long time) {
        String hmStr = "";
        String dateStr = "";
        hmStr = new SimpleDateFormat("HH:mm").format(new Date(time));
        dateStr = new SimpleDateFormat("yy-MM-dd HH:mm").format(new Date(time));
        if (android.text.format.DateUtils.isToday(time)) {
            return "今天 " + hmStr;
        } else if (isYesterday(time)) {
            return "昨天 " + hmStr;
        } else {
            return dateStr;
        }
    }

    public static String time2Date(long time) {
        String hmStr = new SimpleDateFormat("MM-dd HH:mm").format(new Date(time));
        if (hmStr == null) {
            return "";
        }
        return hmStr;
    }

    /**
     * 字符串转时间戳
     *
     * @param date
     * @return
     */
    public static long str2times(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newdate = null;
        try {
            newdate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = newdate.getTime();
        return time;
    }

    /**
     * 是不是明天
     *
     * @param day
     * @return
     */
    public static boolean isTomorrow(String day) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = formatter.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 1) {
                return true;
            }
        }
        return false;
    }

    public static String diffCurrentTime(String time) {
        long newtime = str2times(time);
        long currentTimeMillis = System.currentTimeMillis();
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {
        //获得两个时间的毫秒时间差异
        long diff = newtime - currentTimeMillis;

        if (diff < 0) {
            return "";
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            long day = diff / nd;//计算差多少天
            if (day > 0) {
                stringBuffer.append(day + ",天");
            } else {
                long hour = diff % nd / nh;//计算差多少小时
                if (hour > 0) {
                    stringBuffer.append(hour + ",小时");
                } else {
                    long min = diff % nd % nh / nm;//计算差多少分钟
                    if (min > 0) {
                        stringBuffer.append(min + ",分钟");
                    } else {
//                        long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
//                        if(min > 0){
//                            stringBuffer.append(sec+"秒");
//                        }
                    }
                }
            }
            return stringBuffer.toString();
        }
    }
}
