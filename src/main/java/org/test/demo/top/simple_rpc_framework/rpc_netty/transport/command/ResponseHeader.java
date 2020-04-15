package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command;

import java.nio.charset.StandardCharsets;

public class ResponseHeader extends Header {
    /**
     * 响应状态，0标识成功
     */
    private int code;
    private String error;

    public ResponseHeader(int type, int version, int requestId, Throwable throwable) {
        this(type, version, requestId, Code.UNKNOWN_ERROR.getCode(), throwable.getMessage());
    }

    public ResponseHeader(int type, int version, int requestId) {
        this(type, version, requestId, Code.SUCCESS.getCode(), null);
    }

    public ResponseHeader( int type, int version, int requestId , int code, String error) {
        super(type, version, requestId);
        this.code = code;
        this.error = error;
    }

    @Override
    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
                Integer.BYTES +
                (error == null ? 0 : this.error.getBytes(StandardCharsets.UTF_8).length);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }
}