package org.test.demo.base.locks;

import java.io.Closeable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gx
 * @create 2019-09-03 11:59
 */
public class AutoClose {
    public static void main(String[] args) {
        try (LockByAutoClose ignored = new LockByAutoClose()) {
            System.out.println("do 1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        LockByAutoClose.lock.lock();
        System.out.println("do 2");
        LockByAutoClose.lock.unlock();
    }

    private static class LockByAutoClose implements Closeable {
        private static final Lock lock = new ReentrantLock();

        LockByAutoClose() {
            lock.lock();
        }

        @Override
        public void close() {
            lock.unlock();
        }
    }
}
