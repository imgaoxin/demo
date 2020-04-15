package org.test.demo.top.simple_rpc_framework.rpc_netty.transport;

import java.util.concurrent.CompletableFuture;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Command;

public class ResponseFuture {
    private final int requestId;
    private final CompletableFuture<Command> completableFuture;
    private final long timestamp;

	public ResponseFuture(int requestId, CompletableFuture<Command> completableFuture) {
        this.requestId = requestId;
        this.completableFuture = completableFuture;
        this.timestamp = System.nanoTime();
	}

    public int getRequestId() {
        return this.requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return this.completableFuture;
    }

    long getTimestamp() {
        return this.timestamp;
    }
}