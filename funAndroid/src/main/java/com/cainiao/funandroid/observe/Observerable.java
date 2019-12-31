package com.cainiao.funandroid.observe;

/**
 * 被观察者 接口
 */
public interface Observerable {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver();
}
