package com.cainiao.funandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cainiao.funandroid.R;
import com.cainiao.funandroid.model.RxjavaDataMo;
import com.cainiao.funcommonlibrary.util.LogUtil;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxJavaActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        // RxJava 基本使用
        // dobaseUse();

        /**
         Rxjava 基于事件流的链式调用
         逻辑简洁
         实现优雅
         使用简单
         */
        // doChain();

        // 源码分析
        sourceAnalysis();


    }

    private void sourceAnalysis() {

        /*

         public interface ObservableOnSubscribe<T> {
            // subscribe()方法 接收一个 ObservableEmitter （被观察者 发射器）接口类型对象实例
            // ObservableEmitter 发送事件 是安全可取消的方式
            // 只要 观察者 订阅了 被观察者，ObservableEmitter 发射器就会被调用

            void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception;
         }


         // 接收了一个 ObservableOnSubscribe 接口类型对象 source
         public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {

            // 返回一个 ObservableCreate 实例
            return RxJavaPlugins.onAssembly(new ObservableCreate<T>(source));
         }


        public final class ObservableCreate<T> extends Observable<T> {
            final ObservableOnSubscribe<T> source;

            // 构造参数
            public ObservableCreate(ObservableOnSubscribe<T> source) {
                this.source = source;
            }


            // 回调，在 观察者 订阅了 被观察者 时，回调subscribeActual() 这个函数
            @Override
            protected void subscribeActual(Observer<? super T> observer) {

                // 创建了一个事件发射器 发射事件
                // 因为 CreateEmitter 实现了 Disposable 接口，
                // 因此 这里既是一个ObservableEmitter接口实例，
                // 也是 将 observer观察者 封装成了 Disposable的一个接口实现实例
                CreateEmitter<T> parent = new CreateEmitter<T>(observer);

                // 回调 给 创建 观察者时 的 onSubscribe(Disposable d)方法
                observer.onSubscribe(parent);

                try {

                    // 将 ObservableEmitter 实例传递给 ObservableOnSubscribe 接口
                    // 的 void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception;方法

                    // 最后回调给，创建 被观察者 时重写的
                    // subscribe(ObservableEmitter<RxjavaDataMo> emitter) throws Exception 方法

                    source.subscribe(parent);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    parent.onError(ex);
                }
            }


            // ObservableCreate 内部类；CreateEmitter 实现了 ObservableEmitter 和 Disposable
            static final class CreateEmitter<T> extends AtomicReference<Disposable>
                implements ObservableEmitter<T>, Disposable {

                final Observer<? super T> observer;

                CreateEmitter(Observer<? super T> observer) {
                    this.observer = observer;
                }

                @Override
                public void onNext(T t) {
                    if (t == null) {    // 发送的事件不可以为空
                        onError(new NullPointerException("onNext called with null.
                            Null values are generally not allowed in 2.x operators and sources."));
                        return;
                    }

                    // 若观察者 没有取消订阅 被观察者，则 继续执行 观察者的 onNext(T t)方法，否则不再执行
                    if (!isDisposed()) {
                        // 被观察者 调用 观察者 onNext(T t)方法
                        observer.onNext(t);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    if (!tryOnError(t)) {
                        RxJavaPlugins.onError(t);
                    }
                }

                @Override
                public boolean tryOnError(Throwable t) {
                    if (t == null) {
                        t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
                    }
                    if (!isDisposed()) {
                        try {

                            // 被观察者 调用 观察者 onError(Throwable t)方法
                            observer.onError(t);
                        } finally {
                            dispose();
                        }
                        return true;
                    }
                    return false;
                }

                @Override
                public void onComplete() {
                    if (!isDisposed()) {
                        try {
                            // 被观察者 调用 观察者 onComplete()方法
                            observer.onComplete();
                        } finally {
                            dispose();
                        }
                    }
                }

                @Override
                public void setDisposable(Disposable d) {
                    DisposableHelper.set(this, d);
                }

                @Override
                public void setCancellable(Cancellable c) {
                    setDisposable(new CancellableDisposable(c));
                }

                @Override
                public ObservableEmitter<T> serialize() {
                    return new SerializedEmitter<T>(this);
                }

                @Override
                public void dispose() {
                    DisposableHelper.dispose(this);
                }

                @Override
                public boolean isDisposed() {
                    return DisposableHelper.isDisposed(get());
                }

                @Override
                public String toString() {
                    return String.format("%s{%s}", getClass().getSimpleName(), super.toString());
                }



            }


        }

         */


        // 创建被观察者 - 定义需发送的事件
        /*
        创建被观察者 本质 - 创建了 ObservableCreate对象 ，重写 了 观察者 订阅 被观察者的回调方法 subscribeActual()
        同时 在观察者 和 被观察者没有去掉订阅时，在被观察者的 onNext()、onError()、onComplete()方法中依次调用
        观察者的 onNext()、onError()、onComplete()方法

        subscribeActual()方法内部 创建了 事件发射器 ObservableEmitter ，然后将同一个事件发射器 传递给了 观察者 和 被观察者
        回调给 创建观察者时的 onSubscribe(Disposable d)方法
        和 创建被观察者时的 subscribe(ObservableEmitter<RxjavaDataMo> emitter) 方法
        这样保证了，事件的发射 和 接收都是同一个事件发射器

        // subscribeActual(Observer<? super T> observer) 只有在 观察者 订阅 被观察者时，才会被调用
        @Override
        protected void subscribeActual(Observer<? super T> observer) {

            // 创建了一个事件发射器 发射事件
            // 因为 CreateEmitter 实现了 Disposable 接口，
            // 因此 这里既是一个ObservableEmitter接口实例，
            // 也是 将 observer观察者 封装成了 Disposable的一个接口实现实例
            CreateEmitter<T> parent = new CreateEmitter<T>(observer);
            observer.onSubscribe(parent);

            try {
                source.subscribe(parent);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                parent.onError(ex);
            }
        }

        */
        Observable<RxjavaDataMo> observable = Observable.create(new ObservableOnSubscribe<RxjavaDataMo>() {
            @Override
            public void subscribe(ObservableEmitter<RxjavaDataMo> emitter) throws Exception {
                // 传递事件
                for (int i = 0; i < 5; i++) {
                    RxjavaDataMo rxjavaDataMo = new RxjavaDataMo("千空" + i, 3000);
                    emitter.onNext(rxjavaDataMo);
                }

                emitter.onComplete();

            }
        });


        // 创建 观察者 - 接收并响应事件
        /*

         public interface Observer<T> { // 接口
            void onSubscribe(@NonNull Disposable d);
            void onNext(@NonNull T t);
            void onError(@NonNull Throwable e);
            void onComplete();
         }

         */


        Observer<RxjavaDataMo> observer = new Observer<RxjavaDataMo>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RxjavaDataMo rxjavaDataMo) {
                LogUtil.e(TAG, "接收到传递的事件姓名 ：" + rxjavaDataMo.getName() + " ； " +
                        "接收到传递事件的年龄 ：" + rxjavaDataMo.getAge());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                LogUtil.e(TAG, "事件接收完成 onComplete()");
            }
        };

        // 简历订阅关系
        LogUtil.e(TAG, "创建关系返回一个 ObservableEmitter接口实现对象 source");

        /*

         说明：当观察者 订阅 被观察者 ，这个时候，ObservableEmitter 被观察者发射器会被调用

         public abstract class Observable<T> implements ObservableSource<T> {

             @Override
             public final void subscribe(Observer<? super T> observer) {
                // 回调给 真正创建 观察者时的 ObservableCreate

                // ObservableCreate重写了：
                // protected void subscribeActual(Observer<? super T> observer)
                // 这里面 回调了 观察者的 onSubscribe(）方法 和 被观察者的 subscribe()方法
                // observer.onSubscribe(parent);  和 source.subscribe(parent);


                subscribeActual(observer);
             }

             // 抽象方法 创建 观察者时真正实现这个方法
             protected abstract void subscribeActual(Observer<? super T> observer);

         }


         */
        observable.subscribe(observer);

    }

    private void doChain() {
        /**
         * 整体方法调用顺序：
         *  观察者 onSubscribe() > 被观察者 subscribe() 发送事件 > 观察者 onNext() 接收事件
         *  > 观察者 onComplete() > 被观察者 onComplete() 结束事件
         */
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                LogUtil.e(TAG, "被观察者 subscribe() 发送事件");
                emitter.onNext("第（1）段文案");
                emitter.onNext("第（2）段文案");
                emitter.onComplete();
                LogUtil.e(TAG, "被观察者 onComplete() 结束事件");

            }
        })
                .subscribe(new Observer<String>() { // subscribe()方法 连接被观察者 和 观察者
                    @Override
                    public void onSubscribe(Disposable d) {
                        Disposable disposable = d;
                        LogUtil.e(TAG, "观察者 onSubscribe()");
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtil.e(TAG, "观察者 onNext() 接收事件 :" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "观察者 onError():" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "观察者 onComplete()");
                    }
                });
    }

    private void dobaseUse() {
        // 一、创建 被观察者
        // 创建 被观察者 1  <T> 要操作对象的类型
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                /**
                 * ObservableEmitter<T> 泛型 是操作事件的类型 ，事件发射器
                 *
                 * 定义需要发送的事件，向观察者发送事件
                 *
                 */
                emitter.onNext("Hello");
                emitter.onNext("create()");

                // emitter.onError(new Exception("发生错误了"));

                // 被观察者 可以不发送 onComplete()事件，观察者依然会顺序收到onNext()事件
                emitter.onComplete();

                /**
                 * 由于 被观察者 已经发送了onComplete()事件，下面的两个事件 观察者不再会接收到
                 */
                emitter.onNext("继续事件1");
                emitter.onNext("继续事件2");
            }
        });

        // 创建 被观察者 2
        Observable<String> observable2 = Observable.just("Hello,just()");


        // 创建 被观察者 3
        String[] strArrays = new String[]{"Hello", "from()"};
        Observable<String> observable3 = Observable.fromArray(strArrays);

        // 创建 被观察者的新实现：Flowable 来支持背压Backpressure


        // 二、创建 观察者
        Observer<String> observer = new Observer<String>() {

            /**
             * 观察者接收事件前，默认最先调用 onSubscribe() ，适合做初始化工作
             * Disposable ：被观察者 和 观察者 之间的关系开关
             * @param d
             */
            @Override
            public void onSubscribe(Disposable d) {
                Disposable disposable = d;
                LogUtil.e(TAG, "onSubscribe()");

                // 切断 被观察者 与 观察者之间的订阅
                // 观察者 无法继续 接收 被观察者的事件，但被观察者还是可以继续发送事件
                // disposable.dispose();
            }

            // 被观察者可以发无限个 onNext(）事件，观察者可以依次按照发送的顺序接收无限个 onNext(）事件
            @Override
            public void onNext(String s) {
                LogUtil.e(TAG, "onNext():" + s);
            }

            /** onError() 和 onComplete() 方法互斥，只能有其一 */
            // 事件队列 异常，队列会自动终止，不再允许有事件发出
            // 当被观察者发送 Error事件，观察者接收到时，会调用该方法 进行响应
            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, "onError():" + e.getMessage());
            }

            // 事件队列 结束 ，标志：观察者不再发送onNext(）事件
            // 当被观察者发送 Complete事件，观察者接收到时，会调用该方法 进行响应
            @Override
            public void onComplete() {
                LogUtil.e(TAG, "onComplete()");
            }
        };


        // 三、创建订阅关系
        observable1.subscribe(observer);
        // observable2.subscribe(observer);
        // observable3.subscribe(observer);

        /**
         被观察者 Observable 的 subscribe()具备多个重载的方法：

         // 表示观察者不对被观察者发送的事件作出任何响应（但被观察者还是可以继续发送事件）
         public final Disposable subscribe() {}

         // 表示观察者只对被观察者发送的Next事件作出响应
         public final Disposable subscribe(Consumer<? super T> onNext) {}

         // 表示观察者只对被观察者发送的Next事件 & Error事件作出响应
         public final Disposable subscribe(Consumer<? super T> onNext,
         Consumer<? super Throwable> onError) {}


         // 表示观察者只对被观察者发送的Next事件、Error事件 & Complete事件作出响应
         public final Disposable subscribe(Consumer<? super T> onNext,
         Consumer<? super Throwable> onError, Action onComplete) {}

         // 表示观察者只对被观察者发送的Next事件、Error事件 、Complete事件 & onSubscribe事件作出响应
         public final Disposable subscribe(Consumer<? super T> onNext,
         Consumer<? super Throwable> onError, Action onComplete,
         Consumer<? super Disposable> onSubscribe) {}

         // 表示观察者对被观察者发送的任何事件都作出响应
         public final void subscribe(Observer<? super T> observer) {}

         */

    }
}
