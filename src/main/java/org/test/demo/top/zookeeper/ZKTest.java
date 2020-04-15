package org.test.demo.top.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.ZooKeeper;

/**
 * ZKTest
 */
public class ZKTest {
    private static final String ADDRESS = "192.168.20.251:2181,192.168.20.252:2181/testLock"; // link to /testLock

    private static ZooKeeper getSingleInstance() throws IOException {
        return new ZooKeeper(ADDRESS, 1000, new DefaultWatcher());
    }
    
    public static void main(String[] args) throws Exception {
        final ZooKeeper zk = getSingleInstance();
        // must had path "/testLock"
        // this can't success
        // zk.create("/", "".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                String currentThreadName = Thread.currentThread().getName();
                WatcherCallBack wcb = new WatcherCallBack(zk, currentThreadName);
                // 获取锁
                wcb.lock();
                // 操作
                System.out.println(currentThreadName + " working ...");
                // 释放锁
                wcb.unLock();
            }).start();;
        }

        while (true) {
            Thread.sleep(1000);
        }

        // zk.close();
    }
}