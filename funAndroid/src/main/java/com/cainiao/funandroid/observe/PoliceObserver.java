package com.cainiao.funandroid.observe;

public class PoliceObserver implements Observer {
    private static final String TAG = "PoliceObserver";
    private String name;
    private int age;

    public PoliceObserver() {
    }

    @Override
    public void update(String name, int age) {

        // 业务逻辑
        notifyChange(name, age);
    }

    private void notifyChange(String name, int age) {
        System.out.println("姓名更改为：" + name + "; 年龄变更为：" + age);
    }
}
