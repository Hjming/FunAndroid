package com.cainiao.funcommonlibrary.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.cainiao.funcommonlibrary.Constant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogUtil {
    private static Boolean LOG_SWITCH = true; /*BuildConfig.LOG_DEBUG*/ // 日志文件总开关
    private static Boolean LOG_WRITE_TO_FILE = false;        // 日志写入文件开关
    private static char LOG_TYPE = 'v';                        // 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
    private static String LOG_PATH_SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    // public static final String LOG_DIR = Constant.sContext.getCacheDir() + "/WebLog/";
    private static String mLogName = DateUtil.getCurrentTime() + ".txt";
    // 日志文件在sdcard中的路径
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 1;        // sd卡中日志文件的最多保存天数
    private static String LOG_FILE_NAME = "FunAndroid";    // 本类输出的日志文件名称
    private static String LOG_FILE_SUFFIX = ".log";            // 本类输出的日志文件后缀
    public static final String TAG = "LogUtil ";            // 本对象表示输出的Log名称
    private static SimpleDateFormat LogSdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");                                                    // 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyyMMdd");        // 日志文件格式

    public static String customTagPrefix = "";

    /**
     * w级别日志打印
     *
     * @param tag 日志tag
     * @param msg 要打印的日志信息
     */
    public static void w(String tag, Object msg) { // 警告信息
        log(tag, msg.toString(), 'w');
    }

    /**
     * e级别日志打印
     *
     * @param tag 日志tag
     * @param msg 要打印的日志信息
     */
    public static void e(String tag, Object msg) { // 错误信息
        log(tag, msg.toString(), 'e');
    }

    /**
     * d级别日志打印
     *
     * @param tag 日志tag
     * @param msg 要打印的日志信息
     */
    public static void d(String tag, Object msg) {// 调试信息
        log(tag, msg.toString(), 'd');
    }

    /**
     * i级别日志打印
     *
     * @param tag 日志tag
     * @param msg 要打印的日志信息
     */
    public static void i(String tag, Object msg) {//
        log(tag, msg.toString(), 'i');
    }

    /**
     * v级别日志打印
     *
     * @param tag 日志tag
     * @param msg 要打印的日志信息
     */
    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    /**
     * w级别日志打印
     *
     * @param tag  日志tag
     * @param text 要打印的日志信息String
     */
    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    /**
     * e级别日志打印
     *
     * @param tag  日志tag
     * @param text 要打印的日志信息String
     */
    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    /**
     * d级别日志打印
     *
     * @param tag  日志tag
     * @param text 要打印的日志信息String
     */
    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    /**
     * i级别日志打印
     *
     * @param tag  日志tag
     * @param text 要打印的日志信息String
     */
    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    /**
     * v级别日志打印
     *
     * @param tag  日志tag
     * @param text 要打印的日志信息String
     */
    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }


    private static void log(String tag, String msg, char level) {
        if (StringUtil.isNull(msg)) {
            return;
        }
        if (LOG_SWITCH) {
            if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { // 输出错误信息
                Log.e(tag, TAG + msg);
            } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.w(tag, TAG + msg);
            } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.d(tag, TAG + msg);
            } else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.i(tag, TAG + msg);
            } else {
                Log.v(tag, TAG + msg);
            }
            if (LOG_WRITE_TO_FILE) {
                delFile();
                writeLogtoFile(String.valueOf(level), tag, msg);
            }
        }
    }


    /**
     * 打开日志文件并写入日志
     *
     * @param logtype 日志类型
     * @param tag     tag
     * @param text    写入日志文件内容
     */
    private static void writeLogtoFile(String logtype, String tag, String text) {// 新建或打开日志文件
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = LogSdf.format(nowtime) + "    " + logtype
                + "    " + tag + "    " + text;
        File file = new File(LOG_PATH_SDCARD_DIR, LOG_FILE_NAME + needWriteFiel + LOG_FILE_SUFFIX);
        try {
            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 删除指定的日志文件
     */
    public static void delFile() {
        String needDelFiel = logfile.format(getDateBefore());
        File file = new File(LOG_PATH_SDCARD_DIR, LOG_FILE_NAME + needDelFiel + LOG_FILE_SUFFIX);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE)
                - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }

    /**
     * log数据量很大时,可使用该方法打印log,以显示完整数据(如网络返回数据量过大时)
     *
     * @param tag tag TAG
     * @param msg log输出内容
     */
    public static void logBigData(String tag, String msg) {

        if (!LOG_SWITCH) {
            return;
        }
        int LOG_MAXLENGTH = 2000;
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            if (strLength > end) {
                Log.e(tag + "__FunAndroid__" + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(tag + "__FunAndroid__" + i, msg.substring(start, strLength));
                break;
            }
        }
    }

    /**
     * 打印网络日志
     *
     * @param json JSONObject对象
     */
    public static void writeWebLog(final JSONObject json) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    printWebLog(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private static void printWebLog(JSONObject json) throws Exception {
//        json.put("time", DateUtil.getCurrentTime());
//        d(TAG + "printObj", json.toJSONString());
//        StringBuffer sb = new StringBuffer();
//        sb.append(json.toJSONString());
//        StringWriter mStringWriter = new StringWriter();
//        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
//        mPrintWriter.close();
//        sb.append(mStringWriter.toString());
//        File dir = new File(LOG_DIR);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        mLogName = DateUtil.getCurrentTime() + ".txt";
//        File file = new File(dir, mLogName);
//        if (file.exists()) sb.insert(0, "\n\n-------------网络日志---------------\n\n");
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
//            fileOutputStream.write(sb.toString().getBytes());
//            fileOutputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 格式: customTagPrefix:className.methodName(L:lineNumber),
     * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
     *
     * @param caller
     * @return
     */
    public static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }
}
