package org.test.demo.base.asyn;

import java.util.concurrent.CompletableFuture;

/**
 * @author gx
 * @create 2019-08-19 16:31
 */
public class CompletableFutureTest {

    public static void main(String[] args) {
        CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "线程异步执行1");
            // try {
            // TimeUnit.SECONDS.sleep(3);
            // } catch (Exception e) {
            // }

        }).thenRunAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "异步线程被触发2");
            // try {
            // TimeUnit.SECONDS.sleep(3);
            // } catch (Exception e) {
            // }

        });
        System.out.println(Thread.currentThread().getName() + "线程同步执行结束");
    }
}
