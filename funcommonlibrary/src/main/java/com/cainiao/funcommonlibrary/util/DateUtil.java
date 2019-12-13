package com.cainiao.funcommonlibrary.util;

import android.text.format.Time;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtil {
    public static String DATE_FORMAT_OG = "yyyyMMdd";
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String TIME_FORMAT_SPRIT = "yyyy/MM/dd HH:mm:ss";
    public static String YEAR_MOUTH = "yyyy年MM月";
    public static String YEAR_MOUTH_DAY = "yyyy年MM月dd日 HH:mm";
    public static String MOUTH_DAY_HH_MM = "MM月dd日 HH:mm";
    public static String MOUTH_DAY = "MM月dd日";
    public static String DAY = "dd日";
    public static String HOUR_MIN = "HH:mm";
    public static String DATE_FORMAT_DOT = "yyyy.MM.dd";
    public static String DATE_FORMAT_SPRIT = "yyyy/MM/dd";

    /**
     * time字符串 按照指定format格式 转换成long型的时间戳 ，出现转换异常返回0L
     *
     * @param time   time字符串
     * @param format 指定format eg yyyy-MM-dd HH:mm:ss
     * @return Long型时间戳
     */
    public static long string2Long(String time, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.v("util", "TimeStrToLong():" + e);
        }
        return 0l;
    }

    /**
     * 将Date日期 按照指定的格式转换成string字符串
     *
     * @param date   Date日期
     * @param format format格式 eg yyyy-MM-dd HH:mm:ss
     * @return 字符串时间
     */
    public static String date2Str(Date date, String format) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 将时间字符串 按照指定的格式转成 Date对象
     *
     * @param time   time字符串
     * @param format 指定format eg yyyy-MM-dd HH:mm:ss
     * @return Date型时间对象
     */
    public static Date str2Date(String time, String format) {
        if (StringUtil.isNull(time))
            return null;
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 最简单的方法 Date date=new Date(dstr);
        return date;
    }

    /**
     * Long型时间戳 按照指定format格式转换成对应的时间字符串
     *
     * @param time   Long型时间戳
     * @param format format格式 eg yyyy-MM-dd HH:mm:ss
     * @return format对应的时间字符串
     */
    public static String long2String(long time, String format) {
        if (time > 0l) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = new Date(time);
            return sdf.format(date);
        }
        return "";
    }

    /**
     * Long型时间变成字符串时间
     * 今天 15:00、昨天 15:00,6月10日 15:00, 2015年6月20日 15:00
     *
     * @param time Long型时间戳
     * @return 字符串时间
     */
    public static String long2String(long time) {
        if (time > 0l) {
            SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MOUTH_DAY);
            SimpleDateFormat sdfMD = new SimpleDateFormat(MOUTH_DAY_HH_MM);
            SimpleDateFormat sdfHM = new SimpleDateFormat(HOUR_MIN);
            Date date = new Date(time);
            Date nowDate = new Date();
            String dateStr = "";
            long divDay = DateUtil.getDaysBetween(date, nowDate);
            if (0 == divDay) {
                //今天
                dateStr = "今天 " + sdfHM.format(date);
            } else if (1 == divDay) {
                //昨天
                dateStr = "昨天 " + sdfHM.format(date);
            } else if (date.getYear() == nowDate.getYear()) {
                //同一年
                dateStr = sdfMD.format(date);
            } else {
                dateStr = sdf.format(date);
            }
            return dateStr;
        }
        return "";
    }


    /**
     * 将format 格式的时间字符串  转换成 format2格式的时间串 eg yyyyMMdd change to  yyyy-MM-dd
     *
     * @param dateStr 时间字符串
     * @param format  时间format
     * @param format2 目标format2
     * @return 时间字符串
     */
    public static String changeFormatDate(String dateStr, String format, String format2) {
        String str2 = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
        try {
            Date date = sdf.parse(dateStr);
            str2 = sdf2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str2;
    }

    /**
     * 将format 格式的时间字符串  转换成 format2格式的时间串 eg yyyyMMdd change to  yyyy-MM-dd
     *
     * @param dateStr 时间字符串
     * @param format  时间format
     * @param format2 目标format2
     * @return 时间Date
     */
    public static Date changeFormatToDate(String dateStr, String format, String format2) {
        String str2 = "";
        Date toDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
        try {
            Date date = sdf.parse(dateStr);
            str2 = sdf2.format(date);
            toDate = sdf2.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return toDate;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param startDate 时间毫秒数1
     * @param endDate   时间毫秒数2
     * @return 时间毫秒数1和时间毫秒数2 间隔 单位日  结果做除以1000 * 3600 * 24处理
     */
    public static int differentDaysByMillisecond(String startDate, String endDate) {
        int days = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateS = format.parse(startDate);
            Date dateE = format.parse(endDate);
            days = (int) ((dateE.getTime() - dateS.getTime()) / (1000 * 3600 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 获取某个时间字符串的第一天
     *
     * @param dateStr 时间字符串
     * @return 时间字符串对应的月份的第一天
     */
    public static Date getMonthFirstDay(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = format.parse(dateStr);
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
            cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.getTime();
    }

    /**
     * 获取某个月的下个月的第一天
     *
     * @param dateStr 时间字符串
     * @return 时间字符串对应的月的下一个月的第一天
     */
    public static Date getNextMonthFirstDay(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = format.parse(dateStr);
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
            cal.add(Calendar.MONTH, 1);
            cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.getTime();
    }


    /**
     * 得到当前的年份
     *
     * @return 当前的年份
     */
    public static int getNowYear() {
        Time time = new Time();
        time.setToNow();
        int year = time.year;
        return year;
    }

    /**
     * 得到当前的小时
     *
     * @return 当前的小时
     */
    public static int getNowHour() {
        Time time = new Time();
        time.setToNow();
        int hour = time.hour;
        return hour;
    }

    /**
     * 得到当前的月份
     *
     * @return 当前的月份
     */
    public static int getNowMonth() {
        Time time = new Time();
        time.setToNow();
        int month = time.month;
        return month;
    }

    /**
     * 得到当前的月份中的天数
     *
     * @return 当前的月份中的天数
     */
    public static int getNowDay() {
        Time time = new Time();
        time.setToNow();
        int monthDay = time.monthDay;
        return monthDay;
    }

    /**
     * 得到当前的时间 yyyy-MM-dd 型
     *
     * @return 当前的时间
     */
    public static String getNowDate() {
        Time time = new Time();
        time.setToNow();
        int year = time.year;
        int month = time.month + 1;
        int day = time.monthDay;
        String date = String.format("%d-%02d-%02d", year, month, day);
        return date;
    }

    /**
     * 得到当前的时间 yyyy:MM:dd 型
     *
     * @return 当前的时间
     */
    public static String getNowTime() {
        Time time = new Time();
        time.setToNow();
        int hour = time.hour;
        int min = time.minute;
        int sec = time.second;
        String st = String.format("%d:%02d:%02d", hour, min, sec);
        return st;
    }

    /**
     * 得到当前的时间  默认 yyyy-MM-dd HH:mm:ss 型
     *
     * @return 当前的时间
     */
    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    /**
     * 得到指定样式的 当前的时间
     *
     * @param format 时间format格式
     * @return 当前的时间
     */
    public static String getCurrentTime(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(c.getTime());
    }


    /**
     * 获取两个时间字符串对应的时间间隔长度
     *
     * @param begin 起始时间字符串 默认转换long型format 为yyyy-MM-dd HH:mm:ss
     * @param end   结束时间字符串 默认转换long型format 为yyyy-MM-dd HH:mm:ss
     * @return end - begin之间间隔的long型 数值
     */
    public static long getTimeLength(String begin, String end) {
        long time1 = string2Long(begin, TIME_FORMAT);
        long time2 = string2Long(end, TIME_FORMAT);
        return time2 - time1;
    }

    /**
     * 获取两个时间字符串对应的时间间隔长度
     *
     * @param begin  起始时间字符串 以format格式进行转换long型时间戳
     * @param end    结束时间字符串 以format格式进行转换long型时间戳
     * @param format format格式
     * @return end - begin之间间隔的long型 数值
     */
    public static long getTimeLength(String begin, String end, String format) {
        long time1 = string2Long(begin, format);
        long time2 = string2Long(end, format);
        return time2 - time1;
    }


    /**
     * 得到几天前的时间
     *
     * @param date Date对象
     * @param day  要推前的天数
     * @return 几天前的时间
     */
    public static Date getDateBefore(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param date Date对象
     * @param day  要推后的天数
     * @return 几天后的时间
     */
    public static Date getDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }


    /**
     * 两个日期间隔天数
     *
     * @param startDate 起始日期
     * @param endDate   结束日志
     * @return endDate - startDate之间的天数
     */
    public static long getDaysBetween(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (toCalendar.getTime().getTime() - fromCalendar.getTime()
                .getTime()) / (1000 * 60 * 60 * 24);
    }


    /**
     * 秒转换为时分秒      3670s  = 1小时1分10秒
     *
     * @param time 秒数
     * @return 时分秒字符串
     */
    public static String sec2HourMinSec(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00小时00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            } else if (time <= 3660) {
                if (minute > 60) {
                    second = time - 3600;
                    minute = 60;
                } else {
                    second = time % 60;
                    timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
                }
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99小时59分59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分";
            }
        }
        return timeStr;
    }


    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }




    /**
     * 获取两个日期中间的所有日期
     *
     * @param startDay  起始日期
     * @param endDay  结束日期
     * @return 结束日期-起始日期直接的所有日期
     */
    public static List<Date> getDateFromStartAndEnd(Calendar startDay, Calendar endDay) {

        List<Date> datas = new ArrayList<>();

        // 给出的日期开始日比终了日大则不执行打印
        if (startDay.compareTo(endDay) >= 0) {
            return datas;
        }
        // 现在打印中的日期
        Calendar currentPrintDay = startDay;
        while (true) {
            // 日期加一
            currentPrintDay.add(Calendar.DATE, 1);
            // 日期加一后判断是否达到终了日，达到则终止打印
            if (currentPrintDay.compareTo(endDay) == 0) {
                break;
            }
            datas.add(currentPrintDay.getTime());

        }
        return datas;
    }



    /**
     * 是否小于今天
     *
     * @param date Date对象
     * @return 小于返回true
     */
    public static boolean isLessThanToday(Date date) {

        if (date == null) {
            return false;
        }

        Date nowDate = new Date();
        nowDate = wholePointDate(nowDate);
        date = wholePointDate(date);

        return nowDate.compareTo(date) == 1;

    }


    /**
     * 返回一天的整点信息
     *
     * @param date
     */
    private static Date wholePointDate(Date date) {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        if ((gc.get(Calendar.HOUR_OF_DAY) == 0) && (gc.get(Calendar.MINUTE) == 0)
                && (gc.get(Calendar.SECOND) == 0)) {
            return new Date(date.getTime());
        } else {
            Date date2 = new Date(date.getTime() - gc.get(Calendar.HOUR_OF_DAY) * 60 * 60
                    * 1000 - gc.get(Calendar.MINUTE) * 60 * 1000 - gc.get(Calendar.SECOND)
                    * 1000 - 24 * 60 * 60 * 1000);
            return date2;
        }

    }

    /**
     * 忽略时分秒的前提下 传入的两个date是否相等
     * @param first date1
     * @param second date2
     * @return 相等 返回true 否则 返回false
     */
    public static boolean equalsIngoreHMS(Date first, Date second) {
        Calendar firstC = GregorianCalendar.getInstance();
        firstC.setTime(first);
        firstC.set(Calendar.HOUR_OF_DAY, 0);
        firstC.set(Calendar.MINUTE, 0);
        firstC.set(Calendar.SECOND, 0);
        firstC.set(Calendar.MILLISECOND, 0);

        Calendar secondC = GregorianCalendar.getInstance();
        secondC.setTime(second);
        secondC.set(Calendar.HOUR_OF_DAY, 0);
        secondC.set(Calendar.MINUTE, 0);
        secondC.set(Calendar.SECOND, 0);
        secondC.set(Calendar.MILLISECOND, 0);

        return firstC.equals(secondC);

    }
}
