package com.qymage.sys.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static SimpleDateFormat sdf;

    private static SimpleDateFormat getSimpleDateFormat(String format) {
        sdf = new SimpleDateFormat(format);
        return sdf;
    }

    public static CharSequence formatMills(long duration, String format) {
        getSimpleDateFormat(format);
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt2 = new Date(duration);
        String sDateTime = sdf.format(dt2); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    public static CharSequence formatMills1000(long duration, String format) {
        getSimpleDateFormat(format);
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt2 = new Date(duration * 1000);
        String sDateTime = sdf.format(dt2); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    public static CharSequence formatMills1000(long duration) {
        getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt2 = new Date(duration * 1000);
        String sDateTime = sdf.format(dt2); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    public static CharSequence formatMillsTo(long duration) {
        getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt2 = new Date(duration);
        String sDateTime = sdf.format(dt2); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }

    public static String formatNYR(long duration) {
        getSimpleDateFormat("yyyy-MM-dd");
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt2 = new Date(duration);
        String sDateTime = sdf.format(dt2); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }


    public static CharSequence formatMills(long duration) {
        return formatMills(duration, "yyyy-MM-dd HH:mm:ss");
    }

    public static CharSequence formatDate(Date date) {
        getSimpleDateFormat("yyyyMMddHHmmss");
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        String sDateTime = sdf.format(date); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;
    }


    public static String DateToWeek(long time) {
        String[] WEEK = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int WEEKDAYS = 7;
        Date date = new Date(time * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }
        return WEEK[dayIndex - 1];
    }
}
