package org.test.demo.base.locks;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gx
 * @create 2019-09-03 14:59
 */
public class CASTest1 {
    //账户初始值为0
    private static int balance = 0;

    public static void main(String[] args) {
        //执行10000次转账，每次转入1元
        int count = 10000;
        //线程池
        final ExecutorService executorService = new ThreadPoolExecutor(3, 10, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        //transfer1的锁
        Lock lock = new ReentrantLock();

        final long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            executorService.submit(() -> transfer1(1, lock));
        }

        while (balance < count) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(balance);
        executorService.shutdown();
    }

    //转账服务
    private static void transfer1(int amount, Lock lock) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }
}
