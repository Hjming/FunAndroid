package com.cainiao.funandroid.observe;

import java.util.ArrayList;
import java.util.List;

public class StudentObserverable implements Observerable {
    private List<Observer> mObservers;
    private String name;
    private int age;

    public StudentObserverable() {
        mObservers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        mObservers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        int index = mObservers.indexOf(observer);
        if (index >= 0) {
            mObservers.remove(observer);
        }
    }

    /**
     * 通知所有的观察者 数据更新
     */
    @Override
    public void notifyObserver() {
        for (int i = 0; i < mObservers.size(); i++) {
            Observer observer = mObservers.get(i);
            observer.update(name, age);
        }
    }

    public void setInfotmation(String name, int age) {

        this.name = name;
        this.age = age;
        notifyObserver();
    }
}
