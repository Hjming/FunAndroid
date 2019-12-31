package com.cainiao.funcommonlibrary.util;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

public class SpannableStringUtils {

    /**
     * 为文本设置前景色，效果和TextView的setTextColor()类似
     *
     * Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
     * Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
     * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
     * Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
     */
    public static String setTextForegroundColor(String targetText, String text) {
        int startIndex = 0;
        if (!TextUtils.isEmpty(targetText)) {
            startIndex = text.indexOf(targetText);
        } else {
            startIndex = 0;
        }

        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#66000000"));
        spannableString.setSpan(foregroundColorSpan, startIndex, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString.toString();
    }


    public static String setTextBackgroudColor(String targetText, String text) {
        int startIndex = 0;
        if (!TextUtils.isEmpty(targetText)) {
            startIndex = text.indexOf(targetText);
        } else {
            startIndex = 0;
        }

        SpannableString spannableString = new SpannableString(text);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.GREEN);


        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#66000000"));
        spannableString.setSpan(foregroundColorSpan, startIndex, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString.toString();
    }


}
