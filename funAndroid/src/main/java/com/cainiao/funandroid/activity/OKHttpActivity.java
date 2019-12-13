package com.cainiao.funandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.cainiao.funandroid.R;
import com.cainiao.funandroid.net.okhttp.FunOkhttpCall;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Address;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;

public class OKHttpActivity extends AppCompatActivity {

    private static final String TAG = "OKHttpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        /**
         * 步骤:
         *  1、创建OkHttpClient，http请求的客户端类，两种创建方式
         *      （1）OkHttpClient okHttpClient = new OkHttpClient();
         *      （2）OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
         *  2、创建Request，Request携带请求信息
         *  3、创建Call,将Request封装成Call对象
         *     Call call = okHttpClient.newCall(request);
         *
         *  4、创建Response
         *      4.1、同步 Response response = call.execute();  // 发起网络请求
         *      4.2、异步 Response response = call.enqueue();  // 发起网络请求
         */


//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                syncRequest();
//            }
//        }.start();


    }

//      /**
     //     * 同步请求
     //     */
//    public void syncRequest() {
//        // OkHttpClient okHttpClient = new OkHttpClient();
//
//        /**
//         * OkHttpClient内部类Builder
//         * 内部类Builder的内部类Dispatcher类,接收 同步或者异步的请求队列,然后分发 同步 或者 异步的请求队列.
//         */
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//
//        // 构建Request，Request携带请求报文信息
//        Request request = new Request.Builder()
//                .url()
//                .method()
//                .header()
//                .build();
//
//        Call call = okHttpClient.newCall(request);
//        try {
//            /**
//             * 同步发送请求后，就会进入阻塞状态，直到收到响应
//             */
//            // Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
//            Response response = call.execute();
//
//            Log.e(TAG, response.body().toString());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 异步请求
//     */
//    public void asyncRequest() {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//
//        Request request = new Request.Builder()
//                .url("https://www.baidu.com/")
//                .get()
//                .build();
//
//        Call call = okHttpClient.newCall(request);
//
//        /**
//         * 异步不会阻塞线程
//         *
//         * enqueue()方法开启了一个新的子线程AsyncCall（实际是一个Runnable），请求网络在这个子线程中执行
//         *
//         * onFailure(),onResponse() 都是在子线程中执行
//         *
//         *
//         * 异步：
//         * RealCall 类enqueue()方法作用:
//         *（1）判断 call 是否只执行了一次
//         * RealCall中：
//         *      enqueue(){
//         *          synchronized (this) {
//         *              // executed 表示，call的实例有没有执行过，call的实例只能执行一次
//         *              if (executed) throw new IllegalStateException("Already Executed");
//         *                  executed = true;
//         *          }
//         *
//         *          ...
//         *
//         *          // 将传入的 Callback 对象，封装成 AsyncCall 对象
//         *          client.dispatcher().enqueue(new AsyncCall(responseCallback));
//         *      }
//         *
//         *（2）将传入的 Callback 对象，封装成 AsyncCall 对象
//         *    new AsyncCall(responseCallback)
//         *
//         *（3）使用 Dispatcher.enqueue(){} 进行异步的网络请求
//         *    RealCall中：
//         *    client.dispatcher().enqueue(new AsyncCall(responseCallback));
//         *
//         *    Dispatcher中，enqueue()方法判断，将请求放入在 异步请求队列，还是在 异步就绪队列中
//         *    if(当前正在请求的队列 < 64 && 当前网络请求的host < 5){
//         *      // 将AsyncCall（Runnable）添加到异步请求队列
//         *      // 通过线程池（单例）进行请求
//         *      AsyncCall.executeOn(executorService()); // 将执行 交给了 AsyncCall
//         *    }else{
//         *      // 将AsyncCall（Runnable）添加到异步就绪队列中
//         *    }
//         *
//         *    public synchronized ExecutorService executorService() {
//         *     if (executorService == null) {
//         *       executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
//         *           new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
//         *     }
//         *     return executorService;
//         *   }
//         *
//         *   RealCall中内部类 AsyncCall
//         *   class AsyncCall{
//         *      void executeOn(ExecutorService executorService) {
//         *
//         *          // executorService.execute(AsyncCall);
//         *          // 实际：executorService.execute(Runnable 对象);
//         *          executorService.execute(this);  // 执行Runnable的run方法
//         *
//         *          因为 AsyncCall 继承 NamedRunnable 实现了 Runnable，而
//         *          NamedRunnable 类中：
//         *          // AsyncCall extends NamedRunnable implements Runnable{
//         *              run(){
//         *                  execute();
//         *              }
//         *
//         *              // 抽象类
//         *              protected abstract void execute();
//         *          }
//         *
//         *
//         *
//         *          // 因此具体执行了 AsyncCall 的 execute() 方法
//         *
//         *      }
//         *
//         *
//         *      protected void execute() {
//         *          // 拦截器链
//         *          Response response = getResponseWithInterceptorChain();
//         *
//         *          ...
//         *
//         *          // 子线程中，做UI更新操作，必须要切换到主线程中
//         *          Callback.onFailure(RealCall.this, new IOException("Canceled"));
//         *          Callback.onResponse(RealCall.this, response);
//         *
//         *          ...
//         *
//         *          client.dispatcher().finished(this);
//         *      }
//         *
//         *   }
//         *
//         *
//         *
//         *   // finished()方法作用：
//         *   //     (1)将执行完的请求从异步请求队列中移除
//         *   //     (2)调整异步请求队列
//         *   //     (3)重新计算正在执行的异步线程数量，并给 正在执行的线程数量的变量重新赋值
//         *   Dispatcher中，finished()
//         *
//         *
//         */
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, "失败");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, response.body().toString());
//            }
//        });
//
//    }
//
//    /**
//     * 拦截器是OKHttp中提供的一种强大机制，它可以实现网络监听、请求以及响应重写、请求失败重试等功能。
//     *
//     * 拦截器不区分同步和异步。
//     */
//
//
//    /**
//     * 缓存
//     */
//    public void cacheRequest() {
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .cache(new Cache(new File("cache"), 20 * 1024 * 1024))
//                .build();
//
//        // 构建Request，Request携带请求报文信息
//        Request request = new Request.Builder()
//                .url()
//                .method()
//                .header()
//                .build();
//
//        Call call = okHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//
//            Log.e(TAG, response.body().toString());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     * Cache 类 get()方法 取缓存
//     * @param request
//     * @return
//     */
//    Response get(Request request) {
//        String key = key(request.url());
//        DiskLruCache.Snapshot snapshot;
//        Cache.Entry entry;
//        try {
//            snapshot = cache.get(key);
//            if (snapshot == null) {
//                return null;
//            }
//        } catch (IOException e) {
//            // Give up because the cache cannot be read.
//            return null;
//        }
//
//        try {
//            entry = new Cache.Entry(snapshot.getSource(ENTRY_METADATA));
//        } catch (IOException e) {
//            Util.closeQuietly(snapshot);
//            return null;
//        }
//
//        Response response = entry.response(snapshot);
//
//        if (!entry.matches(request, response)) {
//            Util.closeQuietly(response.body());
//            return null;
//        }
//
//        return response;
//    }
//
//
//    /**
//     * Cache 类 put()方法  存缓存
//     * @param response
//     * @return
//     */
//    CacheRequest put(Response response) {
//        String requestMethod = response.request().method();
//
//        // 判断请求是否有效
//        if (HttpMethod.invalidatesCache(response.request().method())) {
//            try {
//                remove(response.request());
//            } catch (IOException ignored) {
//                // The cache cannot be written.
//            }
//            return null;
//        }
//        if (!requestMethod.equals("GET")) {
//            // Don't cache non-GET responses. We're technically allowed to cache
//            // HEAD requests and some POST requests, but the complexity of doing
//            // so is high and the benefit is low.
//
//            // 不缓存非GET方法的响应
//            return null;
//        }
//
//        if (HttpHeaders.hasVaryAll(response)) {
//            return null;
//        }
//
//        // Cache.Entry 包装了要写入缓存的内容
//        Cache.Entry entry = new Cache.Entry(response);
//        // DiskLruCache 用于缓存写入
//        DiskLruCache.Editor editor = null;
//        try {
//            editor = cache.edit(key(response.request().url()));
//            if (editor == null) {
//                return null;
//            }
//
//            // 开始写入缓存
//            entry.writeTo(editor);
//            return new Cache.CacheRequestImpl(editor);
//        } catch (IOException e) {
//            abortQuietly(editor);
//            return null;
//        }
//    }
//
//
//    /**
//     * 连接池
//     */
//
//
//
//    /**
//     * Returns a recycled connection to {@code address}, or null if no such connection exists. The
//     * route is null if the address has not yet been routed.
//     */
//    RealConnection get(Address address, StreamAllocation streamAllocation, Route route) {
//        assert (Thread.holdsLock(this));
//        for (RealConnection connection : connections) {
//            // 判断连接是否可以使用
//            if (connection.isEligible(address, route)) {
//                streamAllocation.acquire(connection, true);
//                return connection;
//            }
//        }
//        return null;
//    }
//
//
//    void put(RealConnection connection) {
//        assert (Thread.holdsLock(this));
//        if (!cleanupRunning) {
//            cleanupRunning = true;
//            executor.execute(cleanupRunnable);
//        }
//        connections.add(connection);
//    }
//
//
//    /**
//     *
//     * StreamAllocation 类中的 acquire
//     * @param connection
//     * @param reportedAcquired
//     */
//
//    public void acquire(RealConnection connection, boolean reportedAcquired) {
//        assert (Thread.holdsLock(connectionPool));
//        if (this.connection != null) throw new IllegalStateException();
//
//        this.connection = connection;
//        this.reportedAcquired = reportedAcquired;
//
//        // 通过 connection.allocations 的大小，判断连接是否超过了最大连接数
//        connection.allocations.add(new StreamAllocation.StreamAllocationReference(this, callStackTrace));
//    }


}
