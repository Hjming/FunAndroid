package com.cainiao.funandroid.observe;

public class ObserveClient {
    public static void main(String[] args) {
        StudentObserverable studentObserverable = new StudentObserverable();

        PoliceObserver observer1 = new PoliceObserver();
        PoliceObserver observer2 = new PoliceObserver();
        PoliceObserver observer3 = new PoliceObserver();

        studentObserverable.registerObserver(observer1);
        studentObserverable.registerObserver(observer2);
        studentObserverable.registerObserver(observer3);


        studentObserverable.setInfotmation("你的名字", 18);
    }
}
