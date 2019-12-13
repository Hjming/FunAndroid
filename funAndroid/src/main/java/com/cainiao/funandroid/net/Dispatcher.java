package com.cainiao.funandroid.net;
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Deque;
//import java.util.Iterator;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import javax.annotation.Nullable;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.RealCall;
//import okhttp3.RealCall.AsyncCall;
//import okhttp3.Response;
//import okhttp3.internal.Util;

/**
 * Dispatcher：OKHttp的任务调度
 * 作用：
 *    维护请求(同步 和 异步)队列，同时维护一个线程池用于执行异步的网络请求。
 *
 * Policy on when async requests are executed.
 * 关于何时执行异步请求的策略
 *
 * Each dispatcher uses an {@link ExecutorService} to run calls internally.
 * 每个dispatcher在内部使用一个ExecutorService调用请求。
 *
 *
 * If you supply your own executor,
 * it should be able to run {@linkplain #getMaxRequests the configured maximum} number
 * of calls concurrently.
 *
 * 如果提供自己的executor，它能够并发地运行 getMaxRequests()方法配置的最大数量64
 */
public final class Dispatcher {
//    private int maxRequests = 64;   // 最大请求数
//    private int maxRequestsPerHost = 5;
//    private @Nullable Runnable idleCallback;
//
//    /** Executes calls. Created lazily. */
//    private @Nullable ExecutorService executorService;  // 线程池
//
//    /**
//     * 异步请求为什么需要两个队列？生产者 - 消费者模型
//     * Dispatcher 生产者，在主线程中执行
//     * executorService 消费者模型
//     *
//     * 因此需要两个队列，一个执行队列 runningAsyncCalls，一个等待执行队列 readyAsyncCalls
//     */
//
//    /**
//     * 就绪等待缓存的 异步就绪队列
//     *
//     * readyAsyncCalls队列中的线程在什么时候才会被执行？异步调用finished(){
//     *     能够执行 promoteAndExecute() 方法时，readyAsyncCalls队列中的线程就会被执行
//     * }
//     */
//    private final Deque<AsyncCall> readyAsyncCalls = new ArrayDeque<>();
//
//    /** 正在执行的异步请求队列（异步执行队列），包括已经取消但是还没有执行完成的异步请求 */
//    // 还没有结束的异步请求，但是已经先取消了
//    private final Deque<AsyncCall> runningAsyncCalls = new ArrayDeque<>();
//
//
//    /** 正在执行的同步请求队列（同步执行队列），包括已经取消但是还没有执行完成的同步请求 */
//    private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();
//
//    public Dispatcher(ExecutorService executorService) {
//        this.executorService = executorService;
//    }
//
//    public Dispatcher() {
//    }
//
//    public synchronized ExecutorService executorService() {
//        if (executorService == null) {
//            /** 将核心线程池数量设置为0，目的在于，空闲一段时间后，就会将所有的线程全部销毁。*/
//            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
//                    new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
//        }
//        return executorService;
//    }
//
//    /**
//     * Set the maximum number of requests to execute concurrently. Above this requests queue in
//     * memory, waiting for the running calls to complete.
//     *
//     * <p>If more than {@code maxRequests} requests are in flight when this is invoked, those requests
//     * will remain in flight.
//     */
//    public void setMaxRequests(int maxRequests) {
//        if (maxRequests < 1) {
//            throw new IllegalArgumentException("max < 1: " + maxRequests);
//        }
//        synchronized (this) {
//            this.maxRequests = maxRequests;
//        }
//        promoteAndExecute();
//    }
//
//    public synchronized int getMaxRequests() {
//        return maxRequests;
//    }
//
//    /**
//     * Set the maximum number of requests for each host to execute concurrently. This limits requests
//     * by the URL's host name. Note that concurrent requests to a single IP address may still exceed
//     * this limit: multiple hostnames may share an IP address or be routed through the same HTTP
//     * proxy.
//     *
//     * <p>If more than {@code maxRequestsPerHost} requests are in flight when this is invoked, those
//     * requests will remain in flight.
//     *
//     * <p>WebSocket connections to hosts <b>do not</b> count against this limit.
//     */
//    public void setMaxRequestsPerHost(int maxRequestsPerHost) {
//        if (maxRequestsPerHost < 1) {
//            throw new IllegalArgumentException("max < 1: " + maxRequestsPerHost);
//        }
//        synchronized (this) {
//            this.maxRequestsPerHost = maxRequestsPerHost;
//        }
//        promoteAndExecute();
//    }
//
//    public synchronized int getMaxRequestsPerHost() {
//        return maxRequestsPerHost;
//    }
//
//    /**
//     * Set a callback to be invoked each time the dispatcher becomes idle (when the number of running
//     * calls returns to zero).
//     *
//     * <p>Note: The time at which a {@linkplain Call call} is considered idle is different depending
//     * on whether it was run {@linkplain Call#enqueue(Callback) asynchronously} or
//     * {@linkplain Call#execute() synchronously}. Asynchronous calls become idle after the
//     * {@link Callback#onResponse onResponse} or {@link Callback#onFailure onFailure} callback has
//     * returned. Synchronous calls become idle once {@link Call#execute() execute()} returns. This
//     * means that if you are doing synchronous calls the network layer will not truly be idle until
//     * every returned {@link Response} has been closed.
//     */
//    public synchronized void setIdleCallback(@Nullable Runnable idleCallback) {
//        this.idleCallback = idleCallback;
//    }
//
//    /** 异步请求调用 */
//    void enqueue(AsyncCall call) {
//        synchronized (this) {
//            readyAsyncCalls.add(call);
//        }
//        promoteAndExecute();
//    }
//
//    /**
//     * Cancel all calls currently enqueued or executing. Includes calls executed both {@linkplain
//     * Call#execute() synchronously} and {@linkplain Call#enqueue asynchronously}.
//     */
//    public synchronized void cancelAll() {
//        for (AsyncCall call : readyAsyncCalls) {
//            call.get().cancel();
//        }
//
//        for (AsyncCall call : runningAsyncCalls) {
//            call.get().cancel();
//        }
//
//        for (RealCall call : runningSyncCalls) {
//            call.cancel();
//        }
//    }
//
//    /**
//     * Promotes eligible calls from {@link #readyAsyncCalls} to {@link #runningAsyncCalls} and runs
//     * them on the executor service. Must not be called with synchronization because executing calls
//     * can call into user code.
//     *
//     * @return true if the dispatcher is currently running calls.
//     */
//    private boolean promoteAndExecute() {
//        assert (!Thread.holdsLock(this));
//
//        List<AsyncCall> executableCalls = new ArrayList<>();
//        boolean isRunning;
//        synchronized (this) {
//            for (Iterator<AsyncCall> i = readyAsyncCalls.iterator(); i.hasNext(); ) {
//                AsyncCall asyncCall = i.next();
//
//                if (runningAsyncCalls.size() >= maxRequests) break; // Max capacity.
//                if (runningCallsForHost(asyncCall) >= maxRequestsPerHost) continue; // Host max capacity.
//
//                i.remove();
//                executableCalls.add(asyncCall);
//                runningAsyncCalls.add(asyncCall);
//            }
//            isRunning = runningCallsCount() > 0;
//        }
//
//        for (int i = 0, size = executableCalls.size(); i < size; i++) {
//            AsyncCall asyncCall = executableCalls.get(i);
//            asyncCall.executeOn(executorService());
//        }
//
//        return isRunning;
//    }
//
//    /** Returns the number of running calls that share a host with {@code call}. */
//    private int runningCallsForHost(AsyncCall call) {
//        int result = 0;
//        for (AsyncCall c : runningAsyncCalls) {
//            if (c.get().forWebSocket) continue;
//            if (c.host().equals(call.host())) result++;
//        }
//        return result;
//    }
//
//    /** 同步调用，将同步请求直接加入 同步执行请求队列 */
//    synchronized void executed(RealCall call) {
//        runningSyncCalls.add(call);
//    }
//
//    /** 标志异步AsyncCall.execute(){ run() }请求结束 */
//    // AsyncCall.execute() 执行完成后，肯定是要在runningAsyncCalls中移除这个异步线程AsyncCall
//    // 否则，处于就绪的异步队列就无法获得执行
//    // 异步调用
//    void finished(AsyncCall call) {
//        finished(runningAsyncCalls, call);
//    }
//
//    /** 标志同步Call.execute()请求结束*/
//    // 同步调用
//    void finished(RealCall call) {
//        finished(runningSyncCalls, call);
//    }
//
//    private <T> void finished(Deque<T> calls, T call) {
//        Runnable idleCallback;
//        synchronized (this) {
//            if (!calls.remove(call)) throw new AssertionError("Call wasn't in-flight!");
//            idleCallback = this.idleCallback;
//        }
//
//        boolean isRunning = promoteAndExecute();
//
//        if (!isRunning && idleCallback != null) {
//            idleCallback.run();
//        }
//    }
//
//    /** Returns a snapshot of the calls currently awaiting execution. */
//    public synchronized List<Call> queuedCalls() {
//        List<Call> result = new ArrayList<>();
//        for (AsyncCall asyncCall : readyAsyncCalls) {
//            result.add(asyncCall.get());
//        }
//        return Collections.unmodifiableList(result);
//    }
//
//    /** Returns a snapshot of the calls currently being executed. */
//    public synchronized List<Call> runningCalls() {
//        List<Call> result = new ArrayList<>();
//        result.addAll(runningSyncCalls);
//        for (AsyncCall asyncCall : runningAsyncCalls) {
//            result.add(asyncCall.get());
//        }
//        return Collections.unmodifiableList(result);
//    }
//
//    public synchronized int queuedCallsCount() {
//        return readyAsyncCalls.size();
//    }
//
//    public synchronized int runningCallsCount() {
//        return runningAsyncCalls.size() + runningSyncCalls.size();
//    }
}
