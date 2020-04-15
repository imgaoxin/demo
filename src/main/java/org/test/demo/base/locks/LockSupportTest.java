package org.test.demo.base.locks;

import java.util.concurrent.locks.LockSupport;

/**
 * @author gx
 * @create 2019-08-30 11:08
 */
public class LockSupportTest {
    static Thread t1 = null, t2 = null;

    public static void main(String[] args) {
        t1 = new Thread(() -> {
            System.out.println("do 1");
            LockSupport.unpark(t2);
            LockSupport.park();
            System.out.println("do 3");
        });
        t2 = new Thread(() -> {
            LockSupport.park();
            System.out.println("do 2");
            LockSupport.unpark(t1);
        });

        t1.start();
        t2.start();
    }
}
