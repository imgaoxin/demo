package org.test.demo.top.zookeeper;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * WatcherCallBack
 */
public class WatcherCallBack
        implements Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback, AsyncCallback.StatCallback {
    final String lockPath = "/myLock";
    ZooKeeper zk;
    CountDownLatch cdl;
    String currentThreadName;
    String currentRealLockPath;

    public WatcherCallBack(ZooKeeper zk, String currentThreadName) {
        this.zk = zk;
        this.cdl = new CountDownLatch(1);
        this.currentThreadName = currentThreadName;
    }

    public void lock() {
        
        try {
            // if data.toString == currentRealLockPath, so don't to lock, it is reentrant lock
            // byte[] data = zk.getData("/", false, new Stat());
            // if (data != null && data.toString().equals(currentRealLockPath)) {
            //     return
            // } 
            zk.create(lockPath, "".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
                this, "");
            cdl.await();  
            System.out.println(currentThreadName + " lock ...");
        } catch (UnsupportedEncodingException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unLock() {
        try {
            zk.delete(currentRealLockPath, -1);
            System.out.println(currentThreadName + " unLock ...");
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    // watcher exists
    @Override
    public void process(WatchedEvent event) {
        // maybe pre node unLock or dead, notice the watch node
        // execute getChildren, if first to lock, either to retry watch pre node
        switch (event.getType()) {
            case NodeDeleted:
                zk.getChildren("/", false, this, "");
                break;
            default:
                break;
            }
    }

    // StringCallback create
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        if (name != null) {
            System.out.println(currentThreadName + " -> " + name);
            currentRealLockPath = name;
            zk.getChildren("/", false, this, "");
        } else {
            // create fail ...
            // retry ...
            // or set time out
        }

    }

    // Children2Callback getChildren
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        if (children != null) {
            // need to sort
            Collections.sort(children);
            // currentRealLockPath more than children's string a "/"
            int index = children.indexOf(currentRealLockPath.substring(1));
            if (index > 0) {
                // watch pre node
                zk.exists("/" + children.get(index - 1), this, this, "");
            } else if (index == 0) {
                // only me
                try {
                    // set current who hold the lock
                    zk.setData("/", currentRealLockPath.getBytes("utf-8"), -1);
                    cdl.countDown();
                } catch (UnsupportedEncodingException | KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                // not find
                // why i'm alive ...
                // retry ...
            }
        }else {
            // why i'm alive ...
            // retry ...
        }
    }

    // StatCallback exists
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (stat == null) {
            // maybe pre node dead, retry to watch
            zk.getChildren("/", false, this, "");
        }
    }

}