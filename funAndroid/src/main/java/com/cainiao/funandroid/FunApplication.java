package com.cainiao.funandroid;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.baidu.location.BDLocation;
import com.cainiao.funcommonlibrary.Constant;
import com.cainiao.funcommonlibrary.util.DateUtil;
import com.cainiao.funcommonlibrary.util.ThreadPoolUtils;
import com.cainiao.funcommonlibrary.util.Utilities;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class FunApplication extends MultiDexApplication implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "FunApplication";
    public static FunApplication instance = null;
    private static Stack<Activity> activityStack;   // Activity 回退栈管理

    private static HashMap<String, Object> dataMap; // 用来传递一些 Intent 没办法传递的数据

    private BDLocation location;    // 百度地图定位 location

    private List<Activity> mActivityList;    //一键关闭多个activity

    public static long COLD_LAUNCH_START = Long.MAX_VALUE;


    public FunApplication() {
        long l = System.currentTimeMillis();
        if (l < COLD_LAUNCH_START) {
            COLD_LAUNCH_START = l;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Thread.setDefaultUncaughtExceptionHandler(this);
        if (!isMainProcess()) {
            return;
        }
    }

    private boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = this.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        if (Constant.ERROR_DEBUG) {
            //崩溃写入日志      系统根目录\kaka\crash.txt
            ThreadPoolUtils.getInstance().executeIO(new Runnable() {

                @Override
                public void run() {
                    writeErrorLog(ex);

                }
            });
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(2);
    }

    /**
     * 打印错误日志
     *
     * @param ex
     */
    protected void writeErrorLog(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        HashMap<String, String> systemInfo = obtainSystemInfo(getApplicationContext());
        Set<Map.Entry<String, String>> entrySet = systemInfo.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        sb.append("\n\n");

        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        ex.printStackTrace(mPrintWriter);
        mPrintWriter.close();
        sb.append(mStringWriter.toString());
        Log.e(TAG, mStringWriter.toString());
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/funandroid/ErrorLog/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, DateUtil.GetTodayDate() + "funandroid.txt");
        if (file.exists()) sb.insert(0, "\n\n--------------------------------\n\n");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(sb.toString().getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        int pid = android.os.Process.myPid();
        String processAppName = Utilities.getAppName(this, pid);
        if (processAppName != null && processAppName.equalsIgnoreCase("com.ziroom.ziroomcustomer")) {
            switch (level) {
                case TRIM_MEMORY_BACKGROUND:
                case TRIM_MEMORY_MODERATE:
                case TRIM_MEMORY_COMPLETE:
                    if (Fresco.getImagePipeline() != null)
                        Fresco.getImagePipeline().clearMemoryCaches();
            }
        }
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     * @param context
     * @return
     */
    private HashMap<String, String> obtainSystemInfo(Context context) {
        HashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("time", DateUtil.GetNowDateTime());
        map.put("MANUFACTURER", Build.MANUFACTURER);//制造商
        map.put("versionName", BuildConfig.VERSION_NAME);//版本名称
        map.put("versionCode", "" + BuildConfig.VERSION_CODE);//版本号
        map.put("MODEL", String.valueOf(Build.MODEL));//设备型号
        map.put("SDK_INT", String.valueOf(Build.VERSION.SDK_INT));//系统SDK版本号

        return map;
    }


    public List<Activity> getmActivityList() {
        return mActivityList;
    }

    public void setmActivityList(Activity activity) {
        if (mActivityList == null) {
            mActivityList = new ArrayList<Activity>();
        }
        mActivityList.add(activity);
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
////             finishAllActivity();
////            ActivityManager activityMgr = (ActivityManager) context
////                    .getSystemService(Context.ACTIVITY_SERVICE);
////            activityMgr.restartPackage(context.getPackageName());
////            System.exit(0);
//
//            for (int i = 0; i < ActivityStackRecorder.getInstance().size(); i++) {
//                ActivityStackRecorder.getInstance().get(i).finish();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
