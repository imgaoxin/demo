package org.test.demo.top.simple_rpc_framework.rpc_netty.transport;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class InFlightRequests implements Closeable {
    private final static long TIMEOUT_SEC = 10L;
    /**
     * 通过信号量实现背压机制，当在途请求数量超过许可数量时，阻塞线程。
     */
    private final Semaphore semaphore = new Semaphore(10);
    /**
     * 在途请求，已经发送但还没有收到响应的ResponseFuture对象
     */
    private final Map<Integer, ResponseFuture> futureMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledFuture<?> scheduledFuture;
    
    public InFlightRequests() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeoutFutures, TIMEOUT_SEC, TIMEOUT_SEC, TimeUnit.SECONDS);
    }

    // 超时机制
    private void removeTimeoutFutures() {
        futureMap.entrySet().removeIf(entry -> {
            // 超时10s，删除在途请求中的ResponseFuture对象，并返还许可
            if( System.nanoTime() - entry.getValue().getTimestamp() > TIMEOUT_SEC * 1000000000L) {
                semaphore.release();
                return true;
            } else {
                return false;
            }
        });
    }

    /**
     * 添加在途请求。10s内获取到许可则添加，否则抛超时异常
     * @param responseFuture
     * @throws InterruptedException
     * @throws TimeoutException
     */
	public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {
        if (semaphore.tryAcquire(TIMEOUT_SEC, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);
        } else {
            throw new TimeoutException();
        }
	}

	public ResponseFuture remove(int requestId) {
        ResponseFuture future = futureMap.remove(requestId);
        if (future != null) {
            semaphore.release();
        }
        return future;
	}

    @Override
    public void close() throws IOException {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdown();
    }
}