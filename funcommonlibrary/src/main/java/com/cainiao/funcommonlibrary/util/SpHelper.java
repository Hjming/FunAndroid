package com.cainiao.funcommonlibrary.util;

import android.text.TextUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.cainiao.funcommonlibrary.Constant.SpKeyContainer.OTHER.GATEWAY_DIFFTIME;
import static com.cainiao.funcommonlibrary.Constant.SpKeyContainer.OTHER.OTHER_CASE_KEY;
import static com.cainiao.funcommonlibrary.Constant.SpKeyContainer.OTHER.WEBTYPE;
import static com.cainiao.funcommonlibrary.Constant.SpKeyContainer.OTHER.WEB_GATEWAY_DIFFTIME;
import static com.cainiao.funcommonlibrary.Constant.SpKeyContainer.OTHER.WEB_TYPE;
import static com.cainiao.funcommonlibrary.Constant.USER.KEY_LOGIN_INFO;
import static com.cainiao.funcommonlibrary.Constant.USER_KEY;

/**
 * time: 2019-12-12
 * Project_Name: FunAndroid
 * Package_Name:
 * Description: 一个包装类 封装常用的key value 存储和获取的方法
 * Created by hjm.
 */
public class SpHelper {
    private static SPUtils mOtherSPUtils = SPUtils.getInstance(OTHER_CASE_KEY);
    private static SPUtils mUserSPUtils = SPUtils.getInstance(USER_KEY);
    private static SPUtils mLoginSPUtils = SPUtils.getInstance(KEY_LOGIN_INFO);
    private static SPUtils mDiffTimeSPUtils = SPUtils.getInstance(WEB_GATEWAY_DIFFTIME);
    private static SPUtils mWebTypeSPUtils = SPUtils.getInstance(WEB_TYPE);

    /**
     * 存储在sp名为OTHER_CASE_KEY的文件中
     *
     * @param key APPID_VALUE
     *            USER_AGENT
     *            CUSTOM_NAME_KEY
     *            CUSTOM_PHONE_KEY
     *            CUSTOM_ISSIR_KEY
     *            OWNER_SHOW_MFCEO_TIME
     *            SIGNED_USER_TYPE
     */
    public static void putOtherString(String key, String value) {
        mOtherSPUtils.putString(key, value);
    }

    /**
     * 获取在OTHER_CASE_KEY中对应的key值
     *
     * @param key APPID_VALUE
     *            USER_AGENT
     *            CUSTOM_NAME_KEY
     *            CUSTOM_PHONE_KEY
     *            CUSTOM_ISSIR_KEY
     *            OWNER_SHOW_MFCEO_TIME
     *            SIGNED_USER_TYPE
     */
    public static String getOtherString(String key) {
        return mOtherSPUtils.getString(key);
    }


    /**
     * 存储经纬度 到sp
     *
     * @param latidude  纬度
     * @param longitude 经度
     */
    public static void saveLocation(double latidude, double longitude) {
        mLoginSPUtils.putString("location_info_latitude", latidude + "");
        mLoginSPUtils.putString("location_info_longitude", longitude + "");

    }

    /**
     * 获取存储sp中的经纬度 Map
     *
     * @return 经纬度map对象
     */
    public static Map<String, String> getLocation() {
        String latitude = mLoginSPUtils.getString("location_info_latitude", "");
        String longitude = mLoginSPUtils.getString("location_info_longitude", "");
        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
            Map<String, String> map = new HashMap<>();
            map.put("latitude", latitude);
            map.put("longitude", longitude);
            return map;
        }
        return null;
    }

    /**
     * 保存服务器时间和系统时间的差值
     *
     * @param diffTime 服务器时间和系统时间的差值
     */
    public static void putGateWayDiffTime(long diffTime) {
        mDiffTimeSPUtils.putLong(GATEWAY_DIFFTIME, diffTime);
    }

    /**
     * 获取服务器时间和系统时间的差值
     *
     * @return 服务器时间和系统时间的差值
     */
    public static long getGateWayDiffTime() {
        return mDiffTimeSPUtils.getLong(GATEWAY_DIFFTIME, 0L);
    }


    /**
     * @param webType
     */
    public static void putWebType(int webType) {
        mWebTypeSPUtils.putInt(WEBTYPE, webType);
    }

    /**
     * @return
     */
    public static int getWebType() {
        return mWebTypeSPUtils.getInt(WEBTYPE, 0);
    }
}
