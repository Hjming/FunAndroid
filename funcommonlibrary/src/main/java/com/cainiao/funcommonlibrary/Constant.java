package com.cainiao.funcommonlibrary;

import android.app.Application;

public class Constant {
    /**
     * 全局的context对象
     */
    public static Application sContext;

    public static final String DESKEY = "2y5QfvAy";  //接口key
    public static final String SIGNKEY = "hPtJ39Xs"; //接口key

    /**
     * 崩溃日志打印开关
     */
    public static final int YIWANCHENG = 10; // 已完成

    public static final String AREACODE = "areacode";
    public static final String MYAREAID = "myareaid";

    public static final String USER_KEY = "USER";

    public static class USER {
        public static final String UID = "uid";
        public static final String LOGIN_NAME = "loginName";
        public static final String TOKEN = "token";
    }


    /**
     * 正则表达式
     */
    public static class RegexPattern {
        /**
         * 判断字符串是否为日期格式
         */
        public static final String REGEX_DATE_FORMAT = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        /**
         * 判断字符串是否为数字
         */
        public static final String REGEX_STRING_IS_NUMBER = "[0-9]*";

        /**
         * 校验邮箱
         */
        public static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        /**
         * 只允许字母、数字和汉字
         */
        public static final String REGEX_ALPHABET_NUMBER_CHINESE = "[^a-zA-Z0-9\u4E00-\u9FA5]";

        /**
         * 校验护照 or 港澳居民往来证
         */
        public static final String REGEX_PASSPORT_OR_HONG_KONG_AND_MACAO_RESIDENT = "^[A-Za-z0-9]{1,20}$";
        /**
         * 校验全为字母
         */
        public static final String REGEX_ALL_ALPHABET = "^[A-Za-z]+$";
        /**
         * 校验全为数字
         */
        public static final String REGEX_ALL_NUMBER = "^\\d+$";
        /**
         * 校验台湾居民往来证
         */
        public static final String REGEX_PASSPORT_TAIWAN_RESIDENT = "^[A-Za-z0-9]{1,20}$";
        /**
         * 校验营业执照
         */
        public static final String REGEX_BUSINESS_LICENCE = "^[0-9A-Za-z]{1,30}$";
        /**
         * 手机号验证
         */
        public static final String REGEX_IS_MOBILE = "^((13[0-9])|(14[0-9])|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        /**
         * 纳税人识别号验证 纳税人识别号是 15位 18位 20位 由数字 大写字母构成的字符串
         */
        public static final String REGEX_IDENTIFICATION_NUMBER_OF_THE_TAXPAYER = "^(?![A-Z]*$)[A-Z0-9]{15}$|^(?![A-Z]*$)[A-Z0-9]{18}$|^(?![A-Z]*$)[A-Z0-9]{20}$";
        /**
         * 非法字符验证（必须是字母（不分大小写）或者数字的组合）
         */
        public static final String REGEX_ONLY_ALPHABET_NUM_VERITY = "^[A-Za-z0-9]+$";
    }

    public static class ErrorMessage {
        public static final String GETDATA_FAIL_ERROR = "获取数据失败!";
        public static final String DATAREQUEST_FAIL_ERROR = "网络数据请求失败!";
        public static final String DATAPARSE_FAIL_ERROR = "数据解析失败!";
        public static final String DATA_EXCEPTION_ERROR = "服务器异常，正在努力抢修中，请稍后再试!";
        public static final String NETWORK_EXCEPTION_ERROR = "网络异常!";
        public static final String NET_ERROR = "网络故障";
        public static final String NET_EXCEPTION_ERROR = "网络请求失败，请检查网络连接";
        public static final String SDATA_EXCEPTION_ERROR = "请求数据失败！";
        public static final String JDATA_EXCEPTION_ERROR = "解析数据失败！";
    }

}

