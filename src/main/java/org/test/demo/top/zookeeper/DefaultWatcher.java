package org.test.demo.top.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * DefaultWatcher
 */
public class DefaultWatcher implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        // do nothing
    }

}