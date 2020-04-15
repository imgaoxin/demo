package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command;

public class Command{
    protected Header header;
    private byte[] payload;

    public Command(Header header, byte [] payload) {
        this.header = header;
        this.payload = payload;
    }
    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte [] getPayload() {
        return this.payload;
    }

    public void setPayload(byte [] payload) {
        this.payload = payload;
    }
}