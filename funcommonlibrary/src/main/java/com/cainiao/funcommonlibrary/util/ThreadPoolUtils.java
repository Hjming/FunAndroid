package com.cainiao.funcommonlibrary.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtils {
    private static ThreadPoolUtils mInstance;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 1000l;
    private Object mLock = new Object();
    private Object mWorkLock = new Object();
    private ThreadFactory sIOThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "IOThread#" + mCount.getAndIncrement());
        }
    };

    private ThreadFactory sCPUThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "WorkThread#" + mCount.getAndIncrement());
        }
    };

    private ExecutorService mIOThreadPool;
    private ExecutorService mCPUThreadPool;

    private ThreadPoolUtils() {

    }

    public static ThreadPoolUtils getInstance() {
        if (mInstance == null) {
            synchronized (ThreadPoolUtils.class) {
                if (mInstance == null) {
                    mInstance = new ThreadPoolUtils();
                }
            }
        }

        return mInstance;
    }

    public void shutdown() {
        if (mIOThreadPool != null) {
            mIOThreadPool.shutdown();
        }

        if (mCPUThreadPool != null) {
            mCPUThreadPool.shutdown();
        }
    }

    public ExecutorService getIOThreadPool() {
        if (mIOThreadPool == null) {
            synchronized (mLock) {
                try {

                    if (mIOThreadPool == null) {
                        mIOThreadPool = new ThreadPoolExecutor(
                                4, Math.max(4, MAXIMUM_POOL_SIZE),
                                KEEP_ALIVE, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(),
                                sIOThreadFactory);
                    }
                } catch (Exception e) {

                }

            }

        }

        return mIOThreadPool;
    }

    public ExecutorService getWorkThreadPool() {
        if (mCPUThreadPool == null) {
            synchronized (mWorkLock) {
                try {
                    if (mCPUThreadPool == null) {
                        mCPUThreadPool = new ThreadPoolExecutor(
                                4, Math.max(4, CORE_POOL_SIZE),
                                KEEP_ALIVE, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(),
                                sCPUThreadFactory);
                    }

                } catch (Exception e) {

                }
            }

        }

        return mCPUThreadPool;
    }

    public void executeWork(Runnable runnable) {
        getWorkThreadPool().execute(runnable);
    }

    public void executeWorkDelayed(final Runnable runnable, long time) {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getWorkThreadPool().execute(runnable);
            }
        }, time);
    }

    public void executeWorkInUI(Runnable runnable, long time) {
        getHandler().postDelayed(runnable, time);
    }

    public void executeIO(Runnable runnable) {
        getIOThreadPool().execute(runnable);
    }

    public void executeIODelayed(final Runnable runnable, long time) {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getIOThreadPool().execute(runnable);
            }
        }, time);
    }

    private InternalHandler mHandler;

    private Handler getHandler() {
        synchronized (this) {
            if (mHandler == null) {
                mHandler = new InternalHandler();
            }
            return mHandler;
        }
    }

    private static class InternalHandler extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }

}

