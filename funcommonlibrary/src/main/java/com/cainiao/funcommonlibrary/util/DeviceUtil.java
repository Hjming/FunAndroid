package com.cainiao.funcommonlibrary.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.cainiao.funcommonlibrary.Constant;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.cainiao.funcommonlibrary.Constant.SpKeyContainer.OTHER.USER_AGENT;

public class DeviceUtil {
    private final static String  ZR_BASEDEVICE_INFO ="zr_basedevice_info";
    private final static String  ZR_ANDROID_ID ="zr_androidid";
    private final static String  ZR_ANDROID_MAGIC_NUM = "9774d56d682e549c";

    public static String getAndroidNewId() {
        SharedPreferences deviceInfo = Constant.sContext.getSharedPreferences(ZR_BASEDEVICE_INFO, Context.MODE_PRIVATE);
        String androidNewId = deviceInfo.getString(ZR_ANDROID_ID, "");
        if (TextUtils.isEmpty(androidNewId)) {
            androidNewId = Settings.Secure.getString(Constant.sContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(androidNewId) || androidNewId.equals(ZR_ANDROID_MAGIC_NUM)) {
                androidNewId = getUUID();
            }
            deviceInfo.edit().putString(ZR_ANDROID_ID, androidNewId).apply();
        }
        return androidNewId;
    };


