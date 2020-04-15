package org.test.demo.base.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gx
 * @create 2019-09-03 14:59
 */
public class CASTest2 {
    //账户初始值为0
    private static AtomicInteger balance = new AtomicInteger(0);

    public static void main(String[] args) {
        //执行10000次转账，每次转入1元
        int count = 10000;
        //线程池
        final ExecutorService executorService = new ThreadPoolExecutor(3, 10, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        final long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {

            executorService.submit(() -> transfer2(1));

            executorService.submit(() -> transfer3(1));
        }

        while (balance.get() < count) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(balance.get());
        executorService.shutdown();
    }

    //转账服务 cas
    private static void transfer2(int amount) {
        int old = balance.get();
        int _new = old + amount;
        balance.compareAndSet(old, _new);
    }

    // faa : fetch and add
    private static void transfer3(int amount) {
        balance.getAndAdd(amount);
    }
}
