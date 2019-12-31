package com.cainiao.funandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.cainiao.funandroid.R;
import com.cainiao.funandroid.model.RxjavaDataMo;
import com.cainiao.funcommonlibrary.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class RxJavaOperatorActivity extends AppCompatActivity {
    private static final String TAG = "RxJavaOperatorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_operator);

        // createOperator();   // 创建操作符 - 网络请求轮询

        // transformOperator();    // 变换操作符 - 网络请求嵌套回调

        // filterOperator();   // 过滤操作符 - 功能防抖、联想搜索优化

        combineMerge(); // 组合合并操作符 - 从磁盘 / 内存缓存中 获取缓存数据、合并数据源、联合判断
    }

    private void combineMerge() {
        /*
         startWith() — 在数据序列的开头增加一项数据
         merge() — 将多个Observable合并为一个
         mergeDelayError() — 合并多个Observables，让没有错误的Observable都完成后再发射错误通知
         zip() — 使用一个函数,组合多个Observable发射的数据集合，然后再发射这个结果
         and(), then(), and when() — (rxjava-joins 包中) 通过模式和计划组合多个Observables发射的数据集合
         combineLatest() — 当两个Observables中的任何一个发射了一个数据时，通过一个指定的函数组合每个Observable发射的最新数据（一共两个数据），然后发射这个函数的结果
         join() and groupJoin() — 无论何时，如果一个Observable发射了一个数据项，只要在另一个Observable发射的数据项定义的时间窗口内，就将两个Observable发射的数据合并发射
         switchOnNext() — 将一个发射Observables的Observable转换成另一个Observable，后者发射这些Observables最近发射的数据
         */

        // 1、concat() / concatArray()
        // concat() 发送的被观察者数量 <= 4  ;   concatArray() > 4
        LogUtil.e(TAG,"concat()操作符");
        Observable.concat(Observable.just(1, 2, 3, 4),
                Observable.just(5, 6, 7, 8),
                Observable.just(9, 10, 11, 12),
                Observable.just(6, 6, 6, 8))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        LogUtil.e(TAG,"concatArray()操作符");

        LogUtil.e(TAG,"zip()操作符");
        /*
         合并 多个被观察者（Observable）发送的事件，生成一个新的事件序列
         严格按照原先事件序列 进行对位合并
         没有合并的事件，在被观察者没有执行onComplete()方法时，仍然会继续被发送，否则不会发送


         最终合并的事件数量 是 多个被观察者（Observable）中最少的事件数量
         */

    }

    private void filterOperator() {
        filter();
    }

    private void filter() {
        LogUtil.e(TAG, "filter() 过滤 特定条件的事件");
        Observable.just(1, 2, 3, 4, 5)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 4;
                    }
                }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                LogUtil.e(TAG, "过滤后得到的事件是：" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        LogUtil.e(TAG, "ofType() 只发射 指定的数据类型");

        Observable.just(1, "2字符串", 3, "4字符串", new RxjavaDataMo("千空", 3000), 6)
                .ofType(RxjavaDataMo.class) // 获取到只是 RxjavaDataMo 类型的数据
                .subscribe(new Observer<RxjavaDataMo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RxjavaDataMo rxjavaDataMo) {
                        // ofType()过滤后得到的值是：千空:3000
                        LogUtil.e(TAG, "ofType()过滤后得到的值是：" + rxjavaDataMo.getName()
                                + ":" + rxjavaDataMo.getAge());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        LogUtil.e(TAG, "skip() / skipLast() 跳过指定的事件");
        /*
         skip() 跳过开始的N项数据
         skipLast() 跳过最后的N项数据
         */
        Observable.just(1, 2, 3, 4, 5, 6)
                .skip(1)
                .skipLast(2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        /*
                         skip() + skipLast()过滤后得到的值是：2
                         skip() + skipLast()过滤后得到的值是：3
                         skip() + skipLast()过滤后得到的值是：4
                         */
                        LogUtil.e(TAG, "skip() + skipLast()过滤后得到的值是：" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        LogUtil.e(TAG, "distinct() / distinctUntilChanged() 过滤重复的数据");
        /*
         distinct() — 过滤掉重复数据
         distinctUntilChanged() — 过滤掉连续重复的数据
         */

        RxjavaDataMo rxjavaDataMo1 = new RxjavaDataMo("千空", 3000);
        RxjavaDataMo rxjavaDataMo2 = new RxjavaDataMo("索隆", 21);
        RxjavaDataMo rxjavaDataMo3 = new RxjavaDataMo("索隆", 21);
        RxjavaDataMo rxjavaDataMo4 = new RxjavaDataMo("香吉士", 21);
        RxjavaDataMo rxjavaDataMo5 = new RxjavaDataMo("娜美", 18);
        RxjavaDataMo rxjavaDataMo6 = new RxjavaDataMo("乔巴", 16);
        ArrayList<RxjavaDataMo> rxjavaDataMos = new ArrayList<>();

        rxjavaDataMos.add(rxjavaDataMo1);
        rxjavaDataMos.add(rxjavaDataMo2);
        rxjavaDataMos.add(rxjavaDataMo3);
        rxjavaDataMos.add(rxjavaDataMo4);
        rxjavaDataMos.add(rxjavaDataMo5);
        rxjavaDataMos.add(rxjavaDataMo6);

        Observable.fromIterable(rxjavaDataMos)
                .concatMap(new Function<RxjavaDataMo, ObservableSource<RxjavaDataMo>>() {
                    @Override
                    public ObservableSource<RxjavaDataMo> apply(RxjavaDataMo rxjavaDataMo) throws Exception {

                        ArrayList<RxjavaDataMo> rxjavaDataMos2 = new ArrayList<>();

                        if (rxjavaDataMo.getName() != rxjavaDataMo.getName()
                                && rxjavaDataMo.getAge() != rxjavaDataMo.getAge()) {
                            rxjavaDataMos2.add(rxjavaDataMo);
                        }

                        return Observable.fromIterable(rxjavaDataMos2);
                    }
                })
                .distinct()
                .toList()
                .toObservable()
                .subscribe(new Observer<List<RxjavaDataMo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<RxjavaDataMo> rxjavaDataMos) {
                        int size = rxjavaDataMos.size();
                        LogUtil.e(TAG, "distinct()过滤重复数据后得到List大小：" + size);

                        for (RxjavaDataMo rxjavaDataMo : rxjavaDataMos) {
                            LogUtil.e(TAG, "distinct()过滤重复数据后得到的值是：" + "姓名：" + rxjavaDataMo.getName()
                                    + "；年龄：" + rxjavaDataMo.getAge());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        LogUtil.e(TAG, "take() / last() / takeLast() / takeLastBuffer() 发射数据");
        /*

         take() — 只发射开始的N项数据
         takeLast() — 只发射最后的N项数据
         takeLastBuffer() — 将最后的N项数据当做单个数据发射

         last() — 只发射最后的一项数据
         lastOrDefault() — 只发射最后的一项数据，如果Observable为空就发射默认值

         first() / takeFirst( ) — 只发射第一项数据，或者满足某种条件的第一项数据
         firstOrDefault() — 只发射第一项数据，如果Observable为空就发射默认值
         */

        /*
         elementAt() — 发射第N项数据
         elementAtOrDefault() — 发射第N项数据，如果Observable数据少于N项就发射默认值
         */


        /*
         sample() / throttleLast() — 定期发射Observable最近的数据
         throttleFirst() — 定期发射Observable发射的第一项数据
         throttleWithTimeout() / debounce() — 只有当Observable在指定的时间后还没有发射数据时，才发射一个数据
         */

        /*
         timeout() — 如果在一个指定的时间段后还没发射数据，就发射一个异常
         ignoreElements() — 丢弃所有的正常数据，只发射错误或完成通知
        */
    }

    private void transformOperator() {

        // buffer();
        // map();
        // flatMap();
        // concatMap();
    }

    private void buffer() {
    /*
     定期从 被观察者（Obervable）发送的事件中 获取一定数量的事件放到一个缓存数据包中，
     然后发送这个缓存数据包，而不是一次发射一个值。

     buffer(count, skip) 方法
        以List的形式发射缓存包，取决于count和skip的值，
     这些缓存可能会有重叠部分（比如skip < count时），也可能会有间隙（比如skip > count时）
     */
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                // 在数据充足时，总是保证缓存区的大小是给定的值
                // 在开始新的缓冲区之前，应该跳过源ObservableSource发出的多少项
                .buffer(4, 2)
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    /**
                     @param integers 缓存区中的数量
                     */
                    @Override
                    public void onNext(List<Integer> integers) {
                    /*
                     缓存区数据包的大小 = ：4
                        缓存区中的数据：1
                        缓存区中的数据：2
                        缓存区中的数据：3
                        缓存区中的数据：4
                     缓存区数据包的大小 = ：4
                        缓存区中的数据：3
                        缓存区中的数据：4
                        缓存区中的数据：5
                        缓存区中的数据：6
                     缓存区数据包的大小 = ：4
                        缓存区中的数据：5
                        缓存区中的数据：6
                        缓存区中的数据：7
                        缓存区中的数据：8
                     缓存区数据包的大小 = ：4
                        缓存区中的数据：7
                        缓存区中的数据：8
                        缓存区中的数据：9
                        缓存区中的数据：10
                     缓存区数据包的大小 = ：2
                        缓存区中的数据：9
                        缓存区中的数据：10
                     */
                        // 缓存区数据包的大小
                        int size = integers.size();
                        LogUtil.e(TAG, "缓存区数据包的大小 = ：" + size);
                        for (Integer integer : integers) {
                            LogUtil.e(TAG, "缓存区中的数据：" + integer);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void map() {
        /*
         map事件转换符，可以将原始Observable发射的每一项数据应用一个指定的函数，从而变换成另外一种数据
         即，将被观察者发送的事件转换为任意的类型事件

         应用场景:数据类型转换
         */
        // 正常，没有进行转换
        int[] intArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Observable.fromArray(intArray)
                .subscribe(new Observer<int[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(int[] ints) {
                        for (int i = 0; i < ints.length; i++) {
                            LogUtil.e(TAG, "接收到：" + i);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        // 将 int[]数组，转换为 List<RxjavaDataMo> 集合
        Observable.fromArray(intArray)
                .map(new Function<int[], List<RxjavaDataMo>>() {
                    @Override
                    public List<RxjavaDataMo> apply(int[] ints) throws Exception {
                        String name = Thread.currentThread().getName();
                        LogUtil.e(TAG, "发送数据的线程：" + name);
                        List<RxjavaDataMo> rxjavaDataMos = new ArrayList<>();

                        for (int i = 0; i < intArray.length; i++) {
                            RxjavaDataMo rxjavaDataMo = new RxjavaDataMo("数据" + i, i);
                            rxjavaDataMos.add(rxjavaDataMo);
                        }

                        return rxjavaDataMos;
                    }
                }).subscribe(new Observer<List<RxjavaDataMo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<RxjavaDataMo> rxjavaDataList) {
                if (rxjavaDataList != null) {
                    String name = Thread.currentThread().getName();
                    LogUtil.e(TAG, "接收数据的线程：" + name);
                    for (RxjavaDataMo rxjavaDataMo : rxjavaDataList) {
                        LogUtil.e(TAG, "接收到的数据：" + rxjavaDataMo.getName() + " = " + rxjavaDataMo.getAge());
                    }
                } else {
                    LogUtil.e(TAG, "接收到的数据为空");
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                LogUtil.e(TAG, "onComplete()");
            }
        });


    }


    private void flatMap() {
        /*
         FlatMap将一个发射数据的Observable变换为多个 Observables ，然后再将它们发射的数据合并后放进一个单独的Observable
         即，FlatMap将被观察者发送的事件序列进行 拆分、单独转换，再合并成一个新的事件序列进行发送

         应用场景:无序的将被观察者发送的整个事件序列进行变换
         */
        int[] intArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        Observable.fromArray(intArray)
                .flatMap(new Function<int[], ObservableSource<List<RxjavaDataMo>>>() {  // 将 int[] 转换为 List<RxjavaDataMo>

                    @Override
                    public ObservableSource<List<RxjavaDataMo>> apply(int[] ints) throws Exception {

                        List<RxjavaDataMo> rxjavaDataMoList = new ArrayList<>();

                        // 将前5个 转换为 List<RxjavaDataMo> 数组
                        for (int i = 0; i < 5; i++) {
                            RxjavaDataMo rxjavaDataMo = new RxjavaDataMo("数据" + i, ints[i]);
                            rxjavaDataMoList.add(rxjavaDataMo);
                        }

                        return Observable.fromArray(rxjavaDataMoList);
                    }
                }).flatMap(new Function<List<RxjavaDataMo>, ObservableSource<String>>() {   // 将 List<RxjavaDataMo> 转换为 String
            @Override
            public ObservableSource<String> apply(List<RxjavaDataMo> rxjavaDataMoList) throws Exception {

        /*
         传来的是 第一次 经过flatMap 转换过的 数据 List<RxjavaDataMo>
         将这个List<RxjavaDataMo>数据再次 转换为 String 类型，依次发送
         */
                int size = rxjavaDataMoList.size();
                // 经过第一次 flatMap 转换得到的数据大小 = 5
                LogUtil.e(TAG, "经过第一次 flatMap 转换得到的数据大小 = " + size);

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    String s = rxjavaDataMoList.get(i).getName()
                            + "，值：" + rxjavaDataMoList.get(i).getAge() + "；";
                    stringBuilder.append(s);
                }

                return Observable.just(stringBuilder.toString());

            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                // 经过flatMap 转换得到的数据为：数据0，值：0；数据1，值：1；数据2，值：2；数据3，值：3；数据4，值：4；
                LogUtil.e(TAG, "经过flatMap 转换得到的数据为：" + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void concatMap() {
        int[] intArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        Observable.fromArray(intArray)
                .concatMap(new Function<int[], ObservableSource<List<RxjavaDataMo>>>() {
                    @Override
                    public ObservableSource<List<RxjavaDataMo>> apply(int[] ints) throws Exception {
                        List<RxjavaDataMo> rxjavaDataMoList = new ArrayList<>();

                        // 将前5个 转换为 List<RxjavaDataMo> 数组
                        for (int i = 0; i < 5; i++) {
                            RxjavaDataMo rxjavaDataMo = new RxjavaDataMo("数据" + i, ints[i]);
                            rxjavaDataMoList.add(rxjavaDataMo);
                        }

                        return Observable.fromArray(rxjavaDataMoList);
                    }
                }).concatMap(new Function<List<RxjavaDataMo>, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(List<RxjavaDataMo> rxjavaDataMoList) throws Exception {
                int size = rxjavaDataMoList.size();
                // 经过第一次 concatMap 转换得到的数据大小 = 5
                LogUtil.e(TAG, "经过第一次 concatMap 转换得到的数据大小 = " + size);

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    String s = rxjavaDataMoList.get(i).getName()
                            + "，值：" + rxjavaDataMoList.get(i).getAge() + "；";
                    stringBuilder.append(s);
                }

                return Observable.just(stringBuilder.toString());
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                // 经过concatMap 转换得到的数据为：数据0，值：0；数据1，值：1；数据2，值：2；数据3，值：3；数据4，值：4；
                LogUtil.e(TAG, "经过concatMap 转换得到的数据为：" + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


    private void createOperator() {
        /**
         一、创建操作符
         */
        /* 快速创建 start */
        // just();
        // fromArray();
        // fromIterable();

        /**
         empty() : 仅发送Complete事件，直接通知完成
         error() : 仅发送Error事件，直接通知异常
         never() : 不发送任何事件
         */

        /* 快速创建 end */


        /* 延迟 创建 start       网络请求轮询 */

        // defer();
        // timer();
        // interval();
        // intervalRange();
        // range();
        // rangeLong();

        /* 延迟 创建 end */
    }

    /*
     与 range() 一样，只是发送的数据类型是Long类型
     */
    private void rangeLong() {

    }

    /*
     与 intervalRange 一样，只是没有延迟发送事件的功能
     */
    private void range() {
        Observable.range(5, 2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        /**
                         5收到
                         6收到
                         */
                        LogUtil.e(TAG, integer + "收到");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*
     创建一个按固定时间间隔,发射一个可指定的整数序列的Observable
     intervalRange 也是发送一个无限递增的整数序列，但是可以指定发送的整数序列
     */
    private void intervalRange() {
        Observable.intervalRange(5, 2, 1, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        /**
                         5收到
                         6收到
                         */
                        LogUtil.e(TAG, aLong + "收到");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     @param start        事件序列起始点
     @param count        发送事件的个数
     @param initialDelay 第1次事件延迟发送时间
     @param period       时间间隔
     @param unit         时间单位
     @return
     */
    public static Observable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit) {

        return null;
    }

    /*
     创建一个按固定时间间隔,发射一个无限递增的整数序列的Observable
     */
    Disposable mDisposable = null;

    private void interval() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (aLong == 6) {
                            if (mDisposable != null && !mDisposable.isDisposed()) {
                                mDisposable.dispose();
                            }
                        }
                        LogUtil.e(TAG, aLong + "收到");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void timer() {
        // 延迟指定时间后，发送1个值为0的Long类型数
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtil.e(TAG, aLong + "收到");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*
     defer 操作符会等待，直到有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable

     使用 Observable工厂方法动态创建被观察者对象（Observable） & 发送事件
     每次订阅，都会得到一个新的刚创建的的Observable对象，确保Observable对象里的数据是最新的

     应用场景
     动态创建被观察者对象（Observable） & 获取最新的Observable对象数据
     */
    Integer integer = 10;

    private void defer() {

        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(integer);
            }
        });


        integer = 15;

        observable.subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.e(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(Integer value) {
                // 接收到的是：15
                LogUtil.e(TAG, "接收到的是：" + value);
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                LogUtil.e(TAG, "对Complete事件作出响应");
            }
        });


    }

    /*
     直接发送 传入的集合List数据
     */
    private void fromIterable() {
        // 创建一个集合
        List<String> stringList = new ArrayList<>();
        stringList.add("Hello");
        stringList.add("Rxjava");
        stringList.add("fromIterable() 创建操作符");

        Observable.fromIterable(stringList)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    // 直接 传来的是已经 遍历过的 集合元素
                    @Override
                    public void onNext(String s) {
                        LogUtil.e(TAG, s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /*
     fromArray() 创建操作符
     直接发送 传入的数组数据，将数组中的数据转换为Observable对象
     */
    private void fromArray() {
        // 设置一个数组
        String[] strArray = new String[]{"Hello", "Rxjava", "fromArray() 创建操作符"};
        LogUtil.e(TAG, "传递一个数组");
        Observable.fromArray(strArray)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        /*
                         输出：
                         Hello
                         Rxjava
                         fromArray()
                         */
                        LogUtil.e(TAG, s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete()");
                    }
                });


        // 创建一个集合
        List<String> stringList = new ArrayList<>();
        stringList.add("List集合");
        stringList.add("Hello");
        stringList.add("Rxjava");
        stringList.add("fromArray() 创建操作符");

        LogUtil.e(TAG, "传递一个List集合");
        Observable.fromArray(stringList)
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                         /*
                         输出：
                         List集合
                         Hello
                         Rxjava
                         fromArray()
                         */

                        for (String string : strings) {
                            LogUtil.e(TAG, string);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete()");
                    }
                });


    }

    /*
     just() 创建操作符，最多只能发送10个参数
     直接发送 传入的事件
     */
    private void just() {
        Observable.just("Hello", "Rxjava", "just() 创建操作符")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        /*
                         输出：
                         Hello
                         Rxjava
                         just()
                         */
                        LogUtil.e(TAG, s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        /* 输出：onComplete() */
                        LogUtil.e(TAG, "onComplete()");
                    }
                });
    }
}