    /**
     * 手机mac地址
     */
    private static String sMac;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue dp
     * @return px
     */
    public static int dp2px(float dpValue) {
        final float scale = Constant.sContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue px
     * @return dp
     */
    public static int px2dp(float pxValue) {
        final float scale = Constant.sContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获得屏幕宽度
     *
     * @return 屏幕宽度
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) Constant.sContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @return 屏幕高度
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) Constant.sContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     *
     * @return 状态栏的高度
     */
    public static int getStatusHeight() {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = Constant.sContext.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 拨打电话
     *
     * @param context     上下文
     * @param phoneNumber 要拨打的号码
     */
    public static void callPhone(final Context context, String phoneNumber) {
        if (!StringUtil.isEmpty(phoneNumber)) {
            String phoneNumber1 = phoneNumber.trim();// 删除字符串首部和尾部的空格
            String number = phoneNumber1.replace(",", "-");
            // 调用系统的拨号服务实现电话拨打功能
            // 封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            context.startActivity(intent);// 内部类
        }
    }

    /**
     * 发送短信
     *
     * @param context     上下文
     * @param phoneNumber 电话号码
     * @param content     内容
     */
    public static void sendSms(Context context, String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (StringUtil.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", StringUtil.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity Activity上下文
     * @return 当前屏幕截图，包含状态栏
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth();
        int height = getScreenHeight();
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity Activity上下文
     * @return 前屏幕截图，不包含状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth();
        int height = getScreenHeight();
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * AndroidManifest 中的meta-data的值
     *
     * @param key 需要获取的meta的key值
     * @return 返回meta key对应的value值
     */
    public static String getMetaData(String key) {
        try {
            ApplicationInfo ai = Constant.sContext.getPackageManager()
                    .getApplicationInfo(Constant.sContext.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(String key) {
        if (Constant.sContext == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = Constant.sContext.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(Constant.sContext.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = Constant.sContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(Constant.sContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取包名
     *
     * @return 当前应用的版本号
     */
    public static String getPackageName() {
        try {
            PackageManager manager = Constant.sContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(Constant.sContext.getPackageName(), 0);
            String packageName = info.packageName;
            return packageName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * IMEI （唯一标识序列号）
     *
     * @return IMEI
     */
    public static String getIMEI() {
        String deviceId;
        if (isPhone()) {
            deviceId = getDeviceId();
        } else {
            deviceId = getAndroidId();
        }
        return deviceId;
    }

    /**
     * 获取ANDROID ID
     *
     * @return ANDROID ID
     */
    public static String getAndroidId() {
        return Settings.Secure.getString(Constant.sContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static boolean  isErrorDeviceId(String deviceId) {
        return (TextUtils.isEmpty(deviceId) || deviceId.startsWith("0000000"));
    }

    private static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * @param context 上下文对象
     * @return 设备的Id 即IMEI
     */
    public static String getDeviceId(@NonNull Context context) {
        String deviceId ="";
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            try{
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(Constant.sContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    deviceId = tm.getDeviceId();
                }
            }catch (Exception e) {
                deviceId = "";
                e.printStackTrace();
            }
            if(isErrorDeviceId(deviceId)) {
                deviceId = getUUID();
            }
        } else {
            deviceId = getAndroidNewId();
        }

        return deviceId;
    }

    /**
     * 获取设备的Id
     *
     * @return 设备的Id
     */
    public static String getDeviceId() {
        return getDeviceId(Constant.sContext);
    }


    /**
     * @return 获取本机号码
     */
    public static String getLine1Number() {
        TelephonyManager tm = (TelephonyManager) Constant.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(Constant.sContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Constant.sContext, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Constant.sContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getLine1Number();
    }

    /**
     * @return 获取SIM卡序号
     */
    public static String getSimSerialNumber() {
        TelephonyManager tm = (TelephonyManager) Constant.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(Constant.sContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getSimSerialNumber();
    }

    /**
     * @return 获得用户ID
     */
    public static String getSubscriberId() {
        TelephonyManager tm = (TelephonyManager) Constant.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(Constant.sContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getSubscriberId();
    }

    /**
     * 判断设备是否是手机
     *
     * @return true 是 false 否
     */
    public static boolean isPhone() {
        TelephonyManager tm = (TelephonyManager) Constant.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取App版本
     *
     * @return App版本
     */
    public static String getAppVersion() {
        PackageInfo info = null;
        PackageManager manager = Constant.sContext.getPackageManager();

        try {
            info = manager.getPackageInfo(Constant.sContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode + "";
    }

    /**
     * 获取手机的一些信息
     *
     * @return 手机Build的一些信息
     */
    public static String getPhoneInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("Product: " + android.os.Build.PRODUCT + ", CPU_ABI: "
                + android.os.Build.CPU_ABI + ", TAGS: " + android.os.Build.TAGS
                + ", VERSION_CODES.BASE: "
                + android.os.Build.VERSION_CODES.BASE + ", MODEL: "
                + android.os.Build.MODEL + ", SDK: "
                + android.os.Build.VERSION.SDK + ", VERSION.RELEASE: "
                + android.os.Build.VERSION.RELEASE + ", DEVICE: "
                + android.os.Build.DEVICE + ", DISPLAY: "
                + android.os.Build.DISPLAY + ", BRAND: "
                + android.os.Build.BRAND + ", BOARD: " + android.os.Build.BOARD
                + ", FINGERPRINT: " + android.os.Build.FINGERPRINT + ", ID: "
                + android.os.Build.ID + ", MANUFACTURER: "
                + android.os.Build.MANUFACTURER + ", USER: "
                + android.os.Build.USER);

        return sb.toString();
    }

    /**
     * 获取手机联系人
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
     *
     * @param context 上下文;
     * @return 联系人链表
     */
    public static List<HashMap<String, String>> getAllContactInfo(Context context) {
        SystemClock.sleep(3000);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        // 1.获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts表的地址 :raw_contacts
        // view_data表的地址 : data
        // 3.生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");
        // 4.查询操作,先查询raw_contacts,查询contact_id
        // projection : 查询的字段
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"},
                null, null, null);
        // 5.解析cursor
        while (cursor.moveToNext()) {
            // 6.获取查询的数据
            String contact_id = cursor.getString(0);
            // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
            // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
            // 判断contact_id是否为空
            if (!StringUtil.isEmpty(contact_id)) {//null   ""
                // 7.根据contact_id查询view_data表中的数据
                // selection : 查询条件
                // selectionArgs :查询条件的参数
                // sortOrder : 排序
                // 空指针: 1.null.方法 2.参数为null
                Cursor c = resolver.query(date_uri, new String[]{"data1",
                                "mimetype"}, "raw_contact_id=?",
                        new String[]{contact_id}, null);
                HashMap<String, String> map = new HashMap<String, String>();
                // 8.解析c
                while (c.moveToNext()) {
                    // 9.获取数据
                    String data1 = c.getString(0);
                    String mimetype = c.getString(1);
                    // 10.根据类型去判断获取的data1数据并保存
                    if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                        // 电话
                        map.put("phone", data1);
                    } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                        // 姓名
                        map.put("name", data1);
                    }
                }
                // 11.添加到集合中数据
                list.add(map);
                // 12.关闭cursor
                c.close();
            }
        }
        // 12.关闭cursor
        cursor.close();
        return list;
    }

    /**
     * 获取手机短信并保存到xml中
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_SMS"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}</p>
     */
    public static void getAllSMS() {
        // 1.获取短信
        // 1.1获取内容解析者
        ContentResolver resolver = Constant.sContext.getContentResolver();
        // 1.2获取内容提供者地址   sms,sms表的地址:null  不写
        // 1.3获取查询路径
        Uri uri = Uri.parse("content://sms");
        // 1.4.查询操作
        // projection : 查询的字段
        // selection : 查询的条件
        // selectionArgs : 查询条件的参数
        // sortOrder : 排序
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
        // 设置最大进度
        int count = cursor.getCount();//获取短信的个数
        // 2.备份短信
        // 2.1获取xml序列器
        XmlSerializer xmlSerializer = Xml.newSerializer();
        try {
            // 2.2设置xml文件保存的路径
            // os : 保存的位置
            // encoding : 编码格式
            xmlSerializer.setOutput(new FileOutputStream(new File("/mnt/sdcard/backupsms.xml")), "utf-8");
            // 2.3设置头信息
            // standalone : 是否独立保存
            xmlSerializer.startDocument("utf-8", true);
            // 2.4设置根标签
            xmlSerializer.startTag(null, "smss");
            // 1.5.解析cursor
            while (cursor.moveToNext()) {
                SystemClock.sleep(1000);
                // 2.5设置短信的标签
                xmlSerializer.startTag(null, "sms");
                // 2.6设置文本内容的标签
                xmlSerializer.startTag(null, "address");
                String address = cursor.getString(0);
                // 2.7设置文本内容
                xmlSerializer.text(address);
                xmlSerializer.endTag(null, "address");
                xmlSerializer.startTag(null, "date");
                String date = cursor.getString(1);
                xmlSerializer.text(date);
                xmlSerializer.endTag(null, "date");
                xmlSerializer.startTag(null, "type");
                String type = cursor.getString(2);
                xmlSerializer.text(type);
                xmlSerializer.endTag(null, "type");
                xmlSerializer.startTag(null, "body");
                String body = cursor.getString(3);
                xmlSerializer.text(body);
                xmlSerializer.endTag(null, "body");
                xmlSerializer.endTag(null, "sms");
                System.out.println("address:" + address + "   date:" + date + "  type:" + type + "  body:" + body);
            }
            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();
            // 2.8将数据刷新到文件中
            xmlSerializer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置屏幕为横屏
     * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
     * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
     *
     * @param activity activity对象
     */
    public static void setLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity对象
     */
    public static void setPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 判断是否横屏
     *
     * @return 返回 true 是  false 否
     */
    public static boolean isLandscape() {
        return Constant.sContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否竖屏
     *
     * @return 返回 true 是  false 否
     */
    public static boolean isPortrait() {
        return Constant.sContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取软键盘是否弹出
     *
     * @param rootView
     * @return
     */
    public static boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    /**
     * 动态显示软键盘
     *
     * @param edit 输入框
     */
    public static void showSoftInput(EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) Constant.sContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit, 0);
    }

    /**
     * 切换键盘显示与否状态
     *
     * @param edit 输入框
     */
    public static void toggleSoftInput(EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) Constant.sContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 动态隐藏软键盘
     *
     * @param edit 输入框
     */
    public static void hideSoftInput(EditText edit) {
        edit.clearFocus();
        InputMethodManager inputmanger = (InputMethodManager) Constant.sContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    /**
     * 动态隐藏软键盘
     * 避免输入法面板遮挡
     * 在manifest.xml中activity中设置android:windowSoftInputMode="stateVisible|adjustResize"
     *
     * @param activity activity
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 点击隐藏软键盘
     *
     * @param activity activity
     * @param view     view
     */
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 获取UserAgent
     *
     * @return UserAgent字符串
     */
    public static String getUserAgent() {
        String mUserAgent = "";
        if (StringUtil.isNull(mUserAgent)) {
            mUserAgent = SpHelper.getOtherString(USER_AGENT);
            if (StringUtil.isNull(mUserAgent)) {
                WebView mWebView = new WebView(Constant.sContext);
                mUserAgent = mWebView.getSettings().getUserAgentString();
                SpHelper.putOtherString(USER_AGENT, mUserAgent);
            }
        }
        return mUserAgent;
    }


    /**
     * 获取手机mac地址
     *
     * @return
     */
    public static String getMac() {
        if (!TextUtils.isEmpty(sMac)) {
            return sMac;
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();

                byte[] addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }

                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                String mac = buf.toString();
                if (iF.getName().contains("wlan")) {
                    sMac = mac;
//                    return mac;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sMac;
    }
}
