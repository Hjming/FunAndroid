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
import java.util.Locale;
import java.util.TimeZone;

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
    public static String YEAR = "yyyy";
    public static String YEAR2 = "yyyy年";
    public static String DATE_FORMAT_MOUTH_DAY = "M/d";

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

    /**
     * 获取两个日期中间的所有日期
     *
     * @param startDay 起始日期
     * @param endDay   结束日期
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
     * 忽略时分秒的前提下 传入的两个date是否相等
     *
     * @param first  date1
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

    public static long TimeS2L(String time, String format) {
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
     * Long型时间变成秒
     *
     * @param time
     * @param format
     * @return
     */
    public static String TimeL2S(long time, String format) {
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
     * @param time
     * @return
     */
    public static String TimeL2S(long time) {
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

    public static String getFormatDate(Date date, String format) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * 得到当前的时间
     *
     * @return
     */
    public static String GetNowDate() {
        Time time = new Time();
        time.setToNow();
        int year = time.year;
        int month = time.month + 1;
        int day = time.monthDay;

        String date = String.format("%d-%02d-%02d", year, month, day);
        return date;
    }

    /**
     * 得到当前的年份
     *
     * @return
     */
    public static int GetNowYear() {
        Time time = new Time();
        time.setToNow();
        int year = time.year;
        return year;
    }

    /**
     * 得到当前的小时
     *
     * @return
     */
    public static int GetNowHour() {
        Time time = new Time();
        time.setToNow();
        int hour = time.hour;
        return hour;
    }

    /**
     * 得到当前的月份
     *
     * @return
     */
    public static int GetNowMonth() {
        Time time = new Time();
        time.setToNow();
        int month = time.month;
        return month;
    }

    /**
     * 得到当前的月份中的天
     *
     * @return
     */
    public static int GetNowDay() {
        Time time = new Time();
        time.setToNow();
        int monthDay = time.monthDay;
        return monthDay;
    }

    public static String GetNowTime() {
        Time time = new Time();
        time.setToNow();
        int hour = time.hour;
        int min = time.minute;

        int sec = time.second;

        String st = String.format("%d:%02d:%02d", hour, min, sec);
        return st;
    }

    public static String GetNowDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    //获取当前日期 yyyy-MM-dd
    public static String GetTodayDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        return sdf.format(c.getTime());
    }

    public static String GetNowDateTime(String s) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(s);
        return sdf.format(c.getTime());
    }

    public static int GetNowDateMon() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),
                Locale.CHINESE);
        calendar.setTime(new Date());
        // int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        // month=Integer.valueOf(getMon(month));
        // int day = calendar.get(Calendar.DAY_OF_MONTH);
        return month;
    }

    public static int GetNowDateDay() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),
                Locale.CHINESE);
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public static int GetNowDateYear() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),
                Locale.CHINESE);
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static int GetDateYear(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),
                Locale.CHINESE);
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static String nextMonthDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(calendar.getTime());
        return date;
    }

    public static String DateToStr(Date date) {
        if (date == null)
            return "";

        String str = String.format("%d-%02d-%02d", date.getYear() + 1900,
                date.getMonth() + 1, date.getDate());
        return str;
    }

    public static String getStringByFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    public static Date strToDate(String dstr, String format) {
        if (StringUtil.isNull(dstr))
            return null;
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(dstr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 最简单的方法 Date date=new Date(dstr);
        return date;
    }

    public static String getDateStr(String dstr, String format) {
        return getFormatDate(strToDate(dstr, TIME_FORMAT), format);
    }

    public static String TimeToStr(Date date) {
        if (date == null)
            return "";

        String str = String.format("%d:%02d:%02d", date.getHours(),
                date.getMinutes(), date.getSeconds());
        return str;
    }

    public static String DateTimeToStr(Date date) {
        if (date == null)
            return "";

        String str = String.format("%d-%02d-%02d %d:%02d:%02d",
                date.getYear() + 1900, date.getMonth() + 1, date.getDate(),
                date.getHours(), date.getMinutes(), date.getSeconds());
        return str;
    }

    public static String getYearMonth(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat ym = new SimpleDateFormat(YEAR_MOUTH);
        return ym.format(date);
    }

    public static String getMonthDay(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat ym = new SimpleDateFormat(MOUTH_DAY);
        return ym.format(date);
    }

    public static String getHourMinute(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat ym = new SimpleDateFormat(HOUR_MIN);
        return ym.format(date);
    }

    public static List<Date> dateToWeek(Date mdate) {
        int b = mdate.getDay();
        Date fdate;
        List<Date> list = new ArrayList();
        Long fTime = mdate.getTime();
        for (int a = 0; a < 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a, fdate);
        }
        return list;
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getWeekOfDate(int week) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

        return weekDays[week - 1];
    }

    /**
     * 给月份加前缀
     *
     * @param mon
     * @return
     */
    public static String getMon(int mon) {
        String month = "";
        if (mon < 10) {
            month = "0" + mon;
        } else {
            month = "" + mon;
        }
        return month;
    }

    /**
     * 取到int型月份
     *
     * @param str
     * @return
     */
    public static int getIMon(String str) {
        return Integer.valueOf(str.substring(5, 7));
    }

    /**
     * 取到int型天数
     *
     * @param str2
     * @return
     */
    public static int getIDay(String str2) {
        return Integer.valueOf(str2.substring(str2.length() - 2));
    }

    /**
     * 把字符串转为年月日的格式
     *
     * @param date
     * @return
     */
    public static String StrToNorDate(String date) {
        if (StringUtil.isNull(date)) {
            return "";
        }
        String[] d = date.split("-");
        if (d.length >= 3) {
            StringBuffer sb = new StringBuffer();
            sb.append(d[0]);
            sb.append("年");
            sb.append(d[1]);
            sb.append("月");
            sb.append(d[2]);
            sb.append("日");
            return sb.toString();
        }
        return date;
    }

    /**
     * 秒转换为时分秒      3670s  = 1小时1分10秒
     *
     * @param time
     * @return
     */
    public static String secToTime(int time) {
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

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    /**
     * 计算两个日期之间的时间间隔
     *
     * @param startTime
     * @param endTime
     * @return long
     */
    public static long caculateLeftTime(String startTime, String endTime, SimpleDateFormat simpleDateFormat) {
        long left = 0;
        try {
//			begin" : "2015-11-02 15:00:14"
            long start = simpleDateFormat.parse(startTime).getTime();
            long end = simpleDateFormat.parse(endTime).getTime();
            left = end - start;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return left;
    }

    /**
     * @param startTime
     * @param endTime
     * @param simpleDateFormat
     * @return 数组大小为三：分别表示：0:flag;1:month,2:day;
     */
    public static int[] caculateDayAndMonth2(String startTime, String endTime,
                                             SimpleDateFormat simpleDateFormat) {
        int[] info = new int[3];
        // yyyy-MM-dd hh:mm:ss
        int leftMonth = 0;
        int leftDay = 0;
        // 2015-05-12 09:09:15
        // 2016-06-13 12:09:15
        long start = 0;
        long end = 0;
        try {
            start = simpleDateFormat.parse(startTime).getTime();
            end = simpleDateFormat.parse(endTime).getTime();
            if (start > end) {
                info[0] = 1;
                long temp = start;
                start = end;
                end = temp;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Calendar c2 = Calendar.getInstance();
        c2.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        try {
//			Date startDate = simpleDateFormat.parse(startTime);
//			Date endDate = simpleDateFormat.parse(endTime);
//			c1.setTime(startDate);
//			c2.setTime(endDate);
            c1.setTimeInMillis(start);
            c2.setTimeInMillis(end);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // y2>y1
        int y1 = 0, y2 = 0;
        int M1 = 0, M2 = 0;
        // int dM1 = 0, dM2 = 0;
        int dM1 = 0, dM2 = 0;

        {
            y1 = c1.get(Calendar.YEAR);
            y2 = c2.get(Calendar.YEAR);
            M1 = c1.get(Calendar.MONTH) + 1;
            M2 = c2.get(Calendar.MONTH) + 1;
            dM1 = c1.get(Calendar.DAY_OF_MONTH);
            dM2 = c2.get(Calendar.DAY_OF_MONTH);
//			System.out.println("M1当前是月:" + M1);
        }
        {
            if (M2 == M1) {
                if (dM2 == dM1) {
                    leftMonth = (y2 - y1) * 12;
                    leftDay = 0;
//					caculateTime(startTime,endTime,simpleDateFormat);
                } else {
                    if (dM2 > dM1) {
                        leftDay = dM2 - dM1;
                        leftMonth = (y2 - y1) * 12 - M1 + M2;
                    } else {
                        leftMonth = (y2 - y1) * 12 - M1 + M2 - 1;
                        leftDay = cacluteMonthDay(y1, M1 - 1, 1) - dM1 + dM2;
                    }
                }
            } else {
                if (dM2 == dM1) {
                    leftDay = 0;
                    leftMonth = (y2 - y1) * 12 - M1 + M2;
                } else {
                    if (dM2 > dM1) {
                        leftDay = dM2 - dM1;
                        leftMonth = (y2 - y1) * 12 - M1 + M2;
                    } else {
                        leftMonth = (y2 - y1) * 12 - M1 + M2 - 1;
                        leftDay = cacluteMonthDay(y1, M1, 1) - dM1 + dM2;
                    }
                }
            }
        }
        info[1] = leftMonth;
        info[2] = leftDay;

//		System.out.println("相差的月数:" + leftMonth + ",相差的天数:" + leftDay);
        return info;
    }

    /**
     * 计算隔年之间的天数
     *
     * @param startYear
     * @param startMonth 开始的月份
     * @param num
     * @return
     */
    public static int cacluteMonthDay(int startYear, int startMonth, int num) {
        int sumDay = 0;
        int i = 0;
        int tempMonth = 0;
        if (startMonth <= 2) {
            boolean leapYear = startYear % 4 == 0;
            if (startMonth == 1) {
                sumDay += 31;
                tempMonth++;
                i++;
            }
            tempMonth++;
            i++;
            if (startMonth == 2 && leapYear) {
                sumDay += 29;
            } else if (startMonth == 2) {
                sumDay += 28;
            }
            startYear++;
        }
        startMonth = startMonth + tempMonth;
        for (int j = 1; j <= num - i; j++) {
            startMonth = startMonth + j - 1;
            int currentMonth = startMonth % 12 != 0 ? startMonth % 12
                    : startMonth % 12 + 1;

            int monthProper = getMonthProper(currentMonth);
            if (monthProper == 1) {
                sumDay += 31;
            } else if (monthProper == 0) {
                sumDay += 30;
            } else if (currentMonth == 2) {
                boolean sLeapYear = (startYear) % 4 == 0;
                if (sLeapYear) {
                    sumDay += 29;
                } else {
                    sumDay += 28;
                }
            }
        }

        return sumDay;
    }

    /**
     * 判断当月是31天还是30天
     *
     * @param month
     * @return
     */
    public static int getMonthProper(int month) {
        boolean flag = false;
        int bigDay[] = {1, 3, 5, 7, 8, 10, 12};
        int smallDay[] = {4, 6, 9, 11};
        for (int i = 0; i < bigDay.length; i++) {
            if (bigDay[i] == month) {
                flag = true;
                return 1;
            }
        }
        if (!flag) {
            for (int i = 0; i < smallDay.length; i++) {
                if (smallDay[i] == month) {
                    flag = true;
                    return 0;
                }
            }
        }
        return -1;
    }

    public static String getNowMonthDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MOUTH);
        String date = sdf.format(calendar.getTime());
        return date;
    }


    public static String getNextMonthDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MOUTH);
        String date = sdf.format(calendar.getTime());
        return date;
    }

    /**
     * 秒转换成 年月日
     *
     * @return
     * @author wangnana
     */
    public static String getTime(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time * 1000);
        String currenTime = formatter.format(date);
        return currenTime;
    }

    /**
     * 秒转换成 年月日
     *
     * @return
     * @author wangnana
     */
    public static String getTimeString(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        Date date = new Date(time);
        String currenTime = formatter.format(date);
        return currenTime;
    }

    /**
     * 秒转换成 年月日
     *
     * @return
     * @author wangnana
     */
    public static String getTimeString(long time, String type) {

        SimpleDateFormat formatter = new SimpleDateFormat(type);
        Date date = new Date(time);
        String currenTime = formatter.format(date);
        return currenTime;
    }

    /**
     * 校验日期是否今天
     *
     * @param selectedDate
     * @return
     */
    public static boolean isTodayDate(Calendar selectedDate) {
        Locale loc = Locale.getDefault();
        Calendar cal = Calendar.getInstance(loc);
        return cal.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                && cal.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
                && cal.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回一天的整点信息
     *
     * @param date
     */
    public static Date wholePointDate(Date date) {

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
     * @param timestamp
     * @param format
     * @return 返回指定格式的日期数据
     */
    public static String getDateByTimestamp(long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(timestamp);
        String dateTime = sdf.format(date);
        return dateTime;
    }

    /**
     * 转换时间日期格式字串为long型
     *
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将秒值转换成X日X时X分X秒
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
//        int ss = 1000;
        int mi = 60;
        int hh = mi * 60;
        int dd = hh * 24;

//        long day = time / dd;
        long hour = (time) / hh;
        long minute = (time - hour * hh) / mi;
        long second = (time - hour * hh - minute * mi);
        long milliSecond = time - hour * hh - minute * mi - second;

//        String strDay = day < 10 ? "0" + day : "" + day; // 天
        String strHour = hour < 10 ? "0" + hour : "" + hour;// 小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;// 分钟
        String strSecond = second < 10 ? "0" + second : "" + second;// 秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;// 毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;


        return strHour + ":" + strMinute + ":" + strSecond;
    }

    public static long getTodayZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//当天0点
        long zero = calendar.getTimeInMillis();
        return zero;
    }
}
