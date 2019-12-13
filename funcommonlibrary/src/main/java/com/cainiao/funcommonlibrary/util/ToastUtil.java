package com.cainiao.funcommonlibrary.util;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cainiao.funcommonlibrary.Constant;

/**
 * time: 2019-12-12
 * Project_Name: FunAndroid
 * Package_Name:
 * Description:
 * Created by hjm.
 */
public class ToastUtil {
    private static Toast sToast; //Toast对象

    /**
     * 显示吐司内容
     *
     * @param text 吐司内容文本
     */
    public static void showToast(final String text) {
        if (StringUtil.isEmpty(text)) {
            return;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            showToast(text, Toast.LENGTH_LONG);
        } else {
            new Handler(Constant.sContext.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showToast(text, Toast.LENGTH_LONG);
                }
            });
        }
    }

    /**
     * 功能说明：显示吐司内容
     *
     * @param resId 吐司内容资源id
     */
    public static void showToast(final int resId) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            showToast(resId, Toast.LENGTH_LONG);
        } else {
            new Handler(Constant.sContext.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showToast(resId, Toast.LENGTH_LONG);
                }
            });
        }
    }

    /**
     * 防止toast排队
     *
     * @param text     显示的内容字符串
     * @param duration 显示的时间
     */
    public static void showToast(String text, int duration) {
        if (StringUtil.isEmpty(text)) {
            return;
        }
        cancel();
        if (sToast == null) {
            sToast = Toast.makeText(Constant.sContext.getApplicationContext(), null, duration);
        }

        sToast.setText(text);
        sToast.setDuration(duration);

        sToast.show();
    }

    /**
     * 功能说明：显示吐司内容
     *
     * @param res      吐司内容资源id
     * @param duration 吐司弹出时间长短
     */
    public static void showToast(int res, int duration) {
        cancel();
        if (sToast == null) {
            sToast = Toast.makeText(Constant.sContext.getApplicationContext(), res, duration);
        } else {
            sToast.setText(res);
            sToast.setDuration(duration);
        }

        sToast.show();
    }

    /**
     * 带图片吐司
     *
     * @param ImageResourceId 图片资源文件id
     * @param text            显示的文字描述
     * @param duration        显示时间长短
     */
    public static void showToast(final int ImageResourceId, final String text, final int duration) {
        if (!StringUtil.isEmpty(text)) {
            cancel();
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                if (sToast == null) {
                    sToast = new Toast(Constant.sContext.getApplicationContext());
                }
                sToast.setGravity(Gravity.CENTER, 0, 0);
                View toastView = sToast.getView();
                ImageView img = new ImageView(Constant.sContext.getApplicationContext());
                img.setImageResource(ImageResourceId);
                // 创建一个LineLayout容器
                LinearLayout ll = new LinearLayout(Constant.sContext.getApplicationContext());
                ll.addView(img);
                ll.addView(toastView);
                // 将LineLayout容器设置为toast的View
                sToast.setView(ll);
                sToast.show();
                showToast(String.valueOf(text), duration);
            } else {
                new Handler(Constant.sContext.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (sToast == null) {
                            sToast = new Toast(Constant.sContext.getApplicationContext());
                        }
                        sToast.setGravity(Gravity.CENTER, 0, 0);
                        View toastView = sToast.getView();
                        ImageView img = new ImageView(Constant.sContext.getApplicationContext());
                        img.setImageResource(ImageResourceId);
                        // 创建一个LineLayout容器
                        LinearLayout ll = new LinearLayout(Constant.sContext.getApplicationContext());
                        ll.addView(img);
                        ll.addView(toastView);
                        // 将LineLayout容器设置为toast的View
                        sToast.setView(ll);
                        sToast.show();
                        showToast(String.valueOf(text), duration);
                    }
                });
            }
        }
    }

    /**
     * Cancel the toast.
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }

    }

}
