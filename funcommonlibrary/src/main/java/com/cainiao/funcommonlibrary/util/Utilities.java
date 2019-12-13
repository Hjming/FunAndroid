package com.cainiao.funcommonlibrary.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cainiao.funcommonlibrary.BuildConfig;
import com.cainiao.funcommonlibrary.okhttp.HttpUtil;
import com.cainiao.funcommonlibrary.okhttp.parser.FileParser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Utilities {
    /**
     * @param origin
     * @return md5 value
     */
    public static String MD5Encode(String origin) {
        return toMd5(origin.getBytes());
    }


    public static String toMd5(byte[] bytes) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            return toHexString(algorithm.digest(), "");
        } catch (NoSuchAlgorithmException e) {
            Log.v("util", "toMd5():" + e);
            throw new RuntimeException(e);
        }
    }

    private static String toHexString(byte[] bytes, String separator) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex).append(separator);
        }
        return hexString.toString();
    }

    /**
     * 半角转换为全角 说明：解决TextView中文字排版参差不齐的问题
     * 将textview中的字符全角化。即将所有的数字、字母及标点全部转为全角字符，使它们与汉字同占两个字节 by:liubing
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号 说明：解决TextView中文字排版参差不齐的问题 by:liubing
     *
     * @param str
     * @return
     */
    public static String StringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":").replace("，", ",")
                .replace("。", ".").replace("……", "......");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    // 判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
//        p = Pattern.compile("^((13[0-9])|(14[0-9])|(17[0-9])|(15[0-9])|(16[0-9])|(18[0-9]))\\d{8}$"); // 验证手机号
        p = Pattern.compile("^[0-9]{11}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 纳税人识别号验证
     * 纳税人识别号是 15位 18位 20位 由数字 大写字母构成的字符串
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isTaxpayerNum(String str) {
        Pattern p0 = null;
        Matcher m0 = null;
        boolean b0 = false;
        p0 = Pattern.compile("^(?![A-Z]*$)[A-Z0-9]{15}$|^(?![A-Z]*$)[A-Z0-9]{18}$|^(?![A-Z]*$)[A-Z0-9]{20}$"); // 由大写字母和数字组成
        m0 = p0.matcher(str);
        b0 = m0.matches();

        return b0;
    }

    public static boolean isPsw(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("\\d{9-16}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 证件号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean checkCertCode(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[A-Za-z0-9]+"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


    /**
     * 判断是否为整型字符串
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        // +表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")
        boolean isNum = str.matches("[0-9]+");
        return isNum;
    }

    /**
     * 检查字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 字符串插入数组
     *
     * @param arr
     * @param str
     * @return
     */
    public static String[] insert(String[] arr, String str) {
        int size = arr.length;
        String[] tmp = new String[size + 1];
        System.arraycopy(arr, 0, tmp, 0, size);
        tmp[size] = str;
        return tmp;
    }

    /**
     * 获取GPS经纬度
     *
     * @param context
     * @return
     */
    public static String getLocation(Context context) {
        String position = "";

        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            position = latitude + "," + longitude;
        }

        return position;
    }

    /**
     * 检测网路类型
     *
     * @param context
     * @return
     */
    public static String getNetWorkType(Context context) {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

            }
        }


        return strNetworkType;
    }

    /**
     * 拷贝文件
     *
     * @param context
     * @param fileName
     * @param path
     * @return
     */
    public static boolean copyApkFromAssets(Context context, String fileName,
                                            String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    /**
     * 获取渠道编号
     *
     * @param context
     * @return
     */
    public static String getChannelCode(Context context) {
        String code = getMetaData(context, "CHANNEL");
        if (code != null) {
            return code;
        }
        return "C_000";
    }

    private static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
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
     * 检测网络是否链接
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {

        // 获取手机所以连接管理对象（包括wi-fi，net等连接的管理）
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            // 网络管理连接对象
            NetworkInfo info = conn.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    /*
     * 获取手机的唯一设备号
     */
    public static String getDeviceId(Context context) {
        return DeviceUtil.getDeviceId();
    }

    /**
     * 获取Mac
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        if (StringUtil.isNull(info.getMacAddress())) {
            mac = "123";
        }
        return mac;

    }

    /**
     * 产生6位随机数
     *
     * @return
     */
    public static String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += Math.abs(random.nextInt(10));
        }
        return result;
    }

    /**
     * 格式化身份证字符串6-4-4-4格式
     *
     * @param id_card
     * @return
     */
    public static String formatIDCard(String id_card, String sep) {
        id_card = id_card.replaceAll(sep, "");
        if (id_card.length() > 6) {
            id_card = id_card.substring(0, 6) + sep + id_card.substring(6);
        }
        if (id_card.length() > 11) {
            id_card = id_card.substring(0, 11) + sep + id_card.substring(11);
        }
        if (id_card.length() > 16) {
            id_card = id_card.substring(0, 16) + sep + id_card.substring(16);
        }
        if (id_card.length() > 21) {
            id_card = id_card.substring(0, 21);
        }
        return id_card;
    }

    /**
     * 把字符串转为年月日的格式
     *
     * @param date
     * @return
     */
    public static String StrToNorDate(String date) {
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
     * 判定输入汉字
     *
     * @param c
     * @return
     */

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

    }

    /**
     * 获取App版本<BR>
     * 为什么不直接用BuildConfig.VERSION_CODE？
     *
     * @param mContext
     * @return
     */
    @Deprecated
    public static String getAppVersion(Context mContext) {
     /*   PackageInfo info = null;

        PackageManager manager = mContext.getPackageManager();

        try {

            info = manager.getPackageInfo(mContext.getPackageName(), 0);

        } catch (NameNotFoundException e) {

            e.printStackTrace();

        }
        return info.versionCode + "";*/
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    /**
     * 随机指定范围内N个不重复的数
     *
     * @param max 指定范围最大值
     * @param min 指定范围最小值
     * @param n   随机数个数
     * @return int[] 随机数结果集
     */

    public static int[] randomArray(int min, int max, int n) {

        int len = max - min + 1;

        if (max < min || n > len) {

            return null;

        }

        // 初始化给定范围的待选数组

        int[] source = new int[len];

        for (int i = min; i < min + len; i++) {

            source[i - min] = i;

        }

        int[] result = new int[n];

        Random rd = new Random();

        int index = 0;

        for (int i = 0; i < result.length; i++) {

            index = Math.abs(rd.nextInt() % len--);

            // 将随机到的数放入结果集

            result[i] = source[index];

            // 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换

            source[index] = source[len];

        }

        return result;

    }

    /**
     * 获取手机的一些信息
     *
     * @return
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
     * 发短信
     */
    public static void sendMessages(Context context, String phone, String mess) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //At least KitKat
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); //Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra("address", phone);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mess);

            if (defaultSmsPackageName != null) {
                //Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
                sendIntent.setPackage(defaultSmsPackageName);
            }
            context.startActivity(sendIntent);

        } else {
            //For early versions, do what worked for you before.
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.putExtra("sms_body", mess);
            sendIntent.putExtra("address", phone);
            context.startActivity(sendIntent);
        }

