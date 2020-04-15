package org.test.demo.top.communication_protocol;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author gx
 * @create 2019-08-22 16:14
 */
public class MainTest {
    public static final int SERVER_PORT = 9821;
    public static final String SERVER_HOSTNAME = "localhost";

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        CompletableFuture.runAsync(() -> new MyServer(countDownLatch).start(SERVER_HOSTNAME, SERVER_PORT));

        //try {
        //    Thread.sleep(5000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CompletableFuture.runAsync(() -> new MyClient().start(SERVER_HOSTNAME, SERVER_PORT));

    }
}
