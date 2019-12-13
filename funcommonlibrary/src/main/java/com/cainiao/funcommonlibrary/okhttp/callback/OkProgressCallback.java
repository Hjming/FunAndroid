package com.cainiao.funcommonlibrary.okhttp.callback;

import android.os.Handler;
import android.os.Looper;

public abstract class OkProgressCallback {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public void onOKProgress(final long current, final long total){
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                onProgress(current,total);
            }
        });
    }

    public abstract void onProgress(long current,long total);
}
