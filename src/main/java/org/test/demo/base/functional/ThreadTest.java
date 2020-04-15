package org.test.demo.base.functional;

public class ThreadTest {

    public static void main(String[] args) {
        System.out.println("hello vsc");

        // 匿名内部类实现多线程测试
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        }).start();

        // lambda 有且仅有一个抽象方法的接口叫做函数式接口
        // (参数) -> {代码}
        new Thread(() -> System.out.println(Thread.currentThread().getName())).start();

    }
}