//
//        Intent mIntent = new Intent(Intent.ACTION_VIEW);
//        mIntent.putExtra("address", phone);
//        mIntent.putExtra("sms_body", mess);
//        mIntent.setType("vnd.android-dir/mms-sms");
//        context.startActivity(mIntent);

    }

    /**
     * 拨打电话
     */
    public static void callPhone(final Fragment context, final String phone) {
//        ConfirmZiroomDialog.newBuilder(context.getContext())
//                .setTitle("拨打电话")
//                .setContent("是否拨打" + phone + "？")
//                .setBtnConfirmText("呼叫")
//                .setOnConfirmClickListener(new ConfirmZiroomDialog.OnButtonClickClickListener() {
//                    @Override
//                    public void onClick(View v, boolean isConfirm) {
//                        if (isConfirm) {
//                            if (!TextUtils.isEmpty(phone)) {
//                                String number = phone.replace("-", ",");
//                                if (context instanceof Fragment) {
//                                    PermissionUtil.callPhone(context, number);
//                                } else {
//                                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
//                                    context.startActivity(phoneIntent);
//                                }
//                            }
//                        }
//                    }
//                })
//                .build()
//                .show();
    }

    /**
     * 拨打电话
     */
    public static void callPhone(final Context context, final String phone) {
//        ConfirmZiroomDialog.newBuilder(context)
//                .setTitle("拨打电话")
//                .setContent("是否拨打" + phone + "？")
//                .setBtnConfirmText("呼叫")
//                .setOnConfirmClickListener(new ConfirmZiroomDialog.OnButtonClickClickListener() {
//                    @Override
//                    public void onClick(View v, boolean isConfirm) {
//                        if (isConfirm) {
//                            if (!TextUtils.isEmpty(phone)) {
//                                String number = phone.replace("-", ",");
//                                if (context instanceof FragmentActivity) {
//                                    PermissionUtil.callPhone((FragmentActivity) context, number);
//                                } else {
//                                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
//                                    context.startActivity(phoneIntent);
//                                }
//                            }
//                        }
//                    }
//                })
//                .build()
//                .show();
    }

    /**
     * 拨打电话
     */
    public static void callPhone(final Context context, final String phone, final String name) {
//        View view = LayoutInflater.from(context).inflate(
//                R.layout.dialog_ok_cancel, null);
//        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
//        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
//        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
//
//        final AlertDialog dialog = new AlertDialog.Builder(context,
//                R.style.MyDialogTheme).create();
//        // dialog.setView(view, 0, 0, 0, 0);
//        dialog.show();
//        dialog.getWindow().setContentView(view);
//        tv_message.setText("确认要拨打" + phone + "吗?");
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                String number = phone.replace("-", ",");
//                if (context instanceof FragmentActivity) {
//                    PermissionUtil.callPhone((FragmentActivity) context, number);
//                } else {
//                    Intent phoneIntent = new Intent("android.intent.action.CALL",
//                            Uri.parse("tel:" + number));
//                    context.startActivity(phoneIntent);
//                }
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
    }

    private static final double PI = 3.14159265;

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @return
     */
    public static double[] getAround(double lat, double lon, int raidus) {

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901 * 1609) / 360.0;
        double raidusMile = raidus;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        // System.out.println("["+minLat+","+minLng+","+maxLat+","+maxLng+"]");
        return new double[]{minLat, minLng, maxLat, maxLng};
    }


    /**
     * 获取Ip地址
     *
     * @param context
     * @return ip地址
     */
    public static String getIpAddress(Context context) {
        String state = getNetWorkType(context);
        if ("WIFI".equals(state)) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                     en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                         enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }


    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * get请求url拼接,需要在请求地址后面加 ? 和该String
     *
     * @param map
     * @return
     */
    public static String appendGetUrl(Map<String, Object> map) {
        if (map == null || map.size() < 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Set<String> set = map.keySet();
        for (String str : set) {
            String param;
            if (map.get(str) == null) {
                param = "";
            } else {
                param = map.get(str).toString().trim();
            }
            try {
                str = URLEncoder.encode(str, "utf-8");
                param = URLEncoder.encode(param, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            builder.append("&" + str + "=" + param);
        }
        builder.deleteCharAt(0);
        return builder.toString();
    }

    public static boolean checkSpecialChar(String str) throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx = ".*[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#¥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * get请求url拼接,需要在请求地址后面加 ? 和该String
     *
     * @param map
     * @return
     */
    public static String encodeGetParams(Map<String, String> map) {
        if (map == null || map.size() < 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Set<String> set = map.keySet();
        for (String str : set) {
            String param;
            if (map.get(str) == null) {
                param = "";
            } else {
                param = map.get(str).toString();
            }
            try {
                str = URLEncoder.encode(str, "utf-8");
                param = URLEncoder.encode(param, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            builder.append("&" + str + "=" + param);
        }
        builder.deleteCharAt(0);
        return builder.toString();
    }

    /**
     * 把Map<String,Object>转换成Map<String,String>
     *
     * @param map
     * @return
     */
    public static Map<String, String> ConvertObjMap2String(Map<String, Object> map) {
        Map<String, String> Map = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                Map.put(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                Map.put(entry.getKey(), String.valueOf(entry.getValue()));
            } else if (entry.getValue() == null) {
                Map.put(entry.getKey(), "");
            } else {
                Map.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return Map;
    }


    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) {
        /**金额为分的格式 */
        String CURRENCY_FEN_REGEX = "\\-?[0-9]+";
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            try {
                throw new Exception("金额格式有误");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
        } catch (NumberFormatException e) {
            return "";
        }
    }

    // 保留小数点后两位小数
    public static double Number2(double pDouble) {
        BigDecimal bd = new BigDecimal(pDouble);
        BigDecimal bd1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        pDouble = bd1.doubleValue();
        long ll = Double.doubleToLongBits(pDouble);
        return pDouble;
    }

    // 保留小数点后两位小数,显示两个0  例如2显示2.00
    public static String Number(float pDouble) {

        DecimalFormat df = new DecimalFormat("##0.00");
        System.out.println(df.format(pDouble));
        return df.format(pDouble).replace(",", ".");
    }

    // 保留小数点后两位小数,显示两个0  例如2显示2.00
    public static String Number(double pDouble) {

        DecimalFormat df = new DecimalFormat("##0.00");
        System.out.println(df.format(pDouble));
        return df.format(pDouble).replace(",", ".");
    }

    //保留两位小数，四舍五入
    public static float setFloat(float fl) {
        BigDecimal bd = new BigDecimal((double) fl);
        bd = bd.setScale(2, 4);
        fl = bd.floatValue();
        return fl;
    }

    //保留两位小数，四舍五入
    public static double setDouble(Double fl) {
        BigDecimal bd = new BigDecimal((double) fl);
        bd = bd.setScale(2, 4);
        fl = bd.doubleValue();
        return fl;
    }

    //整数，四舍五入
    public static int getInt(float fl) {
        BigDecimal bd = new BigDecimal((double) fl);
        bd = bd.setScale(0, 4);
        int integer = bd.intValue();
        return integer;
    }

    //保留两位小数，四舍五入
    public static float setInt(float fl) {
        BigDecimal bd = new BigDecimal((double) fl);
        bd = bd.setScale(0, 4);
        fl = bd.floatValue();
        return fl;
    }

    //改变textView部分字体颜色
    public static void setTextColor(TextView tv, String text, int start, int length) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#FFA000"));
        builder.setSpan(redSpan, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (tv != null)
            tv.setText(builder);
    }

    //改变textView部分字体颜色
    public static void setTextColor(TextView tv, String text, int start, int length, String textColor) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor(textColor));
        builder.setSpan(redSpan, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (tv != null)
            tv.setText(builder);
    }

    //改变textView部分字体颜色
    public static void setTextColor(TextView tv, String text, List<Integer> start, int length, String textColor) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        if (start != null) {
            for (int i : start) {
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor(textColor));
                builder.setSpan(redSpan, i, i + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (tv != null)
            tv.setText(builder);
    }

    //改变textView部分字体颜色
    public static void setTextColorOwner(TextView tv, String text, int start1, int length1, int start2, int length2) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan1 = new ForegroundColorSpan(Color.parseColor("#CCA758"));
        ForegroundColorSpan redSpan2 = new ForegroundColorSpan(Color.parseColor("#CCA758"));
        builder.setSpan(redSpan1, start1, start1 + length1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(redSpan2, start2, start2 + length2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (tv != null)
            tv.setText(builder);
    }

    public static void setTextColorOwner(TextView tv, String text, int start1, int length1) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan1 = new ForegroundColorSpan(Color.parseColor("#CCA758"));
        builder.setSpan(redSpan1, start1, start1 + length1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (tv != null)
            tv.setText(builder);
    }

    //判断是否是https、http,ftp,file链接
    public static boolean isWebUrl(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(url);
        boolean isMatch = matcher.matches();
        return isMatch;
//		String reg = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";
//		Pattern pat = Pattern.compile(reg);
//		Matcher mat = pat.matcher(url);
//		boolean isMat = mat.matches();

    }

    /**
     *  
     * <p>
     *  根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 格式化小数为小数点后两位的字符串
     *
     * @param value
     * @return
     */
    public static String formatDouble(double value) {
//        BigDecimal   b   =   new   BigDecimal(value);
//        double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();

        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        return df.format(value);//返回的是String类型的
    }

    public static int stringToInteger(String str) {
        int ret = 0;
        if (TextUtils.isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if (ch >= '0' && ch <= '9') {
                    ret = ret * 10 + ch - '0';
                }
            }
        }
        return ret;
    }


    /**
     * 下载pdf文件到本地
     *
     * @param context
     * @param pdfUrl
     * @param pdfName
     */
    public static void downloadPdfFile(Context context, String pdfUrl, String pdfName) {
//        if (!StringUtil.isNull(pdfUrl)) {
//            com.freelxl.baselibrary.util.LogUtil.i("downloadPdf", "下载PDF文件：" + pdfUrl);
//            if (!TextUtils.isEmpty(pdfName)) {
//                if (pdfName.length() > 30) {
//                    pdfName = pdfName.substring(0, 30);
//                }
//            } else {
//                pdfName = "ziroom" + System.currentTimeMillis();
//            }
//            if (!(pdfName.endsWith(".pdf") || pdfName.endsWith(".PDF"))) {
//                pdfName = pdfName + ".pdf";
//            }
//            File fileDir = new File(context.getCacheDir(), "ziroom/file");
//            if (!fileDir.exists()) {
//                fileDir.mkdirs();
//            }
//            File file = new File(fileDir, pdfName);
//            if (!file.exists()) {
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    ToastUtil.showToast("pdf创建失败！");
//                    e.printStackTrace();
//                }
//            }
//
//            HttpUtil.get(pdfUrl)
//                    .tag(context)
//                    .enqueue(new LoadingCallback<File>(context, new FileParser(file)) {
//                        @Override
//                        public void onSuccess(int code, File file) {
//                            super.onSuccess(code, file);
//                            if (file.exists() && file.isFile()) {
//                                Intent intent = new Intent("android.intent.action.VIEW");
//                                intent.addCategory("android.intent.category.DEFAULT");
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                Uri uri = ZRFileProvider.getUriForFile(file);
//                                intent.setDataAndType(uri, "application/pdf");
//                                PackageManager packageManager = context.getPackageManager();
//                                List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                                if (list != null && list.size() > 0) {
//                                    context.startActivity(intent);
//                                } else {
//                                    ToastUtil.showToast("设备不支持查看pdf文件，请安装pdf阅读软件！");
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Throwable e) {
//                            super.onFailure(e);
//                            ToastUtil.showToast("下载失败，请稍后重试！");
//                        }
//                    });
//        } else {
//            ToastUtil.showToast("下载链接为空！");
//        }
    }

    /**
     * H5链接或get链接拼参数, 需要其他参数可以再继续扩展
     *
     * @param mContext      上下文
     * @param getUrl        链接地址
     * @param addToken      是否添加token信息
     * @param addOS         是否添加os信息
     * @param addAppVersion 是否添加app-version信息
     *                      author maoqiang
     * @return
     */

    public static String appendGetUrlParams(Context mContext, String getUrl, boolean addToken, boolean addOS, boolean addAppVersion) {
        getUrl += getUrl.contains("?") ? "&" : "?";
        Map<String, Object> paramsMap = new HashMap<>();

        if (addOS)
            paramsMap.put("os", "android");
        if (addAppVersion)
            paramsMap.put("app_version", "");
        if (addToken)
            paramsMap.put("token", "");

        getUrl += appendGetUrl(paramsMap);
        return getUrl;
    }

    /**
     * 设置控件所在的位置Y，并且不改变宽高，
     * Y为绝对位置，此时X可能归0
     */
    public static void setLayoutY(View view, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(margin.leftMargin, y, margin.rightMargin, y + margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    public static String getAppName(Context context, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();

        if (l == null) {
            return "";
        }

        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    public static void setImgHeight(SimpleDraweeView sv, float proportion) {
        int width = sv.getLayoutParams().width;
        sv.getLayoutParams().height = (int) (width * proportion);
    }

    public static void setImgHeight(final View view, final float proportion) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        int width = view.getLayoutParams().width;
                        view.getLayoutParams().height = (int) (width * proportion);
                    }
                });
            }
        });

    }

    // 计算出该TextView中文字的长度(像素)  
    public static float getTextViewLength(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
// 得到使用该paint写上text的时候,像素为多少  
        float textLength = paint.measureText(text);
        return textLength;
    }

    public static void backgroundAlpha(float f, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = f;
        activity.getWindow().setAttributes(lp);
    }

    public static String trim(String string) {
        string = string.trim();
        while (string.startsWith("　")) {//这里判断是不是全角空格
            string = string.substring(1, string.length()).trim();
        }
        while (string.endsWith("　")) {
            string = string.substring(0, string.length() - 1).trim();
        }
        return string;
    }

    //改变textView部分字体颜色
    public static void setTextColordel(TextView tv, String text, int start1, int length1, int start2, int length2, int size) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan1 = new ForegroundColorSpan(Color.parseColor("#ff3737"));
        builder.setSpan(redSpan1, start1, start1 + length1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StrikethroughSpan(), start2, start2 + length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (size != 0) {
            builder.setSpan(new AbsoluteSizeSpan(size), start2, start2 + length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (tv != null)
            tv.setText(builder);
    }
}
