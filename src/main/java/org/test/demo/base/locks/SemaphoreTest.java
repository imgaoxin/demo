package org.test.demo.base.locks;


// import java.util.concurrent.CountDownLatch;
// import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * @author gx
 * @create 2019-08-30 11:05
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        // countdownlatch semaphore cyclicbarrier
        // 信号量
        Semaphore semaphore = new Semaphore(1);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
